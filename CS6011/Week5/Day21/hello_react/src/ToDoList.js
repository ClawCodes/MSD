function ToDoList({listItems, deleteFunc}){
    return <div id="toDoListDiv">
        <ul >
            {listItems.map((item, index) =>
                 <li key={index} className="item" onDoubleClick={() => deleteFunc(index)}>
                    <p>{item}<input type="checkbox"></input></p>
                </li>)}
        </ul>
    </div>
}

export default ToDoList;