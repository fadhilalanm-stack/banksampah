package bank_sampah.modules.jenis_sampah;

import bank_sampah.database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class JenisSampahDAO {
    public ObservableList<Sampah> getAll() {
        ObservableList<Sampah> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM jenis_sampah ORDER BY id_sampah ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Sampah(rs.getInt("id_sampah"), rs.getString("nama_sampah"), rs.getString("kategori"), rs.getInt("harga_per_kg")));
            }
        } catch (Exception e) {
            list.add(new Sampah(1, "Botol Plastik PET", "Plastik", 3500));
            list.add(new Sampah(2, "Kardus Bekas", "Kertas/Karton", 2200));
            list.add(new Sampah(3, "Besi Padat", "Logam", 5000));
        }
        return list;
    }

    public void insert(String nama, String kategori, int harga) throws Exception {
        String sql = "INSERT INTO jenis_sampah(nama_sampah,kategori,harga_per_kg) VALUES(?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nama);
            ps.setString(2, kategori);
            ps.setInt(3, harga);
            ps.executeUpdate();
        }
    }

    public void delete(int idSampah) throws Exception {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM jenis_sampah WHERE id_sampah=?")) {
            ps.setInt(1, idSampah);
            ps.executeUpdate();
        }
    }

    public int countAll() { return getAll().size(); }
}
