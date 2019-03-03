//////////////////////////////////
// Andrew Yaros - CS 432 - HW 5 //
////// Main JavaScript file //////
//////////////////////////////////

"use strict";
var canvas;
var gl;
var i;
const sidesInATriangle = 3;

//projections to use
const useParallel = 0; //use parallel
const usePerspect = 1; //use perspective
//current values of the paramater
var proj = useParallel;

//more values:
var theta = 0;
var phi = 0;
var radius = 1;
var aspect = 1;
var perspectiveFOV = 45;
var near = 0.1;
var far = 10;
var orthoTop = 1.0;
var bottom = -1.0;
var left = -1.0;
var right = 1.0;

const angleChange = .25;
const radiusChange = .25;

//to initialize variables, run with desired projection and resetUservals to true
//later, when changing projection, set only useOrthoValues to true
//when resetting values, set resetUserVals to true
function resetValues(projectionToUse, resetUserVals) {
	switch (projectionToUse) {
		case useParallel:
			near = -10;
			far = 10;
			radius = 1;
			break;
		case usePerspect:
			near = .01;
			far = 10;
			radius = 3;
			break;
		default:
			break;
	}

	if (resetUserVals) {
		theta = 0;
		phi = 0;
	}
}

//for getting uniform variables:
var modelViewLoc;
var projectionLoc;

//get lines of the SMF data
//this is found in SMFfile.js
var SMFarray = SMFfile.split("\n");

//vertices
var vertices = [];
//indices
var indices = [];

//loop through array, put lines into vertices
for (i = 0; i < SMFarray.length; i++) {
	var currentLine = SMFarray[i].split(" ");
	//console.log(i + " = " + currentLine);
	//if first character is v, add to vertices
	//otherwise if f, add to indices, otherwise skip line
	if (currentLine[0][0] == 'v') { //console.log("is v");
		var curLen = vertices.length;
		vertices.push(vec3(
			parseFloat(currentLine[1]),
			parseFloat(currentLine[2]),
			parseFloat(currentLine[3])
		));
		if (vertices.length - curLen != 1) console.log("ERROR pushing a vertex");
	} else if (currentLine[0][0] == 'f') { //console.log("is f");
		var curLen = indices.length;
		//subtract 1, because indices should start at 0
		//indices in the SMF appear to start at 1
		indices.push(parseInt(currentLine[1]) - 1);
		indices.push(parseInt(currentLine[2]) - 1);
		indices.push(parseInt(currentLine[3]) - 1);
		
		if (indices.length - curLen != 3) console.log("ERROR pushing an index");
	} //else console.log("skipping invalid line");
}

var numIndices = indices.length;
var numVertices = vertices.length;
var numTriangles = parseInt(numIndices/sidesInATriangle);

console.log(numIndices + " indices pushed.");
console.log(numVertices + " vertices pushed.");
console.log("There are " + numTriangles + " triangles.");

var normals = [];

//calculate a normal based on a triangle
//arguments: triangle, and points to use to calculate normals
//a - b, c - d
function calculateNormal(triangle, a, b, c, d) {
	//surface normal = vector cross product of two edges
	var U = vec3( //1st - 0th
		triangle[a][0] - triangle[b][0], 
		triangle[a][1] - triangle[b][1],
		triangle[a][2] - triangle[b][2]
	); var V = vec3( //2nd - 0th
		triangle[c][0] - triangle[d][0], 
		triangle[c][1] - triangle[d][1],
		triangle[c][2] - triangle[d][2]
	);
	
	//cross products
	return vec3(
		(U[1] * V[2]) - (U[2]*V[1]), //N0 = U1V2 - U2V1
		(U[2] * V[0]) - (U[0]*V[2]), //N1 = U2V0 - U0V2
		(U[0] * V[1]) - (U[1]*V[0])  //N2 = U0V1 - U1V0
	);
}

//calculate surface normals for each vertex
for (i = 0; i < numTriangles; i++) {
	//ith triangle consists of
	//vertices defined by indices [i*3 + 0, 1, and 2] taken from indices array
	var ithTriangle = [
		vertices[indices[i * sidesInATriangle]],
		vertices[indices[i * sidesInATriangle + 1]],
		vertices[indices[i * sidesInATriangle + 2]]
	];
	//a - b, c - d
	normals.push(calculateNormal(ithTriangle, 2, 0, 1, 0));
} console.log(normals.length + " normals calculated.");

function normalize(v){
	//length = sqrt(sum of the squares)
	var len = Math.sqrt(
		v[0] * v[0] + 
		v[1] * v[1] + 
		v[2] * v[2]
	); //divide vector by length
	if (len != 0) return vec3(v[0]/len, v[1]/len, v[2]/len);
	else return vec3(NaN, NaN, NaN); //"whatever"
}

var normalized = [];

//normalize each normal
for (i = 0; i < numTriangles; i++) {
	var ith = normals[i];
	normalized.push(normalize(ith));
}

var triangleColors = [];
for (i = 0; i < numTriangles; i++) {
	//for each triangle, get abs of the normalized normal vector and use as color
	var currentColor = vec4(
		Math.abs(normalized[i][0]),
		Math.abs(normalized[i][1]),
		Math.abs(normalized[i][2]),
		1.0
	);
	triangleColors.push(currentColor);
}

//variables to actually use when displaying stuff
//we are rendering many seperate triangles, rather than one connected mesh
var trianglePoints = [];
var trianglePointColors = [];
//create an array of triangle points, and colors for those points
for (i = 0; i < numTriangles; i++) {
	var j;
	for (j = 0; j < sidesInATriangle; j++) {
		//push three vertices: index # (i * 3) + j
		trianglePoints.push(
			vertices[
				indices[
					i*sidesInATriangle + j]]);
		//push the same color for each vertex
		trianglePointColors.push(triangleColors[i]);
	}
}

//calcuate a new degree value in radians btw 0 and 2Pi
function degreeChange(angle, amount) {
	return (angle + amount) % (Math.PI * 2);
}

console.log(vertices);
console.log(indices);
console.log(normals);
console.log(normalized);
console.log(trianglePoints);
console.log(trianglePointColors);

window.onload = function init() {
	canvas = document.getElementById("gl-canvas");

	//initially set the current values to the default values
	resetValues(proj, true);
	
	gl = WebGLUtils.setupWebGL(canvas);
	if (!gl) alert( "WebGL is missing. This must be your fault; what did you do?!");

	gl.viewport(0, 0, canvas.width, canvas.height);
	gl.clearColor(1, 1, 1, 1);
	
	gl.enable(gl.DEPTH_TEST);;
	
	//Load shaders and initialize attribute buffers
	var program = initShaders(gl, "vertex-shader", "fragment-shader");
	gl.useProgram(program);

	//color array atrribute buffer
	var cBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, cBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, flatten(trianglePointColors), gl.STATIC_DRAW);

	var vColor = gl.getAttribLocation(program, "vColor");
	gl.vertexAttribPointer(vColor, 4, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(vColor);

	//vertex array attribute buffer
	var vBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, flatten(trianglePoints), gl.STATIC_DRAW);

	var vPosition = gl.getAttribLocation(program, "vPosition");
	gl.vertexAttribPointer(vPosition, 3, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(vPosition);
	
	//array element buffer
	/*var iBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, iBuffer);
	gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint8Array(indices), gl.STATIC_DRAW);*/
	
	//for setting uniform variables in the shader
	modelViewLoc = gl.getUniformLocation(program, "MV");
	projectionLoc = gl.getUniformLocation(program, "P");
	
	//change projection
	document.getElementById("projectSelector").onclick = function(event) {
		switch(event.target.index) {
		case useParallel: 
			proj = useParallel;
			console.log("Parallel");
			break;
		case usePerspect: 
			proj = usePerspect;
			console.log("Perspective");
			break;
		}
		resetValues(proj, false);
	};
	
	//get keypress and change corresponding transform
	document.onkeypress = function(e) {
		e = e || window.event;
		var currentKey = String.fromCharCode(e.keyCode || e.which);
		console.log(currentKey);
		switch (currentKey) {
		case "r": //reset values
			resetValues(proj, true);
			break;
		case "w": //increase height
			phi = degreeChange(phi, angleChange);
			break;
		case "s": //decrease height
			phi = degreeChange(phi, -1 * angleChange);
			break;
		case "d": //increase rotation
			theta = degreeChange(theta, angleChange);
			break;
		case "a": //decrease rotation
			theta = degreeChange(theta, -1 * angleChange);
			break;
		case "e": //increase orbit
			radius += radiusChange;
			break;
		case "q": //decrease orbit
			radius -= radiusChange;
			break;
		default:
			break;
		}
	};	
	
	render();
}

function render() {
	gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
	
	const at = vec3(0.0, 0.0, 0.0); 
	const up = vec3(0.0, 1.0, 0.0);
	
	var eye;
	var projection;
	var modelView;
	
	switch (proj) {
		case useParallel:
			eye = vec3(
				radius * Math.sin(theta),
				radius * Math.sin(phi),
				radius * Math.cos(theta));
			projection = ortho(left, right, bottom, orthoTop, near, far);
			modelView = lookAt(eye, at, up);
			break;
		case usePerspect:
			eye = vec3(
				radius*Math.sin(theta) * Math.sin(phi),
				radius*Math.sin(theta) * Math.cos(phi),
				radius*Math.cos(theta)
				
			);
			projection = perspective(perspectiveFOV, aspect, near, far);
			modelView = lookAt(eye, at, up);
		default:
			break;
	}
	
	gl.uniformMatrix4fv(modelViewLoc, false, flatten(modelView));
	gl.uniformMatrix4fv(projectionLoc, false, flatten(projection));
	
	gl.drawArrays(gl.TRIANGLES, 0, numIndices);
	requestAnimFrame(render);
}