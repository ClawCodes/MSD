let body1 = document.getElementById("b1");

// Prints to the web page itself
document.writeln("<p>Hello world (document write)</p>")
// Only prints to the console and will not be rendered
console.log("Hello world (console write)")

let myArray = ["Hello", true, 1, 1.2, {"one": 1, "two": 2}];

console.log(myArray);
myArray.pop();
console.log(myArray)

function myFunc(a, b){
    return a + b;
}

let myVarFunc = function(a, b){return a + b}

let num1 = 1;
let num2 = 2;

console.log(myFunc(num1, num2));
console.log(myVarFunc(num1, num2));

let float1 = 1.5;
let float2 = 2.5;

console.log(myFunc(float1, float2));
console.log(myVarFunc(float1, float2));

let s1 = "Hello";
let s2 = "World";

console.log(myFunc(s1, s2));
console.log(myVarFunc(s1, s2));

// Mixed
console.log(myFunc(num1, s2));
console.log(myVarFunc(num1, s2));

console.log(myFunc(num1, float2));
console.log(myVarFunc(s1, float1));