package bank_sampah.modules.jenis_sampah;

import java.sql.Connection;
import java.sql.PreparedStatement;

import bank_sampah.database.DBConnection;
import bank_sampah.util.AlertUtil;
import javafx.collections.ObservableList;

public class JenisSampahController {
    private final JenisSampahDAO dao = new JenisSampahDAO();

    public ObservableList<Sampah> getData() { return dao.getAll(); }
    public int totalKategori() { return dao.countAll(); }

    public void tambah(String nama, String kategori, String hargaText) {
        if (nama.isEmpty() || kategori == null || hargaText.isEmpty()) {
            AlertUtil.warning("Input Kosong", "Semua data harus diisi.");
            return;
        }
        try {
            dao.insert(nama, kategori, Integer.parseInt(hargaText));
            AlertUtil.info("Berhasil", "Jenis sampah berhasil ditambahkan.");
        } catch (NumberFormatException e) {
            AlertUtil.warning("Input Salah", "Harga harus berupa angka.");
        } catch (Exception e) {
            AlertUtil.error("Gagal", "Data gagal disimpan: " + e.getMessage());
        }
    }

    public double rataRataHarga() {
        double total = 0;
        int jumlah = getData().size();
        
        if (jumlah == 0) {
            return 0;
        }
        
        for (Sampah s : getData()) {
            total += s.getHargaPerKg();
        }
        
        return total / jumlah;
    }

    public void hapus(Sampah sampah) {
        String sql = "DELETE FROM jenis_sampah WHERE id_sampah = ?";
        
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, sampah.getIdSampah());
            ps.executeUpdate();
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Sampah sampah, String nama, String kategori, String harga) {
        String sql = "UPDATE jenis_sampah SET nama_sampah = ?, kategori = ?, harga_per_kg = ? WHERE id_sampah = ?";
        
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nama);
            ps.setString(2, kategori);
            ps.setDouble(3, Double.parseDouble(harga));
            ps.setInt(4, sampah.getIdSampah());
            
            ps.executeUpdate();
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}