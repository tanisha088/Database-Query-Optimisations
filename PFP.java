if 2 loops => backward dependency => cannot fiss 
	if 2 loops after fusion => leads to backward dependency => cannot fuse
		if 2 loops => loop carried dependency => can be fissed



PFP

Bernsteins conditions : : when is it safe to RUN R1 and R2 in parallel => no wr,rw or ww dependence.

Andahls law :: upper limit to scalability exists.

Reordering transformations :: dont add/remove statements/dependencies.

level of loop carried dependencies :: index of first non-zero value.


loop interchange is valid  iff => in a perfect loop nest => after reordering => the first non-zero value is non-negative. 

for(int i=0;i<k;i+)                                                       for(int j=0;j<k;j++)
for(int j=0;j<k;j++)          this is equivalent to  ====>>>                  for(int i=0;i<k;i++)
	a[i,j+1]=a[i,j]                                                             a[i,j+1]=a[i,j]

if loops exist after scalar expansion , then scalar expansion does not help

we can also use scalar renaming

T=T+2 has 4 dependencies => 1 is loop independent antidependence => one read on left and write on right.

Fine grained parallelism ::

loop peeling

p=10
for i= 1 to 10:
y[i] = x[i] + x[p]
p=i
end for
 so this can be peeled as putting y[0]=x[0]+x[10] and then inside we have y[i]=x[i]+x[i-1]

 loop skewing
 loop jamming
 loop unrolling
 loop unroll + jamming
 tiling - possible for contigous loops


 Coarse grained parallelism ::

 loop fission = fiss the loop inside the outer loop  block into 2 parts iff the parts are such that there is
  no backward dependency i.e. no loop carried dependency

  i=1 to n
  j=1 to n
  {
  	s1
  	s2
  }

  can be =>

  i=1 to n
  j=1 to n
  s1
  j=1 to n
  s2 

  iff no dependency from s2->s1 - so we can parallelise the outer loop by providing it to threads


  if we have

 for i=1->n
 c1
 c2
 c3

 if c2 has loop carried dependency with itself=> then we can fiss c1 c2 c3 and can fuse c1 and c3 by jamming and parallelise c1c3 and c2 


if by fusing 2 loops = we are getting a backward edge = dont fuse

for i=1 to n
for j=1 to n if there is dependency on outer loops => we see if loop perm possible ( first non-zero value is non-negative) then ,
 we do so and check if possible we can paralleelize outer loops using threads= priority



graph = all those nodes not a part of cycle = can be vectorized



acyclic graph=>forward depedency =>>> can be easily vectorized
 acyclic graph  => backward       ==>> can be vectorized (if can convert the backward intp forward depeenddency by reordering the statements [allowed if depedency between A and B remains before and after reordering])

cyclic graph => try to remove backward dependency by reordering => vectorixe as much as possible.
if self antidependence => vectorize  if self flow dependence => cannot be vectorized.

for(int i=k;i<len;i++)
	a[i]=a[i-k]+b[i]

here , we have a self flow dependence => this is vectorizable if the value of k >= no of iterations that SIMD unit can execute at a time.

if dependency only on outer loop = inner one can be vectorized.

another thing to use is node splitting = when it is difficult to remove backward dependency..

Privatisation eliminates loop carried dependencies = gen done when loop carried dependencies are due to "T"


*****************************OPEN MP********************************************************
The OpenMP specification provides the  environmental variables you can use to modify internal control variables.

The OpenMP specification defines the internal control variables whose values affect the operation of parallel regions.



Environment variables like OMP_NUM_THREADS -> change the value of internal environment variables nthreads-var

#pragma omp parallel => Parallel region, teams of threads,  structured block, interleaved execution across threads.

#pragma omp barrier/critical => Synchronization and race conditions, interleaved execution. 

#pragma omp for/parallel for => Worksharing, parallel loops, loop carried dependencies.

#pragma omp single => Workshare with a single thread.

#pragma omp task/ taskwait => Tasks including the data environment for tasks.

setenv OMP_NUM_THREADS  N => Setting the internal control variable (ICV) for the default number of threads with an environment variable

void omp_set_num_threads()
int omp_get_thread_num()     => Default number of threads and ICV.  ]
                                  SPMD pattern: Create threads in a parallel region and split up the work
int omp_get_num_threads()

 schedule (static [,chunk]) => Loop schedules, loop overheads, and load balance.
schedule(dynamic [,chunk])


******************
double A[1000];
omp_set_num_threads(4);
	#pragma omp parallel{
         int ID = omp_get_thread_num();
             pooh(ID, A);
         }
	printf(“all done\n”);

here since the num of threads are set as 4 => the id value will get 4 diff values = from 0 to 3 and hence the pooh function will deal with data provided
by spmd by dividing the given data into 4 parts. and barrier at end.

The num_threads clause can be used to request threads for a parallel region, ICV remains unchanged
#pragma omp parallel num_threads(8)
but when done using OMP_NUM_THREADS , this updates nthreads-var ICV => Can override this using omp_set_num_threads() function call 
1 env variable , 2 tyoes of fn calls

OpenMP has three major components
Compiler directives
Runtime routines - omp_set_num_threads()
Environment variables - OMP_NUM_THREADS

Compiler directives have the format 
#pragma omp or !$omp           DIRECTIVE-NAME(PARALLEL/SIMD)           CLAUSE(DEFAULT/PRIVATE ....)


A construct is an OpenMP directive + directive-name + clauses + a structured block of code that does something – next slide

#pragma omp parallel  // thread which encounters this => becomes master and creates team of threads
{ 
	#pragma omp for // this assigns the work to master+team
		for (i=0;i<N;i++)
		{
			do_something(i) // i private for each thread by default
		}
}// implicit barrier

Basic approach
Find compute intensive loops
Make the loop iterations independent ... So they can safely execute in any order without loop-carried dependencies
Place the appropriate OpenMP directive and test


WORK SHARING CONSTRUCTS ::

1. schedule clause : static(compile time, same size of chunk and in particular order) , dynamic(run time, same size of chunk and in diff order) , guided(assigned dynamically and chunk size reduces per iteration)

SINGLE/MASTER – restrict execution to one thread
SINGLE – any one thread can execute the structured block
MASTER – master thread executes the structured block

Implicit barrier for SINGLE, none for MASTER

master is very similar to single with two differences:
master will be executed by the master only while single can be executed by whichever thread reaching the region first; and
single has an implicit barrier upon completion of the region, where all threads wait for synchronization, while master doesnt have any.
atomic is very similar to critical, but is restricted for a selection of simple operations.

critical - no implicit barrier at end - serially executed by each thread
master - no implicit end barrier - only executed by master thread
single - implicit barrier at end - executed by any one thread
single no wait - single but without the end implicit barrier

*******************
iska 2nd ans:::
https:
//stackoverflow.com/questions/33441767/difference-between-omp-critical-and-omp-single#:~:text=master%20is%20very%20similar%20to,master%20doesn%27t%20have%20any.=> 2nd ans
*********************

critical vs atomic ::: For a simple increment to a shared variable, atomic and critical are semantically equivalent, but atomic allows the compiler more opportunities for optimisation (using hardware instructions, for example).

In other cases, there are differences. If incrementing array elements (e.g. a++ ), atomic allows different threads to update different elements of the array concurrently whereas critical does not. If there is a more complicated expression on the RHS (e.g. a+=foo() ) then the evaluation of foo() is protected from concurrent execution with critical but not with atomic.

 void result() {
      temp = 10;
#pragma omp parallel private(temp)
      work();
    printf("%d\n", temp);


      when the parallel constructs makes the various work threads => temp is made for each of them and is considered to be uninitialised for them
      and hence no thread knows about the value of the temp=10. we dont know which thread is going to update the print value and hence unspecified value on print.

******************
very good for shared,private,first and last private
https://www3.nd.edu/~zxu2/acms60212-40212-S12/Lec-11-02.pdf
****************      

 int A = 1, B = 1, C = 1;
 #pragma omp parallel default(none) private(B) firstprivate(C)

Inside this parallel region ...
 A is shared by all threads; equals 1
 B and C are local to each thread.
– B’s initial value is undefined
– C’s initial value equals 1
Outside this parallel region ...
 The values of B and C are undefined

shared = the value is shared among all
private = the value is private for each variable and when comes outside , we dont know who will be updating or peinting the value hence unknown situation
          also the value is initialised to be uninitialised as for the threads the variable was created at the time of construct called
first private = the value of the variable is initialised as what has been initialised in the program prior to pragma for all threads although each will handle the data privately
last private = if print statement is present after the end of the pragma - the value printed will be value provided by that thread which executed the last iteration.


For making the temporary view consistent with the memory=> we use flushes





**************************Intel TBB******************************

data parallelism = 1 operation performed over diff subsets of same data
task parallelism = diff operations performed over diff or same data

data parallelism =>Same operations performed on different subsets of same data
Synchronous computation 
Expected speedup is more as there is only one execution thread operating on all sets of data
Amount of parallelization is proportional to the input data size
Designed for optimum load balance

task parallelization=> Different operations are performed on the same or different data
Asynchronous computation 
Expected speedup is less as each processor will execute a different thread or process
Amount of parallelization is proportional to the number of independent tasks
Load balancing depends on the availability of the hardware and scheduling algorithms like static and dynamic scheduling


data paralellsim => data divide karke uspe kaam karo -< bola 1 thread ko
task parallelism => tasks divide karke unpe kaam karo -> bola multiple threads ko


Parallelism vs concurrency => copy

application is compute bound => 1-1 mapping b/w logical and physical threads is the best.

tasks perform concurrently with other tasks (both using diff threads => tasks higher level , threads=> lower level).
 
 time slicing => more logical threads than physical threads possible.

 TBB FEATURES :: Generic Parallel Algorithms
Task Scheduler with work stealing
Generic Highly Concurrent Containers
Scalable Memory Allocation
Low-level Synchronization Primitives

oPEN MP => Language extension consisting of pragmas, routines, and environment variables
Supports C, C++, and Fortran
User can control scheduling policies

OpenMP limited to specified types (for e.g., reduction)


INTEL TBB=>Library for task-based programming

Supports C++ with generics
Automated divide-and-conquer approach to scheduling, with work stealing
Generic programming is flexible with types


TASK PROGRAMMING =>Using generics or templates , we write the algorithms in abstract types (e.g. queue is already built in stl .. Just call that to use it ..need not build it again) which are then computerized later during run time.
C++ templates are functions that can handle different data types without writing separate code for each of them. 

**************
https://www.threadingbuildingblocks.org/docs/help/tbb_userguide/parallel_for.html
******************

parallel fn call hoga => copy constructor copy karega => operator ek defined range ke liye operate karega
and body object operator declared as const. 

X::X(X& x, tbb::split)->
A type is splittable if it has a splitting constructor that allows an instance to be split into two pieces. 
The splitting constructor takes as arguments a reference to the original object, and a dummy argument of type split,
 which is defined by the library. The dummy argument distinguishes the splitting constructor from a copy constructor.
  After the constructor runs, x and the newly constructed object should represent the two pieces of the original x. 
  The library uses splitting constructors in two contexts:
Partitioning a range into two subranges that can be processed concurrently.
Forking a body (function object) into two bodies that can run concurrently

 There are 2 types of splitting constructors ::

Basic Splitting constructor :: division in 2 equal parts
Proportional splitting constructor :: partition may not be equal 

Basic is preffered :: 1) splitting equally is easy  2) equal splitting provides best parallelism 

                                                           
R::R(const R&)                              Copy constructor
                                             
R::~R()                              Destructor
bool R::is_divisible() const 
bool R::empty() const
R::R(R& r, split)                                   True if splitting constructor can be called, false otherwise
                                     True if range is empty, false otherwise
                             Splitting constructor. It splits range r into two subranges. 
                             One of the subranges is the newly constructed range which is the one on the right side.
                              The other subrange is  overwritten onto r , the one on the left side.



blocked_range<int> r(0,30,20);
assert(r.is_divisible());
// Call splitting constructor
blocked_range<int> s(r);
// Now r=[0,15) and s=[15,30) and both have a grainsize 20, inherited from the original value of r
assert(!r.is_divisible()); // FALSE
assert(!s.is_divisible()); // FALSE


CONTROLLING chunking ---- it handles itself how to divide the work into smaller portions/chunks/blocks

The idea of chunking is controlled by 2 factors :: 
Grain size
Partitions 

MORE PARTITION => SMALLER GRAIN SIZE => MORE PARALLELLISM => MORE overheads

APP GRAIN SIZE => BATH TUB CURVE 

COURSE GRAINED PREFFERED FOR SCHEDULING THREADS => SO THAT THERE IS ENOUGH WORK FOR THREADS TO DO IN THE FIRST Place

Range form of parallel_for takes an optional partitioner argument

  parallel_for(range,bodyobject,simple_partitioner());

auto_partitioner: Runtime will try to subdivide the range to balance load, this is the default
simple_partitioner: Runtime will subdivide the range into subranges as finely as possible; method is_divisible will be false for the final subranges
Guarantees that ⎡G/2⎤ ≤ chunksize ≤ G
affinity_partitioner: (CACHE AFFINITY)Request that the assignment of subranges to underlying threads be similar to a previous invocation of parallel_for or parallel_reduce with the same affinity_partitioner object
TRIES TO ASSIGN SAME ITERATION RANGE TO THE SAME THREAD FOR AFFINITY


tbb:: parallel_reduce(range,starting value,reduction function used,kernel or lambda fn used)
auto total = tbb::parallel_reduce( 
                    tbb::blocked_range<int>(0,values.size()),
                    0.0,
                    [&](tbb::blocked_range<int> r, double running_total)
                    {
                        for (int i=r.begin(); i<r.end(); ++i)
                        {
                            running_total += values[i];
                        }

                        return running_total;
                    }, std::plus<double>() );

running_total is is then returned at the end of this call of the kernel function, so that it can be passed as input to another call of the kernel function used to process the next sub-range of iterations.

************
https://www.threadingbuildingblocks.org/docs/help/tbb_userguide/parallel_reduce.html
https://www.threadingbuildingblocks.org/docs/help/tbb_userguide/Simple_Example_Fibonacci_Numbers.html
https://www.threadingbuildingblocks.org/docs/help/tbb_userguide/How_Task_Scheduling_Works.html
REFERENCE YAHA HAI :: https://www.threadingbuildingblocks.org/docs/help/reference/task_scheduler.html
**********

Advantage of task based scheduling over thread scheduling :::
1. Matching parallelism to available resources – problem of over and under subscription
2. Faster task startup and shutdown-  This is because a thread has its own copy of a lot of resources, such as register state and a stack. On Linux, a thread even has its own process id. A task in Intel® Threading Building Blocks, in contrast, is typically a small routine, and also, cannot be preempted at the task level (though its logical thread can be preempted).
3. Improved load balancing - As long as you break your program into enough small tasks, the scheduler usually does a good job of assigning tasks to threads to balance load. With thread-based programming, you are often stuck dealing with load-balancing yourself, which can be tricky to get right.
4. Higher–level thinking - the main advantage of using tasks instead of threads is that they let you think at a higher, task-based, level. With thread-based programming, you are forced to think at the low level of physical threads to get good efficiency, because you have one logical thread per physical thread to avoid undersubscription or oversubscription. You also have to deal with the relatively coarse grain of threads. With tasks, you can concentrate on the logical dependences between tasks, and leave the efficient scheduling to the scheduler.

Each task has a method execute()
Definition should do the work of the task
Return either NULL or a pointer to the next task to run
Once a thread starts running execute(), the task is bound to that thread until execute() returns
During that period, the thread serves other tasks only when it has to wait for some event 

To summarize, the task scheduler's fundamental strategy is 'breadth-first theft and depth-first work". The breadth-first theft rule raises parallelism sufficiently to keep threads busy. The depth-first work rule keeps each thread operating efficiently once it has sufficient work to do.

The scheduler maps these onto physical threads. The mapping is non-preemptive. 
Each task has a method execute(). Once a thread starts running execute(), the task is bound to that thread until execute() returns. 
During that time, the thread services other tasks only when it waits for completion of child tasks or nested parallel constructs, as described below. While waiting, it may run any available task, including unrelated tasks created by this or other threads.

***************
https://www.threadingbuildingblocks.org/docs/help/reference/task_scheduler.html
*************

continuation passing style ::The parent task creates child tasks and specifies a continuation task to be executed when the children complete. The continuation inherits the parent's ancestor. The parent task then exits; it does not block on its children. The children subsequently run, and after they (or their continuations) finish, the continuation task starts running.

 blocking  style:: A second pattern is "blocking style", which uses implicit continuations. It is sometimes less efficient in performance, but more convenient to program. In this pattern, the parent task blocks until its children complete, as shown in the figure below.
                  . While the blocked parent task remains in the stack, the thread can steal and run another task. 

**********
https://www.threadingbuildingblocks.org/docs/help/tbb_userguide/Continuation_Passing.html
*********
*******
https://www.threadingbuildingblocks.org/docs/help/tbb_userguide/Concurrent_Queue_Classes.html
*********


PADHLENA LAST KE 2 LECTURES OF TBB 


*****************************************CUDA AND GPU**********************************************


CPU COMPARISON WITH GPU :::

CPU Aims to reduce memory latency with increasingly large and complex memory hierarchy
Disadvantages
The Intel I7-920 processor has some 8 MB of internal L3 cache, almost 30% of the size of the chip 
Larger cache structures increases the physical size of the processor
Implies more expensive manufacturing costs and increases likelihood of manufacturing defects
Effect of larger, progressively more inefficient caches ultimately results in higher costs to the end user

GPU’s higher performance and energy efficiency are due to different allocation of chip area
High degree of SIMD parallelism, simple in-order cores, less control/sync. logic, less cache/scratchpad capacity  --- less energy efficiency as now no need to maintain the extra cache structures 
ALSO DOES NOT WORK TO REDUCE LATENCY => BUT MAXIMISE THROUGHPUT

Clearly, we should be using GPUs all the time
GPUs can only execute some types of code fast
SIMD parallelism is not well suited for all algorithms
Works better when program Need lots of data parallelism, data reuse, & regularity
Code requires task parallelism
Code requires lots of branching logic or blocking due to IP/OP etc.

GPUs are harder to program and tune than CPUs
Mostly because of their architecture
Fewer tools and libraries exist
Memory hierarchy in GPU is not much transparent like that of CPU .. Like they have global memory , local memory , shared memory etc. 
 And also GPU assumes that the code has sequential access to the memory as code requiring lot of branching etc. don’t really work well for GPU



CUDA => STREAMING MULTIPROCESSORS => MANY STREAMING PROCESSORS OR PROCESSING ELEMENTS 

COMPUTE CAPABILITY => SM VERSION NO.=> GPUs are not standalone yet
CPU = SMALL  COMPLICATED TASKS 

Partition the problem into coarse sub-problems that can be solved independently
Assign each sub-problem to a “block” of threads to be solved in parallel
Each sub-problem is also decomposed into finer work items that are solved in parallel by all threads within the “block”

return type of kernel fns => void 

__device__ float deviceFunc() => callable by device and executed by device
__global__ void deviceFunc() by host and by device
__host__ float hostFunc() by host and host

How to find out the relation between thread ids and threadIdx?
1D: tid = threadIdx.x
2D block of size (Dx, Dy): thread ID of a thread of index (x, y) is (x + yDx)
3D block of size (Dx, Dy, Dz): thread ID of a thread of index (x, y, z) is (x + yDx + zDxDy)
Here x is the number of  blocks  which can be represented as dim3 x(3,2) = because the horizontal axes represents x axis and the vertical axes represents y axis 

mykernel<<<numBlks, thrdsBlk>>>()


dim3 thrdsBlk(x, y, z);// if 2-d then horizontal=x vertical=y then z=1
dim3 numBlks(p, q); 

to change the specified number of blocks from N/TPB to (N+TPB-1)/TPB to ensure
that the block number is rounded up. This also means that the grid index can exceed the maximum legal
 array index, and a control statement is then needed in the kernel to prevent execution with indices
  that would exceed array dimensions and produce segmentation violation errors.

  int j = blockIdx.x * blockDim.x + threadIdx.x;
  int i = blockIdx.y * blockDim.y + threadIdx.y;
  C[i][j] = A[i][j] + B[i][j];

  One strategy is to change the number of blocks from N/TPB to (N+TPB-1)/TPB to ensure rounding up

const int Nx = 11; // not a multiple of
threadsPerBlock.x
const int Ny = 5; // not a multiple of
threadsPerBlock.y

//////////////////////////////////////////////
dim3 threadsPerBlock(4, 3, 1);
dim3 numBlocks(x, y, z);

// assume A, B, C are allocated Nx x Ny float arrays
matrixAdd<<<numBlocks, threadsPerBlock>>>(A, B, C);

gen , no of blocks in a gird = (n/tpb,n/tpb)
but , here , not good to do this as n%tpb !=0 so we use n+tpb-1/tpb
so => for x => 11+4-1/4 = 3 and y=> 5+3-1/3=2 so 3*2 should be grid size...

when kernel launched => blocks assigned to gpu -> Each GPU has a limit on the number of blocks that can be assigned to each SM 
=> then for each block decide no of threads to work

threads of block work concurently = not necessarily synchronzed with other blocks on SM => but threads in warp perform in synchronized manner.


cuda prg part-4 -> slide 7

Warps differ from set of threads in the sense that –>
 same instruction is fetched for all threads in a warp
  whereas a group of threads individually can work on diff instructions if they want.

Individual threads in a warp have their own instruction address counter and register state

What these threads are different from a CPU thread is that CPU threads can each execute different tasks at the same time. 
But GPU threads in a single warp can only execute one same task. For example, if you want to perform 2 operations,
 c = a + b and d = a * b. And you need to perform these 2 calculations on lots of data. You can’t assign one warp to perform
  both calculations at the same time. All 32 threads must work on the same calculation before moving onto the next calculation, 
  although the data the threads process can be different, for example, 1 + 2 and 45 + 17, they have to all work on the addition
   calculation before moving onto the multiplication.

Also divergence occurs only in a warp since , threads will be executing parallely only within warp


prog part 4 => slide 19
Assume that a CUDA device allows up to 8 blocks and 1024 threads per SM, whichever becomes a limitation first
It allows up to 512 threads in each block 
Say for the matrix-matrix multiplication kernel, should we use 8x8, 16x16, or 32x32 thread blocks?



Aim to maximise no of warps = i.e. maximise parallelism

8x8 threads/block
If we use 8x8 blocks, each block would have only 64 threads
We will need 1,024/64=16 blocks to fully occupy an SM
Since there is a limitation of up to 8 blocks in each SM, we have 64x8 = 512 threads/SM
There will be fewer warps to schedule around long-latency operations
Implies that the SM execution resources will likely be underutilized
16x16 threads/block
16x16 blocks give 256 threads per block
Each SM can take 1024/256=4 blocks, which is within the 8-block limitation 
Reasonable configuration since we have full thread capacity in each SM and a maximal number of warps for scheduling around the long-latency operations
32x32 threads/block
32x32 blocks give 1024 threads in each block, exceeding the limit of 512 threads per block

prog part -5 slide 6,7

Register =>Faster than shared memory - Private to a thread


Shared memory =>On-chip memory space, requires load/store operations
Visible to all threads in a block

If an if-then-else statement is present inside the kernel(Along with the _Syncthread(),
 then either all the threads will take the if path, or all the threads will take the else path. 
 This is implied. As all the threads of a block have to execute the sync method call, 
 if threads took different paths, then they will be blocked forever.
similar to what happens with wharf threads => w/o use of _Syncthreads.

Cuda device synchronize ()  takes care of the fact that everything which is present in the kernel function 
gets executed and printed to the standard output (stdout) before the whole program finishes and gets exited 
as was used in the hello world program.

atomicAdd = no other operation can access the address being used in this add function until the whole add operation is completed.

A stream in CUDA is a sequence of operations that execute on the device in the order in which they are issued by the host code. 

The default stream is different from other streams because it is a synchronizing stream with respect to operations on 
the device: no operation in the default stream will begin until all previously issued operations in any stream on the 
device have completed, and an operation in the default stream must complete before any other operation 
(in any stream on the device) will begin.
If there are 2 cuda operations on 2 diff streams , they can easily run concurrently … unless 
and until they themselves are default streams or they have a default stream in between them as then .. 
First ,  the first operation will happen and then the default stream will execute and then the second
 mentioned operation will happen after that. 
Everything in first operation needs to be completed ,, then everything of default stream to be completed 
and then only the new next after operation will be executed.


cudaMemcpy(d_a, h_a, numBytes, cudaMemcpyHostToDevice);// synchronous call and so cpu waits till it gets completed
kernel1<<<1,N>>>(d_a); // asynchronous call cpu goes ahead to the 3rd statement
cudaMemcpy(h_res, d_a, numBytes, cudaMemcpyDeviceToHost); // cpu will wait for the kernel to complete its functioning and so will wait till gpu is available
so appending any function of cpu before the 3rd step will not keep cpu waiting

kernel<<< blocks, threads, bytes >>>();     // default stream
 kernel<<< blocks, threads, bytes, 1 >>>();  // stream 1


 The CUDA stream API has multiple less severe methods of synchronizing the host with a stream.  The function cudaStreamSynchronize(stream) can be used to block the host thread until all previously issued operations in the specified stream have completed. The function cudaStreamQuery(stream) tests whether all operations issued to the specified stream have completed, without blocking host execution. The functions cudaEventSynchronize(event) and cudaEventQuery(event) act similar to their stream counterparts, except that their result is based on whether a specified event has been recorded rather than whether a specified stream is idle. 
You can also synchronize operations within a single stream on a specific event using cudaStreamWaitEvent(event)




