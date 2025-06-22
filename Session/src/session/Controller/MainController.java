/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package session.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import session.Main;
import session.DAO.UserDAO;
import session.Model.Session;
import session.Model.User;

/**
 *
 * @author Lenovo
 */
public class MainController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private TextField TxtUsername;
    @FXML
    private PasswordField TxtPassword;
    @FXML
    private Button BtnLogin;
    @FXML
    private Button BtnRegister;
    
    
    
    @FXML
    private void handleRegister(ActionEvent event){
       try {
        Parent root = FXMLLoader.load(getClass().getResource("/session/View/Register.fxml"));

        Stage stage = (Stage) BtnRegister.getScene().getWindow();

        stage.setScene(new Scene(root));
        stage.centerOnScreen(); 
    } catch (IOException e) {
        e.printStackTrace();
    } 
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws Exception {
        checkLogin();
    }
    
    private void checkLogin() throws Exception {
    String username = TxtUsername.getText().trim();
    String password = TxtPassword.getText().trim();

    if (username.isEmpty() || password.isEmpty()) {
        showAlert("Login Error", "Isi Username dan Password!");
        return;
    }

    try {
        User user = UserDAO.getAccount(username, password);

        if (user != null) {
            Session session = Session.getInstance();
            session.setUsername(user.getUsername());
            session.setPassword(user.getPassword());
            session.setFullname(user.getFullname());
            session.setRole(user.getRole());

            showInfo("Login Success", "Login berhasil, " + user.getFullname());

            Main main = new Main();

            if (user.getRole().equalsIgnoreCase("admin")) {
                main.changeScene("/session/View/DashboardAdmin.fxml");
            } else if (user.getRole().equalsIgnoreCase("user")) {
                main.changeScene("/session/View/DashboardUser.fxml");
            } else {
                showAlert("Login Error", "Role tidak dikenali: " + user.getRole());
            }

        } else {
            showAlert("Login Error", "Username atau Password Salah!");
        }
    } catch (Exception e) {
        e.printStackTrace();
        showAlert("Login Error", "Terjadi kesalahan saat login.");
    }
    }
    
    //alert start
    private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    //alert end

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
