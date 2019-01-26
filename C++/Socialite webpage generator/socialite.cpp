/*====================
Andrew Yaros -- CS 172
Spring 2017 Homework 2
// SOCIALITE  CLASS //
====================*/

#include "socialite.h"
#include <iostream>
#include <string>
#include <fstream>
#include <vector>

/*================
// constructors //
================*/

//default
socialite::socialite()
{
	firstName_ = "";
	lastName_ = "";
	userID_ = "";
	pictureName_ = "";
	websiteURL_ = "";
	websiteDescription_ = "";
}

//with userid
socialite::socialite(string userID)
{
	userID_ = userID;
}


//manually specify all values
socialite::socialite(string firstName, string lastName, string userID, string pictureName, string websiteURL, string websiteDescription)
{
	firstName_ = firstName;
	lastName_ = lastName;
	userID_ = userID;
	pictureName_ = pictureName;
	websiteURL_ = websiteURL;
	websiteDescription_ = websiteDescription;
}

/*==============
// inspectors //
==============*/

string socialite::getFirstName() const
{
	return firstName_;
}

string socialite::getLastName() const
{
	return lastName_;
}
string socialite::getUserID() const
{
	return userID_;
}

string socialite::getPictureName() const
{
	return pictureName_;
}

string socialite::getWebsiteURL() const
{
	return websiteURL_;
}

string socialite::getWebsiteDescription() const
{
	return websiteDescription_;
}
int socialite::getNumberOfCliques() const
{
	return static_cast<int>(cliques_.size());
}
string socialite::getCliqueFromNumber(int i) const
{
	return cliques_[i];
}

/*============
// mutators //
============*/

void socialite::setFirstName(string firstName)
{
	firstName_ = firstName;
}

void socialite::setLastName(string lastName)
{
	lastName_ = lastName;
}
void socialite::setUserID(string userID)
{
	userID_ = userID;
}

void socialite::setPictureName(string pictureName)
{
	pictureName_ = pictureName;
}

void socialite::setWebsiteURL(string websiteURL)
{
	websiteURL_ = websiteURL;
}

void socialite::setWebsiteDescription(string websiteDescription)
{
	websiteDescription_ = websiteDescription;
}
void socialite::addClique(string clique)
{
	cliques_.push_back(clique);
}

/*================
// facilitators //
================*/

/**
Outputs information about a socialite to a stream
@param textOS The output stream to use
 */
void socialite::textInfo(ostream &textOS)
{
	textOS
	<< "Info for Socialite ID: " << userID_ << endl
	<< "======================" << endl
	<< "           First name: " << firstName_ << endl
	<< "            Last name: " << lastName_ << endl
	<< "    Picture file name: " << pictureName_ << endl
	<< "          Website URL: " << websiteURL_ << endl
	<< "  Website description: " << websiteDescription_ << endl
	<< "======================" << endl
	<< "Cliques: " << endl;
	//list cliques if there are any
	if (cliques_.size() != 0)
	{
		for (int i = 0; i < cliques_.size(); i++)
		{
			textOS << "	~ " << cliques_[i] << endl;
		}
	}
	else //otherwise
	{
		textOS << "This user does not belong to any cliques" << endl;
	}
}

/**
 Outputs information to a .txt file
 */
void socialite::textOut()
{
	string txtName = userID_ + ".txt";
	ofstream outputTxt;
	outputTxt.open(txtName);
	
	textInfo(outputTxt);
	
	outputTxt.close();
}

/**
Creates an HTML file with information about the socialite
 */
void socialite::htmlOut()
{
	string outputName = userID_ + ".html";
	ofstream outputFile;
	outputFile.open(outputName);
	
	
	/*===========================================
	// HTML CODE - MAKE HTML GREAT AGAIN #MHGA //
	===========================================*/
	
	outputFile <<
	"<html>" << endl <<
	"<head>" << endl <<
	"	<title>"<< firstName_ << " " << lastName_ << "'s Socialite Page</title>" << endl <<
	"	<style>" << endl <<
	"		body {" << endl <<
	"			letter-spacing: .5px;" << endl <<
	"			font-size: 12px;" << endl <<
	"			font-family: Helvetica, Trebuchet, Trebuchet MS, Arial, sans-serif !important;" << endl <<
	"			color:#444444;" << endl <<
	"			background-color:#eeeeee;" << endl <<
	"			}" << endl <<
	"		#maincontent {" << endl <<
	"			padding-top: 12px;" << endl <<
	"			padding-left: 12px;" << endl <<
	"			padding-right: 12px;" << endl <<
	"			max-width: 768px;" << endl <<
	"			margin-left: auto;" << endl <<
	"			margin-right: auto;" << endl <<
	"			}" << endl <<
	"		img {" << endl <<
	"			max-width: 768px;" << endl <<
	"			height: auto;" << endl <<
	"			}" << endl <<
	"		h1 {" << endl <<
	"			font-size: 40px !important;" << endl <<
	"			letter-spacing: 4px;" << endl <<
	"			font-weight:700 !important;" << endl <<
	"			}" << endl <<
	"		h2 {" << endl <<
	"			font-weight: 200 !important;" << endl <<
	"			text-align: center;" << endl <<
	"			font-size: 20px;" << endl <<
	"			letter-spacing: 20px;" << endl <<
	"			}" << endl <<
	"		h1, h2 {" << endl <<
	"			text-transform: uppercase;" << endl <<
	"			font-family: Helvetica, Verdana, sans-serif !important;" << endl <<
	"			color: #333333 !important;" << endl <<
	"			}" << endl <<
	"	</style>" << endl <<
	"</head>" << endl <<
	"<body ondragstart=\"return false\" onselectstart=\"return false\" onselectstart=\"return false\" >" << endl <<
	"	<div id = \"maincontent\">" << endl <<
	"		<p><h2>SOCIALITE</h2></p>" << endl <<
	"		<p><h1>" << firstName_ << " " << lastName_ << "</h1><br/>" << endl <<
	"			</ul></p>" << endl <<
	"		<p><br/>" << endl <<
	"			<table style=\"width: 100%;\"><tbody><tr valign = top>" << endl <<
	"				<td style=\"width: 35%; padding-right: 5%;\">" << endl <<
	"					<img src = \"" << pictureName_ << "\"" << endl <<
	"					alt=\""<< firstName_ << " " << lastName_ << "'s image\" \" width = 100%/></td>" << endl <<
	"			<td style=\"width: 65%;\">" << endl <<
	"				<div style = \" text-align: left\">My website:</br>" << endl <<
	"					<b><a href = \""<< websiteURL_ << "\" target =\"_blank\">" << endl <<
	"					" << websiteDescription_ << "</a></b></div></br></br>"
	"				<b style = \"text-align: left;\">"<< firstName_ << "'s cliques:</b>" << endl;
	
	//list cliques if there are any
	if (cliques_.size() != 0)
	{
		outputFile << "<ul>" << endl;
		for (int i = 0; i < cliques_.size(); i++)
		{
			outputFile << "					<li>" << cliques_[i] << "</li>" << endl;
		}
		outputFile << "</ul>" << endl;
	}
	else //otherwise
	{
		outputFile << "					<i style=\"color:777777;\">This user does not belong to any cliques</i>" << endl;
	}

	outputFile <<
	"					</br></br>" << endl <<
	"					<i style=\"color:cccccc;\" ><br/>" << endl <<
	"					User ID: "<< userID_ << "</i></p>" << endl <<
	"				</td>" << endl <<
	"				</tr></tbody></table><br/>" << endl <<
	"	</div>" << endl <<
	"</body>" << endl <<
	"</html>";
	
	outputFile.close();
}
