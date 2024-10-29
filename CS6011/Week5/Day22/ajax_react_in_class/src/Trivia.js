import { useState, useEffect } from "react";

function Trivia(){

useEffect(() => {
    console.log("Trivia widget created")
    return () => { console.log("Trivia widget has gone away")}
}, []);

const [question, setQuestion] = useState( "" );
const [answer, setAnswer] = useState( "" );
const [displayAnswer, setDisplayAnswer] = useState( false );

    const loadTriviaFact = async() => {
        setDisplayAnswer(false);
        try{
            const response = await fetch("https://opentdb.com/api.php?amount=1");
            if (!response.ok){
                console.log("Error in fetch");
                throw new Error(`${response.status}`)
            }
            const data = await response.json();
            let result = data.results[0];
            setAnswer(result.correct_answer);
            setQuestion(result.question);
            let categroy = result.categroy;
            let difficulty = result.difficulty;
        } catch (err) {
            console.log(err);
        } finally {
            console.log("done loading");
        }
    }

    function showAnswer(){
        setDisplayAnswer(true);
    }

    return (
        <div>
            <button onClick={loadTriviaFact}>Jepordy</button>
            { (question.length > 0) ? 
                <div>
                    <p>{question}</p>
                    {
                    (displayAnswer) ? 
                        <p>{answer}</p> :
                        <button onClick={showAnswer}>Show answer</button>
                    }
                </div> : 
                    <p>no question yet</p>
                    }
        </div>
    );
}

export default Trivia;