+------------------------+
| Andrew Yaros    SE 311 |
| Homework 1 Readme file |
+------------------------+
| Key Word in Context    |
+------------------------+

=======================
 To build the project:
=======================

	The project can be built with Maven. Navigate to the project directory in the terminal - I named it "maven". To clean and build the project, run:

		mvn clean package compile

	An Eclipse project is included in the Eclipse directory. Really, it's just an src and a bin folder. ¯\_(ツ)_/¯ The directory should be open-able in Eclipse.


=====================
 To run the program:
=====================

	To start the program without arguments, assuming you are still in the "maven" directory, run:

		java -cp target/hw1-1.0-SNAPSHOT.jar Main


	This will use console I/O for both input and output. To use file I/O, you will need to use arguments. To run with arguments, you MUST use four arguments (following "Main"):

		java -cp target/hw1-1.0-SNAPSHOT.jar Main true input.txt true output.txt


	The arguments are as follows:

		1. Set to "true" to use file input. Otherwise, set to "false".*
		2. Pathname of input file. If argument 1 is false, set to "none"**
		3. Set to "true" to use file output.*
		4. Pathname of output file. If argument 3 is false, set to "none"**
	
		*  or if not true set to anything as long as the argument exists
		** or if previous argument isn't true, set to anything as long as argument exists. Note that filenames in the arguments specified without any path (as seen in the aforementioned example above) should be located in the maven folder. A sample input.txt file is provided based on the example from the assignment sheet.


	The program will then run. If you use anything other than 4 arguments the program will default to file I/O because I didn't have time to implement a nicer argument parser. Please note that if you specify an input pathname that doesn't exist, there's nothing else the program can do about it. :/


====================
 Using the program:
====================

	If you use console input, you will be prompted to type in the input line by line. If you use console output, then the output will be printed to the console. If you use file I/O, then the input should be in a file, and the output will be sent to whatever pathname you specify. I guess this is obvious but I'm writing it here as a formality.


================================
 Regarding the software design:
================================

	The UML diagram was exported from IntelliJ Idea's diagram plugin. It is located in a PDF file called "UML.pdf"

	The strategy pattern is implemented twice for input and output implementations, with abstract classes for input and output, and concrete implementations for file and console I/O. By default, the modules are declared as their abstract types, and instantiated using their console implementations; depending on the program arguments one or both I/O modules may be re-instantiated with the file implementations.

	Information hiding is utilized via the LineStorage class, which contains an ArrayList of Line objects. The Line objects themselves each contain an array of Word objects. No other object utilizes the Line and Word objects except for the LineStorage class.

	The input module takes LineStorage, adds new lines, and adds words one by one to each new line through the LineStorage's interface. The CircularShifter class creates additional lines in the LineStorage class with permutations of each original line. The AlphabeticalSorter class sorts the lines in LineStorage by using the LineStorage's swapLines method and a simple insertion sort algorithm. It compares two lines by getting the full text of the line from LineStorage as a string and comparing the strings. The output module looks at LineStorage and outputs each line one by one.

