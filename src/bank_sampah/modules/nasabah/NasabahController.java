package bank_sampah.modules.nasabah;

import bank_sampah.util.AlertUtil;
import javafx.collections.ObservableList;

public class NasabahController {
    private final NasabahDAO dao = new NasabahDAO();

    public ObservableList<Nasabah> getData() {
        return dao.getAll();
    }

    public void tambahNasabah(String nama, String alamat, String noHp) {
        if (nama.isEmpty() || alamat.isEmpty() || noHp.isEmpty()) {
            AlertUtil.warning("Input Kosong", "Nama, alamat, dan nomor HP harus diisi.");
            return;
        }

        try {
            dao.insert(nama, alamat, noHp);
            AlertUtil.info("Berhasil", "Data nasabah berhasil disimpan.");
        } catch (Exception e) {
            AlertUtil.error("Gagal", "Data nasabah gagal disimpan: " + e.getMessage());
        }
    }

    public int totalNasabah() { return dao.countAll(); }
}
