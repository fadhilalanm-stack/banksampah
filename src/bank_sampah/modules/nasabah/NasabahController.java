package bank_sampah.modules.nasabah;

import java.sql.Connection;
import java.sql.PreparedStatement;

import bank_sampah.database.DBConnection;
import bank_sampah.util.AlertUtil;
import javafx.collections.ObservableList;

public class NasabahController {
    private final NasabahDAO dao = new NasabahDAO();

    public ObservableList<Nasabah> getData() {
        return dao.getAll();
    }

    public int totalNasabah() {
        return dao.countAll();
    }

    public void tambahNasabah(String nama, String alamat, String noHp) {
        if (nama.isEmpty() || alamat.isEmpty() || noHp.isEmpty()) {
            AlertUtil.warning(
                    "Input Kosong",
                    "Nama, alamat, dan nomor HP harus diisi."
            );
            return;
        }

        try {
            dao.insert(nama, alamat, noHp);
            AlertUtil.info(
                    "Berhasil",
                    "Data nasabah berhasil disimpan."
            );
        } catch (Exception e) {
            AlertUtil.error(
                    "Gagal",
                    "Data nasabah gagal disimpan: " + e.getMessage()
            );
        }
    }

    public void hapus(Nasabah nasabah) {
        String sql = "DELETE FROM nasabah WHERE kode_nasabah = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nasabah.getKodeNasabah());
            ps.executeUpdate();

            AlertUtil.info(
                    "Berhasil",
                    "Data nasabah berhasil dihapus."
            );

        } catch (Exception e) {
            AlertUtil.error(
                    "Gagal",
                    "Data nasabah gagal dihapus: " + e.getMessage()
            );
        }
    }

    public void update(Nasabah nasabah, String nama, String alamat, String noHp) {
        if (nama.isEmpty() || alamat.isEmpty() || noHp.isEmpty()) {
            AlertUtil.warning(
                    "Input Kosong",
                    "Nama, alamat, dan nomor HP harus diisi."
            );
            return;
        }

        String sql = "UPDATE nasabah SET nama = ?, alamat = ?, no_hp = ? WHERE kode_nasabah = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nama);
            ps.setString(2, alamat);
            ps.setString(3, noHp);
            ps.setString(4, nasabah.getKodeNasabah());

            ps.executeUpdate();

            AlertUtil.info(
                    "Berhasil",
                    "Data nasabah berhasil diperbarui."
            );

        } catch (Exception e) {
            AlertUtil.error(
                    "Gagal",
                    "Data nasabah gagal diperbarui: " + e.getMessage()
            );
        }
    }

    public int totalAktif() {
        int total = 0;

        for (Nasabah n : getData()) {
            if (n.getStatus() != null &&
                    n.getStatus().equalsIgnoreCase("Aktif")) {
                total++;
            }
        }

        return total;
    }
}