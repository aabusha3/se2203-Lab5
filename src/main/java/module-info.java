module se2203b.lab5.tennisballgames {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens se2203b.lab5.tennisballgames to javafx.fxml;
    exports se2203b.lab5.tennisballgames;
}