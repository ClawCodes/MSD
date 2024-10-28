let join = document.getElementById('join');
let leave = document.getElementById('leave');
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

let userName;

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
    messageUser.innerHTML = user + " ";
    messageMeta.appendChild(messageUser);
    let messageDtm = document.createElement("span");
    messageDtm.innerHTML = new Date().toLocaleTimeString();
    messageMeta.appendChild(messageDtm);

    messageContainer.appendChild(messageBlock);
    messageContainer.appendChild(messageMeta);

    chatLog.appendChild(messageContainer);
    chatLog.scrollTop = chatLog.scrollHeight;
}

function submitChatMessage(event) {
    event.preventDefault(); // Prevent form from refreshing page
    ws.send(`message ${formMessage.value}`);
    chatForm.reset();
}

ws.onopen = function (event) {
    isConnected = true;
    console.log("Connection established");
    console.log(event);
};


ws.onmessage = function (messageEvent) {
    let message = JSON.parse(messageEvent.data);
    if (message.type === "join") {
        chatPage.style.display = "block";
        homePage.style.display = "none";
        document.getElementById("room").innerHTML = message.room;
        chatForm.addEventListener("submit", submitChatMessage);
    }
    if (message.type === "message") {
        console.log(message);
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
    userName = userObj.value;
    let room = roomObj.value;

    if (!(validateUserInput(userName) && validateUserInput(room))) {
        alert("Username and Room must contain only letters.");
    } else {
        ws.send(`join ${userName} ${room}`);
    }
}

function leaveChatRoom(){
    chatPage.style.display = "none";
    homePage.style.display = "block";
}

leave.addEventListener('click', leaveChatRoom);

join.addEventListener('click', getUsernameAndPassword);
