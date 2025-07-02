using System;
using System.IO;
using System.Collections.Generic;
using Microsoft.Maui.Controls.Shapes;
using System.Text.RegularExpressions;

namespace ChessBrowser {
    public class Event {
        string name {get; set;}
        string site {get; set;}
        string date {get; set;}
    }

    public class Game {
        string round {get; set;}
        string result {get; set;}
        string moves {get; set;}
        string blackPlayer {get; set;}
        string whitePlayer {get; set;}
    }

    public class Player {
        string name {get; set;}
        int elo {get; set;}
    }

    // internal class Header {
    //     string event_ {get; set;}
    //     string site {get; set;}
    //     string date {get; set;}
    //     string round {get; set;}
    //     string whitePlayer {get; set;}
    //     string blackPlayer {get; set;}
    //     string result {get; set;}
    //     string whiteElo {get; set;}
    //     string blackElo {get; set;}
    // }

    public class PGNParser
    {
        public void parse (string fileName){
            StreamReader reader = new StreamReader(fileName);

            string line = reader.ReadLine();

            Dictionary<string, Player> players;
            Regex headerLinePat = "^[(\w+) \"(.+)\"]";
            while (line != null){
                Event event_ = new Event();
                Player Player = new Player();
                Game game = new Game();

                while (line.StartsWith('[')){
                    
                }
            }
        }

    }
}