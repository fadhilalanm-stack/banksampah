package bank_sampah.modules.transaksi_setor;

import bank_sampah.database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class TransaksiSetorDAO {

    public ObservableList<TransaksiSetor> getAll() {
        ObservableList<TransaksiSetor> list = FXCollections.observableArrayList();

        String sql =
                "SELECT ts.id_transaksi, ts.tanggal, n.nama, js.nama_sampah, " +
                "ts.berat, ts.total_harga, ts.poin, ts.status " +
                "FROM transaksi_setor ts " +
                "JOIN nasabah n ON ts.id_nasabah = n.id_nasabah " +
                "JOIN jenis_sampah js ON ts.id_sampah = js.id_sampah " +
                "ORDER BY ts.id_transaksi DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new TransaksiSetor(
                        rs.getInt("id_transaksi"),
                        rs.getString("tanggal"),
                        rs.getString("nama"),
                        rs.getString("nama_sampah"),
                        rs.getDouble("berat"),
                        rs.getInt("total_harga"),
                        rs.getInt("poin"),
                        rs.getString("status")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

   public ObservableList<String> getJenisSampahList() {

    ObservableList<String> list =
            FXCollections.observableArrayList();

    String sql =
            "SELECT nama_sampah " +
            "FROM jenis_sampah " +
            "ORDER BY nama_sampah";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(rs.getString("nama_sampah"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    public ObservableList<String> getNasabahList() {

    ObservableList<String> list =
            FXCollections.observableArrayList();

    String sql = "SELECT nama FROM nasabah ORDER BY nama";

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
            "SELECT id_nasabah " +
            "FROM nasabah " +
            "WHERE nama = ?";

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

    public int getIdSampah(String namaSampah) throws Exception {

        String sql = "SELECT id_sampah FROM jenis_sampah WHERE nama_sampah = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, namaSampah);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id_sampah");
            }
        }

        throw new Exception("Jenis sampah tidak ditemukan");
    }
    public int getHargaSampah(String namaSampah) {

    String sql =
            "SELECT harga_per_kg " +
            "FROM jenis_sampah " +
            "WHERE nama_sampah = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, namaSampah);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("harga_per_kg");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}

    public void insert(int idNasabah,
                       int idSampah,
                       double berat,
                       int totalHarga,
                       int poin,
                       String tanggal) throws Exception {

        String sql =
                "INSERT INTO transaksi_setor " +
                "(id_nasabah,id_sampah,berat,total_harga,poin,tanggal,status) " +
                "VALUES(?,?,?,?,?,?, 'Berhasil')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNasabah);
            ps.setInt(2, idSampah);
            ps.setDouble(3, berat);
            ps.setInt(4, totalHarga);
            ps.setInt(5, poin);
            ps.setString(6, tanggal);

            ps.executeUpdate();
        }
    }
}