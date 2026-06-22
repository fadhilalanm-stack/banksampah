package bank_sampah.modules.register;

import bank_sampah.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegisterDAO {

    public void register(
            String nama,
            String username,
            String email,
            String password,
            String role
    ) throws Exception {

        String cekSql =
                "SELECT id_user FROM users WHERE username = ?";

        String insertSql =
                "INSERT INTO users (nama, username, email, password, role) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement cek = conn.prepareStatement(cekSql)) {
                cek.setString(1, username);

                ResultSet rs = cek.executeQuery();

                if (rs.next()) {
                    throw new Exception(
                            "Username '" + username + "' sudah digunakan."
                    );
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, nama);
                ps.setString(2, username);
                ps.setString(3, email);
                ps.setString(4, password);
                ps.setString(5, role);

                ps.executeUpdate();
            }
        }
    }

    public boolean usernameExists(String username) {
        String sql =
                "SELECT id_user FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            return false;
        }
    }

    public static class UserRegister {
        public final String nama;
        public final String username;
        public final String email;
        public final String role;

        public UserRegister(
                String nama,
                String username,
                String email,
                String role
        ) {
            this.nama = nama;
            this.username = username;
            this.email = email;
            this.role = role;
        }
    }
}