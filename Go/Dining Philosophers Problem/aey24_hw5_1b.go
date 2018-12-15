/****************\
|* Andrew Yaros *|
|* CS361 HW5 1b *|
\****************/

package main

import (
	"fmt"
	"math/rand"
	"strconv"
	"time"
)

/*      N philosophers sitting at circular table
bowl of noodles in center to share
each has a plate and a fork
states:
	1	think
	2	hungry
	3	eat
	4	sleep
	5	enlightened thinking
	6	enlightened hunger
	7	enlightened eating
k% chance of entering enlightened state can pick any available fork
s% chance of going into sleep state at any other time      */

//turn debugging on (true) and off (false)
//turning on will do lots of printing
const debugme = false

//report status
//n = thread number
//s = state number
func talk(n int, s int) {
	switch s {
	case 1: //thinking
		talkCustom(n, "I'm thinking...")
	case 2: //hungry
		talkCustom(n,  "I'm hungry.")
	case 3: //eating
		talkCustom(n,  "I'm eating!")
	case 4: //sleep thinking
		talkCustom(n,  "...zzz...hmmm...")
	case 5: //sleep hungry
		talkCustom(n,  "...zzz...want food...")
	case 6: //sleep eating
		talkCustom(n,  "...zzz...tastes good...")
	case 7: //enlightened think
		talkCustom(n,  "I'm enlightened.")
	case 8: //enlightened hunger
		talkCustom(n,  "I hunger for knowledge...")
	case 9: //enlightened
		talkCustom(n,  "This is quite good.")
	default: //unknown int
		talkCustom(n,  "Unknown state " + strconv.Itoa(s))
	}
}

//custom report status
//n = thread number
//str = status string
func talkCustom(n int, str string) {
	fmt.Printf("Philosopher #%2d says \"%s\" \n", n, str)
}

//debug printing function
func printdb(str string) {
	if debugme {
		fmt.Print(str + " ")
	}
}

func printForks(arr []int, size int) {
	fmt.Println("Fork status: (Fork # | Philosopher #)")
	for i := 0; i < size; i++ { //for each fork
		fmt.Print("F ")
		fmt.Printf("%2d", i)
		fmt.Print(": ")
		if arr[i] == -1 {
			fmt.Print("____") //print unowned
		} else {
			fmt.Print("P " )
			fmt.Printf("%2d", arr[i])
		}
		if (i+1) % 5 == 0 {
			fmt.Println()
		} else {
			fmt.Print(" | ")
		}

	}
	fmt.Println()
}

//message struct, for communicating over channels
type msg struct {
	owner int
	code int
}

//array of channels
type Chans struct {
	array []chan msg
}


func pickUpFork(number int, forks *Chans) int {
	var sending msg
	sending.owner = number
	sending.code = 1

	forks.array[number] <- sending //send signal to a fork
	var response msg = <- forks.array[number] //get a signal back

	return response.code
}

func takeTwoForks(f1 int, f2 int, forks *Chans) int {
	var response = pickUpFork(f1, forks) //pick up first fork
	if response == 1 || response == 2 {
		response = pickUpFork(f2, forks) //pick up second fork
		if response == 1 || response == 2 {}
			return 1
	}
	return 0
}

//pass in values as pointers, change them
func takeAnyTwoForks(N int, f1 *int, f2 *int, forks *Chans) {
	var response int
	var c = true //continue loop?
	//get first fork
	for c {
		for i := 0; i < N; i++ {
			response = pickUpFork(i, forks)
			if response == 1 {
				*f1 = i
				c = false
			}
		}
	}
	c = true
	//get second fork
	for c {
		for i := 0; i < N; i++ {
			if i != *f1 {
				response = pickUpFork(i, forks)
				if response == 1 {
					*f2 = i
					c = false
				}
			}
		}
	}
}

//put down a fork
//we have to ensure the fork is put down
//number = thread putting a fork down
func putDownFork(number int, forks *Chans) int {
	var sending msg
	sending.owner = number
	sending.code = 2

	var response msg //to hold response code
	response.code = 0
	for response.code == 0 {
		forks.array[number] <- sending //send signal to a fork
		response = <- forks.array[number] //get a signal back
	}
	return response.code
}

//get a random int >= 0 and < maxInt
func randInt(maxInt int) int {
	return rand.Intn(maxInt)
}

//eat for a short time
//f1, f2 = forks
//forks = channels for forks
func eat(f1 int, f2 int, forks *Chans) int  {
	//eat
	// ..."eats"...
	time.Sleep(50*time.Millisecond)
	//put back forks
	var r1, r2 int
	r1 = putDownFork(f1, forks)
	r2 = putDownFork(f2, forks)

	if r1 != 0 && r2 != 0 {
		return 1 //put both forks down
	} else {
		println("shouldnt ever print this lol")
		return 0 //somehow was unable to put both down
	}
}

//while sleeping, wait for a signal from neighbors philosophers
//number = which thread is waiting
//n1, n2 neighbors
//phils - signals for philosophers
func waitForNudge(number int, n1 int, n2 int, phils *Chans) int {
	//wait for response from either neighbor
	for {
		var response = <- phils.array[number]
		if response.code == 1 { //if code is correct
			//make sure response sent by neighbors
			if response.owner == n1 || response.owner == n2 {
				return response.owner
			}
		}
	}
}

//a philosopher is a goroutine
//each goroutine has two channels for picking up forks
//s = sleep percentage, k = enlightened percentages
//number = which philosopher this is
//N = total number of philosophers
//input and output channels
//left fork is, generally, n-1th fork
//right fork is the nth fork
//phils channels are for nudging
func Phil(number int, N int, forks *Chans, phils *Chans) {
	if number >= N || number < 0 { return } //check number argument

	//initialize state to thinking
	var state int = 1
	talkCustom(number, "Hello! I'm thinking.")

	//stuff for random state changes
	const percentMax int = 99	//ints from 0 - 99 used for percentages
	var tempRand = 0			//store random values
	//probabilities, ints from 0 - 99 for each run of for loop
	//these will be checked often
	const sProb int = 2			//chance of entering sleep state
	const kProb int = 3	 	//chance of entering enlightened state
	const hungerProb int = 6	//chance of getting hungry while thinking

	//determine the forks and neighbors for this thread
	//f1 is picked first, f2 is picked second
	var f1, f2, n1, n2 int
	//order depends on whether first, last, or in between
	if number == 0 { //if first fork (0)
		f1 = N-1	//pick up left fork first (N-1th (last) fork)
		f2 = 0		//pick up right fork next (0th fork)
		n1 = N-1	//left neighbor is last phil
		n2 = 0+1	//right neighbor is next phil
	} else if number == N-1 { //if last fork
		f1 = 0		//pick up right fork first (0th fork)
		f2 = N-2	//pick up left fork next (N-2th fork)
		n1 = N-2	//left neighbor is previous phil
		n2 = 0		//right neighbor is first phil
	} else { //otherwise
		f1 = number - 1	//pick up left fork first (number-1th fork)
		f2 = number		//pick up right fork next (numberth fork)
		n1 = number - 1 //left neighbor is previous phil
		n2 = number + 1	//right neighbor is next phil
	}

	var response int = -1 //for channel responses

	//forks for enlightened state
	var efork1, efork2 int
	printdb("p1")
	//main loop for state machine
	for {
		printdb("p2")
		//get random number between 0 and 99
		tempRand = randInt(percentMax)

		//random state changes
		if tempRand > percentMax-sProb {
			//s% chance of entering sleep state while doing something normal
			switch state {
			case 1:       //thinking
				state = 4 //sleep thinking
				talk(number, state)
			case 2:       //hungry
				state = 5 //sleep hungry
				talk(number, state)
			case 3:       //eating
				state = 6 //sleep eating
				talk(number, state)
			}
			printdb("p3")
		} else if tempRand < kProb && state == 1 {
			//k% chance of enlightened state while thinking
			state = 7 //enlightened thinking
			talk(number, state)
			printdb("p4")
		}
		printdb("p5")
		//if thinking, nudge neighbors
		if state == 1 || state == 7 {
			//nudge  neighbor
			printdb("p6")

			var send msg
			send.owner = number
			send.code = 1
			//nudge left neighbor
			select {
			case phils.array[n1] <- send:
				talkCustom(number, "Nudging philosopher #" + strconv.Itoa(n1))
			default:
				printdb("p7")
			}
			//nudge right neighbor
			select {
			case phils.array[n2] <- send:
				talkCustom(number, "Nudging philosopher #" + strconv.Itoa(n2))
			default:
				printdb("p8")
			}
			printdb("p9")
		}
		printdb("p10")
		//depending on state, do different stuff
		switch state {
		/* regular states */
		case 1: //thinking
			//talk(number, state)
			//possibly get hungry
			tempRand = randInt(percentMax)
			if tempRand < hungerProb {
				state = 2 //get hungry
				talk(number, state)
			}
		case 2: //hungry
			//try to pick up forks;
			printdb("p11")
			response = takeTwoForks(f1,f2,forks)
			printdb("p12")
			if response == 1 {
				state = 3 //now eating
				talk(number, state)
			} else {
				talkCustom(number, "Tried picking up forks, still hungry...")
			}
		case 3: //eating
			printdb("p13")
			response = eat(f1, f2, forks)
			printdb("p14")
			if response == 1 {
				state = 1 //go back to thinking
				talk(number, state)
			}
		/* sleep states; these are a tad redundant but oh well */
		case 4: //sleep thinking, wait for nudged
			printdb("p16")
			response = waitForNudge(number, n1, n2, phils)
			printdb("p17")
			//go back to thinking
			state = 1
			talkCustom(number, "I was nudged awake by philosopher #" + strconv.Itoa(response))
			talk(number,state)
		case 5: //sleep hungry, wait for nudged
			printdb("p18")
			response = waitForNudge(number, n1, n2, phils)
			//go back to hungry
			printdb("p19")
			state = 2
			talkCustom(number, "I was nudged awake by philosopher #" + strconv.Itoa(response))
			talk(number,state)
		case 6: //sleep eating, wait for nudged
			printdb("p20")
			response = waitForNudge(number, n1, n2, phils)
			printdb("p21")
			//go back to eating
			state = 3
			talkCustom(number, "I was nudged awake by philosopher #" + strconv.Itoa(response))
			talk(number,state)
		/* enlightened states */
		case 7: //enlightened think: similar to regular think, but change to 8 instead of 2
			//talk(number, state)
			tempRand = randInt(percentMax)
			if tempRand < hungerProb {
				state = 8 //get enlightened hungry
				talk(number, state)
			}
		case 8: //enlightened hunger
			// take any forks
			talkCustom(number, "Taking two available forks")
			//take any two available forks
			takeAnyTwoForks(N, &efork1, &efork2, forks)
			state = 9
			talk(number, state)
		case 9: //enlightened eating
			printdb("p22")
			eat(efork1, efork2, forks)
			printdb("p23")
			state = 1
			talk(number, state)
		default: //exit the loop
			printdb("p24")
			return
		}
		//reset response value!
		response = -1
		printdb("p25")
	}
	//slow stuff down a bit
	time.Sleep(time.Millisecond*1000)
}

/* arbiter - takes pointer to array of forks
each fork has a pointer to the channel the fork uses to communicate

Andrew's magic fork handling protocol (RFC 31337)
-------------------------------------------------
Philosopher threads send messages via fork channels.
Arbiter is listening and receives a request from a philosopher thread:

1 to pick up a fork; this returns:
	1 if success
	0 if fail
	2 if fork already owned

2 to put down a fork; this returns:
	1 if success
	0 if fail
	2 if fork already down
*/
func arbiter(N int, forks *Chans, forkArray []int) {
	//for messages received
	var receive msg
	var cnt uint64 = 0
	//keep arbitrating until program exits
	for {
		cnt++
		if cnt % 200000 == 0 {
			printForks(forkArray, N)
		}
		//printdb("a1")
		//printForks(forkArray, N)
		//printForks(forkArray, N)
		//check each fork
		for i := 0; i < N; i++ {
			//printdb("a2")
			//fmt.Println("Arbiter checking fork #" + strconv.Itoa(i))
			select {
			case receive = <-forks.array[i]: //get a message from the fork's channel
				//fmt.Println("Arbiter getting message from fork's channel")
			default:
				//fmt.Println("Arbiter found no message on this channel. Continuing to next fork.")
				continue
			}
			//build message to send back
			var send msg
			send.owner = -1 //arbitrator
			printdb("a2")
			if receive.code == 1 { //if we received a signal to pick up the fork
				printdb("a3")
				//is fork unowned?
				if forkArray[i] == -1 {
					printdb("a4")
					//set owner of fork to requester
					forkArray[i] = receive.owner
					send.code = 1 //success
				} else if forkArray[i] == receive.owner { //if fork is owned by sender already
					printdb("a5")
					send.code = 2 //already owned
				} else { //cannot pickup
					printdb("a6")
					send.code = 0 //failure
				}
				printdb("a7")
				forks.array[i] <- send
				printdb("a8")

			} else if receive.code == 2 { //if signal to put down fork
				printdb("a9")
				//is fork already down?
				if forkArray[i] == -1 {
					printdb("a10")
					send.code = 2
				} else if forkArray[i] == receive.owner {
					printdb("a11")
					//if signal sent by owner of fork
					//then we can put it down
					forkArray[i] = -1 //set as unowned
					send.code = 1 //success
				} else { //error
					printdb("a12")
					send.code = 0 //failure
				}
				printdb("a13")
				forks.array[i] <- send
				printdb("a14")
			} //else do nothing
			printdb("a15")
		}
		//printdb("a16")
	}
}

//main driver
//sets stuff up
func main() {
	fmt.Println()
	fmt.Println("-------------------------------------------")
	fmt.Println("Andrew Yaros - CS 361 - HW 5 1b - Starting:")
	fmt.Println("-------------------------------------------")

	const N int = 5 //how many philosophers are there?
	if(N < 2) { return } // ( -_____- )

	fmt.Println("Creating forks...")
	var forkChannels Chans //array of fork channels
	forkChannels.array = make([]chan msg, N)
	var forkArray = make([]int, N) //array for fork data

	fmt.Println("Creating philosopher channels...")
	/* array of phil channels
	phil sends a 1 on its own channel to sleep
	waits for another other channel to send a 1 */
	var phils Chans
	phils.array = make([]chan msg, N)

	fmt.Println("Populating arrays...")
	for i := 0; i < N; i++ {
		forkChannels.array[i] = make(chan msg)
		phils.array[i] = make(chan msg)
		forkArray[i] = -1 //unowned fork
	}

	fmt.Println("Creating arbitrator...")
	/* create an arbiter
	give it N, the fork channels, and a blank array
	for it to keep track of forks  */
	go arbiter(N, &forkChannels, forkArray)
	time.Sleep(100 * time.Millisecond)

	fmt.Println("Starting philosopher threads:")
	fmt.Println()
	//create a bunch of philosopher threads
	for i := 0; i < N; i++ {
		go Phil(i, N, &forkChannels, &phils)
	}

	//put main thread to sleep
	//when sleep stops, program ends
	time.Sleep(30 * time.Second) //10 min
}

