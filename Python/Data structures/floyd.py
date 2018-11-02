#!/usr/bin/env python

#Andrew Yaros
#PA 3 - Problem 7

#Floyd's algorithm with option of recovering the paths
#test with problem 6 of review 2

#meant to work with adjmatrix ADT from problem 5
from adjmatrix import *

def shortest(C):
	n = C.N #assuming A C and P are adjmatrixes with equal size
	P = adjmatrix(n)
	A = adjmatrix(n)
	for i in range(n):
		for j in range(n):
			A.matrix[i][j] = C.matrix[i][j]
			P.matrix[i][j] = 0
	for i in range(n):
		A.matrix[i][i] = 0
	for k in range(n):
		for i in range(n):
			for j in range(n):
				if (A.matrix[i][k] + A.matrix[k][j] < A.matrix[i][j]):
					A.matrix[i][j] = A.matrix[i][k] + A.matrix[k][j]
					P.matrix[i][j] = k+1 #because index value is 1 less than actual value

	return A, P

if __name__ == '__main__':
	#example from review 2 problem 6
	#A[1,2]=4, A[1,3]=1, A[1,4]=5, A[1,5]=8, A[1,6]=10, A[3,2]=2, A[4,5]=2, A[5,  +    +6]=1.
	
	prob6 = adjmatrix(6)
	prob6.insert(1,2,4)
	prob6.insert(1,3,1)
	prob6.insert(1,4,5)
	prob6.insert(1,5,8)
	prob6.insert(1,6,10)
	prob6.insert(3,2,2)
	prob6.insert(4,5,2)
	prob6.insert(5,6,1)
	
	print "TESTING FLOYD'S ALGORITHM"
	
	print "Matrix C (From review 2 problem 6):"
	prob6.printmatrix()
	
	A = shortest(prob6)[0]
	P = shortest(prob6)[1]
	
	print "Matrix A:"
	A.printmatrix()
	
	print "Matrix P"
	P.printmatrix()

