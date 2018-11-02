/*-----------------------------------
-------------------------------------
Zipcode to barcode conversion program

By Andrew Yaros - CS 171
Winter 2017 - Section 69

Please make sure to read
the 6 page system manual
------------------------
----------------------*/

#include <iostream>
#include <string>
#include <cmath>
#include <iomanip>

using namespace std;

// number of digits in a zipcode
const int NUMBER_OF_DIGITS = 5;


///////////////FUNCTIONS///////////////////


/** Computes a check digit by summing the digits of a zipcode and subtracting from a multiple of 10
@param zipcode The zip code entered by the user
*/
int makeCheckDigit(int zipcode)
{
	int total = 0;
	int check = 0;
	
	for(int n = 1; n <= NUMBER_OF_DIGITS; n++)
	{
		total = total +
		
		/* Please see the system manual for a
		 
		During this loop we are going to get the nth digit and add it to the
		total. Integer division is used to eliminate the digits on either side
		of the digit we want.
		
		For the nth digit, starting on the right: First: we divide and then 
		multiply the zip code by 10^n. When we subtract that product from
		the zip code, all figures to the left of the nth digit are eliminated.
		e.g. for the 4th digit, we divide and multiply by 10^4 (10000) and 
		subtract: 98765 - 90000 = 8765
		
		static_cast is used on the pow function to convert the result to an 
		int; we dont want to keep decimals */
		
		((zipcode - ((zipcode / static_cast<int>(pow(10, n))) * static_cast<int>(pow(10, n))))
		
		/*/ now, to eliminate figures on the right, and to reduce to a single
		digit, divide by 10^(n-1).       e.g. for the 4th digit, divide by
		 
		10^(4 - 1) = 10^3 = 1000: 8765 / 1000 = 8, which is the fourth digit */
		
		 / static_cast<int>(pow(10, (n - 1))) );
		
	}
	
	// After this loop, all digits have been added into the total. If the
	// total is a multiple of 10, we know the check digit is 0
	
	if (total % 10 == 0)
	{
		return 0;
	}
	
	/* Otherwise, we need the closest multiple of 10 higher than the total.	
	We start by dividing the total by 10. The resulting number, when
	multiplied by 10, is the nearest multiple of 10 lower than the total.
	Now, we just add 10	to that - this is the closest multiple of 10
	higher than the total. Finally, to get the check digit, subtract the
	total from the aformentioned multiple of 10. */
	
	else
	{
		check = (((static_cast<int>(total / 10)) * 10) + 10) - total;
		return check;
	}
}


/** This function converts a number into a binary code used in a postal barcode
@param value The value to be converted into binary
 
This function is fairly straightforward. It is simply an if/else/else statement
which checks whether a value is 1 or 2 or 3 or 4 etc. and returns the correct 
binary code for each number (as a string of |s and :s) */

string convertDigit(int value)
{
	// If the value is (X), report the appropriate barcode
	if (value == 0)
		return "||:::";
	
	else if (value == 1)
		return ":::||";
	
	else if (value == 2)
		return "::|:|";
	
	else if (value == 3)
		return "::||:";
	
	else if (value == 4)
		return ":|::|";
	
	else if (value == 5)
		return ":|:|:";
	
	else if (value == 6)
		return ":||::";
	
	else if (value == 7)
		return "|:::|";
	
	else if (value == 8)
		return "|::|:";
	
	else if (value == 9)
		return "|:|::";
	
	else
		return "";
}


/**
Converts the digits of the zip code into binary and outputs a barcode
@param zipcode The zip code entered by the user

This function uses most of the same math and logic as the makeCheckDigit 
function to get each digit from the zip code. However, there are several
important differences:

This loop goes runs backwards; it starts with the first digit on the left
and goes through each digit from left to right so they can be added to 
the string in that order (The loop from makeCheckDigit checks from right
to left)

Also, in this function, digits are converted to barcode form, and
then are added to a string ("totalcode"). After the loop, a check digit
is calculated and added to the end of the string. Note: the output of 
this function does NOT contain frame bars */
string barcode(int zipcode)
{
	string totalcode = "";

	for(int n = NUMBER_OF_DIGITS; n >= 1; n--)
	{
		// gets each digit, converts it to barcode, and adds that to the barcode string
		totalcode = totalcode + convertDigit( ((zipcode - ((zipcode / static_cast<int>(pow(10,n))) * static_cast<int>(pow(10,n))))
		/ static_cast<int>(pow(10,(n-1)))) );
	}
	
	// gets the check digit and adds it to the barcode string
	totalcode = totalcode + convertDigit(makeCheckDigit(zipcode));

	return totalcode;
}


///////////////MAIN LOOP///////////////////


int main()
{
	// program title and information
	cout << "	----------------------------------" << endl
		 << "	U.S. POSTAL CODE CONVERTER PROGRAM" << endl
		 << "	Andrew Yaros  -  Drexel University" << endl
		 << "	CS 171 Section 069  -  Winter 2017" << endl
		 << "	----------------------------------" << endl
		 << endl << endl;
	
	// prompt user for zip code
	cout << "	Please enter a " << NUMBER_OF_DIGITS << " digit zip code: " << endl << endl << "			";
	int postalcode;
	cin >> postalcode;
	
	// make sure the zip code is not negative
	if (postalcode < 0)
	{
		cout << endl << "Error: a zip code cannot be a negative number." << endl;
		return 1;
	}
	
	// make sure zip code is not over 5 digits or whatever number is specified
	if (postalcode >= pow(10,NUMBER_OF_DIGITS))
	{
		cout << endl << "Error: this zip code is over " << NUMBER_OF_DIGITS << " digits." << endl;
		return 2;
	}
	
	// display information about barcodes to user
	cout << endl
		 << "	Here is zip code '" << setw(5) << setfill('0') << postalcode <<"' converted" << endl
		 << "	into a barcode:   (check digit: " << makeCheckDigit(postalcode) << ")" << endl << endl;
	
	// print the barcode with frame bars on either end
	cout << "	|" << barcode(postalcode) << "|" << endl << endl << endl;
	
	// explanation
	cout << "	The character | represents a full" << endl
	<< "	bar and : represents a half bar." << endl
	<< "	A full frame bar is added at both" << endl
	<< "	the start and end of the barcode." << endl << endl << endl << "	";
	
	return 0;
}
