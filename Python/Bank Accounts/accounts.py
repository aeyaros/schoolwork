#!/usr/bin/python
# Andrew Yaros - CS265 - Assignment 4 - Bank Transactions
# Linux:
# tux4.cs.drexel.edu 4.4.0-96-generic #119-Ubuntu SMP Tue Sep 12 14:59:54 UTC 2017 x86_64 x86_64 x86_64 GNU/Linux
# Python:
# 2.7.12 (default, Nov 19 2016, 06:48:10) 
# [GCC 5.4.0 20160609]
#

#import
import sys
import os
import time
from datetime import date
import re

#arguments
argc = len( sys.argv )

def print_arg_info(argerror) :
   print "\n",argerror,"\n-i for account information \n-h for account history \n-t to insert a transaction\n"

if argc == 2 :
	if sys.argv[1] == "-i" :
		menu_action = 1
		menu_title = "Info\n----"
	elif sys.argv[1] == "-h" :
		menu_action = 2
		menu_title = "History\n-------"
	elif sys.argv[1] == "-t" :
		menu_action = 3
		menu_title = "Insert transaction\n------------------"
	else :
		print_arg_info("Unknown argument; please try again:");
		sys.exit()
else :
	print_arg_info("Please specify only a single argument:");
	sys.exit()

###### READ IN LOG ########### TO READ IN A DIFFERENT FILE, CHANGE THIS LINE
#check for environment variable
if os.getenv("ACCT_LIST") == None :
	print "\nERROR: Environment variable not set:\nPlease set an environment variable called \"ACCT_LIST\" containing the location of the transaction log file\n"
	sys.exit();
account_file = open( os.environ["ACCT_LIST"] ,"r" ) #THIS SHOULD GET THE ENVIRONMENT VARIABLE...

###### BUILD LIST OF TRANSACTIONS FROM FILE
trans_log = []

for current_line in account_file: #read each line
	current_line = current_line.rstrip() #remove whitespace from end of line
	trans_log.append(current_line.split(':'))#convert to list, add to log

account_file.close()

###### BUILD LOG FILE FROM LIST OF ALL TRANSACTIONS
def build_log() :
	log_output = open(".log_temp","w")
	n = 0
	while n < len(trans_log) : #WRITE EACH TRANSACTION TO THE TEMP FILE
		log_output.write(str(trans_log[n][0])+":"+str(trans_log[n][1])+":"+str(trans_log[n][2])+":"+str(trans_log[n][3])+":"+str(trans_log[n][4])+"\n")
		n = n + 1
	log_output.close()
	#RENAME TEMP FILE
	os.rename(".log_temp",os.environ["ACCT_LIST"])

###### DICTIONARY OF USERS
user_dict = {}

###### POPULATE THE DICTIONARY FROM THE LOG DATA
def build_dict() :
	global user_dict
	log_i = 0
	while log_i < len(trans_log) :
		acct_numb = trans_log[log_i][0] #get account number
		acct_name = trans_log[log_i][1] #get account name
		# make a list containing from transaction info in the 
		# (log_i)th transaction from trans_log; i.e. just [ date, D/W, amount ]
		curr_tran = [trans_log[log_i][2], trans_log[log_i][3], trans_log[log_i][4]]
		
		if acct_numb in user_dict :
			# append the transaction in this log entry to 
			# list of transactions in the dictionary value
			user_dict[acct_numb][1].append(curr_tran)
		else :
			#add listing to dictionary - key is acct num, value is name, then a list with the transaction
			user_dict[ acct_numb ] = [acct_name, [curr_tran]]
			##### THIS IS HOW AN ENTRY IS STRUCTURED
			##### user_dict[1337] = [name, [ [tr 0] [tr 1] [tr 2] ...]
		log_i = log_i + 1

def is_name(name_text):
	if name_text == "" : #no empty strings
		return False
	name_match = re.search("[^A-Za-z0-9 ]", name_text) #does input have unwanted characters
	if name_match : #yes
		return False #not a name
	else:
		return True #is a name

def is_num(num_text) : #parse numbers
	if num_text == "." : #only a dot, f
		return False
	elif num_text.isdigit() == True : #nothing other than 0-9 t
		return True
	else:
		num_match = re.search( "\A\d*\.\d*" ,num_text) #0-9 dot 0-9 t
		if num_match :
			return True
		else :
			return False

def calc_balance(acct_num) :
	global user_dict
	tran_list = user_dict[acct_num][1] # get list of transactions
	balance = 0.00
	for tran in tran_list :
		if tran[1] == "D" : # if deposit, add to total
			balance = balance + float(tran[2])
		elif tran[1] == "W" : # if withdrawal, subtract from total
			balance = balance - float(tran[2])
	return balance

def info(acct_num) :
	print ("account #: ").rjust(16),acct_num
	print ("name: ").rjust(16),user_dict[acct_num][0]
	print ("balance: ").rjust(16),"$",
	print '{:.2f}'.format(calc_balance(acct_num)),"\n"
	anykey = raw_input("Press ENTER to continue\n")

def history(acct_num) :
	tran_list = user_dict[acct_num][1]
	for tran in tran_list :
		print tran[0].rjust(16), #date
		if tran[1] == "W" :
			print "withdrawal",
		elif tran[1] == "D" :
			print "deposit   ",
		else :
			print "ERROR: unknown transaction type",
		print " $",'{:.2f}'.format(float(tran[2])) #amount
	print ""
	anykey = raw_input("Press ENTER to continue\n")
	print ""

def get_date() :
	thedate = date.today()
	yr = str(int(str(thedate.year)[-2:])).zfill(2)
	mo = str(int(thedate.month)).zfill(2)
	da = str(int(thedate.day)).zfill(2)
	return yr+"."+mo+"."+da

def add_trans(acct_num, acct_name, typ, amount) :
	global trans_log
	global user_dict
	if typ == "d" :
		typ = "D"
	elif typ == "w" :
		typ = "W"
	cur_date = get_date()
	new_trans = [acct_num, acct_name, cur_date, typ, amount]
	#add the new transaction
	trans_log.append(new_trans)
	#write new log to file, rebuild dict
	build_log()
	build_dict()

def insert(acct_num) :
	global trans_log
	global user_dict
	new_created = 0
	if (acct_num == -1) : #create new account
		print "You have chosen to create a new account!"
		while True :
			if new_created == 1:
				break
			new_num = raw_input("\nEnter a new account number (must be a whole number), or b to go back: ")
			if new_num == "b" :
				return 0
			elif new_num.isdigit() :
				if new_num < 0.0 :
					print "ERROR: Account number must be a positive integer!"
				elif new_num not in user_dict :
					#prompt for name
					while True :
						new_name = raw_input("\nEnter your full name, or b to go back: ")	
						if new_name == "b" :
							break
						elif is_name(new_name) == True :
							#new transaction, deposit 0 dollars
							add_trans(new_num, new_name, "d", 0)
							acct_num = new_num
							print "\nNew account created."
							new_created = 1
							break
						else :
							print "Please try again; only use alphanumeric characters!\n"
				else :
					print "ERROR: This account number has been taken already! Try another.\n"
			else :
				print "ERROR: You did not enter a whole number!\n"
	while True:
		print "Choose an option for account", acct_num, ":\nd) eposit\nw) ithdrawal\nb) ack\n"
		tran_typ = raw_input("Enter choice => ")
		#add transaction to existing acct
		if tran_typ == "b" :
			break
		elif tran_typ == "w" or tran_typ == "d" : 
				while True:
					amount = raw_input("Enter amount, or b to go back: $")
					if amount == "b" :
						break
					elif is_num(amount) :
						if float(amount) > 0 :
							#round amount to two decimal places
							amount = float(str('{:.2f}'.format(float(amount))))							
							#conduct transaction
							add_trans(acct_num, user_dict[acct_num][0],tran_typ, amount)
							if tran_typ == "d" :
								print "\nDeposited", "$"+str(amount), "into account #",acct_num
							if tran_typ == "w" :
								print "\nWithdrew", "$"+str(amount), "from account #",acct_num
							anykey = raw_input("\nPress ENTER to continue\n")
							return 0
						else :
							print "ERROR: Enter a positive amount!"
					else :
						print "ERROR: Please enter a number!"
		else:
			print "ERROR: Invalid input. Try again!"

def menu_main() :
	global trans_log
	global user_dict
	while True :
		# make a list from the dictionary
		key_list = user_dict.keys() # list of keys
		menu_list = [] # list for menu items	
		# EACH MENU ITEM : [ name, acct_num ]
		men_i = 0
		while men_i < len(key_list) :
			men_string = [ user_dict[ key_list[men_i] ][0] , key_list[men_i] ]
			menu_list.append(men_string)
			men_i = men_i + 1	
		# sort list alphabetically
		menu_list = sorted(menu_list, key=lambda get0: get0[0])
		# PRINT THE MENU and get user input
		print ""
		print menu_title
		men_i = 0
		while men_i < len(menu_list) : # print out the menu
			print (str(men_i+1) + ")"), menu_list[men_i][0].ljust(16), menu_list[men_i][1]
			men_i = men_i + 1
		if menu_action == 3 :
			print "n) ew account"
		print "q) uit\n"
		# get user input
		user_choice = raw_input("Enter choice => ")
		# parse input and run other functions:
		print ""
		if user_choice == "q" :
			break
		elif menu_action == 3 and user_choice == "n" :
			insertreturn = insert(-1) #create new account (only if called with -t)
		elif user_choice.isdigit() :
			if int(user_choice) <= len(menu_list) and int(user_choice) > 0 :
				if menu_action == 1 : # get info
					info(menu_list[int(user_choice) - 1][1])
				elif menu_action == 2 : # get history
					history(menu_list[int(user_choice) - 1][1])
				elif menu_action == 3 : #insert transaction
					insertreturn = insert(menu_list[int(user_choice) - 1][1])
			else :
				print "ERROR! Input out of range! Enter a number between 1 and",len(menu_list),"!"
		else :
			print "ERROR! Invalid input! Please try again!"

build_dict() #BUILD DICTIONARY
menu_main() #RUN THE MAIN MENU
# all done!
