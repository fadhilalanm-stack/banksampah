package bank_sampah.modules.jenis_sampah;

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
}
