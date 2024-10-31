import './App.css';
import InputWidget from './InputWidget';
import ChatRoom from './ChatRoom';
import { useEffect, useRef, useState } from "react";


function App() {
  let userName = useRef( "" );
  let room = useRef( "" );
  let chatRoom = useRef( "" );

  const [showChatPage, setShowChatPage] = useState(false);

  const [chatLog, setChatLog] = useState([]);

  // Create Web Socket
  const connection = useRef(null);
  
  useEffect(() => {
    const ws = new WebSocket("ws://localhost:8088");
    
    ws.addEventListener("open", (event) =>{
      console.log("Connection established");
    })

    ws.addEventListener("error", (event) => {
      console.log("Error occurred");
    })

    ws.addEventListener("close", (event) => {
      console.log("Connection closed")
    })

    ws.addEventListener("message", (event) => {
      let message = JSON.parse(event.data);
      console.log("RECIEVED: " + JSON.stringify(message))
      if (message.type === "message") {
        console.log(`ROOMNAME: ${room.current}`)
        setChatLog((prevLog) => [...prevLog, message.message]);
      }
    }, [])

    connection.current = ws;

    return () => connection.current.close()
    
  }, []);

  const togglePages = () => {
    let swapPages = !(!userName.current.value | !room.current.value);

    if (swapPages){
      connection.current.send(`join ${userName.current.value} ${room.current.value}`);
      chatRoom.current = room.current
      setShowChatPage(swapPages);
    }
  }

  function sendMessage(message){
    console.log("SENDING: " + `message ${message}`)
    connection.current.send(`message ${message}`)
  }

  return (
    <div className="App">
      {
        (showChatPage) ? 
        <ChatRoom roomName={chatRoom.current.value} chatLog={chatLog} messageHandler={sendMessage}/>: 
        (
          <div>
            <InputWidget widgetID={1} labelName="Username: " valueRef={userName} placeHolderValue="Enter your username"/>
            <InputWidget widgetID={2} labelName="Room: " valueRef={room} placeHolderValue="Enter room to join"/>
            <button onClick={togglePages}>Join Room</button>
          </div>
        )
      }
    </div>
  );
}

export default App;
