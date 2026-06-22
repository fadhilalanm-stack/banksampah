package bank_sampah.modules.transaksi_setor;

import bank_sampah.util.AlertUtil;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class TransaksiSetorController {

    private final TransaksiSetorDAO dao = new TransaksiSetorDAO();

    public ObservableList<TransaksiSetor> getData() {
        return dao.getAll();
    }

    public ObservableList<TransaksiSetor> getDataByTanggal(
            LocalDate mulai,
            LocalDate selesai
    ) {
        if (mulai == null || selesai == null) {
            AlertUtil.warning(
                    "Input Kosong",
                    "Tanggal mulai dan tanggal selesai harus diisi."
            );
            return getData();
        }

        if (selesai.isBefore(mulai)) {
            AlertUtil.warning(
                    "Input Salah",
                    "Tanggal selesai tidak boleh sebelum tanggal mulai."
            );
            return getData();
        }

        return dao.getByTanggal(mulai, selesai);
    }

    public int getHargaPerKg(String jenis) {
        if (jenis == null || jenis.isEmpty()) {
            return 3500;
        }

        switch (jenis) {
            case "Plastik PET":
                return 3500;
            case "Kertas":
                return 1200;
            case "Logam":
                return 7000;
            case "Kaca":
                return 1500;
            case "Kardus":
                return 2200;
            default:
                return 3500;
        }
    }

    public void simpan(
            double berat,
            String jenis,
            String tanggal
    ) {
        if (jenis == null || jenis.isEmpty()) {
            AlertUtil.warning(
                    "Input Kosong",
                    "Jenis sampah harus dipilih."
            );
            return;
        }

        if (tanggal == null || tanggal.isEmpty()) {
            AlertUtil.warning(
                    "Input Kosong",
                    "Tanggal transaksi harus diisi."
            );
            return;
        }

        if (berat <= 0) {
            AlertUtil.warning(
                    "Input Salah",
                    "Berat sampah harus lebih dari 0."
            );
            return;
        }

        try {
            int harga = getHargaPerKg(jenis);
            int total = (int) (berat * harga);
            int poin = (int) (berat * 10);

            dao.insert(
                    1,
                    1,
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
                    "Transaksi gagal disimpan: " + e.getMessage()
            );
        }
    }

    public void hapus(TransaksiSetor transaksi) {
        if (transaksi == null) {
            AlertUtil.warning(
                    "Peringatan",
                    "Pilih transaksi terlebih dahulu!"
            );
            return;
        }

        try {
            boolean sukses = dao.hapusTransaksi(
                    transaksi.getIdTransaksi()
            );

            if (sukses) {
                AlertUtil.info(
                        "Berhasil",
                        "Transaksi berhasil dihapus."
                );
            } else {
                AlertUtil.error(
                        "Gagal",
                        "Transaksi gagal dihapus."
                );
            }

        } catch (Exception e) {
            AlertUtil.error(
                    "Gagal",
                    "Data gagal dihapus: " + e.getMessage()
            );
        }
    }

    public void exportPdf() {
        AlertUtil.info(
                "Export PDF",
                "Fitur Export PDF siap diimplementasikan."
        );
    }
}