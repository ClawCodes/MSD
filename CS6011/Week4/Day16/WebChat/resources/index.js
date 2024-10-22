let join = document.getElementById('join');
let userObj = document.getElementById('user-entry');
let roomObj = document.getElementById('room-entry');

let homePage = document.getElementById("home-page");
let chatPage = document.getElementById("chat-page");
let chatForm = document.getElementById("chat-form");
let formMessage = document.getElementById("chat-message");
let chatLog = document.getElementById("chat-log");

// Establish websocket
let ws = new WebSocket("ws://localhost:8080");
let isConnected = false;

// Items for rooms
let localChatLog = Array();

function createLogEntry(message, user) {
    let messageContainer = document.createElement("div");
    messageContainer.className = "message-container";

    let messageDiv = document.createElement("div");
    messageDiv.className = "message";
    let messageBlock = document.createElement("p");
    messageBlock.className = "message-body";
    messageBlock.innerHTML = message;
    messageDiv.appendChild(messageBlock);

    // TODO: meta info not showing
    let messageMeta = document.createElement("div");
    messageMeta.className = "message-meta";
    let messageUser = document.createElement("span");
    messageUser.innerHTML = user;
    let messageDtm = document.createElement("span");
    messageDtm.innerHTML = new Date().toLocaleTimeString();

    messageContainer.appendChild(messageBlock);
    messageContainer.appendChild(messageMeta);

    chatLog.appendChild(messageContainer);
}

function submitChatMessage(event) {
    event.preventDefault(); // Prevent form from refreshing page
    ws.send(`message ${formMessage.value}`);
    chatForm.reset();
}

ws.onopen = function () {
    isConnected = true;
    console.log("Connection established");
};


ws.onmessage = function (messageEvent) {
    let message = JSON.parse(messageEvent.data);
    if (message.type === "join") {
        // createRoom(message.room);
        chatPage.style.display = "block";
        homePage.style.display = "none";
        document.getElementById("room").innerHTML = message.room;
        chatForm.addEventListener("submit", submitChatMessage);
    }
    if (message.type === "leave") {
        chatPage.style.display = "none";
        // // TODO: remove chat log
        homePage.style.display = "block";
    }
    if (message.type === "message") {
        createLogEntry(message.message, message.user);
    }
};

ws.onerror = function (e) {
    console.log("Error occurred");
};

ws.onclose = function (e) {
    console.log("Connection closed")
};

function validateUserInput(value) {
    return !(value.match(".*[^a-zA-Z].*"));
}

function getUsernameAndPassword() {
    let userName = userObj.value;
    let room = roomObj.value;

    if (!(validateUserInput(userName) && validateUserInput(room))) {
        alert("Username and Room must contain only letters.");
    } else {
        ws.send(`join ${userName} ${room}`);
    }
}

join.addEventListener('click', getUsernameAndPassword);
