/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package session.Controller;

import com.sun.jdi.connect.spi.Connection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import session.DAO.DBConnection;
import session.DAO.UserDAO;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class RegisterController implements Initializable {
    @FXML
    private TextField TxtUsername;
    @FXML
    private TextField TxtFullname;
    @FXML
    private TextField TxtPassword;
    @FXML
    private Button BtnBuatAkun;

    @FXML
private void handleBuatAkun(ActionEvent event) throws Exception {
    String username = TxtUsername.getText().trim();
    String fullname = TxtFullname.getText().trim();
    String password = TxtPassword.getText().trim();

    if (username.isEmpty() || fullname.isEmpty() || password.isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Peringatan");
        alert.setHeaderText(null);
        alert.setContentText("Semua field wajib diisi!");
        alert.showAndWait();
        return;
    }

    boolean sukses = UserDAO.registerUser(username, fullname, password);

    if (sukses) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Berhasil");
        alert.setHeaderText(null);
        alert.setContentText("Akun berhasil dibuat. Silakan login.");
        alert.showAndWait();

        // Tambahkan pengecekan lokasi file FXML
        URL fxmlLocation = getClass().getResource("/session/View/Main.fxml");
        if (fxmlLocation == null) {
            System.err.println("path not found");
            return;
        }

        Parent root = FXMLLoader.load(fxmlLocation);
        Stage stage = (Stage) BtnBuatAkun.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();

    } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gagal");
        alert.setHeaderText(null);
        alert.setContentText("Gagal membuat akun. Username mungkin sudah dipakai.");
        alert.showAndWait();
    }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
