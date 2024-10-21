let join = document.getElementById('join');
let userObj = document.getElementById('username');
let roomObj = document.getElementById('room');

function getUsernameAndPassword() {
    // fetch("HTTP://localhost:8080/join?" + userObj.value + "&room=" + roomObj)
    //     .then(response => {
    //         if(!response.ok){
    //             console.log("There was an error");
    //         }
    //         console.log(response.text);
    //         return response.text()
    //     }).then(data =>{
    //     alert(data);
    //     // TODO: Establish web socket with server
    //
    // });        return;
    console.log(userObj.value);
    console.log(roomObj.value);
}

join.addEventListener('click', getUsernameAndPassword);
