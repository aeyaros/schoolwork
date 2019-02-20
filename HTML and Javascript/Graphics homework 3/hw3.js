/*------------------------------------
---- Andrew Yaros | CS 432 | HW 3 ----
------------------------------------*/
var g1;
var points;
const maxVertices = 3000;

const sidesInATriangle = 3;
const sidesInASquare = 4;
const sidesInAPentagon = 5;

var updateSpeed = 16;

var counterClockwise = false;
var breatheRadiusFactor = 0; //for resizing circle

// on a scale of .2 to 6; for rotating shapes and for updating breatheRadiusFactor
var rotationDegrees = 3.1; 

/*----------------------------------------------------------*/

//Colors

var black  = vec4(0,0,0, 1);
var white  = vec4(1,1,1, 1);
var red    = vec4(1,0,0, 1);
var pink   = vec4(1,0,1, 1);
var yellow = vec4(1,1,0, 1);
var lime   = vec4(0,1,0, 1);
var cyan   = vec4(0,1,1, 1);
var blue   = vec4(0,0,1, 1);
var green  = vec4(0.0, 0.5, 0.0, 1);
var purple  = vec4(0.5, 0.0, 0.5, 1);

var currentSquareColor = white;
var currentEllipseColor = red;

var triangleColors = [
	vec4(1,0,0,1),
	vec4(0,0,1,1),
	vec4(0,1,0,1)
];

/*----------------------------------------------------------*/

//OBJECT VARIABLES
//yes I know globals are bad
//please don't kill me
//the less I have to pass through functions as I figure this out, the better

//create squares
var squareCenter = new vec2(0.0, -0.25);
var squareRad = 0.6; //square radius
var squareDec = 0.1; //decrementer; each inner square is this much smaller than the one outside it
var squares = getSquares(squareCenter, squareRad, squareDec);


//create ellipse
var ellipseCenter = new vec2(-0.6, 0.65);
var ellipseScale = new vec2(1, 0.6);
var ellipse = nGon(ellipseCenter, .225, 100, ellipseScale, false);


//create triangle
var triangleCenter = new vec2(0, 0.65);
var triangleScale = new vec2(1,1);
var triangle = nGon(triangleCenter, .25, sidesInATriangle, triangleScale, false);

//create circle
var circleCenter = new vec2(0.6, 0.65);
var circleScale = new vec2(1,1);
var circleRadius = 0.225;
var circle = nGon(circleCenter, circleRadius, 128, circleScale, true);

//array for pentagons
var pentaArray = []; //shapes
var pentaColors = []; //colors

/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/

window.onload = function init() {
	var canvas = document.getElementById("gl-canvas");
	gl = WebGLUtils.setupWebGL(canvas);
	if(!gl) { 
		alert("WebGL didn't show up for work.")
	}
	
	/*----------------------------------------------------------*/
	
	//Configure WebGL - set up buffers
	
	gl.viewport(0, 0, canvas.height, canvas.width);
	gl.clearColor(0.0, 0.0, 0.0, 1.0);

	var program = initShaders( gl, "vertex-shader", "fragment-shader");
	gl.useProgram( program );
	
	var vBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, maxVertices, gl.STATIC_DRAW);
	
	var vPosition = gl.getAttribLocation(program, "vPosition");
	gl.vertexAttribPointer(vPosition, 2, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(vPosition);
	
	var cBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, cBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, 16*maxVertices, gl.STATIC_DRAW);
	
	var vColor = gl.getAttribLocation( program, "vColor");
	gl.vertexAttribPointer(vColor, 4, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(vColor);
	
	//event handlers
	
	document.getElementById("theslider").onchange = function(event) {
			//.2 + (((100 - x)/100) * 6)
			rotationDegrees = .2 + ((event.target.value/100) * 5.8);
	};
		
	//change square color
	document.getElementById("squareColorSelector").onclick = function(event) {
			switch(event.target.index) {
			case 0: currentSquareColor = white; break;
			case 1: currentSquareColor = pink; break;
			case 2: currentSquareColor = yellow; break;
			case 3: currentSquareColor = cyan; break;
			case 4: currentSquareColor = lime; break;
		}
	};
	
	
	/*----------------------------------------------------------*/
	//initial render
	render(program, vBuffer, vPosition, cBuffer, vColor);
	//render function will repeat using settimeout
}

//get keypress; change ellipse color
document.onkeypress = function(e) {
	e = e || window.event;
	var currentKey = String.fromCharCode(e.keyCode || e.which);
	console.log(currentKey);
	switch (currentKey) {
	case "r":
		currentEllipseColor = red;
		break;
	case "g":
		currentEllipseColor = green;
		break;
	case "b":
		currentEllipseColor = blue;
		break;
	case "y":
		currentEllipseColor = yellow;
		break;
	case "p":
		currentEllipseColor = purple;
		break;
	case "w":
		currentEllipseColor = white;
		break;
	default:
		break;
	}
};

/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/

//RENDER FUNCTION: Draw stuffs

function render(program, vBuffer, vPosition, cBuffer, vColor) {
	/*----------------------------------------------------------*/
	//clear window
	gl.clear( gl.COLOR_BUFFER_BIT );
	
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	//modify objects
	
	var flipRotation = -1; //to reverse angle direction
	//this is multiplied by the theta value we use to update the rotation
	
	//flip rotation?
	if (counterClockwise) {
		flipRotation = 1; //if counterclockwise, we dont need to reverse direction
	}
	
	var thetaUpdate = rotationDegrees * flipRotation; 
	
	//rotate triangle
	triangle = rotateShape(triangle, thetaUpdate, triangleCenter);
	
	//rotate squares	
	for (i = 0; i < squares.length; i++) { //for each square
		for (j = 0; j < squares[i].length; j++) { //for each point in ith square
			//rotate square opposite that of triangle, so multiply by -1
			squares[i][j] = rotatePoint(squares[i][j], -1*thetaUpdate, squareCenter);
		}
	}
	
	//recalculate circle with new radius
	var currentCircleRadius = (circleRadius/2) + 
			((circleRadius/2) * Math.abs(Math.cos(breatheRadiusFactor)));
	circle = nGon(circleCenter, currentCircleRadius, 128, circleScale, true);
	
	//update the radius changing variable
	//speed at which it is updated relates to rotational speed of other shapes
	breatheRadiusFactor = breatheRadiusFactor + (rotationDegrees/25);
	
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	
	
	//Set up with vertex fragment shader
	program = initShaders( gl, "vertex-shader", "fragment-shader-alt" );
	gl.useProgram( program );
	
		
	//draw triangle 
	gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, flatten(triangle), gl.STATIC_DRAW);
	for (i = 0; i < sidesInATriangle; i++) { //set colors of triangle
		gl.bindBuffer( gl.ARRAY_BUFFER, vBuffer);
		var t = triangle[i];
		gl.bufferSubData(gl.ARRAY_BUFFER, 8*i, flatten(t));
		
		gl.bindBuffer( gl.ARRAY_BUFFER, cBuffer);
		t = vec4(triangleColors[i]);
		gl.bufferSubData(gl.ARRAY_BUFFER, 16*i, flatten(t));
	}
	
	
	
	
	//draw triangle
	gl.drawArrays(gl.TRIANGLE_FAN, 0, sidesInATriangle);
	
	//draw circle
	gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, flatten(circle), gl.STATIC_DRAW);
	for (i = 0; i < circle.length; i++) { //set colors for triangle fan
		gl.bindBuffer( gl.ARRAY_BUFFER, vBuffer);
		var t = circle[i];
		gl.bufferSubData(gl.ARRAY_BUFFER, 8*i, flatten(t));
		gl.bindBuffer( gl.ARRAY_BUFFER, cBuffer);
		
		//red gradient
		var redBrightness = (1 / circle.length) * i;
		t = vec4(redBrightness, 0, 0, 1);
		gl.bufferSubData(gl.ARRAY_BUFFER, 16*i, flatten(t));
	}
	gl.drawArrays(gl.TRIANGLE_FAN, 0, circle.length);
	
	
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	/*----------------------------------------------------------*/
	//Set up with flat fragment shader
	program = initShaders( gl, "vertex-shader", "fragment-shader" );
	gl.useProgram( program );
	
	//draw squares
	var colorLoc = gl.getUniformLocation( program, "color");
	for (i = 0; i < squares.length; i++) {
		//set color of square based on even or odd
		if (i % 2 == 0) gl.uniform4fv(colorLoc, currentSquareColor);
		else gl.uniform4fv(colorLoc, black);
		
		var currentSquare = squares[i];
		
		gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
		gl.bufferData(gl.ARRAY_BUFFER, flatten(currentSquare), gl.STATIC_DRAW);
		gl.drawArrays(gl.TRIANGLE_FAN, 0, sidesInASquare);
	}
	
	//draw ellipse
	gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, flatten(ellipse), gl.STATIC_DRAW);
	gl.uniform4fv(colorLoc, currentEllipseColor); //set to chosen color
	gl.drawArrays(gl.TRIANGLE_FAN, 0, ellipse.length);
	
	//draw pentagons
	for (i = 0; i < pentaArray.length; i++) { //for each pentagon
		gl.uniform4fv(colorLoc, pentaColors[i]); //set color
		
		var currentPenta = pentaArray[i];
		
		gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
		gl.bufferData(gl.ARRAY_BUFFER, flatten(currentPenta), gl.STATIC_DRAW);
		gl.drawArrays(gl.TRIANGLE_FAN, 0, sidesInAPentagon);
	}
	
	setTimeout(
		function () {
			render(program, vBuffer, vPosition, cBuffer, vColor);
		}, updateSpeed
	);
}


/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/
/*----------------------------------------------------------*/

function toggleDirection() {
	counterClockwise = !counterClockwise;
}

function makeNewPentagon(inputX,inputY) {
	//((x)-250)/250
	var Cwidth = document.getElementById("gl-canvas").width;
	var Cheight = document.getElementById("gl-canvas").height;
	var ChalfWidth = Cwidth/2; var ChalfHeight = Cheight/2;
	
	var glX = ((inputX - ChalfWidth )/ChalfWidth);
	var glY = -1 * ((inputY - ChalfHeight )/ChalfHeight);
	
	//get center
	var center = vec2(glX, glY);
	console.log(inputX);
	console.log(glX);
	console.log(inputY);
	console.log(glY);
	
	var randradius = 0.04 + (0.03 * Math.random());
			
	//create shape
	var newPentagon = nGon(center, randradius, sidesInAPentagon, triangleScale, false);
	
	//rotate a random amount
	newPentagon = rotateShape(newPentagon, Math.random()*360, center);
	
	//add to arrays
	pentaArray.push(newPentagon); //shape and a random color
	pentaColors.push(new vec4(Math.random(), Math.random(), Math.random(), 1));
}


//create an n-sided polygon
function nGon(center, radius, numSides, scale, flip) {
	radius = Math.abs(radius); //make sure radius is positive
	numSides = Math.floor(numSides); //make sure number of sides is a whole number
	if (numSides < 3) numSides = 3;
	
	//get angle n in radians
	var n = (360.0/numSides) * (Math.PI/180);
	
	//create array
	var vertices = [];
	
	//add each vertex to the array
	for (i = 0; i < numSides; i++) {
		var theta = i * n; //the angle theta
		if (flip) { vertices.push(vec2(
				center[0] + scale[0] * radius * Math.cos(theta), 
				center[1] + scale[1] * radius * Math.sin(theta)));
		} else { vertices.push(vec2(
				center[0] + scale[0] * radius * Math.sin(theta), 
				center[1] + scale[1] * radius * Math.cos(theta)));
		}
	} return vertices;
}


//get array of six squares
//just isn't working as a for loop
function getSquares(center, largestLen, decrement) {
	var squareScale = new vec2(1,1);
	var squares = [
		nGon(center, largestLen, sidesInASquare, squareScale, false),
		nGon(center, largestLen - (1 * decrement), sidesInASquare, squareScale, false),
		nGon(center, largestLen - (2 * decrement), sidesInASquare, squareScale, false),
		nGon(center, largestLen - (3 * decrement), sidesInASquare, squareScale, false),
		nGon(center, largestLen - (4 * decrement), sidesInASquare, squareScale, false),
		nGon(center, largestLen - (5 * decrement), sidesInASquare, squareScale, false)
	];
	return squares;
}

//rotate a point about another point
/*
	x and y in reference to an origin
	T = theta
	
	Rotational matrix:
	
	--  --     --           --     --  --
	| x' |     | cosT  -sinT |     | x  |
	| y' |  =  | sinT   cosT |  *  | y  |
	--  --     --           --     --  --
	
	x' = xcosT - ysinT
	y' = xsinT + ycosT
	
	counterclockwise if theta is positive
	
	*/
//initial, pivot are vec2, theta is angle in degrees
function rotatePoint(initial, theta, pivot) {
	//convert theta from degrees to radians
	var T = theta * (Math.PI/180);
	
	//get coordinates of initial point relative to the pivot point
	//i.e. switch coordinate frames
	var X = initial[0] - pivot[0];
	var Y = initial[1] - pivot[1];
	
	//get new coordinates in pivot point frame
	var newX = (X * Math.cos(T)) + (-1 * Y * Math.sin(T));
	var newY = (X * Math.sin(T)) + (     Y * Math.cos(T));
	
	//convert back to original frame
	newX = newX + pivot[0];
	newY = newY + pivot[1];
	
	return vec2(newX, newY);
}

//rotate a shape, point by point
//shape is array of vec2
//center is vec2
//theta is angle in degrees
function rotateShape(shape, theta, center) {
	var rotatedShape = [];
	//for each point in the shape
	for (i = 0; i < shape.length; i++) {
		//rotate the ith point and push it to the new array
		rotatedShape.push(rotatePoint(shape[i], theta, center));
	}
	return rotatedShape;
}

