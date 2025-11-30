package app.clubconnectfx;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        Navigator.setStage(stage);
        stage.setWidth(900);     // set width
        stage.setHeight(600);    // set height
        stage.centerOnScreen();  // center on screen
        Navigator.loadLogin();   // load your login screen
    }


    public static void main(String[] args) {
        launch();
    }
}
