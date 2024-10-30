import { StatusBar } from 'expo-status-bar';
import { useState, useRef, useEffect } from 'react';
import { StyleSheet, Text, View } from 'react-native';

export default function App() {
  const ws = useRef(null);

  const [message, setMessage] = useState("");

  useEffect(
    () => {
      ws.current = new WebSocket("ws://10.0.2.2:8088"); // must use this IP instead of 

      ws.current.onOpen = () => {
        console.log("Connection established");
      }

      ws.current.onMessage = (event) => {
        let object = JSON.parse(event.data);
        setMessage(object.user);
      }

      return () => {
        ws.current.close();
      }
    }, []);



  return (
    <View style={styles.container}>
      <Text>bing ging</Text>
      <StatusBar style="auto" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
