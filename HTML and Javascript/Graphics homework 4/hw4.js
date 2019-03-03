//Andrew Yaros - CS 432 HW 4
//This is based on one of the textbook examples

"use strict";
var canvas;
var gl;
var numVertices  = 36;
var totalDegrees = 360;

//the current axis and transformation type to use
var axis = 0;
var transformValue = 0; //can be 0 to 6

//axes to operate on
const xAxis = 0; //x
const yAxis = 1; //y
const zAxis = 2; //z

//transform variables that can be used
const sValue = 0; //scale value
const rValue = 1; //rotation value
const tValue = 2; //translation value
const sDelta = 3; //scale delta
const rDelta = 4; //rotation delta
const tDelta = 5; //translation delta

//THE VALUE FOR INCREMENTING DELTA VALUES
const scaleDD = 0.1;
const rotateDD = 1;
const translateDD = 0.1;

//CURRENT transformation values - how transformed are we
var scale = new Array(3);
var rotate = new Array(3);
var translate = new Array(3);

//CURRENT transformation deltas - how much do we transform
var scaleDelta = new Array(3);
var rotateDelta = new Array(3);
var translateDelta = new Array(3);

//This function sets all values to default values
//This should be used in the initialization function to set up the cube
function resetValues() {
	scale = [1, 1, 1];
	rotate = [56, -29, -36];
	translate = [0, 0, 0];
	scaleDelta = [0.2, 0.2, 0.2];
	rotateDelta = [15, 15, 15];
	translateDelta = [0.2, 0.2, 0.2];
}

//for getting uniform variables
var scaleLoc;
var rotateLoc;
var translateLoc;

var vertices = [
	vec3(-0.5, -0.5,  0.5),
	vec3(-0.5,  0.5,  0.5),
	vec3( 0.5,  0.5,  0.5),
	vec3( 0.5, -0.5,  0.5),
	vec3(-0.5, -0.5, -0.5),
	vec3(-0.5,  0.5, -0.5),
	vec3( 0.5,  0.5, -0.5),
	vec3( 0.5, -0.5, -0.5)
];

var vertexColors = [
	vec4( 1, 0, 0, 1), //red
	vec4( 1, 0, 1, 1), //magenta
	vec4( 1, 1, 1, 1), //white
	vec4( 1, 1, 0, 1), //yellow
	vec4( 0, 0, 0, 1), //black
	vec4( 0, 0, 1, 1), //blue
	vec4( 0, 1, 1, 1), //cyan
	vec4( 0, 1, 0, 1)  //green
];

var indices = [
	1, 0, 3,
	3, 2, 1,
	2, 3, 7,
	7, 6, 2,
	3, 0, 4,
	4, 7, 3,
	6, 5, 1,
	1, 2, 6,
	4, 5, 6,
	6, 7, 4,
	5, 4, 0,
	0, 1, 5
];

//change a single value in the html
function changeHTMLvalue(id, newValue) {
	document.getElementById(id).innerHTML = newValue;
}

//output values to html
function updateHTMLvalues() {
	changeHTMLvalue("x_ScaleValue", scale[xAxis]);
	changeHTMLvalue("y_ScaleValue", scale[yAxis]);
	changeHTMLvalue("z_ScaleValue", scale[zAxis]);
	
/*	changeHTMLvalue("x_ScaleDelta", scaleDelta[xAxis]);
	changeHTMLvalue("y_ScaleDelta", scaleDelta[yAxis]);
	changeHTMLvalue("z_ScaleDelta", scaleDelta[zAxis]);
*/
	changeHTMLvalue("x_RotateValue", rotate[xAxis]);
	changeHTMLvalue("y_RotateValue", rotate[yAxis]);
	changeHTMLvalue("z_RotateValue", rotate[zAxis]);
	
/*	changeHTMLvalue("x_RotateDelta", rotateDelta[xAxis]);
	changeHTMLvalue("y_RotateDelta", rotateDelta[yAxis]);
	changeHTMLvalue("z_RotateDelta", rotateDelta[zAxis]);
*/
	changeHTMLvalue("x_TranslateValue", translate[xAxis]);
	changeHTMLvalue("y_TranslateValue", translate[yAxis]);
	changeHTMLvalue("z_TranslateValue", translate[zAxis]);
	
/*	changeHTMLvalue("x_TranslateDelta", translateDelta[xAxis]);
	changeHTMLvalue("y_TranslateDelta", translateDelta[yAxis]);
	changeHTMLvalue("z_TranslateDelta", translateDelta[zAxis]);
*/
}

//this function increases or decreases a value based on the current selected axis and transform
//set decrement to false to increase
//set decrement to true to decrease
function changeSelected(decrement) {
	var factor = 10;
	var increment = 1; //multiply by a delta value
	if(decrement) increment = -1; //multiply the delta by -1 to decrement instead of increment
	switch (transformValue) {
		case sValue: //scale value
			scale[axis] = ((scale[axis] * factor) + 
								(scaleDelta[axis] * factor * increment))/factor; 
			break;
		case sDelta: //scale delta
			scaleDelta[axis] = ((scaleDelta[axis] * factor) + 
								(scaleDD * factor * increment))/factor; 
			break;
		case rValue: //rotate value
			rotate[axis] = (((rotate[axis] * factor) + 
								(rotateDelta[axis] * factor * increment))/factor) % totalDegrees; 
			break;
		case rDelta: //rotate delta
			rotateDelta[axis] = ((rotateDelta[axis] * factor) + 
								(rotateDD * factor * increment))/factor; 
			break;
		case tValue: //translate value
			translate[axis] = ((translate[axis] * factor) + 
								(translateDelta[axis] * factor * increment))/factor; 
			break;
		case tDelta: //translate delta
			translateDelta[axis] = ((translateDelta[axis] * factor) + 
								(translateDD * factor * increment))/factor; 
			break;
		default: break;
	} updateHTMLvalues(); //when values are changed, update the html
}

window.onload = function init() {
	canvas = document.getElementById("gl-canvas");

	//initially set the current values to the default values
	resetValues();
	
	//then update them in the html
	updateHTMLvalues();

	gl = WebGLUtils.setupWebGL(canvas);
	if (!gl) alert( "WebGL is missing. This must be your fault; what did you do?!");

	gl.viewport(0, 0, canvas.width, canvas.height);
	gl.clearColor(0.95, 0.95, 0.95, 1);

	gl.enable(gl.DEPTH_TEST);;
	
	//Load shaders and initialize attribute buffers
	var program = initShaders(gl, "vertex-shader", "fragment-shader");
	gl.useProgram(program);

	//array element buffer
	var iBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, iBuffer);
	gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint8Array(indices), gl.STATIC_DRAW);

	//color array atrribute buffer
	var cBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, cBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, flatten(vertexColors), gl.STATIC_DRAW);

	var vColor = gl.getAttribLocation(program, "vColor");
	gl.vertexAttribPointer(vColor, 4, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(vColor);

	//vertex array attribute buffer
	var vBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, flatten(vertices), gl.STATIC_DRAW);

	var vPosition = gl.getAttribLocation(program, "vPosition");
	gl.vertexAttribPointer(vPosition, 3, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(vPosition);
	
	//for setting uniform variables in the shader
	rotateLoc = gl.getUniformLocation(program, "rotate");
	scaleLoc = gl.getUniformLocation(program, "scale");
	translateLoc = gl.getUniformLocation(program, "translate");
	
	//event listeners! I KNOW YOU LOVE EVENT LISTENERS
	
	//change axis
	document.getElementById("axisSelector").onclick = function(event) {
		switch(event.target.index) {
		case xAxis: axis = xAxis; break;
		case yAxis: axis = yAxis; break;
		case zAxis: axis = zAxis; break;
		}
	};
	
	//change transform type
	document.getElementById("transformSelector").onclick = function(event) {
		switch(event.target.index) {
		case 0: transformValue = sValue; break;
		case 1: transformValue = rValue; break;
		case 2: transformValue = tValue; break;
		case 3: transformValue = sDelta; break;
		case 4: transformValue = rDelta; break;
		case 5: transformValue = tDelta; break;
		default: break;
		}
	};
	
	//get keypress and change corresponding transform
	document.onkeypress = function(e) {
		e = e || window.event;
		var currentKey = String.fromCharCode(e.keyCode || e.which);
		console.log(currentKey);
		switch (currentKey) {
		case "r": //reset
			resetValues();
			break;
		case "a": //increase x transform
			document.getElementById("axisSelector").selectedIndex = "0";
			axis = xAxis; changeSelected(false); break;
			
		case "d": //decrease x transform
			document.getElementById("axisSelector").selectedIndex = "0";
			axis = xAxis; changeSelected(true); break;
			
		case "w": //increase y transform
			document.getElementById("axisSelector").selectedIndex = "1";
			axis = yAxis; changeSelected(false); break;
			
		case "s": //decrease y transform
			document.getElementById("axisSelector").selectedIndex = "1";
			axis = yAxis; changeSelected(true); break;
			
		case "q": //increase z transform
			document.getElementById("axisSelector").selectedIndex = "2";
			axis = zAxis; changeSelected(false); break;
			
		case "e": //decrease z transform
			document.getElementById("axisSelector").selectedIndex = "2";
			axis = zAxis; changeSelected(true); break;
			
		default:
			break;
		}
	};
	
	//reset values button
	document.getElementById("resetValues").onclick = function(event) {
		resetValues();
		updateHTMLvalues();
	};
	
	render();
}

function render() {
	gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
	
	gl.uniform3fv(scaleLoc, scale);
	gl.uniform3fv(rotateLoc, rotate);
	gl.uniform3fv(translateLoc, translate);

	gl.drawElements(gl.TRIANGLES, numVertices, gl.UNSIGNED_BYTE, 0);
	
	requestAnimFrame(render);
}