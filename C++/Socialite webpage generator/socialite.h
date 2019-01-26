/*====================
Andrew Yaros -- CS 172
Spring 2017 Homework 2
SOCIALITE CLASS HEADER
====================*/

#ifndef socialite_h
#define socialite_h
#include <iostream>
#include <string>
#include <vector>

using namespace std;

class socialite
{
public: //pubic parts of the class (LOL programming jokes)
	
	/*================
	// constructors //
	================*/
	
	//default
	socialite();
	socialite(string userID);
	socialite(string firstName, string lastName, string userID, string pictureName, string websiteURL, string websiteDescription);
	
	/*==============
	// inspectors //
	==============*/
	
	string getFirstName() const;
	string getLastName() const;
	string getUserID() const;
	string getPictureName() const;
	string getWebsiteURL() const;
	string getWebsiteDescription() const;
	int getNumberOfCliques() const;
	string getCliqueFromNumber(int i) const;
	
	/*============
	// mutators //
	============*/
	
	void setFirstName(string firstName);
	void setLastName(string lastName);
	void setUserID(string userID);
	void setPictureName(string pictureName);
	void setWebsiteURL(string websiteURL);
	void setWebsiteDescription(string websiteDescription);
	void addClique(string clique);
	
	/*================
	// facilitators //
	================*/
	
	void textInfo(ostream &textOS);
	void textOut();
	void htmlOut();
	
private: //private parts of the class (LOL MOAR programming jokes)
	
	/*==============
	// attributes //
	==============*/
	
	string firstName_;
	string lastName_;
	string userID_;
	string pictureName_;
	string websiteURL_;
	string websiteDescription_;
	vector<string> cliques_;
};

#endif
