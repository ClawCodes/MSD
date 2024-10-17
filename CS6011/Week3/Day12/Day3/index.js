// In class bees

// Create a bee
let bee = { 'x': 100, 'y': 100, 'speed': Math.random(5)};
bee.image = new Image();
bee.image.src = './images/bee.png';

// Draw bee
let canvas = document.querySelector('#mycanvas'); // Assumes canvas is in HTML
canvas.style.cursor = 'none';
let ctx = canvas.getContext('2d');
ctx.drawImage(bee.image, bee.x, bee.y, canvas.width, canvas.height);


let cursorLocation = {x:0,y:0};


function handleMouseMove(event){
    // bee.x = event.x - (bee.image.width / 2);
    // bee.y = event.y - canvas.offsetTop - (bee.image.height / 2);
    // ctx.clearRect(0, 0, canvas.width, canvas.height);
    // ctx.drawImage(bee.image, bee.x, bee.y, canvas.width, canvas.height);
    cursorLocation.x = event.x;
    cursorLocation.y = event.y;
}

function handleMove(){
    // is bee.x/y == to cursorLocation?
    // otherwise is x < cursorLocation.x
    if (bee.x < cursorLocation.x){
        bee.x += bee.speed;
    }
    else {
        bee.x -= bee.speed;
    }
    // otherwise is y < cursorLocation.y
}

function draw(){
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.drawImage(bee.image, bee.x, bee.y, canvas.width, canvas.height);
}

function mainGameLoop(){
    handleMove();
    draw();
    window.requestAnimationFrame(mainGameLoop);
}

canvas.onmousemove = handleMouseMove;

// Create a button
let btn = document.createElement('button');
btn.textContent = "Click to load data";

function handleLoadData(event) {
    let spinner = document.createElement('div'); // This would be insserting a gif into th div
    spinner.id = 'spinner';
}

btn.addEventListener('click', handleLoadData)
document.body.prepend(btn);


function pause(ms){
    return new Promise(resolve => setTimeout(resolve, ms));
}
pause(1000).then(() => {})


console.log(bee['x'] + ':' + bee['y']);

