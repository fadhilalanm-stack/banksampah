package bank_sampah.modules.laporan;

import bank_sampah.database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LaporanDAO {

    public ObservableList<Laporan> getLaporan() {

        ObservableList<Laporan> list =
                FXCollections.observableArrayList();

        String sql =
                "SELECT ts.id_transaksi, ts.tanggal, n.nama, " +
                "ts.berat, ts.poin " +
                "FROM transaksi_setor ts " +
                "JOIN nasabah n ON ts.id_nasabah=n.id_nasabah " +
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
            e.printStackTrace();
        }

        return list;
    }

    public int getTotalTransaksiHariIni() {

        String sql =
                "SELECT COUNT(*) total " +
                "FROM transaksi_setor " +
                "WHERE DATE(tanggal)=CURDATE()";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getTotalSampah() {

        String sql =
                "SELECT IFNULL(SUM(berat),0) total " +
                "FROM transaksi_setor";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTotalNasabahAktif() {

        String sql =
                "SELECT COUNT(DISTINCT id_nasabah) total " +
                "FROM transaksi_setor";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}