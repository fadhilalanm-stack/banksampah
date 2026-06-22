package bank_sampah.modules.transaksi_setor;

import bank_sampah.database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class TransaksiSetorDAO {

    public ObservableList<TransaksiSetor> getAll() {
        ObservableList<TransaksiSetor> list =
                FXCollections.observableArrayList();

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
            list.add(new TransaksiSetor(
                    1,
                    "2026-06-05",
                    "Ahmad Subarjo",
                    "Plastik PET",
                    5.2,
                    18200,
                    52,
                    "Berhasil"
            ));
        }

        return list;
    }

    public ObservableList<TransaksiSetor> getByTanggal(
            LocalDate mulai,
            LocalDate selesai
    ) {
        ObservableList<TransaksiSetor> list =
                FXCollections.observableArrayList();

        String sql =
                "SELECT ts.id_transaksi, ts.tanggal, n.nama, js.nama_sampah, " +
                "ts.berat, ts.total_harga, ts.poin, ts.status " +
                "FROM transaksi_setor ts " +
                "JOIN nasabah n ON ts.id_nasabah = n.id_nasabah " +
                "JOIN jenis_sampah js ON ts.id_sampah = js.id_sampah " +
                "WHERE DATE(ts.tanggal) BETWEEN ? AND ? " +
                "ORDER BY ts.id_transaksi DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(mulai));
            ps.setDate(2, Date.valueOf(selesai));

            try (ResultSet rs = ps.executeQuery()) {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insert(
            int idNasabah,
            int idSampah,
            double berat,
            int totalHarga,
            int poin,
            String tanggal
    ) throws Exception {

        String sql =
                "INSERT INTO transaksi_setor " +
                "(id_nasabah, id_sampah, berat, total_harga, poin, tanggal, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'Berhasil')";

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

    public void delete(int idTransaksi) throws Exception {
        String sql =
                "DELETE FROM transaksi_setor " +
                "WHERE id_transaksi = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTransaksi);
            ps.executeUpdate();
        }
    }

    public boolean hapusTransaksi(int idTransaksi) {
        try {
            delete(idTransaksi);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int countAll() {
        return getAll().size();
    }
}