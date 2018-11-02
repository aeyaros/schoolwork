Andrew Yaros
CS 350
HW1 Part 1
READ ME FILE

Regarding menus: Menu class contains one or more options. Each submenu can carry out a unique set of code with a Lambda expression through the menu_command interface. This is clumsy since I have to define everything through main. However, I didn't want to have 5 million classes of menus; this is what made the most sense to me in the... limited... amount of time I gave myself to consider this aspect of the assignment. ¯\_(ツ)_/¯

Regarding surveys/tests: Surveys and tests will be saved with serialization.

Regarding questions; Three main types: 

Essay questions, which are just three (as mentioned in lecture) simple answers.

Multiple-multiple choice, which allows multiple choices and multiple correct answers. Booleans are evil, but I am evil too, so it all works out just fine. Multiple choice questions are derived from that and only allow a single right answer and a single choice to be made when asked. T/F questions consist of 2 answers, one T and the other F. Only one of the two is correct. Short answer questions allow the user to type in a short answer; because it is derived from MMC questions, it allows for more than one answer to be accepted, if this functionality is desired in the future; it also hides it's answers rather than printing them like MC and MMC and T/F do.

Finally, there are matching questions; each answer has two strings, one for the left column and one for the right column, as well as an index number. I won't go into full detail right now about how this will work but it involves putting the left strings and the index into one column, and doing the same with the right in random order, and comparing user input indices with the right column indices. Ranking questions are a subset of this and only use one of the Strings and the index in the match_answer type.