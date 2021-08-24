import csv, os

os.chdir(os.path.dirname(os.path.abspath(__file__)))

output = []
node = dict()

with open("HorseIR") as f:
    for line in f:
        src = line[line.index('(')+1:line.index(')')].strip()
        tmp = line[line.index('t'):line.index(':')].strip()
        
        args = line[line.rindex('(')+1:line.rindex(')')].strip().split(',')
        for i in range(len(args)):
            args[i] = args[i].strip()

            # print(args[i])
            # print(node)
            # print()
            if args[i] in node:
                output.append([node[args[i]][1:], src[1:]])

        node[tmp] = src

# print(node)
# print(output)
with open("DataDependenceGraph.csv", "w", newline="") as f:
    writer = csv.writer(f)
    writer.writerows(output)

'''
(S0) t0 : bool = @geq ( c0 , 2010 - 09 - 01 : date );
(S1) t1 : bool = @leq ( c0 , 2010 - 09 - 30 : date );
(S2) t2 : bool = @and ( t0 , t1 );
(S3) t3 : f64 = @compress ( t2 , c1 );
(S4) t4 : f64 = @compress ( t2 , c2 );
(S5) t5 : f64 = @mul ( t3 , t4 );
(S6) t6 : f64 = @sum ( t5 );
'''