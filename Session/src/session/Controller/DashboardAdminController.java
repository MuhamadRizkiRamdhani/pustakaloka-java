/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package session.Controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import session.DAO.BukuAdminDAO;
import session.DAO.KategoriAdminDAO;
import session.Main;
import session.Model.Buku;
import session.Model.KategoriBuku;
import session.Model.Session;

/**
 * FXML Controller class
 *
 * @author Lenovo
 */
public class DashboardAdminController implements Initializable {
    @FXML private TextField LJudul;
    @FXML private ComboBox<KategoriBuku> CKategori;
    @FXML private TextField LPengarang;
    @FXML private TextField LPenerbit;
    @FXML private TextField LTahun;
    @FXML private TextField LEdisi;

    @FXML private Button BtnAdd;
    @FXML private Button BtnUpdate;
    @FXML private Button BtnDelete;
    @FXML private Button BtnLogoutAdmin;

    @FXML private TableView<Buku> tabel;
    @FXML private TableColumn<Buku, String> CKodeBuku;
    @FXML private TableColumn<Buku, String> kategori;
    @FXML private TableColumn<Buku, String> CJudul;
    @FXML private TableColumn<Buku, String> CPengarang;
    @FXML private TableColumn<Buku, String> CPenerbit;
    @FXML private TableColumn<Buku, String> CTahun;
    @FXML private TableColumn<Buku, String> CEdisi;
    @FXML private TableColumn<Buku, LocalDate> tahun_pengadaan;

    @FXML private DatePicker datepicker;

    private Buku selectedBuku;
    private KategoriAdminDAO kategoriDAO = new KategoriAdminDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadDataKategori();
        loadDataBuku();

        tabel.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> selectBuku(newSelection)
        );

        tabel.setOnMouseClicked(e -> selectBuku(tabel.getSelectionModel().getSelectedItem()));
    }

    private void setupTable() {
        CKodeBuku.setCellValueFactory(new PropertyValueFactory<>("kode_buku"));
        kategori.setCellValueFactory(cellData -> {
        String kode = cellData.getValue().getKode_kategori();
        String nama = kategoriMap.getOrDefault(kode, "Tidak Diketahui");
        return new ReadOnlyStringWrapper(nama);
        });
        CJudul.setCellValueFactory(new PropertyValueFactory<>("judul"));
        CPengarang.setCellValueFactory(new PropertyValueFactory<>("pengarang"));
        CPenerbit.setCellValueFactory(new PropertyValueFactory<>("penerbit"));
        CTahun.setCellValueFactory(new PropertyValueFactory<>("tahun"));
        CEdisi.setCellValueFactory(new PropertyValueFactory<>("edisi"));
        tahun_pengadaan.setCellValueFactory(new PropertyValueFactory<>("tahun_pengadaan"));
    }

    private void loadDataBuku() {
        tabel.setItems(FXCollections.observableArrayList(BukuAdminDAO.getBuku()));
    }

    private void loadDataKategori() {
        ObservableList<KategoriBuku> kategoriList = FXCollections.observableArrayList(KategoriAdminDAO.getKategori());
    CKategori.setItems(kategoriList);

    // Bikin map dari kode ke nama
    kategoriMap.clear();
    for (KategoriBuku k : kategoriList) {
        kategoriMap.put(k.getKode_kategori(), k.getNama_kategori());
    }
    }

    private void selectBuku(Buku buku) {
        if (buku != null) {
            selectedBuku = buku;
            LJudul.setText(buku.getJudul());
            LPengarang.setText(buku.getPengarang());
            LPenerbit.setText(buku.getPenerbit());
            LTahun.setText(buku.getTahun());
            LEdisi.setText(buku.getEdisi());
            datepicker.setValue(buku.getTahun_pengadaan());
            
            for (KategoriBuku kategori : CKategori.getItems()) {
            if (kategori.getKode_kategori().equals(buku.getKode_kategori())) {
                CKategori.getSelectionModel().select(kategori);
                break;
            }
        }
    }
    }
    
    //CRUD
    @FXML
    private void addBuku(ActionEvent event) {
    if (!isInputValid()) return;

    String kodeBaru = BukuAdminDAO.generateKodeBuku(); 

    Buku newBuku = new Buku(
        kodeBaru,
        CKategori.getValue().getKode_kategori(),
        LJudul.getText(),
        LPenerbit.getText(),
        LPengarang.getText(),
        LTahun.getText(),
        LEdisi.getText(),
        datepicker.getValue()
    );

    BukuAdminDAO.addBuku(newBuku);
    showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Data berhasil ditambahkan.");
    loadDataBuku();
    clearFields();
    }


    @FXML
    private void updateBuku(ActionEvent event) {
        if (selectedBuku == null) {
        showAlert(Alert.AlertType.ERROR, "Update Gagal", "Silakan pilih buku yang ingin diperbarui terlebih dahulu.");
        return;
    }

    if (!isInputValid()) {
        return; 
    }

    selectedBuku.setKode_kategori(CKategori.getValue().getKode_kategori());
    selectedBuku.setJudul(LJudul.getText());
    selectedBuku.setPengarang(LPengarang.getText());
    selectedBuku.setPenerbit(LPenerbit.getText());
    selectedBuku.setTahun(LTahun.getText());
    selectedBuku.setEdisi(LEdisi.getText());
    selectedBuku.setTahun_pengadaan(datepicker.getValue());

    BukuAdminDAO.updateBuku(selectedBuku);
    showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Data berhasil diperbarui.");
    loadDataBuku();
    clearFields();
    }

    @FXML
    private void deleteBuku(ActionEvent event) {
        if (selectedBuku == null) {
        showAlert(Alert.AlertType.ERROR, "Hapus Gagal", "Silakan pilih buku yang ingin dihapus terlebih dahulu.");
        return;
    }
    if (showDeleteConfirmation("Konfirmasi Hapus", "Apakah Anda yakin ingin menghapus buku ini?")) {
        BukuAdminDAO.deleteBuku(selectedBuku.getJudul());
        showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Data berhasil dihapus.");
        loadDataBuku();
        clearFields();
        }
    }
    
   //CRUD

    private void clearFields() {
        LJudul.clear();
        LPengarang.clear();
        LPenerbit.clear();
        LTahun.clear();
        LEdisi.clear();
        datepicker.setValue(null);
        CKategori.setValue(null);
        selectedBuku = null;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showDeleteConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private boolean isFieldEmpty() {
        return CKategori.getValue() == null ||
               LJudul.getText().isEmpty() ||
               LPengarang.getText().isEmpty() ||
               LPenerbit.getText().isEmpty() ||
               LTahun.getText().isEmpty() ||
               LEdisi.getText().isEmpty() ||
               datepicker.getValue() == null;
    }
    
    //validasi
        private boolean isInputValid() {
        if (CKategori.getValue() == null || LJudul.getText().isEmpty() ||
                LPengarang.getText().isEmpty() || LPenerbit.getText().isEmpty() ||
                LTahun.getText().isEmpty() || LEdisi.getText().isEmpty() || datepicker.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Semua field harus diisi.");
            return false;
        }

        if (!LTahun.getText().matches("^\\d{4}$")) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Tahun harus terdiri dari 4 digit angka.");
            return false;
        }

        if (!LEdisi.getText().matches("^\\d{1,2}$")) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Edisi hanya boleh 1-2 digit angka.");
            return false;
        }

        try {
            Integer.parseInt(LTahun.getText());
            Integer.parseInt(LEdisi.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Validasi", "Tahun dan Edisi harus berupa angka.");
            return false;
        }

        return true;
    }
    //validasi
        
    @FXML
    private void handleEksport(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Simpan File CSV");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    fileChooser.setInitialFileName("data_buku.csv");

    File file = fileChooser.showSaveDialog(null);
    if (file != null) {
        exportTableToCSV(file);
    }
    }
    
    private void exportTableToCSV(File file) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        // Header
        writer.write("Kode Buku;Kategori;Judul;Pengarang;Penerbit;Tahun;Edisi;Tahun Pengadaan");
        writer.newLine();

        // Isi data
        for (Buku buku : tabel.getItems()) {
            writer.write(String.format("%s;%s;%s;%s;%s;%s;%s;%s",
                    buku.getKode_buku(),
                    buku.getKode_kategori(),
                    buku.getJudul(),
                    buku.getPengarang(),
                    buku.getPenerbit(),
                    buku.getTahun(),
                    buku.getEdisi(),
                    buku.getTahun_pengadaan() != null ? buku.getTahun_pengadaan().toString() : ""));
            writer.newLine();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukses");
        alert.setHeaderText(null);
        alert.setContentText("Data berhasil diekspor ke " + file.getAbsolutePath());
        alert.showAndWait();

    } catch (IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Gagal");
        alert.setHeaderText("Terjadi kesalahan saat menyimpan file.");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    }
    }
        
    @FXML
    private void handleLogoutAdmin(ActionEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logout");
    alert.setHeaderText("Yakin ingin logout?");
    alert.setContentText("Semua sesi akan dihapus.");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        Session session = Session.getInstance();
        session.setUsername(null);
        session.setPassword(null);
        session.setFullname(null);
        session.setRole(null);

        try {
            Main main = new Main();
            main.changeScene("/session/View/Main.fxml");
            showInfo("Logout", "Berhasil logout!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Logout", "Gagal Logout!");
        }
    }
    }
    
    private void showInfo(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
    }
    
    private Map<String, String> kategoriMap = new HashMap<>();
    
}
