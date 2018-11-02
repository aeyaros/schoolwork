/*=======================================
=========================================
Andrew Yaros  --  CS 171  --  Winter 2017
Homework 4 - "Lets Make A Deal" simulator
=========================================
=======================================*/


#include <iostream>
#include <iomanip>
#include <cstdlib>
#include <vector>
#include <string>
#include <ctime>


using namespace std;


//globals
const char C = 'C'; // for "car"
const char G = 'G'; // for "goat"
const int HOW_MANY_DOORS = 3; // this should be 3.0, obviously
const double PERCENT_NUMBER = 100.0; // a percentage is x out of PERCENT_NUMBER


/**
 Genertates a random integer between 1 and a number
 @param numberOfDoors The number of doors)
 @return an integer between 1 and a specified number
 */
int random_num(int numberOfDoors)
{
	int doornumber = (rand() % numberOfDoors) + 1;
	return doornumber;
}


/**
 Randomly assigns 'C' to a door; assigns 'G' to all others
 @param door1 First door
 @param door2 Second door
 @param door3 Third door
 */
void setupDoors(char &door1, char &door2, char &door3)
{
	//set all doors to g; only real random decision in this function is which door will be C
	door1 = G;
	door2 = G;
	door3 = G;
	
	//this determines which door will be C
	//randomDoor will be either 1, 2, or 3
	int randomDoor = random_num(HOW_MANY_DOORS);
	
	//sets the appropriate variable to C based on which number randomDoor is:
	if (randomDoor == 3)
	{
		door3 = C;
	}
	else if (randomDoor == 2)
	{
		door2 = C;
	}
	else //if (randomDoor == 1)
	{
		door1 = C;
	}
}


/**
 Takes a person's door choice (either player or Monty) and assigns the appropriate character (G for goat, C for car)
 @param door1 The character (g or c) for door 1
 @param door2 The character (g or c) for door 2
 @param door3 The character (g or c) for door 3
 @param doorVariable The number of the door a person picked
 @param charVariable Variable to hold the character (g or c) of that door
 */
void assignDoorCharacter(char door1, char door2, char door3, int doorVariable, char &charVariable)
{
	if (doorVariable == 3)
	{
		charVariable = door3;
	}
	else if (doorVariable == 2)
	{
		charVariable = door2;
	}
	else //if (doorVariable == 1)
	{
		charVariable = door1;
	}
}


/**
 Determines which door the player picks and which door Monty picks
 @param door1 Value of door 1
 @param door2 Value of door 2
 @param door3 Value of door 3
 @param doorPlayer The door the player picks
 @param doorMonty The door Monty picks
 */
void pickDoorChoices(char door1, char door2, char door3, int &doorPlayer, int &doorMonty)
{
	//The player chooses a door
	doorPlayer = random_num(HOW_MANY_DOORS); //picks door
	
	//character variable for monty's pick
	char montyDoorChar = ' ';

	//Monty needs to pick a door which isn't the player's door and isn't the car door
	do
	{
		doorMonty = random_num(HOW_MANY_DOORS); //Monty picks a door
		assignDoorCharacter(door1, door2, door3, doorMonty, montyDoorChar);
	}
	while (montyDoorChar == C || doorMonty == doorPlayer);
	//if the character is C, or Monty chooses the same door as the player did, then Monty needs to repick
}


/**
 Determines if the player picked a door with character C (a door with a car); if true then the game is won, otherwise it is lost
 @param doorPlayer The number of the door the player picked
 @param door1 Character value for door 1
 @param door2 Character value for door 2
 @param door3 Character value for door 3
 @return whether the game is won or not
 */
bool didPlayerWin(char door1, char door2, char door3, int doorPlayer)
{
	char doorCharacter;
	assignDoorCharacter(door1, door2, door3, doorPlayer, doorCharacter);
	
	if (doorCharacter == C)
	{
		return true;
	}
	else
	{
		return false;
	}
}


/**
 Picks the door that hasn't been chosen by anyone yet
 @param door1 Character value of door 1
 @param door2 Character value of door 2
 @param door3 Character value of door 3
 @param doorPlayer Character value of the door the player chose
 @param doorMonty Character value of the door Monty chose
 @return the number of the door which hasn't been chosen
 */
int guessAgain(char door1, char door2, char door3, char doorPlayer, char doorMonty)//, int &secondGuess, char &secondGuessChar)
{
	//variable for door number
	int secondGuess;
	
	//pick the final door
	do
	{
		secondGuess = random_num(HOW_MANY_DOORS); //second guess
		//assignDoorCharacter(door1, door2, door3, secondGuess, secondGuessChar);
	}
	while (secondGuess == doorMonty || secondGuess == doorPlayer);
	//keep guessing as long as the door is one of the ones already guessed
	
	return secondGuess;
}


/**
 Simulates the game
 @param numberOfGames The number of times to run the simulation
 @param secondG Whether a second guess is made or not
 @return the percentage of games won
 */
double guessSimulation(int numberOfGames, bool secondG)
{
	//door character variables
	char door1;
	char door2;
	char door3;
	
	//player door number variables
	int playerDoor;
	int montysDoor;
	
	//boolean for winning or loosing
	bool gameResult;
	
	//second guessing integer variable
	int secondGuess;
	
	//counter
	double gamesWon = 0;
	
	//play the game many times
	for (int i = 1; i <= numberOfGames; i++)
	{
		//set up doors
		setupDoors(door1, door2, door3);
		
		//pick doors
		pickDoorChoices(door1, door2, door3, playerDoor, montysDoor);
		
		//should a second guess be made?
		if (secondG == true)
		{
			secondGuess = guessAgain(door1, door2, door3, playerDoor, montysDoor);
			playerDoor = secondGuess;
		}
		
		//did the player win?
		gameResult = didPlayerWin(door1, door2, door3, playerDoor);
		
		if (gameResult == true)
		{
			gamesWon++;
		}
	}
	
	//calculate and return percentage of games won
	double percentageWon = (gamesWon / numberOfGames) * PERCENT_NUMBER;
	return percentageWon;
}


/*====================================
//////////// MAIN PROGRAM ////////////
====================================*/
int main()
{
	//random starting point
	srand(time(NULL));
	
	//titles
	cout<< "===============================================" << endl
	<< "   \"Let's make a deal!\" simulation program   " << endl
	<< "Andrew Yaros - Homework 4 - CS171 - Winter 2017" << endl
	<< "===============================================" << endl << endl;
	
	//information
	cout<< "Hey, remember that game show with Monty Hall?" << endl
	<< "There are three doors. One has a car behind" << endl
	<< "it and the other two have goats behind them." << endl
	<< "You start by choosing a door. Then, Monty will" << endl
	<< "choose one of the other doors, which will have" << endl
	<< "a goat behind it. You now have the choice to" << endl
	<< "keep your original guess or to choose the" << endl
	<< "remaining door. Now, just one of the two has a" << endl
	<< "goat, and the other has a car." << endl << endl
	<< "===============================================" << endl << endl
	<< "The question is, are you more likely to win if" << endl
	<< "you stick with your original guess or if you" << endl
	<< "make a second guess?" << endl << endl
	<< "===============================================" << endl << endl;
	
	//number of times to run each sumulation function (each experiment of 10000)
		//the table will not show every one of these expriments but they will all be included
		//when calculating the final average of all percentages
	const int HOW_MANY_EXPERIMENTS = 100;
	
	//how many times should the game be played during the experiment (5000 for each scenario, 10000 times total)
	const int HOW_MANY_GAMES = 5000;
	
	//values for percentages
	double firstPercentage;
	double secondPercentage;
	
	//for calculating average percentages
	double firstTotalPercent = 0;
	double secondTotalPercent = 0;
	
	//vectors to hold percentage values, with 100 + 1 slots
	vector<double> firstGuessVector(PERCENT_NUMBER + 1);
	vector<double> secondGuessVector(PERCENT_NUMBER + 1);
	
	//how often should we take one of the percentage values
	int tableLengthFactor = 4; // the number of rows in a table is 100 / tableLengthFactor
	int resultRetentionFactor = (tableLengthFactor * HOW_MANY_EXPERIMENTS) / PERCENT_NUMBER;
	
	//Counter for vectors: important! This will be used when drawing the table
	int vectorCounter = 1;
	
	//main experiment loop
	for (int n = 1; n <= HOW_MANY_EXPERIMENTS; n++)
	{
		//play the game 10000 times
		firstPercentage = guessSimulation(HOW_MANY_GAMES, false); //when the player chooses their first guess
		secondPercentage = guessSimulation(HOW_MANY_GAMES, true); //when the player makes a second guess
		
		//add results to vectors to make tables
		if (n % resultRetentionFactor == 0) //...but not every result
		{
			firstGuessVector[vectorCounter] = firstPercentage;
			secondGuessVector[vectorCounter] = secondPercentage;
			vectorCounter++;
		}
		
		//add percentages to totals
		firstTotalPercent += firstPercentage;
		secondTotalPercent += secondPercentage;
	}
	
	//calculate the average of percentages
	double firstAveragePercent = (firstTotalPercent / (HOW_MANY_EXPERIMENTS * PERCENT_NUMBER) ) * PERCENT_NUMBER;
	double secondAveragePercent = (secondTotalPercent / (HOW_MANY_EXPERIMENTS * PERCENT_NUMBER) ) * PERCENT_NUMBER;
	
	//table values
	int numberColumnWidth = 2;
	int percentColumnWidth = 5;
	int percentColumnPrecision = 2;
	
	//more info
	cout << "Strategy 1 = Choose first guess" << endl;
	cout << "Strategy 2 = Make a second guess" << endl << endl;
	
	cout << "The program will run " << HOW_MANY_EXPERIMENTS << " experiments. Each" << endl;
	cout << "experiment will play through the game" << endl;
	cout << (HOW_MANY_GAMES + HOW_MANY_GAMES) << " times ("<< HOW_MANY_GAMES << " for each strategy)." << endl << endl;
	cout << "===============================================" << endl << endl;

	//table header
	cout << "Here are " << PERCENT_NUMBER / tableLengthFactor << " experiment results showing how" << endl;
	cout << "often each strategy wins you a car:" << endl << endl;
	cout << "	 #  Strategy 1   Strategy 2" << endl;
	cout << "	---------------------------" << endl;
	
	//draw each row of the table
	for (int x = 1; x <= (vectorCounter-1); x++)
	{
		cout << "	" << fixed << setfill(' ') << setw(numberColumnWidth) << x << "     " << fixed
			<< setw(percentColumnWidth) << setprecision(percentColumnPrecision)
			<< setfill('0') << firstGuessVector[x] << " %      " << fixed << setw(percentColumnWidth)
			<< setprecision(percentColumnPrecision) << setfill('0') << secondGuessVector[x] << " %" << endl;
	}
	
	//table footer
	cout << "	---------------------------" << endl << endl;

	//show averages:
	cout << "Based on the results of all " << HOW_MANY_EXPERIMENTS << " experiments:" << endl << endl;
	
	cout << "	Strategy 1 works " << fixed << setfill('0') << setprecision(percentColumnPrecision)
		 << firstAveragePercent << "% of the time." << endl << endl;
	
	cout << "	Strategy 2 works " << fixed << setfill('0') << setprecision(percentColumnPrecision)
		 << secondAveragePercent << "% of the time." << endl << endl;
	
	//which strategy is best?
	if (secondAveragePercent > firstAveragePercent)
	{
		cout << "===============================================" << endl << endl;
		cout << "Making a second guess is the best strategy." << endl << endl;
		cout << "===============================================" << endl;
	}
	else
	{
		cout << "===============================================" << endl << endl;
		cout << "Choosing your first guess is the best strategy." << endl << endl;
		cout << "===============================================" << endl;
	}
	
	//all done
	cout << endl << endl;
	return 0;
}
