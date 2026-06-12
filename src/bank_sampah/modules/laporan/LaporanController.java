package bank_sampah.modules.laporan;

import bank_sampah.util.AlertUtil;
import javafx.collections.ObservableList;

public class LaporanController {

    private final LaporanDAO dao =
            new LaporanDAO();

    public ObservableList<Laporan> getData() {
        return dao.getLaporan();
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

    public void generate() {

        String laporan =
                "Total Transaksi Hari Ini : "
                        + getTotalTransaksiHariIni()

                        + "\n\nTotal Sampah Terkumpul : "
                        + getTotalSampah() + " Kg"

                        + "\n\nTotal Nasabah Aktif : "
                        + getTotalNasabahAktif();

        AlertUtil.info(
                "Generate Laporan",
                laporan
        );
    }

    public void exportPdf() {

        AlertUtil.info(
                "Export PDF",
                "Fitur Export PDF siap diimplementasikan."
        );
    }
}