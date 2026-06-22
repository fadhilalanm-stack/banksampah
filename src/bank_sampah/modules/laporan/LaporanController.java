package bank_sampah.modules.laporan;

import bank_sampah.util.AlertUtil;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class LaporanController {

    private final LaporanDAO dao = new LaporanDAO();

    public ObservableList<Laporan> getData() {
        return dao.getLaporan();
    }

    public ObservableList<Laporan> getDataByTanggal(LocalDate mulai, LocalDate selesai) {
        if (mulai == null || selesai == null) {
            AlertUtil.warning("Input Kosong", "Tanggal mulai dan tanggal selesai harus diisi.");
            return getData();
        }

        if (selesai.isBefore(mulai)) {
            AlertUtil.warning("Input Salah", "Tanggal selesai tidak boleh sebelum tanggal mulai.");
            return getData();
        }

        return dao.getLaporanByTanggal(mulai, selesai);
    }

    public int getTotalTransaksiHariIni() {
        return dao.getTotalTransaksiHariIni();
    }

    public double getTotalSampah() {
        return dao.getTotalSampah();
    }

    public int getTotalNasabahAktif() {
        return dao.getTotalNasabahAktif();
    }

    public void generate(LocalDate mulai, LocalDate selesai) {
        if (mulai == null || selesai == null) {
            AlertUtil.warning("Input Kosong", "Tanggal mulai dan tanggal selesai harus diisi.");
            return;
        }

        String laporan =
                "Periode Laporan : "
                        + mulai
                        + " sampai "
                        + selesai
                        + "\n\nTotal Transaksi Hari Ini : "
                        + getTotalTransaksiHariIni()
                        + "\n\nTotal Sampah Terkumpul : "
                        + getTotalSampah()
                        + " Kg"
                        + "\n\nTotal Nasabah Aktif : "
                        + getTotalNasabahAktif();

        AlertUtil.info("Generate Laporan", laporan);
    }

    public void exportPdf() {
        AlertUtil.info(
                "Export PDF",
                "Fitur Export PDF siap diimplementasikan."
        );
    }

    public void hapus(Laporan laporan) {
        if (laporan == null) {
            AlertUtil.warning("Peringatan", "Pilih transaksi terlebih dahulu!");
            return;
        }

        try {
            boolean sukses = dao.hapusTransaksi(laporan.getIdTransaksi());

            if (sukses) {
                AlertUtil.info("Berhasil", "Transaksi berhasil dihapus.");
            } else {
                AlertUtil.error("Gagal", "Transaksi gagal dihapus.");
            }

        } catch (Exception e) {
            AlertUtil.error("Gagal", "Data gagal dihapus: " + e.getMessage());
        }
    }
}