/*==========
ANDREW YAROS
CS 172  HW 3
SPRING  2017
TIC.CPP file
==========*/

#include <vector>
#include <iostream>
#include <string>
#include "symbol.h"
#include "tic.h"

using namespace std;

tBoard::tBoard()
{
	//add a blank to each row, 3 times
	for(int i = 0; i <= 2; i++)
	{
		row0_.push_back(BLANK);
		row1_.push_back(BLANK);
		row2_.push_back(BLANK);
	}
}


//changes an item in a row to a symbol
//enter the vector of the row, then the column (0,1,2), then the symbol X or O)
void tBoard::setUpRow(vector<symbol> &rowVector, int colNumber, symbol symbolValue)
{
	rowVector[colNumber] = symbolValue;
}


//gets item at these coordinates
symbol tBoard::getRowItem(int x, int y) const
{
	if (x == 0)
	{
		return row0_[y];
	}
	else if(x == 1)
	{
		return row1_[y];
	}
	else if(x == 2)
	{
		return row2_[y];
	}
	else
	{
		return BLANK;
	}
}


//Makes a move on the board
//X is the row and y is the column
//m is the symbol to place (either X or O)
//It returns true if the move was made
//If the move is illegal, return false and do not change the table
bool tBoard::move(symbol m, int x, int y)
{
	if( (getRowItem(x, y) == BLANK) &&
		(y == 0 || y == 1 || y == 2) )
	{
		if (x == 0)
		{
			setUpRow(row0_, y, m);
			return true;
		}
		else if(x == 1)
		{
			setUpRow(row1_, y, m);
			return true;
		}
		else if(x == 2)
		{
			setUpRow(row2_, y, m);
			return true;
		}
		else
			return false;
	}
	else
		return false;
}


//sets winner variable to a symbol
void tBoard::setWinner(symbol m)
{
	winner_ = m;
}


//Returns true if the game is over
//This could be because of a winner or because of a tie
bool tBoard::game_over()
{
	//first check for 3 in a row and 3 in a column
	for(int i = 0; i <= 2; i++)
	{
		if //check rows
			(getRowItem(i, 0) == getRowItem(i, 1) &&
			 getRowItem(i, 1) == getRowItem(i, 2) &&
			 getRowItem(i, 0) != BLANK)
		{
			setWinner(getRowItem(i, 0));
			return true;
		}
		else if //check columns
			(getRowItem(0, i) == getRowItem(1, i) &&
			 getRowItem(1, i) == getRowItem(2, i) &&
			 getRowItem(0, i) != BLANK)
		{
			setWinner(getRowItem(0, i));
			return true;
		}
	}
	
	//check to see if diagonals contain a win
	if //diagonal 1
		(getRowItem(0, 0) == getRowItem(1, 1) &&
		 getRowItem(1, 1) == getRowItem(2, 2) &&
		 getRowItem(0, 0) != BLANK)
	{
		setWinner(getRowItem(0, 0));
		return true;
	}
	
	if //diagonal 2
		(getRowItem(0, 2) == getRowItem(1, 1) &&
		 getRowItem(1, 1) == getRowItem(2, 0) &&
		 getRowItem(0, 2) != BLANK)
	{
		setWinner(getRowItem(0, 2));
		return true;
	}
	
	/*
	 we've checked everywhere for a win
	 if function still hasn't returned true by now
	 then game is not over as long as there is a blank space left
	 as soon as we find one blank space we know to continue
	 */
	for (int x = 0; x <= 2; x++) //go through each row
	{
		for(int y = 0; y <= 2; y++) //go through every item in each row
		{
			if(getRowItem(x, y) == BLANK)
			{
				return false;
			}
		}
	}
	//if no blanks are left, game is over
	return true;
}


//Returns who won X or O.
//If the game was a tie, return  BLANK
symbol tBoard::winner()
{
	return winner_;
}


//Overload the output operator
ostream & operator<<(ostream& os, const tBoard& myTable)
{
	os << endl
	<< " |  0  |  1  |  2  |" << endl
	<< " +-----------------+" << endl
	<< "0|  "
		<< myTable.getRowItem(0,0) << "  |  "
		<< myTable.getRowItem(0,1) << "  |  "
		<< myTable.getRowItem(0,2) << "  |" << endl
	<< " +-----------------+" << endl
	<< "1|  "
		<< myTable.getRowItem(1,0) << "  |  "
		<< myTable.getRowItem(1,1) << "  |  "
		<< myTable.getRowItem(1,2) << "  |" << endl
	<< " +-----------------+" << endl
	<< "2|  "
		<< myTable.getRowItem(2,0) << "  |  "
		<< myTable.getRowItem(2,1) << "  |  "
		<< myTable.getRowItem(2,2) << "  |" << endl
	<< " +-----------------+" << endl << endl;
	return os;
}
