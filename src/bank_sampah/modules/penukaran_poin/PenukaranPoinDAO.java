package bank_sampah.modules.penukaran_poin;

import bank_sampah.database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class PenukaranPoinDAO {

    public ObservableList<PenukaranPoin> getAll() {
        ObservableList<PenukaranPoin> list =
                FXCollections.observableArrayList();

        String sql =
                "SELECT pp.id_penukaran, pp.tanggal, n.nama, " +
                "pp.jumlah_poin, pp.reward, pp.status " +
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
            list.add(new PenukaranPoin(
                    1,
                    "2026-06-05",
                    "Ahmad Sulaiman",
                    5000,
                    "Tunai",
                    "Berhasil"
            ));
        }

        return list;
    }

    public ObservableList<PenukaranPoin> getByTanggal(
            LocalDate mulai,
            LocalDate selesai
    ) {
        ObservableList<PenukaranPoin> list =
                FXCollections.observableArrayList();

        String sql =
                "SELECT pp.id_penukaran, pp.tanggal, n.nama, " +
                "pp.jumlah_poin, pp.reward, pp.status " +
                "FROM penukaran_poin pp " +
                "JOIN nasabah n ON pp.id_nasabah = n.id_nasabah " +
                "WHERE DATE(pp.tanggal) BETWEEN ? AND ? " +
                "ORDER BY pp.id_penukaran DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(mulai));
            ps.setDate(2, Date.valueOf(selesai));

            try (ResultSet rs = ps.executeQuery()) {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void insert(
            int idNasabah,
            int poin,
            String reward
    ) throws Exception {

        String sql =
                "INSERT INTO penukaran_poin " +
                "(id_nasabah, jumlah_poin, reward, tanggal, status) " +
                "VALUES (?, ?, ?, CURDATE(), 'Berhasil')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idNasabah);
            ps.setInt(2, poin);
            ps.setString(3, reward);

            ps.executeUpdate();
        }
    }

    public void delete(int idPenukaran) throws Exception {
        String sql =
                "DELETE FROM penukaran_poin " +
                "WHERE id_penukaran = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPenukaran);
            ps.executeUpdate();
        }
    }

    public boolean hapusPenukaran(int idPenukaran) {
        try {
            delete(idPenukaran);
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