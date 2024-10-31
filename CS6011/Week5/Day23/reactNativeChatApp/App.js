import InputWidget from './InputWidget';
import ChatRoom from './ChatRoom';
import { useEffect, useRef, useState } from "react";
import { View, Button } from 'react-native';

function App() {
  const [userName, setUserName] = useState("");
  const [room, setRoom] = useState("");
  const [showChatPage, setShowChatPage] = useState(false);
  const [chatLog, setChatLog] = useState([]);
  const chatRoom = useRef(""); // to store the room name persistently
  const connection = useRef(null);


  useEffect(() => {
    const ws = new WebSocket("ws://10.0.2.2:8088");

    ws.onopen = () => console.log("Connection established");
    ws.onerror = () => console.log("Error occurred");
    ws.onclose = () => console.log("Connection closed");

    ws.onmessage = (event) => {
      const message = JSON.parse(event.data);
      if (message.type === "message") {
        setChatLog((prevLog) => [...prevLog, message.message]);
      }
    };

    connection.current = ws;
    return () => connection.current?.close();
  }, []);

  const togglePages = () => {
    if (userName && room) {
      connection.current.send(`join ${userName} ${room}`);
      chatRoom.current = room;
      setShowChatPage(true);
    }
  };

  const returnToHome = () => {
    chatRoom.current = "";
    userName.current = "";
    room.current = "";
    connection.current.send(`leave`);
    setShowChatPage(false);
  }

  function sendMessage(message) {
    connection.current.send(`message ${message}`);
  }

  return (
    <View style={{ padding: 10 }}>
      {showChatPage ? (
        <ChatRoom roomName={chatRoom.current} chatLog={chatLog} messageHandler={sendMessage} leaveHandler={returnToHome}/>
      ) : (
        <View>
          <InputWidget labelName="Username: " value={userName} onChangeText={setUserName} placeHolderValue="Enter your username" />
          <InputWidget labelName="Room: " value={room} onChangeText={setRoom} placeHolderValue="Enter room to join" />
          <Button title="Join Room" onPress={togglePages} />
        </View>
      )}
    </View>
  );
}

export default App;