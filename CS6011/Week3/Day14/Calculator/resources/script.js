let x = document.getElementById("xVal");
let y = document.getElementById("yVal");
let submit = document.getElementById("calculate");
let result = document.getElementById("result");

let ws = new WebSocket("ws://localhost:8080");
let isConnected = false;

ws.onopen = function(){
    isConnected = true;
    console.log("Connection established");
};

ws.onmessage = function(messageEvent){
    console.log(messageEvent);
    result.textContent = "Result = " + messageEvent.data;
};

ws.onerror = function(e){
    console.log("Error occurred");
};

ws.onclose = function(e){
    console.log("Connection closed")
};

submit.addEventListener('click', function(){
});

submit.addEventListener('click', function(){
    let xValue = Number(x.value);
    let yValue = Number(y.value);

    if(!(isNaN(xValue) ||  isNaN(yValue))) {
        // fetch("HTTP://localhost:8080/calculate?" + x.value + "&y=" + y.value)
        //     .then(response => {
        //         if(!response.ok){
        //             console.log("There was an error");
        //         }
        //         console.log(response.text);
        //         return response.text()
        //     }).then(data =>{
        //     alert(data);
        //     result.textContent = "Result = " + data;
        // });
        if (isConnected){
            console.log(xValue + " " + yValue);
            ws.send(xValue + " " + yValue);
        }
    }
});

// submit.addEventListener('click', function(){
//     let ajaxRequest = new XMLHttpRequest();
//     let xValue = Number(x.value);
//     let yValue = Number(y.value);
//
//     if (isNaN(xValue) || isNaN(yValue)) {
//
//         ajaxRequest.open('GET', "HTTP://localhost:8080/calculate?" + x.value + "&y=" + y.value);
//         ajaxRequest.addEventListener('load', function () {
//             result.textContent = "Result = " + this.responseText;
//         });
//         ajaxRequest.addEventListener('error', function(){
//            console.log(this.response);
//         });
//     }
//     else {
//         alert("Please only use numbers");
//     }
//
//     ajaxRequest.send();
// });