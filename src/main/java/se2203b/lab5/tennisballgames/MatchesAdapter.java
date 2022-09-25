package se2203b.lab5.tennisballgames;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 *
 * @author Abdelkader Ouda
 */
public class MatchesAdapter {

    Connection connection;

    public MatchesAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;
        if (reset) {
            Statement stmt = connection.createStatement();
            try {
                // Remove tables if database tables have been created.
            // This will throw an exception if the tables do not exist
            stmt.execute("DROP TABLE Matches");
                // then do finally
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
                // do finally to create it
            } finally {
                // Create the table of Matches
                stmt.execute("CREATE TABLE Matches ("
                        + "MatchNumber INT NOT NULL PRIMARY KEY, "
                        + "HomeTeam CHAR(15) NOT NULL REFERENCES Teams (TeamName), "
                        + "VisitorTeam CHAR(15) NOT NULL REFERENCES Teams (TeamName), "
                        + "HomeTeamScore INT, "
                        + "VisitorTeamScore INT "
                        + ")");
                populateSamples();
            }
        }
    }
    
    private void populateSamples() throws SQLException{
            // Create a listing of the matches to be played
            this.insertMatch(1, "Astros", "Brewers");
            this.insertMatch(2, "Brewers", "Cubs");
            this.insertMatch(3, "Cubs", "Astros");
    }
        
    
    public int getMax() throws SQLException {
        int num = 0;
        ResultSet rs;

        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String sqlStatement = "SELECT MAX(MatchNumber) FROM Matches";

        // Execute the statement and return the result
        rs = stmt.executeQuery(sqlStatement);

        if (rs.next())
            num = rs.getInt(1);

        return num+1;
    }
    
    public void insertMatch(int num, String home, String visitor) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO Matches (MatchNumber, HomeTeam, VisitorTeam, HomeTeamScore, VisitorTeamScore) "
                + "VALUES (" + num + " , '" + home + "' , '" + visitor + "', 0, 0)");
    }
    
    // Get all Matches
    public ObservableList<Matches> getMatchesList() throws SQLException {
        ObservableList<Matches> matchesList = FXCollections.observableArrayList();
        ResultSet rs;

        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String sqlStatement = "SELECT * FROM Matches";

        // Execute the statement and return the result
        rs = stmt.executeQuery(sqlStatement);

        int matchNum = 1;
        while (rs.next()) {
            matchesList.add(new Matches(matchNum++,
                    rs.getString("HomeTeam"),
                    rs.getString("VisitorTeam"),
                    rs.getInt("HomeTeamScore"),
                    rs.getInt("VisitorTeamScore")));
        }
        return matchesList;
    }


    public ObservableList<String> getMatchesNamesList() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        ResultSet rs;
        
        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String sqlStatement = "SELECT * FROM Matches";

        // Execute the statement and return the result
        rs = stmt.executeQuery(sqlStatement);
        
        // Loop the entire rows of rs and set the string values of list
        while (rs.next())
            list.add(rs.getString("MatchNumber") + "-" +
                    rs.getString("HomeTeam") + "-" + rs.getString("VisitorTeam"));
        
        return list;
    }

    public void setTeamsScore(int matchNumber, int hScore, int vScore) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("UPDATE Matches SET HomeTeamScore = " + hScore + ", VisitorTeamScore =" + vScore + " WHERE MatchNumber = " + matchNumber);
    }
}
