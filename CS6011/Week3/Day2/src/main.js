// Scratch project for in class notes


// Assumes an HTML document exists
// let myDiv = document.querySelector('#myDiv');
// myDiv.style = 'boarder: 1px solid blue; padding: 20px'
//
// // myDiv.onclick = function () {
// //   console.log('clicked');
// // }
//
// function handleLoadCB(){
//   console.log("load was successful")
//   console.log(this.responseText); // This refers to the XMLHttpRequest in which handleLoadCB was added as an event listener
//
//   // Add loaded information to the webpage
//   myDiv.innerHTML = responseText; // Adds the fetched information within the div tags
// }
//
// function handleError(){
//   console.log("An error occurred")
// }
//
// function handleClick(){
//   let xhr = new XMLHttpRequest(); // This does AJAX for us
//   xhr.open("GET", "snippet.html"); // When clicked, fetch this file
//   xhr.addEventListener('load', handleLoadCB); // CB describes this function is a callback
//   xhr.addEventListener('error', handleError);
//   xhr.send();
//   event.stopPropagation();
// }
//
// myDiv.addEventListener('click', handleClick);


let myCan = document.querySelector("#mycan");
myCan.style = 'border: 1px solid blue; padding 20px';

let ctx = myCan.getContext('2d');

let myImage = new Image();
myImage.src = "cat.jpeg";

let xPos = 100;
let movingRight = true;

myImage.onload = function () {
    ctx.drawImage(myImage, xPos, 200);
}

function draw() {
    ctx.drawImage(myImage, xPos, 200);
    ctx.clearRect(0, 0, 1000, 1000);
    if (xPos > 500) {
        movingRight = false;
    } else if (xPos < 0) {
        movingRight = true;
    }
    if (movingRight) {
        xPos += 1;
    } else {
        xPos -= 1;
    }
    window.requestAnimationFrame(draw);
}

document.onmousemove = function (event) {
    ctx.clearRect(0, 0, 1000, 1000);
    ctx.drawImage(myImage, event.x, event.y);
}

