package bank_sampah.modules.laporan; // Menentukan package tempat class ini berada

import bank_sampah.database.DBConnection; // Mengimpor kelas untuk koneksi database
import javafx.collections.FXCollections; // Mengimpor kelas untuk membuat ObservableList
import javafx.collections.ObservableList; // Mengimpor tipe data ObservableList
import java.sql.Connection; // Mengimpor kelas Connection untuk koneksi database
import java.sql.PreparedStatement; // Mengimpor PreparedStatement untuk menjalankan query SQL
import java.sql.ResultSet; // Mengimpor ResultSet untuk menampung hasil query

// Class DAO (Data Access Object) yang bertugas mengambil dan mengelola data laporan dari database
public class LaporanDAO {

    // Method untuk mengambil seluruh data laporan transaksi
    public ObservableList<Laporan> getLaporan() {

        // Membuat list kosong yang nantinya berisi objek Laporan
        ObservableList<Laporan> list =
                FXCollections.observableArrayList();

        // Query SQL untuk mengambil data transaksi beserta nama nasabah
        String sql =
                "SELECT ts.id_transaksi, ts.tanggal, n.nama, " +
                "ts.berat, ts.poin " +
                "FROM transaksi_setor ts " +
                "JOIN nasabah n ON ts.id_nasabah=n.id_nasabah " +
                "ORDER BY ts.id_transaksi DESC";

        // Membuka koneksi database dan menjalankan query
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Mengulangi setiap data hasil query
            while (rs.next()) {

                // Menambahkan data ke dalam ObservableList
                list.add(new Laporan(
                        "#TX-" + rs.getInt("id_transaksi"), // ID transaksi dengan format #TX-
                        rs.getString("tanggal"),            // Mengambil tanggal transaksi
                        rs.getString("nama"),               // Mengambil nama nasabah
                        rs.getDouble("berat"),              // Mengambil berat sampah
                        rs.getInt("poin")                   // Mengambil poin yang diperoleh
                ));
            }

        } catch (Exception e) {
            // Menampilkan error jika terjadi kesalahan
            e.printStackTrace();
        }

        // Mengembalikan daftar laporan
        return list;
    }

    // Method untuk menghitung jumlah transaksi pada hari ini
    public int getTotalTransaksiHariIni() {

        // Query menghitung jumlah transaksi berdasarkan tanggal hari ini
        String sql =
                "SELECT COUNT(*) total " +
                "FROM transaksi_setor " +
                "WHERE DATE(tanggal)=CURDATE()";

        // Membuka koneksi dan menjalankan query
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Jika data ditemukan
            if (rs.next()) {
                // Mengembalikan jumlah transaksi
                return rs.getInt("total");
            }

        } catch (Exception e) {
            // Menampilkan pesan error
            e.printStackTrace();
        }

        // Jika gagal, mengembalikan nilai 0
        return 0;
    }

    // Method untuk menghitung total berat sampah yang telah disetor
    public double getTotalSampah() {

        // Query untuk menjumlahkan seluruh berat sampah
        String sql =
                "SELECT IFNULL(SUM(berat),0) total " +
                "FROM transaksi_setor";

        // Membuka koneksi database
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Jika data tersedia
            if (rs.next()) {
                // Mengembalikan total berat sampah
                return rs.getDouble("total");
            }

        } catch (Exception e) {
            // Menampilkan error jika terjadi masalah
            e.printStackTrace();
        }

        // Jika gagal, mengembalikan 0
        return 0;
    }

    // Method untuk menghitung jumlah seluruh nasabah
    public int getTotalNasabahAktif() {

        // Query menghitung jumlah data pada tabel nasabah
        String sql =
                "SELECT COUNT(*) total " +
                "FROM nasabah";

        // Membuka koneksi database
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Jika data ditemukan
            if (rs.next()) {
                // Mengembalikan jumlah nasabah
                return rs.getInt("total");
            }

        } catch (Exception e) {
            // Menampilkan pesan error
            e.printStackTrace();
        }

        // Jika gagal, mengembalikan nilai 0
        return 0;
    }

    // Method untuk menghapus transaksi berdasarkan ID transaksi
    public boolean hapusTransaksi(String idTransaksi) {

        // Menghapus teks "#TX-" agar hanya tersisa angka ID
        String id = idTransaksi.replace("#TX-", "");

        // Query SQL untuk menghapus data transaksi
        String sql =
                "DELETE FROM transaksi_setor " +
                "WHERE id_transaksi=?";

        // Membuka koneksi database
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Mengisi parameter query dengan ID transaksi
            ps.setInt(1, Integer.parseInt(id));

            // Menjalankan query DELETE
            // Mengembalikan true jika data berhasil dihapus
            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            // Menampilkan pesan error jika terjadi kesalahan
            e.printStackTrace();
        }

        // Mengembalikan false jika penghapusan gagal
        return false;
    }
}