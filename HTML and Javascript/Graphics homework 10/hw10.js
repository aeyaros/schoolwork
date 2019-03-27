//////////////////////////////////
// Andrew Yaros - CS432 - HW 10 //
////// Main JavaScript file //////
//////////////////////////////////

"use strict";
var canvas;
var gl;
var program;

const sidesInATriangle = 3;

const arm3dist = 1.5; //arbitrary arm 3 distance
const joint1Max = 180;
const joint2Max = 140;
const joint3Max = 100;
const armIncrementVal = 4; //how fast to move arms

//values
var theta;
var phi;
var radius;
var near;
var far;
const aspect = 1;
const perspectiveFOV = 45;
const angleChange = .25;
const radiusChange = .25;

//arm joint values
var joint1rotation = 0; //degrees
var joint2rotation = 0; //degrees
var joint3translation = 0; //percent of arbitrary distance 

var joint1pivotpoint;
//var joint1vector;
var joint2pivotpoint;
//var joint2vector;

function resetPivots() {
	joint1pivotpoint = [0, 0, 0]; //stays the same; at the center
	joint2pivotpoint = [4, 0, 0]; //rotates with arm 1	
}
function resetArms() {
	joint1rotation = 0; //degrees
	joint2rotation = 0; //degrees
	joint3translation = 0; //percent of arbitrary distance 
}

//a cube
const cubeVertices = [
	vec4(-.5, -.5,  .5, 1),
	vec4(-.5,  .5,  .5, 1),
	vec4( .5,  .5,  .5, 1),
	vec4( .5, -.5,  .5, 1),
	vec4(-.5, -.5, -.5, 1),
	vec4(-.5,  .5, -.5, 1),
	vec4( .5,  .5, -.5, 1),
	vec4( .5, -.5, -.5, 1)
];

//cube indices: every 3 indices = 1 triangle
const cubeIndices = [
	1, 0, 3, 3, 2, 1,
	2, 3, 7, 7, 6, 2,
	3, 0, 4, 4, 7, 3,
	6, 5, 1, 1, 2, 6,
	4, 5, 6, 6, 7, 4,
	5, 4, 0, 0, 1, 5
];

//use to initialize values
//when resetting camera angle/distance, set resetcamera to true
function resetValues(resetcamera) {
	near = .01;
	far = 100000;
	if (resetcamera) {
		radius = 20;
		theta = 1;
		phi = 1.25;
	}
}

//for getting uniform variables:
var modelViewLoc;
var projectionLoc;
var scaleLoc;

//define light sources and materials
var lightPosition = vec3(0, 4, 2);

var materialAmbient = [vec4(0.6, 0.2, 0.2, 1.0)//default
	];
		
var materialDiffuse = [vec4(0.9, 0.1, 0.1, 1.0) //default
	];
		
var materialSpecular = [vec4(0.8, 0.8, 0.8, 1.0) //default
	];

var materialShininess = [80.0, 25, 50, 50, 50];

var lightAmbient = [vec4(0.2, 0.2, 0.2, 1.0) //default
	]; 

var lightDiffuse = [vec4(1, 1, 1, 1.0) //default
	]; 

var lightSpecular = [vec4(1.0, 1.0, 1.0, 1.0) //default
	];
	
var diffuse1 = vec4(0.6, 0.2, 0.2, 1.0)

function parseSMF(SMFarray, vertices, indices) {
	//loop through array, put lines into vertices
	var i;
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
}

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

function getMatrices(oldvertices, indices, scale, transform, trianglePoints, trianglePointColors, indexedVertexNormals) {
	//copy vertices from cube source
	var vertices = [];
	var i;	
	for (i = 0; i < oldvertices.length; i++) {
		vertices.push(vec4(0,0,0,1));
	}
	
	var numIndices = indices.length;
	var numVertices = vertices.length;
	var numTriangles = parseInt(numIndices/sidesInATriangle);
	console.log(numIndices + " indices pushed.");
	console.log(numVertices + " vertices pushed.");
	console.log("There are " + numTriangles + " triangles.");
	
	//multiply vertices by scale factors and add transforms
	var i;	
	for (i = 0; i < vertices.length; i++) {
		var j;
		for (j = 0; j < sidesInATriangle; j++) {
			vertices[i][j] = (oldvertices[i][j] * scale[j]) + transform[j];
		}
	}
	
	var normals = [];
	//calculate surface normals for each vertex
	for (i = 0; i < numTriangles; i++) {
		//ith triangle consists of
		//vertices defined by indices [i*3 + 0, 1, and 2] taken from indices array
		var ithTriangle = [
			vertices[indices[i * sidesInATriangle]],
			vertices[indices[i * sidesInATriangle + 1]],
			vertices[indices[i * sidesInATriangle + 2]]
		]; //a - b, c - d
		normals.push(calculateNormal(ithTriangle, 2, 0, 1, 0));
	} console.log(normals.length + " normals calculated.");
	
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
		); triangleColors.push(currentColor);
	}
	
	//variables to actually use when displaying stuff
	//we are rendering many seperate triangles, rather than one connected mesh
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
			//normals			
			//gouraud shading
			//indexedVertexNormals.push(normalizedvertexnorms[indices[i*sidesInATriangle + j]]);
		}
	}
	//flat shading
	//for each index
	for (i = 0; i < normalized.length; i++) {
		var j;
		for (j = 0; j < sidesInATriangle; j++) {
			indexedVertexNormals.push(normalized[i]);
		}
	}
}

//calcuate a new degree value in radians btw 0 and 2Pi
//used for changing the camera
function degreeChange(angle, amount) {
	return (angle + amount) % (Math.PI * 2);
}

var base = [];
var baseColors = [];
const baseSize = [5, 0.5, 5];
const basePosition = [0, -4.25, 0];
var baseNorms = [];
getMatrices(cubeVertices, cubeIndices, baseSize, basePosition, base, baseColors, baseNorms);

var post = [];
var postColors = [];
const postSize = [.75, 4, .75];
const postPosition = [0, -2, 0];
var postNorms = [];
getMatrices(cubeVertices, cubeIndices, postSize, postPosition, post, postColors, postNorms);

var arm1 = [];
var arm1Colors = [];
const arm1Size = [4, .5, .5];
const arm1Position = [2, 0, 0];
var arm1Norms = [];
getMatrices(cubeVertices, cubeIndices, arm1Size, arm1Position, arm1, arm1Colors, arm1Norms);

var arm2 = [];
var arm2Colors = [];
const arm2Size = [4, .375, .375];
const arm2Position = [6, 0, 0];
var arm2Norms = [];
getMatrices(cubeVertices, cubeIndices, arm2Size, arm2Position, arm2, arm2Colors, arm2Norms);

var arm3 = [];
var arm3Colors = [];
const arm3Size = [.25, 3.5, .25];
const arm3Position = [8, 0, 0];
var arm3Norms = [];
getMatrices(cubeVertices, cubeIndices, arm3Size, arm3Position, arm3, arm3Colors, arm3Norms);

//rotate a point around the origin in the xz plane
function rotatePoint(point, angle) {
	var oldX = point[0];
	var oldZ = point[2];
	var newX = oldX * Math.cos(angle) - oldZ * Math.sin(angle); //x
	var newZ = oldZ * Math.cos(angle) + oldX * Math.sin(angle); //z
	var newY = point[1]; //keep height
	var neww = point[3]; //fourth item in array, probably 1 or something
	return [newX, newY, newZ, neww];
}

function rotatePointAround(point, pivot, angle) {
	var newY = point[1];//not rotating this so dont need to shift
	var newW = point[3]; //fourth item in array, probably 1 or something
	
	//shift to origin
	var oldX = point[0] - pivot[0];
	var oldZ = point[2] - pivot[2];
	
	//rotate 
	var newX = oldX * Math.cos(angle) - oldZ * Math.sin(angle); //x
	var newZ = oldZ * Math.cos(angle) + oldX * Math.sin(angle); //z
	
	//shift back
	newX += pivot[0];
	newZ += pivot[2];
	
	//new vector
	return [newX, newY, newZ, newW];
}

//rotate every point in a mesh and return new array; uses xz plane
function rotatePoints(pointsArray, rotationOrigin, angle) {
	var shiftx = rotationOrigin[0];
	var shifty = rotationOrigin[1];
	var shiftz = rotationOrigin[2];
	
	//deep copy
	var newArray = [];
	var i;
	for (i = 0; i < pointsArray.length; i++) {
		newArray.push([]);
		var j;
		for(j = 0; j < pointsArray[i].length; j++) {
			newArray[i].push(pointsArray[i][j]);
		}
	}
	
	var i; //rotate each point
	for (i = 0; i < pointsArray.length; i++) {
		newArray[i] = rotatePointAround(newArray[i], rotationOrigin, angle);
	}
		
	return newArray;
}

//degrees to radians
function d2r(degrees) {
	return degrees * (Math.PI/180);
}

//draw a part
function drawPart(triangles, normalArray) {
	var nBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, nBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, flatten(normalArray), gl.STATIC_DRAW);
	
	var vNormal = gl.getAttribLocation(program, "vNormal");
	gl.vertexAttribPointer(vNormal, 3, gl.FLOAT, false, 0, 0 );
	gl.enableVertexAttribArray(vNormal);

	var vBuffer = gl.createBuffer();
	gl.bindBuffer(gl.ARRAY_BUFFER, vBuffer);
	gl.bufferData(gl.ARRAY_BUFFER, flatten(triangles), gl.STATIC_DRAW);

	var vPosition = gl.getAttribLocation(program, "vPosition");
	gl.vertexAttribPointer(vPosition, 4, gl.FLOAT, false, 0, 0);
	gl.enableVertexAttribArray(vPosition);
	
	var ambient =  mult(lightAmbient[0],  materialAmbient[0]);
	var diffuse =  mult(lightDiffuse[0],  materialDiffuse[0]);
	var specular = mult(lightSpecular[0], materialSpecular[0]);
		
	gl.uniform4fv(gl.getUniformLocation(program, "A"),  flatten(ambient));
	gl.uniform4fv(gl.getUniformLocation(program, "D"),  flatten(diffuse));
	gl.uniform4fv(gl.getUniformLocation(program, "S"),  flatten(specular));
	gl.uniform3fv(gl.getUniformLocation(program, "lP"), flatten(lightPosition));
	gl.uniform1f( gl.getUniformLocation(program, "sH"), materialShininess[0]);
	
	gl.drawArrays(gl.TRIANGLES, 0, triangles.length);
}

//translate a matrix
function shiftMatrix(matrix, x, y, z) {
	var i;
	var newmatrix = [];
	for (i = 0; i < matrix.length; i++) {
		newmatrix.push([]);
		newmatrix[i].push(matrix[i][0] + x);
		newmatrix[i].push(matrix[i][1] + y);
		newmatrix[i].push(matrix[i][2] + z);
		newmatrix[i].push(matrix[i][3]);
	} return newmatrix;
}

//for changing position w/ keyboard; wraps around min/max values
function changeValue(origVal, increment, minVal, maxVal) {
	var newVal = origVal + increment;
	if (newVal > maxVal) newVal = minVal + 1;
	else if (newVal < minVal) newVal = maxVal - 1;	
	return newVal;
}

window.onload = function init() {
	canvas = document.getElementById("gl-canvas");

	//initially set the current values to the default values
	resetValues(true);
	resetPivots();
	
	gl = WebGLUtils.setupWebGL(canvas);
	if (!gl) alert( "WebGL is missing. This must be your fault; what did you do?!");

	gl.viewport(0, 0, canvas.width, canvas.height);
	gl.clearColor(1, 1, 1, 1);
	
	gl.enable(gl.DEPTH_TEST);
	
	//Load shaders and initialize attribute buffers
	program = initShaders(gl, "gouraud-vertex-shader", "gouraud-fragment-shader");
	gl.useProgram(program);
	
	//for setting uniform variables in the shader
	modelViewLoc = gl.getUniformLocation(program, "MV");
	projectionLoc = gl.getUniformLocation(program, "P");
	scaleLoc = gl.getUniformLocation(program, "dims");
	
	//get keypress and change corresponding transform
	document.onkeypress = function(e) {
		e = e || window.event;
		var currentKey = String.fromCharCode(e.keyCode || e.which);
		switch (currentKey) {
		case "r": resetValues(true); break; //reset values
		case "w": phi = degreeChange(phi, angleChange); break; //increase height
		case "s": phi = degreeChange(phi, -1 * angleChange); break; //decrease height
		case "d": theta = degreeChange(theta, angleChange); break; //increase rotation
		case "a": theta = degreeChange(theta, -1 * angleChange); break; //decrease rotation
		case "e": radius += radiusChange; break; //increase orbit
		case "q": radius -= radiusChange; break; //decrease orbit
		case "y": joint1rotation = changeValue(joint1rotation, armIncrementVal, -1 * joint1Max, joint1Max); //change value
			document.getElementById("joint1").value = joint1rotation; //set slider
			console.log("Moved joint 1 to " + joint1rotation + " degrees.");
			break;
		case "t": joint1rotation = changeValue(joint1rotation, -1*armIncrementVal, -1 * joint1Max, joint1Max); //change value
			document.getElementById("joint1").value = joint1rotation; //set slider
			console.log("Moved joint 1 to " + joint1rotation + " degrees.");
			break;
		case "h": if (joint2rotation + armIncrementVal <= joint2Max) { 
				joint2rotation = joint2rotation + armIncrementVal;
			} else joint2rotation = joint2Max;
			document.getElementById("joint2").value = joint2rotation; //set slider
			console.log("Moved joint 2 to " + joint2rotation + " degrees.");
			break;
		case "g": if (joint2rotation - armIncrementVal >= (-1 * joint2Max)) { 
				joint2rotation = joint2rotation - armIncrementVal;
			} else joint2rotation = (-1 * joint2Max); 
			document.getElementById("joint2").value = joint2rotation; //set slider
			console.log("Moved joint 2 to " + joint2rotation + " degrees.");
			break;
		case "n": if (joint3translation + armIncrementVal <= joint3Max) {
			joint3translation = joint3translation + armIncrementVal;
			} else joint3translation = joint3Max;
			document.getElementById("joint3").value = joint3translation; //set slider
			console.log("Moved joint 3 to " + joint3translation + " percent.");
			break;
		case "b": if (joint3translation - armIncrementVal >= (-1 * joint3Max)) { 
				joint3translation = joint3translation - armIncrementVal;
			} else joint3translation = (-1 * joint3Max);
			document.getElementById("joint3").value = joint3translation; //set slider
			console.log("Moved joint 3 to " + joint3translation + " percent.");
			break;
		case "x": resetPivots(); 
			resetArms(); //reset values
			document.getElementById("joint1").value = 0; 
			document.getElementById("joint2").value = 0;
			document.getElementById("joint3").value = 0; 
			break;
		default: break;
		} 
		//console.log("Pressed " + currentKey + ": Phi: " + phi + " Theta: " + theta + " Radius: " + radius);
	};
	
	//sliders - get rotation values from html
	//joint 1 rotation
	document.getElementById("joint1").onchange = function(event) {
		joint1rotation = parseInt(event.target.value);
		console.log("Moved joint 1 to " + joint1rotation + " degrees.");
	}; //joint 2 rotation
	document.getElementById("joint2").onchange = function(event) {
		joint2rotation = parseInt(event.target.value);
		console.log("Moved joint 2 to " + joint2rotation + " degrees.");
	}; //joint 3 translation
	document.getElementById("joint3").onchange = function(event) {
		joint3translation = parseInt(event.target.value);
		console.log("Moved joint 3 by " + joint3translation + " percent.");
	};
	render();
}

//render
function render() {
	gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
	
	var joint1radians = d2r(joint1rotation);
	var joint2radians = d2r(joint2rotation);
	
	//set view
	const at = vec3(0.0, 0.0, 0.0); 
	const up = vec3(0.0, 1.0, 0.0);
	var eye;
	var projection;
	var modelView;
	eye = vec3(
		radius * Math.sin(theta) * Math.sin(phi),
		radius * Math.sin(theta) * Math.cos(phi),
		radius * Math.cos(theta)
	);
	
	projection = perspective(perspectiveFOV, aspect, near, far);
	modelView = lookAt(eye, at, up);
	gl.uniformMatrix4fv(modelViewLoc, false, flatten(modelView));
	gl.uniformMatrix4fv(projectionLoc, false, flatten(projection));
	
	//reset pivots
	resetPivots();
	
	var joint3trans = (joint3translation/100) * arm3dist; //normalize joint3 translate percentage
	var new3 = shiftMatrix(arm3, 0, joint3trans, 0); //adjust arm 3 position
	
	//rotate arms 2 and 3 around point 2
	new3 = rotatePoints(new3, joint2pivotpoint, joint2radians);
	var new2 = rotatePoints(arm2, joint2pivotpoint, joint2radians);
	
	//rotate everything around point 1
	var new1 = rotatePoints(arm1, joint1pivotpoint, joint1radians);
	new2 = rotatePoints(new2, joint1pivotpoint, joint1radians);
	new3 = rotatePoints(new3, joint1pivotpoint, joint1radians);
	

	
	//draw parts
	drawPart(base, baseNorms);
	drawPart(post, postNorms);
	drawPart(new1, arm1Norms);
	drawPart(new2, arm2Norms);
	drawPart(new3, arm3Norms);
	
	requestAnimFrame(render);
}