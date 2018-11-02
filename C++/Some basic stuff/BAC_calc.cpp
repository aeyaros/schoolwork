/*====================================
======================================
Blood Alcohol Concentration Calculator
By Andrew Yaros - CS 171 - Winter 2017
Section 069 - 2/9/2017
======================
====================*/

//////////////////////////////////////////// HEADER

#include <iostream>
#include <iomanip>
#include <cmath>
#include <string>

using namespace std;



//////////////////////////////////////////// GLOBAL CONSTANTS

//DOUBLES
const double safe = 0.00;
const double someImpairment = 0.04;
const double significantAffected = 0.08;
const double someCriminalPenalties = 0.10;
const double deathPossible = 0.30;

const double female_const = 4.5;			//female constant
const double male_const = 3.8;				//male constant
const double loss_rate = 0.00025;			//per minute


//STRINGS
const string SAFE = "Safe To Drive";
const string SOMEIMPAIR = "Some Impairment";
const string SIGNIFICANT = "Driving Skills Significantly Affected";
const string MOST_STATES = "Criminal Penalties in Most US States";
const string ALL_STATES = "Legally Intoxicated - Criminal Penalties in All US States";
const string YOURE_DEAD = "Death is Possible!";



//////////////////////////////////////////// FUNCTIONS



/**
Finds male and female BAC; divides the number of drinks by one's body weight and multiplies the product by a gender-specific constant
@param numDrinks	the number of drinks taken
@param weight		body weight, in pounds
@param duration	time since last drink, in minutes
@param maleBAC	male specific constant
@param femaleBAC	female-specific constant
*/
void computeBloodAlcoholConcentration(int numDrinks, int weight, int duration, double &maleBAC, double &femaleBAC)
{
	//doubles to be used for calculations
	double minutes_double = duration;
	double drinks_double = numDrinks;
	double pounds_double = weight;
	
	//First, calculations will be stored in these variables. Afterwards, if they are negative they will be set to 0
	//Once that check is done, the reference parameters will be changed accordingly
	double Mbac;
	double Fbac;
	
	
	//MALE calculations
	Mbac = ((drinks_double / pounds_double) * male_const) - (minutes_double * loss_rate);		//calculate male
	
	if (Mbac < 0.0) //check for negative
	{
		Mbac = 0.0; //correct if negative
	}
	if (Mbac > 1) //BAC cant be over 1
	{
		Mbac = 1.0;
	}
	maleBAC = Mbac; //send result to reference parameter
	
	
	//FEMALE calculations
	Fbac = ((drinks_double / pounds_double) * female_const) - (minutes_double * loss_rate);		//female
	
	if (Fbac < 0.0) //check for negative
	{
		Fbac = 0.0; //correct if negative
	}
	if (Fbac > 1) //BAC cant be over 1
	{
		Fbac = 1.0;
	}
	femaleBAC = Fbac; //send result to reference parameter
	
}



/**
Returns text describing one's impairment given their BAC
@param bac The user's BAC
*/
string impairment(double bac)
{
	//.03 or greater   death likely
	if (bac > deathPossible)
	{
		return YOURE_DEAD;
	}
	
	//from .1 to just under .3  criminal penalties in all states
	else if (bac >= someCriminalPenalties)
	{
		return ALL_STATES;
	}
	
	//from .08 to just under .1  criminal penalties in most states
	else if (bac >= significantAffected)//.08
	{
		return MOST_STATES;
	}
	
	//from .04 to just under .08  skills significantly affected
	else if (bac >= someImpairment)//.04
	{
		return SIGNIFICANT;
	}
	
	//greater than 0 and less than .04   some imparement
	else if (bac > safe)
	{
		return SOMEIMPAIR;
	}
	
	//safe
	else
	{
		return SAFE;
	}
}



/**
Asks the user a question, recieves an integer from the user, and returns integer
@param message The text asking the user a question
@param lower The lower bound for the integer
@param upper The higher bound for the integer
*/
int promptForInteger(const string &message, int lower, int upper)
{
	int variable;
	
	do
	{
		cout << message << endl << endl << "	";
		cin >> variable;
		cout << endl;
	}
	while (variable < lower || variable > upper);
	
	return variable;
}



/**
Asks user if they are male or female and returns M or F
@param message Text string asking the user if they are male or female
*/
char promptForMorF(const string &message)
{
	char usergender;
	
	do
	{
		cout << message << endl << endl << "	";
		cin >> usergender;
		cout << endl;
	}
	while(usergender != 'M' && usergender != 'F');
	
	return usergender;
}



/**
This function prints a chart showing how intoxicated someone is after each drink, starting from 0
@param weight The users weight
@param duration Time since last drink
@param isMale Boolean to check gender
*/
void showImparementChart(int weight, int duration, bool isMale)
{
	//shows from 0 drinks to drinknum-1 drinks; the loop runs this many times starting with 0
	const int DRINKNUM = 11;
	
	//column widths
	const int WIDTH_COLUMN_1 = 8;
	const int WIDTH_COLUMN_2 = 4;
	
	//values to be referenced/changed each time the BAC is calculated as the chart is created
	double male_chart_bac;
	double female_chart_bac;
	
	//chart header
	cout << setw(WIDTH_COLUMN_1) << "	# drinks" << "  " << setw(WIDTH_COLUMN_2) << "  BAC" << "  " << "Status" << endl;
	cout << "	--------------------------------------------------------------------------" << endl;

	if (isMale == true)
	{
		for (int n = 0; n < DRINKNUM; n++)
		{
			computeBloodAlcoholConcentration(n, weight, duration, male_chart_bac, female_chart_bac);
			
			cout << "	" << setw(WIDTH_COLUMN_1) << setfill(' ') << n << "  " << setw(WIDTH_COLUMN_2)  << setprecision(WIDTH_COLUMN_2 - 1) << fixed << male_chart_bac << "  " <<  impairment(male_chart_bac) << endl;
			
		}
	}
	else //exact same as before but using female BAC variables in the cout
	{
		for (int n = 0; n < DRINKNUM; n++)
		{
			computeBloodAlcoholConcentration(n, weight, duration, male_chart_bac, female_chart_bac);
			
			cout << "	" << setw(WIDTH_COLUMN_1) << setfill(' ') << n << "  " << setw(WIDTH_COLUMN_2)  << setprecision(WIDTH_COLUMN_2 - 1) << fixed << female_chart_bac << "  " << impairment(female_chart_bac) << endl;

			
		}
	}
}



//////////////////////////////////////////// MAIN

int main()
{
	//program title and info
	cout << "	======================================" << endl
		 << "	Blood Alcohol Concentration Calculator" << endl
		 << "	By Andrew Yaros  --  Drexel University" << endl
		 << "	CS 171  -  Section 069  -  Winter 2017" << endl
		 << "	======================================" << endl << endl << endl;
	
	
	//upper bound values for user prompts:
	int time_upper_bound = 3000;
	int weight_upper_bound = 1000;
	
	
	//initialize weight variable and prompt user
	int weight;
	weight = promptForInteger("	Please enter your weight (in pounds): ", 1, weight_upper_bound);
	
	
	//initialize time variable and prompt user
	int time;
	time = promptForInteger("	Please enter the time passed since your last drink (in minutes): ", 0, time_upper_bound);

	
	//initialize gender variable and prompt user
	char gender;
	gender = promptForMorF("	Are you male or female? Type M for male or F for female. ");
	
	
	//for displaying "male" or "female" on the chart information: string variable for text is created and changed based on whether user typed M or F
	string chart_gender = "";
	
	//boolean value for impairment chart function created, and assigned true if M and false if F
	bool chart_male_boolean = true;
	
	
	//checking user's gender input
	if (gender == 'M')
	{
		chart_gender = "male";
		chart_male_boolean = true;
	}
	else
	{
		chart_gender = "female";
		chart_male_boolean = false;
	}
	
	
	//print user information
	cout << "	==========================================================================" << endl << endl;
	cout << "	Your information: " << endl;
	cout << "	Weight: " << weight << " pounds, " << chart_gender << ", " << time << " minute";
	if (time != 1)//checks to see if we need to add an "s" after the word minutes
	{
		cout << "s";
	}
	cout << " since last drink" << endl << endl;
	
	
	//print out chart
	showImparementChart(weight, time, chart_male_boolean);
	
	cout << endl << endl << endl;

	return 0;

}
