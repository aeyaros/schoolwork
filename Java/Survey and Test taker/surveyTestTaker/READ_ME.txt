Andrew Yaros____
CS350 HW1 Parts 2 and 3
DESCRIPTION FILE

	(Part 2 - initial functionality)

Note. Tests and quizzes are saved to local folders via Serialization. The way I did this, on my computer they were put in the same folder as the project directory. I'm not sure how this will work on different environments, but the way things are set up, it will check to see if folders are where they should be, and will create the folders if it does not see them. If the folders are created automatically in a different spot, then you can simply move the .srv and .tst file to the new locations of the "Surveys" and "Tests" folders.

I did not use backslashes explicitly; rather I used File.seperator, and other such things to avoid OS specific references. I haven't actually run it in tux yet though. :/

All required functionality should be implemented. There are some unused functions for most of the questions, like ask and edit functions, which aren't relevant for Part 2, but I wrote them because I needed to understand how those functions would work before doing other functions.

Menus are objects with menuOptions, which have menuCommands. menuCommand is an interface, where I use a lambda-blocks... or whatever the correct term was... to define code which gets executed. The menus are all built within the main function. I and O are abstracted into their own classes; there are I and O abstract classes, as well as a consoleInput and consoleOutput classes. Other kinds of I/O can be added in the future if necessary.

Everything should be working fine, aside from some minor formatting/spacing adjustments I still need to add to the UI. There's definitely some refactoring and other cleanup work that I'll need to do for other assignments. There shouldn't be any issues with input; I haven't run into any yet and I'm doing plenty of parsing. 

When creating questions, the program should guide you through the process and ask you for each bit of info one at a time. It will ask you each time if you want to add more answers... that sort of thing. 

Note that ranking, matching, and multiple-multiple-choice questions will use a special input method when the user takes the exam: Given options A,B,C,D,E, the user must type 1,2,3,4,5 (in whatever order necessary), without spaces or any other characters. This gets parsed... very carefully... into an int[], which is used to do other things I won't get into right now. For MMC questions, you can type as many comma-seperated responses (numbers) you want as long as it is less than or equal to the number of available responses and each of the numbers is in range, and none are repeated. For matching and ranking, you have to have an exact match with the number of answers, because you have to match/rank stuff. Of course, this isn't actually usable by a user yet since that's going to be for assignment 3. When creating questions, answers are added one at a time.

	(Part 3 - complete functionality)

This now has grading (tests only), modifying, tabulating, and taking of tests and surveys. Taking surveys and tests basically consists of a function that loops over all the question objects stored in a survey/test and "asks" them, one by one. At this point, the functionality for the current question takes over, does its thing, and then finishes. Then the next question is asked. There is a variable, timesTaken, which is basically for determining how many times a test has been taken, so I know whether it is possible to grade or tabulate it.

Originally, I tried simply saving a copy of the finished survey/test within the survey/test itself. I could not get that working. I then tried just saving a copy of the array of questions, since that's all that is really necessary to grade and tabulate the survey/test, since the answers are stored within each question. Had either of those things worked, I wouldn't need a timesTaken variable since I could have just checked the size of the array holding previous copies of tests/surveys. Because this didn't work, I resorted to:

1) Storing the responses as strings, since the tabulation basically just lists how often different answers were chosen whenever a particular question was asked, so if I get everything in the form of strings I just need to count the strings. Strings are also nice to me, and usually work properly and don't give me strange issues.

2) Auto-grading the tests immediately after completion and storing the grade, as well as the date taken, in an object designed for this purpose, so I could store grades of past tests. This is necessary, since the grading function itself needs to look at the completed questions; the data I store as strings does not contain whether the question was answered correctly. Either way, what the user sees now is effectively the same as what they would have seen had I gotten things working the way I wanted to, so it doesn't make much difference.

I just want you to know that these steps were taken out of desperation. There was lots of code which was deleted and rewritten in this process of debugging. :/

Modifying a survey/test removes all saved copies of answers and grades (tests only). I did this because it makes sense, since a modified test is effectively a new test; old grades or responses are irrelevant. There's also an important distinction to be made in the code between the data which is "temporarily" stored in a question (in an ArrayList or array called "userAnswers"; the different data type for different questions is why I did not make it part of the base question class ) and the longer-term data in the form of strings (savedResponses). I never actually got around to allowing the actual addition and subtraction of responses from a question. This would be fairly trivial but I was trying to solve other issues first and I feel that this would be unnecessary since it wasn't explicitly stated that we needed this much functionality. So, you can modify responses but you can't add or remove them, or add or remove questions. ...but you can most definitely modify them.

At this point, tabulation basically involves getting strings and counting the number of times responses appear, and printing them. This is somewhat different depending on the question type.

Saving and loading seems to be working fine. I did run into an issue where I got a java.io.StreamCorruptedException with an invalid stream header. This apparently involves something the file system does when you delete a file from one of the directories; I'm not exactly sure what was happening but I found that by moving the test or survey files out of the directories, deleting them, and running the program again (so it creates them again) and moving the files back, they are read properly.

An example test and an example survey were created, and you should be able to tabulate them and grade the test; both were taken several times.

Obviously there are areas that desperately need to be refactored; mainly the menus that aren't in the main class (that are embedded within questions and surveys and tests). Had I started this whole process earlier and been more thoughtful, then that would have been less of an issue.
