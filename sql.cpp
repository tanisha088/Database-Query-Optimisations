#include <bits/stdc++.h>

using namespace std;

#define M 10000000
long long int a = 0.33*M;
long long int b = 0.66*M;

int** DB_TABLE;
int thread_no;
unsigned long long *final_result;



void *compute_func(void* num)
{
	int id = *(int*)(num);
	int x,y;
	final_result[id] = 0;


	y = (M/thread_no)*(id+1); ///id number goes from 0 to (no. of thread-1)
	x = (M/thread_no)*id;

	//cout<<x<<" "<<y<<endl;

		// (statement S0)
	int** cond1 = new int* [M/thread_no];
	for (int i=x;i<y;i++)
	{
		cond1[i-x] = new int [3];
	}
	int j = 0;
	for (int i=x;i<y;i++)
	{
		if (DB_TABLE[i][2]>=a)
		{
			cond1[j][0] = DB_TABLE[i][0];
			cond1[j][1] = DB_TABLE[i][1];
			cond1[j][2] = DB_TABLE[i][2];
			j++;
		}

	}

	// (statement S1)
	int** cond2 = new int* [M/thread_no];
	for (int i=x;i<y;i++)
	{
		cond2[i-x] = new int [3];
	}
	int k = 0;
	for (int i=x;i<y;i++)
	{
		if (DB_TABLE[i][2]<=b)
		{
			cond2[k][0] = DB_TABLE[i][0];
			cond2[k][1] = DB_TABLE[i][1];
			cond2[k][2] = DB_TABLE[i][2];
			k++;
		}
	}

	//(statement S2)

	int** cond1_and_cond2 = new int* [M/thread_no];
	for (int i=0;i<y;i++)
	{
		cond1_and_cond2[i] = new int [3];
	}

	long long int left = 0;
	long long int right = 0;
	int flag = 0;
	while (cond1[left][2] != cond2[right][2])
	{
		if (cond1[left][2] < cond2[right][2]) left++;
		else right++;
		if (left>=M/thread_no || right>=M/thread_no) 
		{
			flag = 1;
			break;
		}
	}

	int m = 0;
	if (flag != 1)
	{
		while (cond1[left][2] == cond2[right][2])
		{
			cond1_and_cond2[m][0] = cond2[right][0];
			cond1_and_cond2[m][1] = cond2[right][1];
			cond1_and_cond2[m][2] = cond2[right][2];
			left++; right++; m++;
			if (left>=M/thread_no || right>=M/thread_no) break;
		}
	}

	//(statement S3)
	vector<int> val1;
	for (int i=0;i<m;i++)
	{
		val1.push_back(cond1_and_cond2[i][0]);
	}

	//cout<<val1[0]<<endl;

	//(statement S4)
	vector<int> val2;
	for (int i=0;i<m;i++)
	{
		val2.push_back(cond1_and_cond2[i][1]);
	}

	//(statement S5)
	vector<int> result;
	for (int i=0;i<m;i++)
	{
		//cout<<val1[i]<<" "<<val2[i]<<endl;
		result.push_back(val1[i]-val2[i]);
	}

	//statement (S6)
	//unsigned long long int final_result = 0;
	for (int i=0;i<m;i++)
		final_result[id]+= result[i];

	//cout<<x<<" "<<y<<" : "<<final_result[id]<<endl;

}

unsigned long long compute_func_init(unsigned long long y)
{
	int** cond1 = new int* [M/thread_no];
	for (int i=0;i<y;i++)
	{
		cond1[i] = new int [3];
	}
	int j = 0;
	for (int i=0;i<y;i++)
	{
		if (DB_TABLE[i][2]>=a)
		{
			cond1[j][0] = DB_TABLE[i][0];
			cond1[j][1] = DB_TABLE[i][1];
			cond1[j][2] = DB_TABLE[i][2];
			j++;
		}

	}

	// (statement S1)
	int** cond2 = new int* [M/thread_no];
	for (int i=0;i<y;i++)
	{
		cond2[i] = new int [3];
	}
	int k = 0;
	for (int i=0;i<y;i++)
	{
		if (DB_TABLE[i][2]<=b)
		{
			cond2[k][0] = DB_TABLE[i][0];
			cond2[k][1] = DB_TABLE[i][1];
			cond2[k][2] = DB_TABLE[i][2];
			k++;
		}
	}
	//(statement S2)

	int** cond1_and_cond2 = new int* [M/thread_no];
	for (int i=0;i<y;i++)
	{
		cond1_and_cond2[i] = new int [3];
	}

	long long int left = 0;
	long long int right = 0;
	int flag = 0;
	while (cond1[left][2] != cond2[right][2])
	{
		if (cond1[left][2] < cond2[right][2]) left++;
		else right++;
		if (left>=M/thread_no || right>=M/thread_no) 
		{
			flag = 1;
			break;
		}
	}

	int m = 0;
	if (flag != 1)
	{
		while (cond1[left][2] == cond2[right][2])
		{
			cond1_and_cond2[m][0] = cond2[right][0];
			cond1_and_cond2[m][1] = cond2[right][1];
			cond1_and_cond2[m][2] = cond2[right][2];
			left++; right++; m++;
			if (left>=M/thread_no || right>=M/thread_no) break;
		}
	}

	//(statement S3)
	vector<int> val1;
	for (int i=0;i<m;i++)
	{
		val1.push_back(cond1_and_cond2[i][0]);
	}

	//cout<<val1[0]<<endl;

	//(statement S4)
	vector<int> val2;
	for (int i=0;i<m;i++)
	{
		val2.push_back(cond1_and_cond2[i][1]);
	}

	//(statement S5)
	vector<int> result;
	for (int i=0;i<m;i++)
	{
		//cout<<val1[i]<<" "<<val2[i]<<endl;
		result.push_back(val1[i]-val2[i]);
	}

	//statement (S6)
	unsigned long long int ret_val= 0;
	for (int i=0;i<m;i++)
		ret_val+= result[i];

	//cout<<"0 "<<y<<" : "<<ret_val<<endl;

	return ret_val;
}




int main(int argc, char *argv[])
{
	//dynamic allocation 
	DB_TABLE = new int* [M];
	for (int i=0;i<M;i++)
	{
		DB_TABLE[i] = new int [3];
	}
	//0th column -> Row number
	// 1st column -> Item Price 
	//2nd column -> Item Discount
	//3rd column -> Item Date

	int count = 0;
	for (int i=0;i<M;i++)
	{
		DB_TABLE[i][0] = ++count;
		DB_TABLE[i][1] = count;
		DB_TABLE[i][2] = count;
	}

	//for (int i=0;i<M;i++) 
	//cout<<DB_TABLE[i][0]<<" "<<DB_TABLE[i][1]<<" "<<DB_TABLE[i][2]<<endl;


	int i, j, *tid;
	pthread_t *threads;
	pthread_attr_t attr;
	unsigned long long sum=0; 

	if (argc != 2) {
		printf ("Please enter number of threads too.\n");
		exit(1);
	}


	thread_no = atoi(argv[1]);
	threads = (pthread_t*)malloc(thread_no*sizeof(pthread_t));
	final_result = (unsigned long long*)malloc(thread_no*sizeof(unsigned long long));
    tid = (int*)malloc(thread_no*sizeof(int));
    for (i=0; i<thread_no; i++) tid[i] = i;

	pthread_attr_init(&attr);

	for (i=1; i<thread_no; i++) 
	{
		pthread_create(&threads[i], &attr, compute_func, &tid[i]);
   	}

   	//for thread 0
	sum = compute_func_init(M/thread_no);
	
	for (i=1; i<thread_no; i++) {
		pthread_join(threads[i], NULL);
		sum += final_result[i];
	}

	//cout<<"Final query output is: "<<sum<<endl;

	//clean up
	for (int i=0;i<M;i++)
	{
		delete [] DB_TABLE[i];
	}
	delete [] DB_TABLE;

	return 0;
}