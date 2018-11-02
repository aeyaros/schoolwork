#!/usr/bin/env python
#Andrew Yaros
#PA 3 - Problem 3
#dictionary using binary search trees

class set:
	element = None #elementtype
	leftchild = None #pointer to left node
	rightchild = None #pointer to right node
	
	def __init__(self):
		self.element = None #elementtype
		self.leftchild = None  #pointer to left node
		self.rightchild = None #pointer to right node
	
	def empty(self):
		if self.element is None and self.leftchild is None and self.rightchild is None:
			return True
		else:
			return False
	
	def leaf(self):
		if self.leftchild is None and self.rightchild is None:
			return True
		else:
			return False
	
	def MEMBER(self, x):
		if (self.empty() == True): #null
			return False
		elif (x == self.element):
			return True
		elif (x < self.element):
			return self.leftchild.MEMBER(x)
		else: #(x.element < self.element)
			return self.rightchild.MEMBER(x)
	
	def INSERT(self, x):
		if (self.empty() == True): #null
			self.element = x
			self.leftchild = set() #initialize left
			self.rightchild = set() #initialize right
		elif (x < self.element):
			self.leftchild.INSERT(x)
		elif (x > self.element):
			self.rightchild.INSERT(x)
		else: # x is not < or > than A, so must be ==
			print x, " is already in A"
	
	def DELETEMIN(self): #remove smallest element
		if self.leftchild is None: #then A points to smallest element
			temp = self.element #get smallest element
			print temp, "is smallest"
			if self.rightchild is not None: 
				tempright = self.rightchild
				self.element = tempright.element
				self.leftchild = tempright.leftchild
				self.rightchild = tempright.rightchild
			return temp #return smallest element
		else: #node pointed to by A has a left child
			self.leftchild.DELETEMIN() #recursive call

	def DELETE(self, x):
		if (self.empty() == False): #not null
			#print "a"
			if (x < self.element):
				#print "b"
				self.leftchild.DELETE(x)
			elif (x > self.element):
				#print "c"
				self.rightchild.DELETE(x)
			elif (self.leftchild == None) and (self.rightchild == None):
				#print "d"
				self.element = None #delete leaf holding x
				self.leftchild = None
				self.rightchild = None
			elif (self.leftchild == None):
				#print "d"
				temp = set()
				temp.element = self.rightchild.element
				temp.leftchild = self.rightchild.leftchild
				temp.rightchild = self.rightchild.rightchild

				self.element = temp.element
				self.leftchild = temp.leftchild
				self.rightchild = temp.rightchild
			elif (self.rightchild == None):
				#print "e"
				temp = set()
				temp.element = self.leftchild.element
				temp.leftchild = self.leftchild.leftchild
				temp.rightchild = self.leftchild.rightchild
				
				self.element = temp.element
				self.leftchild = temp.leftchild
				self.rightchild = temp.rightchild
			else: #both children present
				#print "f"
				self.element = self.rightchild.DELETEMIN()
				
	def printpre(self): #print out preorder
		if (self.empty() == False):
			print self.element, ',',
			if (self.leaf() == False):
				if self.leftchild is not None:
					self.leftchild.printpre() , ',',
				if self.rightchild is not None:
					self.rightchild.printpre() , ',',
		#else:
		#	print "null ,",	

class dictbst: #dictionary ADT based on binary search tree
	bst = None
	def __init__(self):
		self.bst = set()
		
	def insert(self, x):
		self.bst.INSERT(x)
		
	def delete(self, x):
		if(self.bst.empty() == True): #null
			print "BST is already null"
		else:
			#elif (self.bst.MEMBER(x) == True): #delete the item
			self.bst.DELETE(x)
	#	else: #item not in tree
	#		print "Item not in BST"
	 
	def member(self, x):
		if(self.bst.MEMBER(x) == True):
			return True
		else:
			return False
	
	def makenull(self):
		self.bst = set()
	
	def printbst(self):
		self.bst.printpre() #print preorder of bst
		print "" #then go to new line

#TESTING
from random import *

if __name__ == '__main__':
	print "TESTING BST DICTIONARY"
	
	test1 = dictbst()
	record1 = []
	
	num = 20
	
	for a in range(num):
		x = random()
		x = int(x * 1000)
		record1.append(x)
		test1.insert(x)
	print ""
	test1.printbst()
		
	for i in range(num):
		print "removing ", record1[i]
		test1.bst.DELETE(record1[i])
		test1.printbst()
	
