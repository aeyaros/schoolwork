/*===================
Andrew Yaros - CS 451
Checkers Online
Clientside home page script
=========================*/

//aey - get the URL
var thehostname = window.location.hostname;
var theprotocol = window.location.protocol;
var theport = window.location.port;
const THE_URL = theprotocol + "//" + thehostname + ":" + theport;
console.log(THE_URL);
		
/*
if they havent already tried to join, then you will be waiting for them to join with you
keep asking for a game with the other person
if the other person is in a game with you
timeout after several seconds
*/		

/*  credit to w3 schools for this cookie function
https://www.w3schools.com/js/js_cookies.asp */
//this is the w3 schools example function for getting info from a cookie
function getCookie(cname) {
	var name = cname + "=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var ca = decodedCookie.split(';');
	for(var i = 0; i <ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') c = c.substring(1);
		if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
	} return "";
}

//aey - if there is a current game, remove from server
function endGameRequest() {
	var requestData = { };
	//aey - AJAX request
	$.ajax({
		type: "GET",
		url: THE_URL + "/endGame",
		contentType: "application/json; charset=utf-8",		
		data: JSON.stringify(requestData),
		dataType: "html",
		success: function(msg) {
			console.log(msg);					
			//aey - do nothing
		}, error: function(xhr,ajaxOptions, thrownError) {
			errorFlash(0, 0);
			console.log("Couldn't contact server to send endGame request");
		}
	});
}
endGameRequest();

//aey - get UUID
var PLAYER_ID = getCookie("playerId");
//aey - set div element
document.getElementById("myUUID").innerHTML = PLAYER_ID;


//timeout after so many attemps at randomly joining
var joinCount = 0;
const joinMax = 500;

//aey - function to go to the game page
function playGame() {
	window.location.href = "checkers.html";
}

//aey - function to go to another page
function goTo(pageName) {
	window.location.href = "./" + pageName + ".html";
}

//aey - join with UUID
function joinGame(oppoUUID) {
	//GET /invite -- expects { 'uuid': <opponent_uuid_string> }, return { 'haveGame': <0/1> }
	
	var requestData = { "uuid":oppoUUID };
	//AJAX request
	$.ajax({
		type: "POST",
		url: THE_URL + "/invite",
		contentType: "application/json; charset=utf-8",		
		data: JSON.stringify(requestData),
		dataType: "html",
		success: function(msg) {
			console.log(msg);
			msg = JSON.parse(msg);
			
			//if a game is ready, then join the game
			if(msg.haveGame == 1) {
				playGame();
			} else console.log("Doesnt have game");
		}, error: function(xhr,ajaxOptions, thrownError) {
			console.log("Couldn't contact server for invite game");
		}
	});
}

//aey - join random game
function randomGame() {
	joinCount++;
	document.getElementById("randomUUID").innerHTML = "Searching for another game...";
	//if max number of attempts
	if(joinCount > joinMax) {
		joinCount = 0;
		//set button text briefly
		document.getElementById("randomUUID").innerHTML = "Request timed out.<br/> Try again.";
		setTimeout(function () {
			//change button text back
			document.getElementById("randomUUID").innerHTML = "Join random game";
		}, 2000);
		return -1;
	}
	//GET /random -- expects nothing, return { 'haveGame': <0/1> }
	var requestData = {};
	//AJAX request
	$.ajax({
		type: "GET",
		url: THE_URL + "/random",
		contentType: "application/json; charset=utf-8",		
		data: JSON.stringify(requestData),
		dataType: "html",
		success: function(msg) {
			console.log(msg);
			msg = JSON.parse(msg);
			
			//if a game is ready, then join the game
			if(msg.haveGame == 1) {
				document.getElementById("randomUUID").innerHTML = "Found one!";
				playGame();
			} else {
				console.log("Trying again to get random game");
				setTimeout(randomGame(), 200);
			}
		}, error: function(xhr,ajaxOptions, thrownError) {
			console.log("Couldn't contact server for random game");
		}
	});
}


//aey - user clicks a button and joins a game, either randomly or with a specific ID
function buttonClicked(joinType) {
	if(joinType == "random") {
		joinCount = 0; //start at 0
		setTimeout(randomGame(), 500);
	} else if(joinType == "uuid"){
		//join a game with the id selected by the user
		var opId = document.getElementById("oppoID").value;
		console.log("Opponent ID = " + opId);
		joinGame(opId);
	}
}
