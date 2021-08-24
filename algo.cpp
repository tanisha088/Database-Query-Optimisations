#include <iostream> 
#include <list> 
#include <stack> 
#include <string>
#include <vector>
#include <queue>
#include <algorithm>
#include <bits/stdc++.h>
using namespace std; 

vector<int> topsort; 

map<int, string> statement_mapping;
map<string, string> command_mapping;
map<string, string> shape_mapping;
map<string, string> fn_mapping;
map<string, string> stmt_args1_mapping;
map<string, string> stmt_args2_mapping;
map<int,vector<int>> parentarr;

vector<string> find_Fusible_Section(int);

int counter = 0;
const int ed=7;

        vector<int> adj[ed]; 


void addEdge(vector<int> adj[], int u, int v) 
{ 
    adj[u].push_back(v); 
  //  adj[v].push_back(u); 

} 

int vis[ed + 1]; 
int ans[ed + 1]; 

int visited[ed+1];

bool isReduction_func(int nodeval)
{ 
    string stmtname= statement_mapping.find(nodeval)->second;
    string commnd_name = command_mapping.find(stmtname)-> second;
     
  //   cout<<"func "<<nodeval<<"  "<<stmtname<<"  "<<commnd_name<<" \n";

    if(commnd_name.compare("sum")==0 || commnd_name.compare("avg")==0 || commnd_name.compare("min")==0 || commnd_name.compare("max")==0)
    {
      return true;   
    }

    return false;
}


void topologicalSortUtil(int v, int* visited,
                                stack<int>& Stack , vector<int> adj[] , int V) 
{ 
    // Mark the current node as visited. 
    visited[v] = 1; 
  
    // Recur for all the vertices 
    // adjacent to this vertex 
    vector<int>::iterator i; 
    for (i = adj[v].begin(); i != adj[v].end(); ++i) 
    {
        if (visited[*i]==0) 
            topologicalSortUtil(*i, visited, Stack,adj,V); 
    }
    
  
    // Push current vertex to stack 
    // which stores result 
    Stack.push(v); 
} 


void  topologicalSort(vector<int> adj[] , int V) 
{ 
    stack<int> Stack; 
  
    // Mark all the vertices as not visited 
    
    int visited[ed];

    
    for (int i = 0; i < V; i++) 
        visited[i] = 0; 
  
    // Call the recursive helper function 
    // to store Topological 
    // Sort starting from all 
    // vertices one by one 
    for (int i = 0; i < V; i++) 
        if (visited[i] == 0) 
            topologicalSortUtil(i, visited, Stack,adj,V); 
  
    // Print contents of stack 
    while (Stack.empty() == false) { 
    topsort.push_back(Stack.top());
        Stack.pop(); 
    } 
} 


bool isConforming(int node1,int node2)
{
  if (node1==-1 || node2==-1) return false;
    string stmtname1= statement_mapping.find(node1)->second;
 
    string stmtname2= statement_mapping.find(node2)->second;

    string s1= shape_mapping.find(stmtname1)->second;

    string s2= shape_mapping.find(stmtname2)->second;

    if( ((s1.compare("1")==0) && (s2.compare("1")==0)) || ((s1.compare("d")==0) && (s2.compare("d")==0)) || 

        ((s1.compare("s")==0) && (s2.compare("d")==0)) || ((s1.compare("d")==0) && (s2.compare("s")==0)) || 
         
         ((s1.compare("s")==0) && (s2.compare("s")==0) ))
    {

        return true;
    }
    
    return false;

}


vector<string> fetchFusibleStmts(int node1,int node2)
{
    if(isConforming(node1,node2))
    {
        std::vector<string> vec = find_Fusible_Section(node2);
        //for (int i=0;i<vec.size();i++) cout<<vec[i]<<" ";
          //cout<<endl;
        return vec;
    }

    std::vector<string> list;
    return list;
}


vector<string> findFromReduction(int node)
{
    visited[node]=1;

    string stmtname= statement_mapping[node];

    std::vector<string> res_list;


    std::vector<int> parlist = parentarr[node];

         int res =  parlist[0];
         res_list = find_Fusible_Section(res);
        

        
      res_list.push_back(stmtname);

      return res_list;
}

vector<string> find_Fusible_Section(int i)
{

    if(i!=-1 && visited[i]==0)
    {
        visited[i]=1;
        string stmtname= statement_mapping[i];
         string fn_name = fn_mapping[stmtname];
        std::vector<string> fuse_list;

       if(fn_name.compare("E")==0 ||  fn_name.compare("S")==0)
       {
          
                    std::vector<int> par = parentarr[i];
                    fuse_list = fetchFusibleStmts(i,par[0]);
                   
                    if (par.size()>1)
                    {                                  
                   std::vector<string> list2 = fetchFusibleStmts(i,par[1]);
                   fuse_list.insert(fuse_list.end(), list2.begin(), list2.end());
        }
                 

        /*

            std::vector<int> par = parentarr[i];
            vector<string> tempf1 = fetchFusibleStmts(i,par[0]);
            
            if (par.size()>1) 
            {
              std::vector<string> list2 = fetchFusibleStmts(i,par[1]);
              tempf1.insert(fuse_list.end(), list2.begin(), list2.end());
            }
              int s1 = tempf1.size();

            for(int k=0;k<s1;k++)
            {
            string m1 = tempf1[k] + std::to_string(counter);
            fuse_list.push_back(m1);
            }

            counter++; 
            */
                 

       }
       else if(fn_name.compare("U")==0 || fn_name.compare("B")==0)
       {
                            std::vector<int> par = parentarr[i];

              fuse_list = fetchFusibleStmts(i,par[0]);
       }
       else if(fn_name.compare("X")==0)
       {
             std::vector<int> par = parentarr[i];

              fuse_list = fetchFusibleStmts(i,par[1]);
       }
       else
       {
            
       }
        fuse_list.push_back(stmtname);
       return fuse_list;
    }

    else
    {
        std::vector<string> v;
        return v;
    }
}

int main() 
{ 
    // Create a graph given in the above diagram
          statement_mapping.insert({0,"S0"});    
          statement_mapping.insert({1,"S1"});    
          statement_mapping.insert({2,"S2"});    
          statement_mapping.insert({3,"S3"});    
          statement_mapping.insert({4,"S4"});    
          statement_mapping.insert({5,"S5"});    
          statement_mapping.insert({6,"S6"});    
                
    
    
    command_mapping.insert({"S0","geq"});
    command_mapping.insert({"S1","leq"});
    command_mapping.insert({"S2","and"});
    command_mapping.insert({"S3","compress"});
    command_mapping.insert({"S4","compress"});
    command_mapping.insert({"S5","mul"});
    command_mapping.insert({"S6","sum"});

    
    
    shape_mapping.insert({"S0","d"});
    shape_mapping.insert({"S1","d"});
    shape_mapping.insert({"S2","d"});
    shape_mapping.insert({"S3","s"});
    shape_mapping.insert({"S4","s"});
    shape_mapping.insert({"S5","s"});
    shape_mapping.insert({"S6","1"});

    
    
    fn_mapping.insert({"S0","E"});
    fn_mapping.insert({"S1","E"});
    fn_mapping.insert({"S2","E"});
    fn_mapping.insert({"S3","S"});
    fn_mapping.insert({"S4","S"});
    fn_mapping.insert({"S5","E"});
    fn_mapping.insert({"S6","R"});


stmt_args1_mapping.insert({"S0","c0"});
stmt_args1_mapping.insert({"S1","c0"});
stmt_args1_mapping.insert({"S2","t0"});
stmt_args1_mapping.insert({"S3","t2"});
stmt_args1_mapping.insert({"S4","t2"});
stmt_args1_mapping.insert({"S5","t3"});
stmt_args1_mapping.insert({"S6","t5"});


stmt_args2_mapping.insert({"S0","2010-09-01 : date"});
stmt_args2_mapping.insert({"S1","2010-09-30 : date"});
stmt_args2_mapping.insert({"S2","t1"});
stmt_args2_mapping.insert({"S3","c1"});
stmt_args2_mapping.insert({"S4","c2"});
stmt_args2_mapping.insert({"S5","t4"});
stmt_args2_mapping.insert({"S6","phi"});

std::vector<int> va;
va.push_back(-1);
parentarr[0] = va;
parentarr[1] = va;
std::vector<int> vb;
vb.push_back(0);
vb.push_back(1);
parentarr[2] = vb;
std::vector<int> vc;
vc.push_back(2);
parentarr[3] = vc;
parentarr[4] = vc;
std::vector<int> vd;
vd.push_back(3);
vd.push_back(4);
parentarr[5] = vd;
std::vector<int> ve;
ve.push_back(5);
parentarr[6] = ve;





    

    addEdge(adj,0,2); 
    addEdge(adj,1,2); 
    addEdge(adj,2,3); 
    addEdge(adj,2,4); 
    addEdge(adj,3,5); 
    addEdge(adj,4,5);
    addEdge(adj,5,6); 
    cout << "Following is a Topological Sort of the given graph \n"; 
    

 //    Function Call 
   topologicalSort(adj,ed); 

   /*
   for (int i=0;i<ed;i++)
   {
    for (int j=0;j<adj[i].size();j++) cout<<i<<" "<<adj[i][j]<<" ";
      cout<<endl;
   }
   */

   
    reverse(topsort.begin(), topsort.end()); 


    for (int i = 0; i < topsort.size(); i++) 
       cout << topsort[i] << " "; 
   cout<<"\n";



    for(int i=0;i<topsort.size();i++)
    {
       int node = topsort[i];
       //cout<<node<<" \n";
       
       if(visited[i]==0)
       {

         if(isReduction_func(node))
         {
            std::vector<string> reduction_section = findFromReduction(node);
            for(auto i1=reduction_section.begin();i1!=reduction_section.end();i1++)
            {
                cout<<*i1<<" "<<"\n";
            }
         }

            else
         {

           std::vector<string> fusible_section = find_Fusible_Section(node);
           for(auto i1=fusible_section.begin();i1!=fusible_section.end();i1++)
            {
                cout<<*i1<<" "<<"\n";
            }

           }

       }

    }





    return 0; 
}
