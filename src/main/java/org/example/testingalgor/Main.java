package org.example.testingalgor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Visualizer visualizer = new Visualizer();
        BorderPane root = new BorderPane();

        root.setCenter(visualizer.getCanvas());
        root.setRight(visualizer.getRightPanel());
        root.setBottom(visualizer.getControls());

        Scene scene = new Scene(root,1280,800);

        var css = getClass().getResource("/style.css");

        if(css != null){
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setTitle("AlgoView");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

        visualizer.generateArray();
    }

    public static void main(String[] args) {
        launch();
    }
}