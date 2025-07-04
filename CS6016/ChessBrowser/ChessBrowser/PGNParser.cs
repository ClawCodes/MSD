using System.Text.RegularExpressions;

namespace ChessBrowser {
    public class RawGame {
        public string event_ {get; set;}
        public string site {get; set;}
        public string date {get; set;}
        public string round {get; set;}
        public string whitePlayer {get; set;}
        public string blackPlayer {get; set;}
        public string result {get; set;}
        public string whiteElo {get; set;}
        public string blackElo {get; set;}
        public string moves {get; set;}

        public void setAttr(string attr, string value){
            value = value.Replace("'", "''"); // Escape single quotes for SQL insert
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
                    date = value.Replace('.', '-');
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
        
        public void parse (string fileName){
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
                allGames.Add(game);
                line = reader.ReadLine();
            }
        }

        public List<RawGame> GetGames(){
            return allGames;
        }
    }
}