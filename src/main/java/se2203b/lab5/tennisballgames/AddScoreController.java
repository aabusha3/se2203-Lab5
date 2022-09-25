package se2203b.lab5.tennisballgames;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 *
 * @author Abdelkader Ouda
 */
public class AddScoreController implements Initializable {

    @FXML
    Button cancelBtn, saveBtn;

    @FXML
    ComboBox<String> matchBox;

    @FXML
    TextField homeTeamScore, visitorTeamScore;


    // The data variable is used to populate the ComboBoxs
    final ObservableList<String> data = FXCollections.observableArrayList();

    // To reference the models inside the controller
    private MatchesAdapter matchesAdapter;
    private TeamsAdapter teamsAdapter;

    public void setModel(MatchesAdapter match, TeamsAdapter team) {
        matchesAdapter = match;
        teamsAdapter = team;
        buildComboBoxData();
    }

    @FXML
    public void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void save() {
        try {
            String[] matchData = matchBox.getValue().replaceAll("\\s+", "").split("-");
            matchesAdapter.setTeamsScore(Integer.parseInt(matchData[0]), Integer.parseInt(homeTeamScore.getText()), Integer.parseInt(visitorTeamScore.getText()));
            teamsAdapter.setStatus(matchData[1], matchData[2], Integer.parseInt(homeTeamScore.getText()), Integer.parseInt(visitorTeamScore.getText()));
        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }
            
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void buildComboBoxData() {
        try {
            data.addAll(matchesAdapter.getMatchesNamesList());
        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }
    }

     private void displayAlert(String msg) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/main/resources/se2203b/lab5/tennisballgames/WesternLogo.png"));
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }
     
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        matchBox.setItems(data);
    }


}
