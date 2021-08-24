To compile unoptimized C++ code:
g++ sql.cpp -o sql -pthread 

To run the above code with 1 thread:
time ./sql 1

To run the above code with 2 threads:
time ./sql 2

To run the above code with 4 threads:
time ./sql 4


To compile unoptimized C++ code:
g++ opt.cpp -o opt -pthread

To run the above code with 1 thread:
time ./opt 1

To run the above code with 2 threads:
time ./opt 2

To run the above code with 4 threads:
time ./opt 4

g++ algo.cpp -o algo 
./algo

To generate Data Dependence Graph (Input is HorseIR file, output is DataDependenceGraph.csv)
python3 HorseToGraph.py

