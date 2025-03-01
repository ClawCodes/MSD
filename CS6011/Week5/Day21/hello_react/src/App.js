import './App.css';
import ToDoList from './ToDoList';
import InputArea from './InputArea'

import { useState } from 'react';


function App() {
  const [items, setItems] = useState(["item1", "item2"]);

  function addItem(textAreaRef){
    // Used to be
    // items.push(textAreaRef.current.value);
    // setItems(items); // Why did this update on delete?
    setItems([...items, textAreaRef.current.value]);
  };

  function deleteItem(index) {
    setItems((prevItems) => prevItems.filter((_, i) => i !== index));
  };

  return (
    <div className="App">
      <h1>To-Do List</h1>
      <ToDoList listItems={items} deleteFunc={deleteItem} />
      <InputArea addItemFunc={addItem} />
    </div>
  );
}

export default App;
