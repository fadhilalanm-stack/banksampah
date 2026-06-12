package bank_sampah.modules.transaksi_setor;

import bank_sampah.util.AlertUtil;
import javafx.collections.ObservableList;

public class TransaksiSetorController {

    private final TransaksiSetorDAO dao = new TransaksiSetorDAO();

    public ObservableList<TransaksiSetor> getData() {
        return dao.getAll();
    }

    public ObservableList<String> getNasabahList() {
        return dao.getNasabahList();
    }

    public ObservableList<String> getJenisSampahList() {
    return dao.getJenisSampahList();
}
    public int getHargaPerKg(String jenis) {

    if (jenis == null || jenis.isEmpty()) {
        return 0;
    }

    return dao.getHargaSampah(jenis);
}

    public void simpan(String namaNasabah,
                       double berat,
                       String jenis,
                       String tanggal) {

        if (namaNasabah == null || namaNasabah.isEmpty()) {
            AlertUtil.warning("Validasi", "Pilih nasabah terlebih dahulu.");
            return;
        }

        if (jenis == null || jenis.isEmpty()) {
            AlertUtil.warning("Validasi", "Pilih jenis sampah.");
            return;
        }

        if (berat <= 0) {
            AlertUtil.warning("Validasi", "Berat harus lebih dari 0.");
            return;
        }

        try {

            int harga = getHargaPerKg(jenis);
            int total = (int) (berat * harga);
            int poin = (int) (berat * 10);

int idNasabah = dao.getIdNasabah(namaNasabah);            int idSampah = dao.getIdSampah(jenis);

            dao.insert(
                    idNasabah,
                    idSampah,
                    berat,
                    total,
                    poin,
                    tanggal
            );

            AlertUtil.info(
                    "Berhasil",
                    "Transaksi setoran berhasil disimpan."
            );

        } catch (Exception e) {
            AlertUtil.error(
                    "Gagal",
                    e.getMessage()
            );
        }
    }
}