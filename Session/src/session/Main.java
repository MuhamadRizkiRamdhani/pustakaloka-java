/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package session;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Lenovo
 */
public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();


        Parent root = FXMLLoader.load(getClass().getResource("/session/View/Main.fxml"));
     
        Scene scene = new Scene(root);

       
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void changeScene(String fxmlFile) throws Exception {
    Parent newRoot = FXMLLoader.load(getClass().getResource(fxmlFile));

    if (primaryStage.getScene() == null) {
        primaryStage.setScene(new Scene(newRoot));
    } else {
        primaryStage.getScene().setRoot(newRoot);
    }

    primaryStage.sizeToScene();  
    primaryStage.centerOnScreen(); 
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
