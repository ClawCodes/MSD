function myFunc(){
    console.log("Hello World!")
}

let x = 5;

console.log(typeof x);


let myArray = [2, 3,2, "hi"];

console.log(myArray[0]);

for(let val of myArray){
    console.log(val);
}

for (let i = 0; i < myArray.length; i++){
    console.log(myArray[i]);
}

let myObject = {};

myObject.name = "Chris";
myObject.grades = [90, 80, 70];

console.log(myObject.name);
console.log(myObject.grades);

let secondObject = {"Name": "Ahmad", gpa:90};
secondObject.printInfo = function(){
    console.log(this.Name);
    console.log(this['gpa'])
}

secondObject.printInfo();

let myfunction = function(){
    console.log("Hello from the js function");
};

myfunction();


// Assign function to variable
let myFuncVar = myFunc;
myFuncVar();

// Working with HTML (Accessing HTML elements and manipulating them)
let myProg = document.getElementById("myProg");
myProg.style.fontsize="39px";
myProg.textContent="Hello from the js world";

// Create your own HTML elements and push to the DOM
let myHTMLElement = document.createElement("p");
myHTMLElement.textContent="Created by js";
// Add to DOM
myProg.appendChild(myHTMLElement)