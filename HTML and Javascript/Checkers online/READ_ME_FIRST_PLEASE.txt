+-----------------+
| READ THIS FIRST |
+-----------------+

================================================
I did the front end development on this project.
================================================
Thus, work explicitly done by me can be found in 
the following places:

    /src/main/resources/public/	
        (The front end UI files I made)

    /Prototypes
        (some initial HTML I wrote)

Additionally, I contributed to our project 
documentation in the /Documents directory.
================================================

    Note: To actually get this to work, you need
to run the server my group members wrote. Rather 
than doing this yourself, you can go to the 
following link to see the project:

================================================
    https://andrewyaros.com/checkers.html
================================================

    This page will redirect you to the 
AWS-hosted page where I'm hosting the project. 
Yes, I know this isn't configured to be on a 
subdomain of my website. I'm getting to that. :/

------------------------------------------------

    You can play this in your browser against 
another person by using the "Join random game" 
option. Make sure you both click it at around 
the same time.

------------------------------------------------

    You can also play against yourself this way
if you open up two different browser windows; 
note that you will need to make sure both 
displayed UUIDs are different. This can be done 
by opening one of the windows in private 
browsing mode, or by using two different 
browsers (i.e. one Firefox window and one Chrome 
window). What matters is that the browser cookie 
on both windows is different!

------------------------------------------------

    As seen in the final submission document: 
The source code consists of java files from the 
server, and html, javascript, and css files. To 
set up the server, one needs an existing Ubuntu 
17.04 server with Apache Maven installed. After 
cloning the repository, or copying the files 
onto the server, the project can be built with 
“mvn compile” and cleaned with “mvn clean.” To 
start the server, run the command:

    “sudo nohup mvn exec:java &”

to begin broadcasting on port 443. The game can 
then be accessed over the internet through a 
web browser using the server’s IP at port 443.

------------------------------------------------

    -Andrew Yaros
