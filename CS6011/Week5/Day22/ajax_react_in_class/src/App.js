import { useState } from 'react';
import './App.css';
import Trivia from './Trivia';

function App() {

  const [showWidget, setShowWidget] = useState(false);


  function toggleShowWidgets(){
    setShowWidget(!showWidget);
  }

  return (
    <div className="App">
      <button onClick={toggleShowWidgets}>Toggle Widgets</button>
      {
        (showWidget) ? <Trivia/> : <p>Nothing to see here.</p>
      }
    </div>
  );
}

export default App;
