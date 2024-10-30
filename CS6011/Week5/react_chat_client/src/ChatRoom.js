import { useRef } from "react";

function ChatRoom({roomName, chatLog, messageHandler}){
    let inputMessage = useRef( "" );

    const handleSubmit = (event) => {
        event.preventDefault(); // Prevent form submission from reloading the page
        messageHandler(inputMessage.current.value);
        inputMessage.current.value = ""; // Clear the input field after sending
    };

    return (
        <div id="chat-room">
            <h1>{roomName}</h1>
            <div id="chat-log">
                {
                    chatLog.map((message, idx) => 
                        <p key={idx}>{message}</p>
                    )
                }
            </div>
            <form onSubmit={handleSubmit}>
                <input id="input-message" type="text" ref={inputMessage}></input>
                <input id="submit-message" type="submit" value="post"></input>
            </form>
        </div>
    )
}

export default ChatRoom;