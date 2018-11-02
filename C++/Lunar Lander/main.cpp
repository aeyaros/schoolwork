/*==============================================
////////////////////////////////////////////////
Andrew Yaros - CS 171 - Winter 2017 - Homework 5
LUNAR LANDER GAME - AMERICA FIRST, AMERICA FIRST

 
"America is the greatest country the world has
 ever seen. No other nation on God's green Earth
 had or will ever have the ingenuity and courage
 to land men on the moon. Believe me.“
 
	-Benjamin Franklin
 
 
 "We're here to take our country back."
 
	-Neil Armstrong

////////////////////////////////////////////////
==============================================*/

#include <iostream>
#include <fstream>
#include <string>
#include <cmath>

using namespace std;

//globals
const double GRAVITY = 5; //feet per second ^2
int minFuelUnits = 0; //pounds
int maxFuelUnits = 30; //pounds


/*=======================================
 ///////// FUNCTION DECLARATIONS /////////
 =======================================*/
void reportStatus(ostream &os, double time, double height, double velocity, double fuel, string name);
void updateStatus(double &velocity, double burnAmount, double &fuelRemaining, double &height);
void introduction(istream &is, ostream &os, string target, string replacement);
void touchdown(double &elapsedTime, double &velocity, double &burnAmount, double &height);
void touchDownStatus(ostream &os, double time, double velocity, double fuel);
void finalAnalysis(ostream &os, double velocity);
void show_title_text(ostream &os);


/*========================
 ////////// MAIN //////////
 ========================*/
int main()
{
	//title text
	show_title_text(cout);
	
	cout << "To log your session, please enter a name for" << endl << "your log: ";
	string logFileName;
	getline(cin, logFileName);
	logFileName = logFileName + ".txt";
	
	//create and open log file
	ofstream logStream;
	logStream.open(logFileName);
	
	//log file header
	logStream << "Lunar Lander Game log file: " << logFileName << endl << "By Andrew Yaros - CS 171 - Winter 2017" << endl << endl;
	cout << endl;
	
	//get ship name
	string shipName;
	cout << "Type a name for your ship and press enter:" << endl << endl << "	";
	getline(cin, shipName);
	cout << endl;
	logStream << "Ship name: " << shipName << endl << endl;
	
	//does user want instructions?
	string instructionPrompt;
	do
	{
		cout << "Do you want to see instructions?" << endl << "(Please type y/Y for yes or n/N for no) " << endl << endl << "	";
		cin >> instructionPrompt;
		cout << endl;
	} while (instructionPrompt != "y" && instructionPrompt != "n" && instructionPrompt != "Y" && instructionPrompt != "N");
	
	//if yes display instructions
	if (instructionPrompt == "y" || instructionPrompt == "Y")
	{
		logStream << "Did user want instructions? " << instructionPrompt << endl << endl;
		
		//instructions text file
		ifstream instructStream;
		instructStream.open("input.txt");
		
		//word in the file to be replaced
		string spacecraft_input_target = "$SPACECRAFT";
		
		//output
		introduction(instructStream, cout, spacecraft_input_target, shipName);
		introduction(instructStream, logStream, spacecraft_input_target, shipName);
		cout << endl << endl;
		logStream << endl << endl;
		
		instructStream.close();
	}
	else
	{
		logStream << "Did user want instructions? " << instructionPrompt << endl << endl;
	}
	
	//start of game
	cout << "***** START OF GAME *****" << endl << endl;
	logStream << "***** START OF GAME *****" << endl << endl;
	
	//set initial conditions
	double height = 1000;
	double velocity = 50;
	double fuel = 150;
	double elapsedTime = 0;
	double currentBurn;
	
	//as long as you are above ground...
	while (height > 0)
	{
		//report current status
		reportStatus(cout, elapsedTime, height, velocity, fuel, shipName);
		reportStatus(logStream, elapsedTime, height, velocity, fuel, shipName);
		
		string userQuestion = "";
		
		//get the input if there is still fuel left
		if (fuel > 0)
		{
			//user question asked for the first time during this iteration of main while loop (height>0)
			userQuestion = "How much fuel do you want to burn?";
			
			do
			{
				cout << userQuestion << endl << "Enter a number between " << minFuelUnits << " and " << maxFuelUnits << ": ";
				logStream << userQuestion << endl << "Enter a number between " << minFuelUnits << " and " << maxFuelUnits << ": ";
				
				//get input
				cin >> currentBurn;
				logStream << " " << currentBurn;
				cout << endl << endl;
				logStream << endl << endl;
				
				//user question changed so if there's an error, future iterations of this dowhile loop will have error message
				userQuestion = "Error! Input out of bounds!";
			}
			while (currentBurn > maxFuelUnits || currentBurn < minFuelUnits);
			
			//if not enough fuel left, set current burn to remaining amount of fuel to burn off the rest of it
			if (currentBurn > fuel)
			{
				currentBurn = fuel;
				cout << "**** ALERT: BURNING REMAINING FUEL ****" << endl << endl;
				logStream << "**** ALERT: BURNING REMAINING FUEL ****" << endl << endl;
			}
		}
		else //if no fuel left
		{
			currentBurn = 0;
			
			//warn user
			cout << "**** OUT OF FUEL ****" << endl << endl;
			logStream << "**** OUT OF FUEL ****" << endl << endl;
		}
		
		//update status
		updateStatus(velocity, currentBurn, fuel, height);
		
		//one more second has passed
		elapsedTime++;
	}
	
	//landing
	touchdown(elapsedTime, velocity, currentBurn, height);
	touchDownStatus(cout, elapsedTime, velocity, fuel);
	touchDownStatus(logStream, elapsedTime, velocity, fuel);
	finalAnalysis(cout, velocity);
	finalAnalysis(logStream, velocity);
	
	//all done
	logStream.close();
	cout << endl << endl;
	return 0;
}


/*=============================
 ////////// FUNCTIONS //////////
 =============================*/

/**
 Outputs the current status of the spacecraft to a stream
 @param os The output stream
 @param time The elapsed time in seconds
 @param height The height of the spacecraft in feet
 @param velocity The speed of the spacecraft; downwards is positive
 @param fuel The number of fuel units left
 @param name The name of the spacecraft
 */
void reportStatus(ostream &os, double time, double height, double velocity, double fuel, string name)
{
	os << "Status of your " << name << " spacecraft:" << endl;
	os << "Time  : " << time << " seconds" << endl;
	os << "Height: " << height << " feet" << endl;
	os << "Speed : " << velocity << " feet/second" << endl;
	os << "Fuel  : " << fuel << " lbs" << endl;
}

/**
 Outputs the exact status of the spacecraft at touchdown to a stream
 @param os The output stream
 @param time The elapsed time
 @param velocity The speed of the spacecraft; downwards is positive
 @param fuel The number of fuel units left
 */
void touchDownStatus(ostream &os, double time, double velocity, double fuel)
{
	os << "***** CONTACT *****" << endl;
	os << "Touchdown at " << time << " seconds" << endl;
	os << "Landing velocity = : " << velocity << " feet/second." << endl;
	os << fuel << " pounds remaining." << endl << endl;
}

/**
 Computes status of the spacecraft one second after the current time
 @param velocity The current speed of the spacecraft
 @param burnAmount How many units of fuel to burn
 @param fuelRemaining The number of fuel units remaining
 @param height The height of the spacecraft
 */
void updateStatus(double &velocity, double burnAmount, double &fuelRemaining, double &height)
{
	//keep old velocity
	double originalVelocity = velocity;
	
	//assign new velocity
	velocity += GRAVITY - burnAmount;
	
	//height computed from avg of velocities
	height -= (originalVelocity + velocity) / 2;
	
	//calculate remaining fuel
	fuelRemaining -= burnAmount;
}

/**
 Takes text from an input stream, replaces any instances of a target string with replacement text, and outputs the text to an output stream
 @param is The input stream
 @param os The output stream
 @param target Specific text to replace
 @param replacement The replacement text
 */
void introduction(istream &is, ostream &os, string target, string replacement)
{
	//while not end of file
	while (!is.eof())
	{
		string current_word;
		//read word into target variable
		is >> current_word;
		//if target is what we want to replace
		if (current_word == target)
		{
			//send name of ship to output
			os << replacement << " ";
		}
		else
		{
			//send to output
			os << current_word << " ";
		}
	}
	
	//go back to beginning of file
	is.clear(); //no more eof
	is.seekg(0); //first position
}

/**
 Calculates the exact status of the spacecraft at touchdown
 @param elapsedTime The elapsed time
 @param velocity The speed of the spacecraft; downwards is positive
 @param burnAmount The number of units
 @param height - the height of the spacecraft
 */
void touchdown(double &elapsedTime, double &velocity, double &burnAmount, double &height)
{
	//assign previous velocity
	double prev_velocity = velocity - (GRAVITY - burnAmount);
	
	//assign previous time
	double old_time = elapsedTime - 1;
	
	//assign previous height
	double prev_height = height + ((prev_velocity + velocity) / 2);
	
	//time to reach the surface = delta
	double delta;
	
	//calculations
	if (burnAmount != GRAVITY)
	{
		delta = (sqrt((prev_velocity * prev_velocity) + (prev_height * (10 - (2 * burnAmount)))) - prev_velocity) / (GRAVITY - burnAmount);
	}
	else
	{
		delta = prev_height / prev_velocity;
	}
	
	//assign new values
	elapsedTime = old_time + delta;
	velocity = prev_velocity + (GRAVITY - burnAmount) * delta;
	height = 0;
}

/**
 Insults the user for anything less then a perfect landing
 @param os The output stream
 @param velocity How fast the spacecraft was going at touchdown
 */
void finalAnalysis(ostream &os, double velocity)
{
	if (velocity >= 50)
	{
		os << "You totaled an entire mountain!" << endl;
		os << "Your family will be notified... sooner or later." << endl;
	}
	else if (velocity >= 30)
	{
		os << "Your ship is a wreck!" << endl;
		os << "Your family will be notified... by telegraph." << endl;
	}
	else if (velocity >= 10)
	{
		os << "You blasted a huge crater!" << endl;
		os << "Your family will be notified... by mail." << endl;
	}
	else if (velocity >= 5)
	{
		os << "Your ship is a heap of junk!" << endl;
		os << "Your family will be notified... by email." << endl;
	}
	else if (velocity >= 2)
	{
		os << "You blew it!" << endl;
		os << "Your family will be notified." << endl;
	}
	else if (velocity > 0)
	{
		os << "A little bumpy." << endl;
	}
	else if (velocity == 0)
	{
		os << "Congratulations! A perfect landing!!" << endl;
	}
}

/**
 The best ASCII art title text you have ever seen in your life
 @param os An output stream so the user can see it
 */
void show_title_text(ostream &os)
{
 os << "LL      UU  UU  NNNNNN  AAAAAA  RRRRRR" << endl
	<< "LL      UU  UU  NN  NN  AA  AA  RR  RR" << endl
	<< "LL      UU  UU  NN  NN  AA  AA  RR RR " << endl
	<< "LL      UU  UU  NN  NN  AAAAAA  RRRR  " << endl
	<< "LL      UU  UU  NN  NN  AA  AA  RR RR " << endl
	<< "LLLLLL  UUUUUU  NN  NN  AA  AA  RR  RR" << endl << endl
	
	<< "LL      AAAAAA  NNNNNN  DDDD    EEEEEE  RRRRRR" << endl
	<< "LL      AA  AA  NN  NN  DD DDD  EE      RR  RR" << endl
	<< "LL      AA  AA  NN  NN  DD  DD  EEEEEE  RR RR " << endl
	<< "LL      AAAAAA  NN  NN  DD  DD  EE      RRRR  " << endl
	<< "LL      AA  AA  NN  NN  DD DDD  EE      RR RR " << endl
	<< "LLLLLL  AA  AA  NN  NN  DDDD    EEEEEE  RR  RR" << endl << endl
	
	<< " GGGGGGGG       AA      MM      MM  EEEEEEEEEE" << endl
	<< "GGG    GGG     AAAA     MMMM  MMMM  EE        " << endl
	<< "GG            AA  AA    MM MMMM MM  EE        " << endl
	<< "GG   GGGGG   AA    AA   MM  MM  MM  EEEEEEEEEE" << endl
	<< "GG      GG  AAAAAAAAAA  MM      MM  EE        " << endl
	<< "GGG    GGG  AA      AA  MM      MM  EE        " << endl
	<< " GGGGGGGG   AA      AA  MM      MM  EEEEEEEEEE" << endl << endl
	
	<< "==============================================" << endl
	<< "ANDREW YAROS - Homework 5 - CS171 - Winter '17" << endl
	<< "==============================================" << endl << endl;
}
