package bank_sampah.modules.nasabah;

import bank_sampah.database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class NasabahDAO {
    public ObservableList<Nasabah> getAll() {
        ObservableList<Nasabah> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM nasabah ORDER BY id_nasabah DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Nasabah(
                        rs.getInt("id_nasabah"),
                        rs.getString("kode_nasabah"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        rs.getString("no_hp"),
                        rs.getInt("poin"),
                        rs.getString("status")
                ));
            }
        } catch (Exception e) {
            list.add(new Nasabah(1, "NSB-001", "Ahmad Subarjo", "Jl. Merpati No. 12", "081234567890", 2600, "Aktif"));
            list.add(new Nasabah(2, "NSB-002", "Siti Maryam", "Jl. Kenanga No. 8", "082111112222", 4800, "Aktif"));
        }
        return list;
    }

    public void insert(String nama, String alamat, String noHp) throws Exception {
        String sql = "INSERT INTO nasabah(kode_nasabah, nama, alamat, no_hp, poin, status) VALUES(?,?,?,?,0,'Aktif')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String kode = "NSB-" + System.currentTimeMillis() % 10000;
            ps.setString(1, kode);
            ps.setString(2, nama);
            ps.setString(3, alamat);
            ps.setString(4, noHp);
            ps.executeUpdate();
        }
    }

    public int countAll() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM nasabah");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception ignored) {}
        return getAll().size();
    }
}
