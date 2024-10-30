function InputWidget({widgetID, labelName, valueRef, placeHolderValue}){
    return (
        <div id={widgetID}>
            <label>{labelName}</label><input type="text" ref={valueRef} placeholder={placeHolderValue}></input>
        </div>
    )
}

export default InputWidget;