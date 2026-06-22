package bank_sampah.modules.laporan;

import bank_sampah.database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class LaporanDAO {

    public ObservableList<Laporan> getLaporan() {
        ObservableList<Laporan> list = FXCollections.observableArrayList();

        String sql =
                "SELECT ts.id_transaksi, ts.tanggal, n.nama, ts.berat, ts.poin " +
                "FROM transaksi_setor ts " +
                "JOIN nasabah n ON ts.id_nasabah = n.id_nasabah " +
                "ORDER BY ts.id_transaksi DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Laporan(
                        "#TX-" + rs.getInt("id_transaksi"),
                        rs.getString("tanggal"),
                        rs.getString("nama"),
                        rs.getDouble("berat"),
                        rs.getInt("poin")
                ));
            }

        } catch (Exception e) {
            list.add(new Laporan("#TX-1", "2026-06-19", "Ahmad Subarjo", 5.5, 550));
            list.add(new Laporan("#TX-2", "2026-06-19", "Siti Maryam", 3.0, 300));
        }

        return list;
    }

    public ObservableList<Laporan> getLaporanByTanggal(LocalDate mulai, LocalDate selesai) {
        ObservableList<Laporan> list = FXCollections.observableArrayList();

        String sql =
                "SELECT ts.id_transaksi, ts.tanggal, n.nama, ts.berat, ts.poin " +
                "FROM transaksi_setor ts " +
                "JOIN nasabah n ON ts.id_nasabah = n.id_nasabah " +
                "WHERE DATE(ts.tanggal) BETWEEN ? AND ? " +
                "ORDER BY ts.id_transaksi DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(mulai));
            ps.setDate(2, Date.valueOf(selesai));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Laporan(
                            "#TX-" + rs.getInt("id_transaksi"),
                            rs.getString("tanggal"),
                            rs.getString("nama"),
                            rs.getDouble("berat"),
                            rs.getInt("poin")
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getTotalTransaksiHariIni() {
        String sql =
                "SELECT COUNT(*) FROM transaksi_setor " +
                "WHERE DATE(tanggal) = CURDATE()";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            return getLaporan().size();
        }

        return 0;
    }

    public double getTotalSampah() {
        String sql = "SELECT IFNULL(SUM(berat), 0) FROM transaksi_setor";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            double total = 0;

            for (Laporan l : getLaporan()) {
                total += l.getBerat();
            }

            return total;
        }

        return 0;
    }

    public int getTotalNasabahAktif() {
        String sql =
                "SELECT COUNT(*) FROM nasabah " +
                "WHERE status = 'Aktif'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            return 0;
        }

        return 0;
    }

    public void delete(int idTransaksi) throws Exception {
        String sql = "DELETE FROM transaksi_setor WHERE id_transaksi = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTransaksi);
            ps.executeUpdate();
        }
    }

    public boolean hapusTransaksi(String idTransaksi) {
        try {
            String id = idTransaksi.replace("#TX-", "");
            delete(Integer.parseInt(id));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int countAll() {
        return getLaporan().size();
    }
}