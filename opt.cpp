#include <bits/stdc++.h>
#define M 10000000

using namespace std;

long long int a = 0.33*M;
long long int b = 0.66*M;

int** DB_TABLE;
int thread_no;
unsigned long long *final_result;

void init_table(int ***DB_TABLE)
{
	int i,j,k;
	int** data;
	data = (int **) malloc(sizeof(int *) * M);
	for (k=0;k<M;k++)
	{
		data[k] = (int *) malloc(sizeof(int) * 3);
	}

	int count = 0;
	for (i=0;i<M;i++)
	{
		data[i][0] = ++count;
		data[i][1] = count;
		if (i%2==0)
		data[i][2] = count;
		else data[i][2] = -count;
	}

	*DB_TABLE = data;
}


void *compute_func(void *num)
{
	int id = *(int*)(num);
	int x,y;
	final_result[id] = 0;

	//cout<<x<<" "<<y<<endl;

	y = (M/thread_no)*(id+1); ///id number goes from 0 to (no. of thread-1)
	x = (M/thread_no)*id;

	for (int i=x;i<y;i++)
	{
		if (DB_TABLE[i-x][2]>=a && DB_TABLE[i-x][2]<=b)
		{
			//printf("%d\n",i);
			final_result[id]+= (DB_TABLE[i-x][0]*DB_TABLE[i-x][1]);
		}
	}
}


unsigned long long compute_func_init(unsigned long long y)
{
	int id = 0;
	unsigned long long ret_val = 0;
	for (int i=0;i<y;i++)
	{
		if (DB_TABLE[i][2]>=a && DB_TABLE[i][2]<=b)
		{
			//printf("%d\n",i);
			ret_val+= (DB_TABLE[i][0]*DB_TABLE[i][1]);
		}
	}
	return ret_val;
}


int main(int argc, char *argv[])
{
	init_table(&DB_TABLE);

	//for (int i=0;i<M;i++)
	//printf("%d %d %d\n", DB_TABLE[i][0], DB_TABLE[i][1], DB_TABLE[i][2]);


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




	
	//printf("Final query output is: %llu\n", sum);

	free (DB_TABLE);
	return 0;
}