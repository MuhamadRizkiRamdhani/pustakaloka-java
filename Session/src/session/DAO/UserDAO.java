/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package session.DAO;
import session.Model.User;
import java.sql.*;

/**
 *
 * @author Lenovo
 */
public class UserDAO {
    public static User getAccount(String username, String password) {
        String query = "SELECT * FROM users " +
                       "WHERE username = ? AND password = md5(?)";
        try {
            // Membuat koneksi ke database
            Connection koneksi = DBConnection.getConnection();

            // Membuat prepared statement
            PreparedStatement pstmt = koneksi.prepareStatement(query);

            // mengisi parameter pertama dengan inputan username
            pstmt.setString(1, username);

            // mengisi parameter kedua dengan inputan password
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("fullname"),
                    rs.getString("role")
                );
            }

            // Menutup koneksi
            rs.close();
            pstmt.close();
            koneksi.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Jika user tidak ditemukan
    }
    
    public static boolean registerUser(String username, String fullname, String password) {
    String query = "INSERT INTO users (username, fullname, password, role) " +
                   "VALUES (?, ?, MD5(?), 'user')";

    try {
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);

        stmt.setString(1, username);
        stmt.setString(2, fullname);
        stmt.setString(3, password); 

        int rowsInserted = stmt.executeUpdate();

        stmt.close();
        conn.close();

        return rowsInserted > 0;

    } catch (SQLException e) {
        System.out.println("Error saat register user: " + e.getMessage());
        return false;
    }
    }
}
