package bank_sampah.modules.penukaran_poin;

import bank_sampah.database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class PenukaranPoinDAO {
    public ObservableList<String> getNasabahList() {

    ObservableList<String> list =
            FXCollections.observableArrayList();

    String sql =
            "SELECT nama FROM nasabah ORDER BY nama";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(rs.getString("nama"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

public int getIdNasabah(String namaNasabah) throws Exception {

    String sql =
            "SELECT id_nasabah FROM nasabah WHERE nama = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, namaNasabah);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("id_nasabah");
        }
    }

    throw new Exception("Nasabah tidak ditemukan");
}
public ObservableList<PenukaranPoin> getAll() {

    ObservableList<PenukaranPoin> list =
            FXCollections.observableArrayList();

    String sql =
            "SELECT pp.id_penukaran, " +
            "pp.tanggal, " +
            "n.nama, " +
            "pp.jumlah_poin, " +
            "pp.reward, " +
            "pp.status " +
            "FROM penukaran_poin pp " +
            "JOIN nasabah n ON pp.id_nasabah = n.id_nasabah " +
            "ORDER BY pp.id_penukaran DESC";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

            list.add(new PenukaranPoin(
                    rs.getInt("id_penukaran"),
                    rs.getString("tanggal"),
                    rs.getString("nama"),
                    rs.getInt("jumlah_poin"),
                    rs.getString("reward"),
                    rs.getString("status")
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
    public void insert(int idNasabah, int poin, String reward) throws Exception {
        String sql = "INSERT INTO penukaran_poin(id_nasabah,jumlah_poin,reward,tanggal,status) VALUES(?,?,?,CURDATE(),'Berhasil')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idNasabah);
            ps.setInt(2, poin);
            ps.setString(3, reward);
            ps.executeUpdate();
        }
    }
}
