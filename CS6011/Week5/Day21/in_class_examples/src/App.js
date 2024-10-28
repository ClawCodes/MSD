import logo from './logo.svg';
import './App.css';
import {useRef, useState } from 'react';


function App() {
  const li = useRef(null );

  const [count, setCount] = useState(0);
  function handleClick(event){
    // When you change the state of the component,the DOM is rebuilt.
    setCount(count + 1);
    li.current.innerHTML = count;
  }

  let data = ["Hello", "How", "Are", "You"];

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
        <br></br>
        {/* Since we are updating count with use state, the count here will be refreshed */}
        <button onClick={handleClick}>Press count: {count}</button>
        <br></br>
        <ul>
          <li ref={li}> previous count... </li>
          {data.map((item) => <li>{item}</li>)}
          {/* Conditional rendering */}
          {data.map((item, index) => index < count ? <li>{item}</li> : <li>hidden</li>)}
        </ul>
      </header>
    </div>
  );
}

export default App;
