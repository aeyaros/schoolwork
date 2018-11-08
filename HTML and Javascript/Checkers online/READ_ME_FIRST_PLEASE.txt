READ THIS FIRST
---------------

I did the front end development on this project. Thus, work explicitly done by me can be found in the following places:

	/src/main/resources/public/	(Front end UI files that I created)
	/Prototypes			(some initial HTML I wrote)

Additionally, I contributed to our project documentation in the /Documents directory.

Note: To actually get this to work, you need to run the server my group members wrote. I intend to get that up and running sooner or later as soon as I rent out some space on AWS or somewhere else, but until then I'm showcasing the code I wrote here and providing the server code in case you actually want to build it and test my UI. Whoever you are, I'm sure you're a busy person and I doubt you have the time to do this...

As seen in the final submission document: The source code consists of java files from the server, and html, javascript, and css files. To set up the server, one needs an existing Ubuntu 17.04 server with Apache Maven installed. After cloning the repository, or copying the files onto the server, the project can be built with “mvn compile” and cleaned with “mvn clean.” To start the server, run the command “sudo nohup mvn exec:java &” to begin broadcasting on port 443. The game can then be accessed over the internet through a web browser using the server’s IP at port 443.

	-Andrew