using System.Text.RegularExpressions;

namespace ChessBrowser {
    internal class RawGame {
        string event_ {get; set;}
        string site {get; set;}
        string date {get; set;}
        string round {get; set;}
        string whitePlayer {get; set;}
        string blackPlayer {get; set;}
        string result {get; set;}
        string whiteElo {get; set;}
        string blackElo {get; set;}
        string moves {get; set;}

        public void setAttr(string attr, string value){
            if (attr == "Event"){
                event_ = value;
            }
            if (attr == "Site"){
                site = value;
            }
            if (attr == "EventDate"){
                if (value.Contains('?')){
                    date = "0000-00-00";
                } else {
                    value.Replace('.', '-');
                    date = value;
                }
            }
            if (attr == "Round") {
                round = value;
            }
            if (attr == "White") {
                whitePlayer = value;
            }
            if (attr == "Black") {
                blackPlayer = value;
            }
            if (attr == "Result") {
                if (value == "1-0"){
                    result = "W";
                }
                if (value == "0-1"){
                    result = "B";
                }
                else {
                    result = "D";
                }
            }
            if (attr == "WhiteElo") {
                whiteElo = value;
            }
            if (attr == "BlackElo") {
                blackElo = value;
            }
            if (attr == "moves"){
                moves = value;
            }
        }

        public override string ToString()
        {
            return $"Event={event_}Site={site}Date={date}Round={round}White={whitePlayer}" +
            $"Black={blackPlayer}Result={result}WhiteElo={whiteElo}BlackElo={blackElo}";
        }
    }

    public class PGNParser
    {
        private List<RawGame> allGames = new List<RawGame>();
        public void parse (string fileName, MainPage mainPage){
            StreamReader reader = new StreamReader(fileName);

            string line = reader.ReadLine();
            Regex headerLinePat = new Regex(@"\[(\w+)\s""(.+)""\]");
            while (!string.IsNullOrEmpty(line)){
                RawGame game = new RawGame();
                while (line.StartsWith('[')){
                    Match match_ = headerLinePat.Match(line);
                    string lineType = match_.Groups[1].Value;
                    string lineValue = match_.Groups[2].Value;

                    game.setAttr(lineType, lineValue);
                    line = reader.ReadLine();
                }

                line = reader.ReadLine();

                string moves = "";
                while(!string.IsNullOrEmpty(line)){
                    moves += line;
                    line = reader.ReadLine();
                }
                game.setAttr("moves", moves);

                mainPage.setOutText(game.ToString());    
                line = reader.ReadLine();
            }
        }

        List<RawGame> GetGames(){
            return allGames;
        }
    }
}