/*==========
ANDREW YAROS
CS 172  HW 3
SPRING  2017
TIC.H header
==========*/

/*=============================================
CS 172 - Assignment 3 - Tic-Tac-Toe
Main File by Mark Boady
Drexel University 2016
You may add new methods (public or private) and
new private attributes.
You may NOT remove or change any methods given.
=============================================*/

#ifndef _TIC_TAC_TOE_
#define _TIC_TAC_TOE_

#include <vector>
#include <iostream>
#include <string>
#include "symbol.h"

using namespace std;

class tBoard
{
	private:
		//rows
		vector<symbol> row0_;
		vector<symbol> row1_;
		vector<symbol> row2_;
	
		//symbol variable for winner
		//will remain blank unless someone wins
		//if the board fills up and no one wins, its a tie
		symbol winner_ = BLANK;
	
	public:
		//Default Constructor
		//Makes a board with all blank spaces
		tBoard();
	
		//changes an item in a row to a symbol
		//enter the vector of the row, then the column (0,1,2), then the symbol X or O)
		void setUpRow(vector<symbol> &rowVector, int colNumber, symbol symbolValue);
	
		//outputs the symbol at these coordinates
		symbol getRowItem(int x, int y) const;
	
		//Makes a move on the board
		//X is the row and y is the column
		//m is the symbol to place (either X or O)
		//It returns true if the move was made
		//If the move is illegal, return false and do not change the table
		bool move(symbol m, int x, int y);
	
		//sets winner variable to a symbol
		void setWinner(symbol m);
	
		//Returns true if the game is over
		//This could be because of a winner or because of a tie
		bool game_over();
	
		//Returns who won X or O.
		//If the game was a tie, return  BLANK
		symbol winner();
};
//Overload the output operator
ostream & operator<<(ostream& os, const tBoard& myTable);

#endif
