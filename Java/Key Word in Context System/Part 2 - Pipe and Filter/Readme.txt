Andrew Yaros
SE 311 HW #2
Read me file
------------

The project consists of this readme file, an Eclipse project (in the Eclipse folder), a Maven project (in the Maven folder), and a UML diagram (UML.pdf) generated using IntelliJ Idea Ultimate's diagram generator. The Eclipse project can be opened by opening the directory in Eclipse. To run the maven project, cd to the Maven directory, run "mvn clean package compile", and then run:

	java -cp target/hw2-1.0-SNAPSHOT.jar UpperCaseMain true TestFile.txt false none 3

Where UpperCaseMain is the main class, and the five items following it are the arguments, as described below, which may be changed as necessary.

This consists of a combination of my HW 1 and Lab 1 assignments. All modules are now extensions of the filter module. There are two generators; one for files and one for console input. There is a single uppercase converter that outputs to the console or a file based on an input variable. There is both console and file I/O. Because we are using a pipe and filter model, there is no need for a LineStorage class. Thus, abstraction of lines and words is now absent; the project just uses ArrayLists of strings for each line.

As with my homework 1, I/O is determined by command line arguments, as described. Please use a total of five (5) arguments every time the program is run!

	1) Should be "true" or "false": determines if file input is used (console if false).
	2) Pathname of the input file. If argument 1 is false, just type "none" or "null" or anything as long as you have the argument there.
	3) Should be "true" or "false": determines if file output is used (console if false).
	4) Pathname of output file. As with 2), please include "none" or "null" if you aren't using output as long as you have something for this argument.
	5) "1", "2" or "3": Determines whether the uppercase lines are output after the input, the circular shifting, or the alphabetization, respectively. If you type this argument incorrectly it will default to the "3" setting.

If you choose console input, you may type in the lines into the console one by one, as with hw1 and lab 1. Otherwise, be sure to either type the full filepath, or put a file in the program's working directory. I have done this already using a sample file called TestFile.txt.