README FOR ASSIGNMENT 4
BY ANDREW YAROS


The program is basically just a python script. I believe the environment variable stuff should be working. If it does not work, it will give an error. To get the environment variables to work I did:

	ACCT_LIST=logfilename.log
	export ACCT_LIST

The program should parse everything properly... and should correct for user input errors; i.e. when typing a number instead of a letter at the prompt for a deposit/withdrawal amount. Account names can only have alphanumeric characters in them to prevent any colons from getting into the log file, since I'm certain that would totally screw things up. Deposits and withdrawals must be positive numbers and can be integers or floating point numbers.

In most cases, when running in insert mode, you can press "b" to go back to the previous step, like if you make a mistake entering an account number, or if you decide you want to make a withdrawal instead of a deposit.

Basically, the log file is read into a list, trans_log, and new transactions are appended to this list, after which the updated log is output to a temporary file, before that file is renamed to overwrite the original. Because of this, the exact order of the input log file is preserved (aside from the newly added transactions).

A dictionary is also used in the program; it is rebuilt when accounts are created and transactions are added. This dictionary neatly organizes the log into a set of values indexed by account number. Each value is a list; the first item is the name, and the second item is another list - the list of transactions for the account (and each transaction itself is a list, containing the date, D/W, and the amount).

The dictionary is then used to quickly get information and history on each account and to build the main menu. The menu itself is another list of lists, containing names and account numbers, which is built from the dictionary and sorted by name. The menu_main() function is where most of the program happens; it is run only once at the very end of the program.

I'm using global declarations for trans_log and user_dict (the log list and the dictionary). I'm not sure if it's necessary.

The makefile has targets info, history, and insert, for running the program in -i, -h, and -t respectively. It also has a README target.

I guess that's all I have to say. Hopefully everything is working and nothing is horrible or broken. 

(O_o)

