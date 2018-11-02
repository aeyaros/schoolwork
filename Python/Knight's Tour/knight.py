#!/usr/bin/python
# Andrew Yaros - CS265 - Assignment 2 - Knight's Tour
# Linux:
# 4.4.0-96-generic #119-Ubuntu SMP Tue Sep 12 14:59:54 UTC 2017 x86_64 x86_64 x86_64 GNU/Linux
# Python:
# 2.7.12 (default, Nov 19 2016, 06:48:10) 
# [GCC 5.4.0 20160609]
# :)

#importing stuffs
import sys
import random

#argument handling
argc = len( sys.argv )

ydim = int(sys.argv[1]) #y dimension of the board - rows
xdim = int(sys.argv[2]) #x dimension of the board - columns
maxattempts = int(sys.argv[3]) #the max number of moves to make
if (xdim < 1) or (ydim < 1) or (maxattempts < 1) :
	print "Arguments must be 1 or higher"

###### The pointer lives here ######
ypointer = 0
xpointer = 0

# basic move functions
def moveDown ( yvar, num ) :
	global ydim
	if ( yvar + num ) < ydim :
		return (yvar + num)
	else : 
		return -1
def moveUp( yvar, num ) :
	if ( yvar - num ) >= 0 :
		return (yvar - num)
	else :
		return -1
def moveRight ( xvar, num ) :
	global xdim
	if ( xvar + num ) < xdim :
		return (xvar + num)
	else :
		return -1
def moveLeft( xvar, num ) :
	if ( xvar - num ) >= 0 :
		return (xvar - num)
	else :
		return -1

#list of move types; this function should only be called with n between 1 and 8
#returns 0 if a move is valid and nonzero if a move is not valid
def knightmoves(n) :
	global ypointer
	global xpointer
	if n == 1 : #UUL
		ypointer = moveUp(ypointer, 2)
		xpointer = moveLeft(xpointer, 1)
	elif n == 2 : #ULL
		ypointer = moveUp(ypointer, 1)
		xpointer = moveLeft(xpointer, 2)
	elif n == 3 : #UUR
		ypointer = moveUp(ypointer, 2)
		xpointer = moveRight(xpointer, 1)
	elif n == 4 : #URR
		ypointer = moveUp(ypointer, 1)
		xpointer = moveRight(xpointer, 2)
	elif n == 5 : #DDL
		ypointer = moveDown(ypointer, 2)
		xpointer = moveLeft(xpointer, 1)
	elif n == 6 : #DLL
		ypointer = moveDown(ypointer, 1)
		xpointer = moveLeft(xpointer, 2)
	elif n == 7 : #DDR
		ypointer = moveDown(ypointer, 2)
		xpointer = moveRight(xpointer, 1)
	elif n == 8 : #DRR
		ypointer = moveDown(ypointer, 1)
		xpointer = moveRight(xpointer, 2)
	else :
		print "ERROR ERROR ERROR"
		return 2 #this should NEVER happen
	if (ypointer < 0) or (xpointer < 0) :
		return 1 # move out of bounds
	else :
		return 0 # move worked

#creation of the chessboard; a list of rows (ydim rows)
#each row has several columns (xdim cols)
#refer to chessboard using chessboard[Y coordinate][X coordinate]
chessboard = []

#function resets the chessboard; adds rows to chessboard list
#there are xdim 0s in each row (xdim = number of columns)
def chessboardreset() :
	global chessboard
	chessboard = []
	for ycord in range(0, ydim, 1) :
		chessboard.append([])
		for xcord in range(0, xdim, 1) :
			chessboard[ycord].append(0)

#function to print chessboard and make things look very nice and fancy
def printboard() :
	global ydim
	global xdim
	global chessboard
	for ycord in range(0, ydim) :
		for xcord in range(0, xdim) :
			if chessboard[ycord][xcord] == 0 :
				if xdim * ydim < 100 :
					print " x",
				else :
					print "  x",
			else :
				if xdim * ydim < 100 :
					print str(chessboard[ycord][xcord]).rjust(2),
				else :
					print str(chessboard[ycord][xcord]).rjust(3),
		print '\n',

#function to pick random number between 1 and 8 
random.seed(a=	None)
def randomselect() :
   return random.randint(1,8)

#variable to track attempts tried so far
tourcount = 0
#variable to break the loop if success is found
foundsuccess = 0

######## START TOURING ########
while (tourcount <= maxattempts) and (foundsuccess == 0) :
	tourcount = tourcount + 1
	chessboardreset()
	#set coordinate variables for knight location
	ycurrent = 0
	xcurrent = 0
	chessboard[0][0] = 1
	#move counter starts at 2 because the spot on the board with 1 is already at coordinates 0,0
	movecount = 2
	endtour = 0 #variable to break tour loop
	#this loop represents one move on the board; it continues until no more moves can be made
	while endtour == 0 :
		templist = [] #clear list of tried moves
		#set pointer to current location
		ypointer = ycurrent
		xpointer = xcurrent
		while (len(templist) < 8) : # and (innerbreak == 0) : #keep trying as long as we haven't tried all 8 moves
			tempval = randomselect() #set tempval to num between 1 and 8
			while tempval in templist :
				tempval = randomselect() #keep generating until you get a move you haven't tried yet
			templist.append(tempval) # add this move to temp list of attempted moves
			#reset pointer to current position
			ypointer = ycurrent
			xpointer = xcurrent
			#move the pointer and check return value for knightmoves function
			tempreturn = knightmoves(tempval) #the side effects of this function should move the pointer
			if (chessboard[ypointer][xpointer] == 0) and (tempreturn == 0) : #the move is valid, continue
				#update current location to the pointer value
				ycurrent = ypointer
				xcurrent = xpointer
				chessboard[ypointer][xpointer] = movecount #write move number to current spot on chessboard
				movecount = movecount + 1 #increment movecount
				templist = [] #reset templist
				break #the current move is finished
		if len(templist) == 8 : #we have tried all eight possible moves; there is nowhere else to move
			endtour = 1 #end tour loop
	#check board for empty spaces
	spaceleft = 0
	for ycord in range(0, (ydim)) :
		for xcord in range(0, (xdim)) :
			if chessboard[ycord][xcord] == 0 :
				spaceleft = 1
	#if we don't find empty space, we have succeeded
	if spaceleft == 0 :
		foundsuccess = 1
####end of loop of tour attempts
#if the loop ends and foundsuccess = 1, then we found success
if foundsuccess == 1 :
	print "FOUND SUCCESS AFTER", tourcount, "ATTEMPTS!"
else :
	print "FAIL:"
printboard() #ALL DONE! "KNIGHTS TO MEET YOU" LOL
