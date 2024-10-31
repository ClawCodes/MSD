import { useState } from "react";
import { View, Text, TextInput, Button, ScrollView } from 'react-native';

function ChatRoom({ roomName, chatLog, messageHandler, leaveHandler }) {
    const [inputMessage, setInputMessage] = useState("");

    const handleSendMessage = () => {
        messageHandler(inputMessage);
        setInputMessage(""); // Clear the input field after setting
    };

    return (
        <View style={{ padding: 10 }}>
            <Text style={{ fontSize: 24, fontWeight: "bold" }}>{roomName}</Text>
            <ScrollView style={{ height: 300, marginVertical: 10 }}>
                {chatLog.map((message, idx) => (
                    <Text key={idx} style={{ padding: 5 }}>{message}</Text>
                ))}
            </ScrollView>
            <TextInput
                style={{ borderColor: 'gray', borderWidth: 1, padding: 5 }}
                value={inputMessage}
                onChangeText={setInputMessage}
                placeholder="Type your message here"
            />
            <Button title="Send Message" onPress={handleSendMessage} />
            <Text>{'\n'}</Text>
            <Button title="Leave" onPress={leaveHandler} />
        </View>
    );
}

export default ChatRoom;