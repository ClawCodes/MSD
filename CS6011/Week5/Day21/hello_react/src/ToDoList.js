function ToDoList({listItems, deleteFunc}){
    return <div id="toDoListDiv">
        {/* <div className="item">
            {listItems.map((item, index) =>
                 <div key={index}  onDoubleClick={() => deleteFunc(index)}>
                    <p>{item}<input type="checkbox"></input></p>
                </div>)}
        </div> */}

        <ul >
            {listItems.map((item, index) =>
                 <li key={index} className="item" onDoubleClick={() => deleteFunc(index)}>
                    <p>{item}<input type="checkbox"></input></p>
                </li>)}
        </ul>
    </div>
}

export default ToDoList;