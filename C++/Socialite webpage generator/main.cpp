/*====================
Andrew Yaros -- CS 172
Spring 2017 Homework 2
SOCIALITE CLASS TESTER
====================*/

#include "socialite.h"
#include <iostream>
#include <string>
#include <fstream>
#include <vector>

using namespace std;

/*=============
// FUNCTIONS //
=============*/

/**
 Outputs user information to wherever
 @param user The socialite to be used
 */
void outputStuff(socialite &user)
{
	user.textOut();
	user.htmlOut();
}

/*===========
// GLOBALS //
===========*/
const string ENDOFDATA = "++++";

/*=========
 // MAIN //
=========*/
int main()
{
	cout << "SOCIALITE I/O test program" << endl
		 << "Andrew Yaros -- Homework 2" << endl
		 << "CS 172 ------- Winter 2017"<< endl << endl;
	
	cout << "Reading data.";
	
	/*================
	// FILE PARSING //
	================*/
	
	//temp stuff
	socialite tempPerson;
	string tempString = "";
	
	//USER DATA VECTOR
	vector<socialite>userData;
	
	//open input file
	ifstream inputFile;
	inputFile.open("Socialite_setup.txt");
	
	int i = 0;
	while (!inputFile.eof())
	{
		//add new item to vector
		userData.push_back(tempPerson);
		
		getline(inputFile, tempString);
		userData[i].setUserID(tempString);
		
		getline(inputFile, tempString);
		userData[i].setFirstName(tempString);
		
		getline(inputFile, tempString);
		userData[i].setLastName(tempString);
		
		getline(inputFile, tempString);
		userData[i].setPictureName(tempString);
		
		getline(inputFile, tempString);
		userData[i].setWebsiteURL(tempString);
		
		getline(inputFile, tempString);
		userData[i].setWebsiteDescription(tempString);
		
		getline(inputFile, tempString);
		//is this the end of the data or are there any cliques?
		if (tempString != ENDOFDATA)
		{
			do
			{
				//since not ++++ add this clique to the object
				userData[i].addClique(tempString);
				//read next line to see if there are more cliques
				getline(inputFile, tempString);
			}
			while (tempString != ENDOFDATA);
		}
		tempString = "";
		cout << ".";
		i++;
	}
	inputFile.close();
	
	/*===============
	// FILE OUTPUT //
	===============*/
	
	cout << endl << "Outputting data to files.";
	//take all data in the vector and output it to html and text
	
	for(int j = 0; j < userData.size(); j++)
	{
		outputStuff(userData[j]);
		cout << ".";
	}
	
	cout << endl;
	cout << "All done!" << endl << endl;
	
	return 0;
}
