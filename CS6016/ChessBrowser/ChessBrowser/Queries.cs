using Microsoft.Maui.Controls;
using MySqlConnector;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ChessBrowser
{
  internal class Queries
  {

    /// <summary>
    /// This function runs when the upload button is pressed.
    /// Given a filename, parses the PGN file, and uploads
    /// each chess game to the user's database.
    /// </summary>
    /// <param name="PGNfilename">The path to the PGN file</param>
    internal static async Task InsertGameData( string PGNfilename, MainPage mainPage )
    {
      // This will build a connection string to your user's database on atr,
      // assuimg you've typed a user and password in the GUI
      string connection = mainPage.GetConnectionString();

      PGNParser parser = new PGNParser();
      parser.parse(PGNfilename);

      mainPage.SetNumWorkItems(parser.GetGames().Count);
      RawGame g = null;
      using ( MySqlConnection conn = new MySqlConnection( connection ) )
      {
        try
        {
          // Open a connection
          conn.Open();
          foreach (RawGame game in parser.GetGames()){
            // Insert Players
            MySqlCommand playerCmd = conn.CreateCommand();
            playerCmd.CommandText = @$"INSERT INTO Players(Name, Elo)
                                       VALUES ('{game.whitePlayer}', '{game.whiteElo}'),
                                              ('{game.blackPlayer}', '{game.blackElo}')
                                       ON DUPLICATE KEY UPDATE Elo = CASE
                                                                      WHEN VALUES(Elo) > Elo THEN VALUES(Elo)
                                                                      ELSE Elo
                                                                     END;";
            playerCmd.ExecuteNonQuery();
            // Insert Event
            MySqlCommand eventCmd = conn.CreateCommand();
            eventCmd.CommandText = @$"INSERT IGNORE INTO Events(Name, Site, Date)
                                    VALUES ('{game.event_}', '{game.site}', '{game.date}');";
            eventCmd.ExecuteNonQuery();
            // Insert Game
            MySqlCommand gameCmd = conn.CreateCommand();
            gameCmd.CommandText = @$"INSERT IGNORE INTO Games(Round, Result, Moves, BlackPlayer, WhitePlayer, eID)
                                      VALUES ('{game.round}', '{game.result}', '{game.moves}',
                                              (SELECT pID FROM Players WHERE Name = '{game.blackPlayer}'),
                                              (SELECT pID FROM Players WHERE Name = '{game.whitePlayer}'),
                                              (SELECT Events.eID FROM Events WHERE Name = '{game.event_}' AND Site = '{game.site}' AND Date = '{game.date}'));";
            gameCmd.ExecuteNonQuery();
            await mainPage.NotifyWorkItemCompleted();
          }

        }
        catch ( Exception e )
        {
          System.Diagnostics.Debug.WriteLine( e.Message );
        }
      }

    }


    /// <summary>
    /// Queries the database for games that match all the given filters.
    /// The filters are taken from the various controls in the GUI.
    /// </summary>
    /// <param name="white">The white player, or null if none</param>
    /// <param name="black">The black player, or null if none</param>
    /// <param name="opening">The first move, e.g. "1.e4", or null if none</param>
    /// <param name="winner">The winner as "W", "B", "D", or null if none</param>
    /// <param name="useDate">True if the filter includes a date range, False otherwise</param>
    /// <param name="start">The start of the date range</param>
    /// <param name="end">The end of the date range</param>
    /// <param name="showMoves">True if the returned data should include the PGN moves</param>
    /// <returns>A string separated by newlines containing the filtered games</returns>
    internal static string PerformQuery( string white, string black, string opening,
      string winner, bool useDate, DateTime start, DateTime end, bool showMoves,
      MainPage mainPage )
    {
      // This will build a connection string to your user's database on atr,
      // assuimg you've typed a user and password in the GUI
      string connection = mainPage.GetConnectionString();

      // Build up this string containing the results from your query
      string parsedResult = "";

      // Use this to count the number of rows returned by your query
      // (see below return statement)
      int numRows = 0;

      string queryText = "";
      using ( MySqlConnection conn = new MySqlConnection( connection ) )
      {
        try
        {
          parsedResult += "b";
          // Open a connection
          conn.Open();
          MySqlCommand query = conn.CreateCommand();
          
          query.CommandText = @$"WITH white_player AS (
                                    SELECT *
                                    FROM Players
                                    WHERE @WhitePlayer IS NULL OR Name = @WhitePlayer
                                ),
                                black_player AS (
                                  SELECT *
                                  FROM Players
                                  WHERE @BlackPlayer IS NULL OR Name = @BlackPlayer
                                ),
                                events AS (
                                  SELECT *
                                  FROM Events
                                  WHERE Date BETWEEN @startParam AND @endParam
                                )
                                SELECT e.Name as EventName,
                                       e.Site,
                                       e.Date,
                                       wp.Name AS WP,
                                       wp.Elo AS wpElo,
                                       bp.Name AS BP,
                                       bp.Elo AS bpElo,
                                       g.Result,
                                       g.Moves
                                FROM Games AS g
                                JOIN black_player AS bp
                                  ON g.BlackPlayer = bp.pID
                                JOIN white_player AS wp
                                  ON g.WhitePlayer = wp.pID
                                JOIN events AS e
                                  ON g.eID = e.eID
                                WHERE (@Result IS NULL OR g.Result = @Result) AND g.Moves LIKE @Moves;
                              ";

        query.Parameters.AddWithValue("@WhitePlayer", string.IsNullOrEmpty(white) ? null : white);
        query.Parameters.AddWithValue("@BlackPlayer", string.IsNullOrEmpty(black) ? null : black);
        query.Parameters.AddWithValue("@startParam", useDate ? start : DateTime.MinValue);
        query.Parameters.AddWithValue("@endParam", useDate ? end : DateTime.MaxValue);
        query.Parameters.AddWithValue("@Result", string.IsNullOrEmpty(winner) ? null : winner);
        query.Parameters.AddWithValue("@Moves", string.IsNullOrEmpty(opening) ? "%" : $"{opening}%");

        queryText = query.CommandText;

        using (MySqlDataReader reader = query.ExecuteReader()){  
          while (reader.Read()){
              parsedResult += $"Event: {reader["EventName"]}\n";
              parsedResult += $"Site: {reader["Site"]}\n";
              parsedResult += $"Date: {reader["Date"]}\n";
              parsedResult += $"White: {reader["WP"]} ({reader["wpElo"]})\n";
              parsedResult += $"Black: {reader["BP"]} ({reader["bpElo"]})\n";
              parsedResult += $"Result: {reader["Result"]}\n";

              if (showMoves){
                parsedResult += $"Moves: {reader["Moves"]}\n";
              }
              parsedResult += "\n";
              numRows += 1;
          }
        }   
        }
        catch ( Exception e )
        {
          parsedResult += e.Message;
          System.Diagnostics.Debug.WriteLine( e.Message );
        }
      }
      return numRows + " results\n\n" + parsedResult;
    }
  }
}
