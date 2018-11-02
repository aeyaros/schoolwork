#!/usr/bin/env python

#Andrew Yaros
#PA 3 - Problem 5
#Dijkstra's shortest paths algorithm w/ adjacency matrix as representation of the graph
#test with problem 6 from review 2


#book says the matrix should be boolean for the adjacency matrix version of the ADT
#matrix can be treated as boolean by whether or not a space is 0 (False) or not 0 (True)
#but for a labeled graph; I have to account for the cost of each path
#so the value of a spot on the graph can just be the cost

#because indicies in python arrays start at 0:
#indices which are brought into the function as arguments do not and should not start at 0; they should start at 1, as is the case for example matricies in the book
#when an argument is passed through, when we access the arrays with that value we actually subtract 1 from the value when we do so
#if i is "x" coordinate and j is "y" coordinate, then i corresponds to columns and j corresponds to rows
class adjmatrix:
	matrix = None #array for matrix
	N = 0 #for the size of the matrix
	def __init__(self, n): #create an n x n matrix
		self.N = n #size attribute is set to n
		self.matrix = [] #empty array
		for i in range(self.N): #i = 0, i < n
			self.matrix.append([]) #create new column with empty array
			for j in range(self.N): #j = 0, j < n
				self.matrix[i].append(0) #create j rowss in ith column
	
	def insert(self, i, j, x):
		if(i > self.N or j > self.N):
			print "Out of bounds error"		
		else: #i and j correspond to indices i-1 and j-1
			self.matrix[i-1][j-1] = x
		
	def retrieve(self, i, j):
		if(i > self.N or j > self.N):
			print "Out of bounds error"
		else: #i and j correspond to indices i-1 and j-1
			return self.matrix[i-1][j-1]			
	
	def delete(self, i, j):
		if(i > self.N or j > self.N):
			print "Out of bounds error"	
		else: #i and j correspond to indices i-1 and j-1
			self.matrix[i-1][j-1] = 0
	
	def empty(self):
		for i in range(self.N): #i = 0, i < n
			for j in range(self.N): #j = 0, j < n
				if self.matrix[i][j] is not 0: #if something is in the matrix
					return False #not empty
		return True #otherwise nothing is there, so empty
	
	def makenull(self):
		self = adjmatrix()
	
	def FIRST(self, v): #v is an input, offset by 1; i is internal and is not offset
		for i in range(self.N): #i = 0, i < n
			if self.matrix[v-1][i] is not 0: #if A[v, i]; indices are 1 less than inputs 
				return i + 1 #offset by 1 since i is the internal index
		return 0 #v has no adjacent vertex
		
	def NEXT(self, v, i):
		if i >= self.N:
			print "Out of bounds error"
			return 0
		else:
		
			for j in range(i,self.N): #j = i, j < n - i is already 1 larger than it should be already, becuase it is the external input, where j refers to the internal indices 
				if self.matrix[v-1][j] is not 0:
					return j
			return 0
		
	def VERTEX(v, i): #From the book: "Example 6.5. If the adjacency matrix representation is chosen, VERTEX(v, i) returns i."
		return i #...
	
	def printmatrix(self, x = 2):
		#var x is optional used to format for numbers with x decimal places
		print " " * (x + 2), #top corner
		for i in range(self.N): #indices
			print repr(i + 1).rjust(x),
		print "" #to go to new line
		#horizontal seperator line
		print ' ' * x, '-' * ((x+1) * (i+1) + 1) 
		#now print each row of the table
		for j in range(self.N):
			print repr(j + 1).rjust(x), "|", #row index
			for i in range(self.N): #print each item in row
				if(self.matrix[i][j] == 0):
					print ' ' * x,
				else:
					print repr(self.matrix[i][j]).rjust(x) ,
			print "" #new line

#dijkstra's algorithm
def dijkstra(C):
	n = C.N #get size from list
	S = [1] #initialize S
	D = []  #initialize D
	for i in range(1, n): # i = 2 (index 1), i < n+1 (index n)  (i.e. <= n)
		D.append(C.matrix[0][i]) #initialize D from C
	for i in range(0, n-1): # i = 1 (index 0), i < n (index n-1) (i.e. <= n-1)
		#choose vertex w in V-S that D[w] is minimum
		w = 1 + D.index(min(D)) #get min fom D, get index of that min, add 1 since index value is 1 less than actual value
		S.append(w) #add w to S
		for v in S:
			D[v] = min(D[v], (D[w] + C.matrix[w][v]) )
	
	return D

if __name__ == '__main__':
	#TEST CODE
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
	
	print "Review 2 problem 6 matrix"
	prob6.printmatrix()
	
	print "TESTING DIJKSTRA'S ALGORITHM"
	
	D = dijkstra(prob6) 
	print D

