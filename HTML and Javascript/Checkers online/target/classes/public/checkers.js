/*===================
Andrew Yaros - CS 451
Checkers Online
Clientside game script
====================*/

//aey - html rgb color values
const REDCOLOR       = "#ea4942";	//red squares
const BLACKLIGHTER   = "#828282";	//lighter black square color
const BLACKCOLOR     = "#454545";	//darker black square color
const REDPIECE       = "#d03232";	//red pieces
const REDSTROKE      = "#b41515";	//red piece stroke
const BLACKPIECE     = "#252525";	//black pieces
const BLACKSTROKE    = "#000000";	//black stroke
const KINGSTROKE     = "#eedd88";	//outline of kings on board
const DARKKINGSTROKE = "#bb8800";	//outline of kings against white
const HIGHLIGHTCOLOR = "#00ff44";	//board highlight color

//aey - get the URL
var thehostname = window.location.hostname;
var theprotocol = window.location.protocol;
var theport = window.location.port;
const THE_URL = theprotocol + "//" + thehostname + ":" + theport;

//aey - board canvas element
var board = document.getElementById("Board");
var boardCtx = board.getContext("2d");

//aey - pieces canvas element
var pieces = document.getElementById("Pieces");
var pieceCtx = pieces.getContext("2d");

//aey - overlay canvas element
var overlay = document.getElementById("Overlay");
var overCtx = overlay.getContext("2d");
overCtx.globalAlpha = .5; //overlay is semi-transparent

//aey - player's pieces taken canvas element
var playerPieces = document.getElementById("PlayerPieces");
var PPctx = playerPieces.getContext("2d");

//aey - opponent's pieces taken canvas element
var opponentPieces = document.getElementById("OppoPieces");
var OPctx = opponentPieces.getContext("2d");

//aey - clear a canvas
function clearCanvas(canvas, ctx) {
	ctx.clearRect(0, 0, canvas.width, canvas.height);
}

//aey - used for drawing the board
//aey - toggled on and off to draw either black or red squares
var isblack = true;
function toggleblack() {
	if(isblack) isblack = false;
	else isblack = true;
}

//aey - board dimensions
const BOARD_SIZE = board.width;
const BOARD_DIM = 8;
const SQUARE_SIZE = BOARD_SIZE/BOARD_DIM;

//aey - captured board dimensions
const CAPTURE_BOARD_SIZE = playerPieces.width;
const CAPTURE_BOARD_DIM = 6;
const CAPTURED_PIECE_WIDTH = CAPTURE_BOARD_SIZE/CAPTURE_BOARD_DIM;

//aey - there are two possible colors you can play as
//aey - the player will be assigned either red, or black
//aey - the actual values of these variables is arbitrary, as long as they are different from each other
const PLAYER_COLOR_BLACK = "black";
const PLAYER_COLOR_RED = "red";

//aey - representation of pieces on board
const R = 1;
const B = 2;
const RK = 3;
const BK = 4;

//aey - initial board array
const initialBoard = 
					[[0,R,0,R,0,R,0,R],
					 [R,0,R,0,R,0,R,0],
					 [0,R,0,R,0,R,0,R],
					 [0,0,0,0,0,0,0,0],
					 [0,0,0,0,0,0,0,0],
					 [B,0,B,0,B,0,B,0],
					 [0,B,0,B,0,B,0,B],
					 [B,0,B,0,B,0,B,0]];

//----------------------------
//aey - variables for the game
//aey - initialize user ID variables

//this is the w3 schools example function for getting info from a cookie
function getCookie(cname) {
	var name = cname + "=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var ca = decodedCookie.split(';');
	for(var i = 0; i <ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}
	return "";
}

var PLAYER_ID = getCookie("playerId");
var OPPO_ID = 1137;//initialize; will be gotten later by status update
//aey - is the player allowed to move?
var IS_PLAYER_TURN = false;
//aey - two main states for a turn; before piece chose, after piece chosen
//this asks, is player on the second turn yet?		
var IS_A_PIECE_SELECTED = false; 
//aey - is the game not over?
var GAME_NOT_OVER = true;
//aey - did player win?
var IS_WINNER = false;
//aey - initialize player color
var PLAYER_COLOR = "0";
//aey - initialize a variable for opponent color
var OPPONENT_COLOR = "0";

//aey - at first, no pieces are taken
var PLAYER_NUM_KINGS = 0;
var PLAYER_NUM_PAWNS = 0;
var OPPONENT_NUM_KINGS = 0;
var OPPONENT_NUM_PAWNS = 0;

//aey - temporary variables to hold the currently selected piece 
var tempStartX = 0;
var tempStartY = 0;
//----------------------------
//aey - counter vars for rematch function
var rematchRequests = 0;
var maxRequests = 500;
//aey - set a current board as the initial board
var currentBoard = initialBoard;

function getPiece(boardx, boardy) {
	return currentBoard[boardy][boardx];
}

//aey - inputs are x and y board coordinates, so 0 - 7
//the opposite of getBoardCoords
function getRealCoords(boardx,boardy) {
	return [boardx*SQUARE_SIZE , boardy*SQUARE_SIZE];
}

//aey - given an x and a y, get the board's coordinates of a click (returns [0-7, 0-7] )
//the opposite of getRealCoords
function getBoardCoords(x, y) {
	return [ Math.floor(x/SQUARE_SIZE), Math.floor(y/SQUARE_SIZE)];
}

//aey - draw a checkers piece on the checkerboard
//x coordinate, y coordinate
//color of the piece
//isking: boolean
function drawPiece(x, y, color, isking) {
	var cord = getRealCoords(x,y);
	var xcenter = cord[0] + (SQUARE_SIZE/2);
	var ycenter = cord[1] + (SQUARE_SIZE/2);
	pieceCtx.beginPath();
	pieceCtx.arc(xcenter, ycenter, (SQUARE_SIZE/2) - (SQUARE_SIZE/10), 0, 2 * Math.PI, false);
	if(isking) { 
		if(color == REDPIECE) {
			pieceCtx.fillStyle=REDSTROKE;
		} else if (color == BLACKSTROKE) {
			pieceCtx.fillStyle=BLACKSTROKE;
		} pieceCtx.fill();
	} else {
		pieceCtx.fillStyle=color;
	} pieceCtx.fill();
	
	//aey - draw outer stroke differently if king, red, or black
	pieceCtx.lineWidth=7;
	       if(isking) {
		pieceCtx.strokeStyle=KINGSTROKE;
	} else if(color == REDPIECE) {
		pieceCtx.strokeStyle=REDSTROKE;
	} else if(color == BLACKPIECE) {
		pieceCtx.strokeStyle=BLACKSTROKE;
	} pieceCtx.stroke();
}

//aey - there is already a red div behind the board with a gradient
//aey - draw a big black gradient first, then cut away the parts where we want red squares
function drawBoard() {
	isblack = false; //aey - set initial square to red
	//aey - first fill board with a black gradient
	boardCtx.beginPath();
	boardCtx.rect(0, 0, BOARD_SIZE, BOARD_SIZE);
	var blackGrad = boardCtx.createLinearGradient(0, 0, 0, BOARD_SIZE);
	blackGrad.addColorStop(0, BLACKLIGHTER);
	blackGrad.addColorStop(1, BLACKCOLOR);
	boardCtx.fillStyle=blackGrad;
	boardCtx.fill();
	//then remove red squares
	for(j = 0; j < BOARD_DIM; j++) {
		for(i = 0; i < BOARD_DIM; i++){
			if(!isblack) {
				removeSquare(boardCtx, i, j);
			} toggleblack();
		} //even-dimensioned boards require this toggled each row
		if(BOARD_SIZE%2 == 0) toggleblack();
	}
} //-----------------------------
//aey - draw the board right away
//-------------------------------
drawBoard();
//----------
placePieces(initialBoard); //for show

//aey - get update from server 
function updateStatus() {
	//aey - send the player ID, get the board for that game
	var requestData = { "playerID":PLAYER_ID } ;
	
	//AJAX request
	$.ajax({
		type: "GET",
		url: THE_URL + "/statusUpdate",
		contentType: "application/json; charset=utf-8",		
		data: JSON.stringify(requestData),
		dataType: "html",
		success: function(msg) {
			console.log(msg);
			msg = JSON.parse(msg);
			
			
			//get opponent id
			OPPO_ID = msg.opponentUuid;
			
			//get the board data from msg
			//set currentBoard to that data 
			currentBoard = msg.currentBoard;
			
			//update the player color, I guess
			if(msg.playerColor == 0) {
				PLAYER_COLOR = PLAYER_COLOR_RED;
				OPPONENT_COLOR = PLAYER_COLOR_BLACK;
			} else {
				PLAYER_COLOR = PLAYER_COLOR_BLACK;
				OPPONENT_COLOR = PLAYER_COLOR_RED;
			}
			//aey - upadte page elements
			if(PLAYER_COLOR == PLAYER_COLOR_BLACK) {
				document.getElementById("playercolor").innerHTML = "black";
			} else {
				document.getElementById("playercolor").innerHTML = "red";
			} document.getElementById("playerid").innerHTML = PLAYER_ID;
			
			//update the board; placePieces
			placePieces(currentBoard);
			
			//update values for captured players
			PLAYER_NUM_KINGS = msg.oppoKingsCaptured;	//number of the player's pieces that were captured
			PLAYER_NUM_PAWNS = msg.oppoPawnsCaptured;
			OPPONENT_NUM_KINGS = msg.userKingsCaptured; //number of the opponent's pieces that were captured
			OPPONENT_NUM_PAWNS = msg.userPawnsCaptured;
			
			//temp values to create to draw the captured pieces
			var tempColorP, tempColorO;
			if(PLAYER_COLOR == PLAYER_COLOR_RED) {
				tempColorP = REDPIECE;
				tempColorO = BLACKPIECE;
			} else {
				tempColorO = REDPIECE;
				tempColorP = BLACKPIECE;
			}
			
			//draw captured pieces for user
			drawCapturedPieces(PLAYER_NUM_KINGS, PLAYER_NUM_PAWNS, tempColorP, PPctx);
			drawCapturedPieces(OPPONENT_NUM_KINGS, OPPONENT_NUM_PAWNS, tempColorO, OPctx);
			

			
			
			//is the game over?
			if(msg.isOver == 1) {
				GAME_NOT_OVER = false; //break while loop
				//set winner
				if(msg.winner == 1) IS_WINNER = true;
				else IS_WINNER = false;
			} else {
				//otherwise, game not over
				GAME_NOT_OVER = true;
								//is players turn
				if(msg.playersTurn == 1) {
					//if so, set states IS_PLAYER_TURN to true
					//also set IS_A_PIECE_SELECTED to false
					IS_PLAYER_TURN = true;
					console.log("It is the player's turn");
					//IS_A_PIECE_SELECTED = false;
					//console.log("Player hasn't selected a piece");
					document.getElementById("ismyturn").innerHTML = "It's <b>your</b> turn. | ";
					document.getElementById("ismyturn").innerHTML = "It's <b>your</b> turn. | ";
				} else { //if not, set both to false
					IS_PLAYER_TURN = false;
					console.log("It is NOT the player's turn");
					IS_A_PIECE_SELECTED = false;
					console.log("Player hasn't selected a piece");
					document.getElementById("ismyturn").innerHTML = "It's the <b>opponent's</b> turn. | ";
				}	
			}			
		}, error: function(xhr,ajaxOptions, thrownError) {
			errorFlash(0, 0);
			console.log("Couldn't contact server for status update");
		}
	});
}

//aey - get the coordinates for a captured piece
//aey - technically, the upper left coordinates
function getCapturedCoords(i) {
	//if i is from 0 to 5, then first row
	if(i >= 0 && i <= 5) {
		return [i*CAPTURED_PIECE_WIDTH ,0];
	} //if i is from 6 to 11, then second row
	else if(i >= 6 && i <= 11) {
		return [(i - 6)*CAPTURED_PIECE_WIDTH,CAPTURED_PIECE_WIDTH];
	}
}

//aey - draw a smaller piece on a captured pieces board
function drawSmallPiece(i, color, isking, canvasCtx) {
	var cord = getCapturedCoords(i);
	var xcenter = cord[0] + (CAPTURED_PIECE_WIDTH/2);
	var ycenter = cord[1] + (CAPTURED_PIECE_WIDTH/2);
	canvasCtx.beginPath();
	canvasCtx.arc(xcenter, ycenter, (CAPTURED_PIECE_WIDTH/2) - (CAPTURED_PIECE_WIDTH/10), 0, 2 * Math.PI, false);
	if(isking) { 
		if(color == REDPIECE) canvasCtx.fillStyle=REDSTROKE;
		else /*if (color == BLACKPIECE)*/ canvasCtx.fillStyle=BLACKSTROKE;
	} else canvasCtx.fillStyle=color;
	canvasCtx.fill();
		
	//aey - draw outer stroke differently if king, red, or black
	canvasCtx.lineWidth=11;
	if(isking) canvasCtx.strokeStyle=DARKKINGSTROKE;
	else if(color == REDPIECE) canvasCtx.strokeStyle=REDSTROKE;
	else /*if(color == BLACKPIECE)*/ canvasCtx.strokeStyle=BLACKSTROKE;
	canvasCtx.stroke();
}

//aey - draw up to 12 captured pieces
function drawCapturedPieces(numkings, numpawns, color, canvasCtx) {
	var count = 0; //which piece is it
	
	//first draw kings, if any
	if(numkings > 0) {
		for(i = 0; i < numkings; i++) {
			drawSmallPiece(count, color, true, canvasCtx);
			count++;
		}
	} //then draw pawns, if any
	if(numpawns > 0) {
		for(i = 0; i < numpawns; i++) {
			drawSmallPiece(count, color, false, canvasCtx);
			count++;
		}
	}
}

function placePieces(piecearray) {
	//check dimensions of array
	if(piecearray.length != BOARD_DIM) {
		console.log("ERROR: Piece array dimension doesn't match board dimension");
		return;
	} for(i = 0; i < piecearray.length; i++) {
		if(piecearray[i].length != BOARD_DIM) {
			console.log("ERROR: Piece row dimesnion doesn't match board dimension");
			return;
		}
	} //aey - if we haven't returned, dimensions are correct
	//aey - clear the main board pieces
	clearCanvas(Pieces, pieceCtx);
	//aey - clear the captured pieces
	clearCanvas(playerPieces, PPctx);
	clearCanvas(opponentPieces, OPctx);
	for(j = 0; j < piecearray.length; j++) { //place each column
		for(i = 0; i < piecearray[j].length; i++) {
			       if(piecearray[i][j] == R) { //if red
				drawPiece(j, i, REDPIECE, false);
			} else if(piecearray[i][j] == B) { //if black
				drawPiece(j, i, BLACKPIECE, false);
			} else if(piecearray[i][j] == RK){ //if red king
				drawPiece(j, i, REDPIECE, true);
			} else if(piecearray[i][j] == BK){ //if black king
				drawPiece(j, i, BLACKPIECE, true);
			}
		}
	}
}

function removeSquare(context, boardx, boardy) {
	var cord = getRealCoords(boardx,boardy);
	context.clearRect(cord[0], cord[1], SQUARE_SIZE, SQUARE_SIZE);
}

function highlightSquare(boardx, boardy) {
	overCtx.beginPath();
	overCtx.lineWidth="1";
	var cord = getRealCoords(boardx,boardy);
	overCtx.rect(cord[0], cord[1], SQUARE_SIZE, SQUARE_SIZE);
	overCtx.fillStyle=HIGHLIGHTCOLOR;
	overCtx.fill();
}

//aey - event function for clicking on canvas
//aey - we will redefine this to other functions as needed
function onOverlayClick(boardx, boardy) {
	return 0;
}

//aey - listener function
function listenFunction(e){
	//aey - get dimensions of the board canvas
		var rect = overlay.getBoundingClientRect();
		
		//aey - get x coordinate on the board canvas
		//	relative to canvas size
		var x = (e.clientX - rect.left) * 
				(overlay.width / rect.width);
		
		//aey - get y coordinate on the board canvas
		//	relative to canvas size		
		var y = (e.clientY - rect.top) * 
				(overlay.height / rect.height);
				
		console.log("Clicked on actual coordinates x: " + x + ", y: " + y);
		
		//aey - get board coordinates
		var bc = getBoardCoords(x, y);
		console.log("Board coordinate: X: " + bc[0] + " Y: " + bc[1]);
		
		//aey - call function for event
		onOverlayClick(bc[0], bc[1]);
} //aey - add event listners
overlay.addEventListener('click', listenFunction, false);
overlay.addEventListener('dblclick', listenFunction, false);
overlay.addEventListener('dragstart', listenFunction, false);

//aey - empty onclick function
//flash board if not players turn
function errorFlash(boardx, boardy) {
	overCtx.rect(0, 0, BOARD_SIZE, BOARD_SIZE);
	overCtx.fillStyle = "#ffffff";
	overCtx.fill();
	setTimeout(function () {
		clearCanvas(Overlay, overCtx);
	}, 180);
}
function doNothing(x, y) {
	//do nothing
}

//aey - blank matrix for holding valid moves
const blankValidMovesMatrix = 
						[[0,0,0,0,0,0,0,0],
						 [0,0,0,0,0,0,0,0],
						 [0,0,0,0,0,0,0,0],
						 [0,0,0,0,0,0,0,0],
						 [0,0,0,0,0,0,0,0],
						 [0,0,0,0,0,0,0,0],
						 [0,0,0,0,0,0,0,0],
						 [0,0,0,0,0,0,0,0]];

//aey - initialize matrix to hold the current vaid moves
var curValidMoves = blankValidMovesMatrix;

//aey - highlight valid moes
function highlightValidMoves() {
	if(IS_A_PIECE_SELECTED){ //aey - we can only highlight moves if a piece is selected
		for(j = 0; j < curValidMoves.length; j++) {
			for(i = 0; i < curValidMoves[j].length; i++) {
				if(curValidMoves[i][j] == 1) highlightSquare(i, j);
			}
		}
	}
}

//aey - given a piece, calculate valid moves
function calculateValidMoves(startX, startY) {
	//store the coordinates
	tempStartX = startX;
	tempStartY = startY;
	
	curValidMoves = blankValidMovesMatrix; //initialize matrix
	
	//aey - send server request to calculate valid moves
	//send the player ID and the x, y coordinates
	var requestData = { "x":startX, "y":startY } ;
	
	//AJAX request
	$.ajax({
		type: "POST",
		url: THE_URL + "/validMoves",
		contentType: "application/json; charset=utf-8",		
		data: JSON.stringify(requestData),
		dataType: "html",
		success: function(msg) {
			console.log(msg);
			msg = JSON.parse(msg);
			
			var tempValidMoves = msg.matrix;
			for(j = 0; j < tempValidMoves.length; j++) {
				for(i = 0; i < tempValidMoves[j].length; i++) {
					curValidMoves[i][j] = tempValidMoves[j][i];
				}
			}
			
			//curValidMoves = msg.matrix;
			
			//aey - check if there is any valid move for this piece
			var isThereAnyMove = false;
			for(j = 0; j < curValidMoves.length; j++) {
				for(i = 0; i < curValidMoves[j].length; i++) {
					if(curValidMoves[i][j] == 1) {
						isThereAnyMove = true;
					}
				}
			} //if so, then a piece has now been selected
			if(isThereAnyMove) {
				//go to the next game state
				IS_A_PIECE_SELECTED = true;
				highlightValidMoves();
				console.log("Found a valid move");
			} else {
				IS_A_PIECE_SELECTED = false;
				errorFlash(0, 0);
				console.log("No valid moves");
			}
		}, error: function(xhr,ajaxOptions, thrownError) {
			errorFlash(0, 0);
			console.log("Couldn't contact server to get valid moves");
		}
	});
}

// { "matrix":[ [0,0,0,0,1,0,0,0],[1,0,1,0,1,0,0,0], ... ] }

function isMoveValid(boardx, boardy) {
	if(curValidMoves[boardx][boardy] == 1) return true;
	else return false;
}

//aey - check the current board to see what piece is there
//if it is a valid piece, then highlight moves
//if not, then flash screen
function choosePiece(boardx, boardy) {
	if(!IS_A_PIECE_SELECTED) { //only choose piece if piece not selected
		//aey - check if there is a piece on current board at those coordinates
		var cur = getPiece(boardx, boardy);
		console.log("Got piece " + cur + " at " + boardx + " and " + boardy);
		//aey - if the piece is a valid piece
		if (cur != 0) {
			//aey - what color is the player
			//if player is red and player clicked a red piece
			//or if player is black and clicked a black piece
			if(( PLAYER_COLOR.localeCompare(PLAYER_COLOR_RED)   == 0 && (cur == R || cur == RK) ) || 
			   ( PLAYER_COLOR.localeCompare(PLAYER_COLOR_BLACK) == 0 && (cur == B || cur == BK) )) {
				//aey - then calculate moves
				calculateValidMoves(boardx, boardy);					
			} else { 
				//errorFlash(0, 0);
				console.log("Player clicked on a space that isn't his");
			}
		} else { 
			//errorFlash(0, 0); 
			console.log("Player clicked on blank space");
		}
	}
}

function chooseDestination(boardx, boardy) {
	if(isMoveValid(boardx, boardy)) { //aey - if move is valid
		onOverlayClick = doNothing;  //aey - disable user input
		//aey - then submit the move to the server
		//aey - send server request to calculate valid moves
		//send the player ID and the x, y coordinates
		var requestData = { "playerID":PLAYER_ID, "x1":tempStartX, "y1":tempStartY, "x2":boardx , "y2":boardy } ;
		//AJAX request
		$.ajax({
			type: "POST",
			url: THE_URL + "/sendMove",
			contentType: "application/json; charset=utf-8",		
			data: JSON.stringify(requestData),
			dataType: "html",
			success: function(msg) {
				console.log(msg);
				msg = JSON.parse(msg);
				
				//aey - if confirmed then get board from serverredraw board
				//aey - if move is valid server sends a 1, else a 0
				
				//aey - if move is confirmed by server
				if(msg.valid == 1) {
					//aey - update the game status from the server
					updateStatus();
				} else {
					updateStatus();
					IS_A_PIECE_SELECTED = false;
					console.log("Server says move was invalid");
				} //aey - clear the canvas
				clearCanvas(overlay, overCtx);
			}, error: function(xhr,ajaxOptions, thrownError) {
				errorFlash(0, 0);
				console.log("Couldn't contact server to send move");
			}
		});
					
		//aey - if the server says the game is over
		//GAME_NOT_OVER == false;      //aey - game now over
	} else { //move not valid
		//move out of the state
		IS_A_PIECE_SELECTED = false;
		//clear canvas
		clearCanvas(overlay, overCtx);
		//updateStatus(); //update status; user will have to repick a piece
	}
}

//aey - button to go home
function goHome() {
	window.location.href = "home.html";
}

//aey - function to request a rematch
function requestRematch() {
	rematchRequests++;
	//aey - if over max requests, timeout
	if(rematchRequests > maxRequests) {
		//aey - reset button text
		document.getElementById("rematchbutton").innerHTML = "Request rematch";
		//enable button
		document.getElementById("rematchbutton").disabled = false;
		rematchRequests = 0;
		return -1;
	} else {
		//aey - rename button
		document.getElementById("rematchbutton").innerHTML = "Waiting for other player";
		//aey - disable button
		document.getElementById("rematchbutton").disabled = true;
		
		//GET /rematch -- expects { 'uuid': <opponent_uuid_string> }, return { 'haveGame': <0/1> }
		var requestData = { "uuid": OPPO_ID };
		//aey - AJAX request
		$.ajax({
			type: "POST",
			url: THE_URL + "/rematch",
			contentType: "application/json; charset=utf-8",		
			data: JSON.stringify(requestData),
			dataType: "html",
			success: function(msg) {
				console.log(msg);
				msg = JSON.parse(msg);
				
				//aey - if a game is ready, then join the game
				if(msg.haveGame == 1) {
					window.location.href = "checkers.html";
				} else {
					setTimeout(requestRematch(), 200);
				}
			}, error: function(xhr,ajaxOptions, thrownError) {
				errorFlash(0, 0);
				console.log("Couldn't contact server to get rematch");
			}
		});
	}
}

//aey - remove game from server
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

//aey - milliseconds to update
var updateFrequency = 500; //milliseconds
var stopGame = 0;//initialize

//aey - game update function
function updateGameState() {
	console.log("Updating game state");
	if(GAME_NOT_OVER){
		if(IS_PLAYER_TURN) {
			//console.log("Player's turn");
			if(!IS_A_PIECE_SELECTED) {
				//console.log("Player can select a piece");
				//aey - redefine onclick function
				//aey - user may pick a piece to move
				onOverlayClick = choosePiece;
			} else {
				//console.log("Current temporary selection is " + tempStartX + " , " + tempStartY);
				//aey - redefine onclick function
				//aey - user must pick a place to move to
				onOverlayClick = chooseDestination;
			}
		} else {
			//console.log("Not players turn");
			//aey - redefine onclick function
			//aey - user not allowed to move
			onOverlayClick = errorFlash;
			updateStatus();
		}
	} else {
		console.log("Game is over now");
		//aey - tell server we are done
		endGameRequest();
		var winnerstring = "";
		if(IS_WINNER) winnerstring += "You (" + PLAYER_COLOR + ") win!";
		else winnerstring += "You lost! " + OPPONENT_COLOR + " won!";
		//aey - display the winner
		document.getElementById("whowon").innerHTML = winnerstring;
		//aey - display gameover menu; stop running this function
		document.getElementById("gameOver").style.display = "block";
		updateStatus();
		clearInterval(stopGame); //stop looping
	}
}

//aey - get the status from the server, then place initial pieces on the board
//---------------------------------
updateStatus(); //this is important
//---------------------------------

//aey - player color, current board, updating pieces on the board, etc. is set by the updatestatus function
var updateFunction = updateGameState;
//aey - update the game state once a second
stopGame = setInterval(updateFunction, updateFrequency);