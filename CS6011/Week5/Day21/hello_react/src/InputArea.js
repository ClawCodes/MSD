import { useRef } from "react";

function InputArea({addItemFunc}){
    let inputTextArea = useRef(null);

    return <div if="inputDiv">
            <input id="input-area" type="text" ref={inputTextArea}></input>
            <button onClick={() => addItemFunc(inputTextArea)}>Add</button>
        </div>
}

export default InputArea;