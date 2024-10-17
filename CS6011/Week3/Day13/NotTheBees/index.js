// Create global refs (canvas and context)
let canvas = document.querySelector('#myCanvas');
canvas.style.cursor = 'none';
let ctx = canvas.getContext('2d');

let cursorLocation = {x:0, y:0};

function handleMouseMove(event){
    cursorLocation.x = event.x;
    cursorLocation.y = event.y;
}

// canvas.onmousemove = handleMouseMove;
canvas.addEventListener('mousemove', handleMouseMove);


let friendship = { 'x': canvas.width / 2, 'y': canvas.height / 2 };
friendship.image = new Image();
friendship.image.src = './images/bffs.png'

function moveFriendship(){
    friendship.image.x = cursorLocation.x;
    friendship.image.y = cursorLocation.y;
}


function getRandom(min, max) {
    return Math.random() * (max - min) + min;
}

function createZuck(xpos, ypos) {
    let zuck = { 'x': xpos, 'y': ypos, 'speed': getRandom(2, 7)};
    zuck.image = new Image();
    zuck.image.src = './images/zuck.png';
    return zuck;
}

function createZucks() {
    let zucks = Array();
    for (let i = 0; i < 10; i++) {
        let xPos = getRandom(100, 500);
        let yPos = getRandom(200, 800);
        zucks.push(createZuck(xPos, yPos));
    }
    return zucks;
}

let zucks = createZucks();


// TODO: check if zuck overlaps friendship and pause
function checkIfFriendsForever(zuck){

}


function drawZucks(){
    for (let zuck of zucks){
        if (zuck.x < cursorLocation.x){
            zuck.x += zuck.speed;
        } else{
            zuck.x -= zuck.speed;
        }
        if (zuck.y < cursorLocation.y){
            zuck.y += zuck.speed;
        } else{
            zuck.y -= zuck.speed;
        }
        ctx.drawImage(zuck.image, zuck.x, zuck.y);
    }
}


function draw(){
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.drawImage(friendship.image, cursorLocation.x, cursorLocation.y);
    drawZucks();
}

function mainGameLoop(){
    moveFriendship();
    draw();
    window.requestAnimationFrame(mainGameLoop);
}

mainGameLoop()
