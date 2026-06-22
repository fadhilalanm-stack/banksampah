package bank_sampah.modules.dashboard;

import bank_sampah.util.AlertUtil;

public class DashboardController {

    private final DashboardDAO dao = new DashboardDAO();

    public int getTotalNasabah() {
        return dao.countNasabah();
    }

    public double getTotalSampahHariIni() {
        return dao.totalSampahHariIni();
    }

    public int getTotalTransaksi() {
        return dao.countTransaksi();
    }

    public int getTotalPoin() {
        return dao.totalPoin();
    }

    public int getTotalTransaksiHariIni() {
        return dao.totalTransaksiHariIni();
    }

    public int getTotalNasabahAktif() {
        return dao.totalNasabahAktif();
    }

    public void generateDashboard() {
        String laporan =
                "Total Nasabah : "
                        + getTotalNasabah()
                        + "\n\nTotal Sampah Hari Ini : "
                        + getTotalSampahHariIni()
                        + " Kg"
                        + "\n\nTotal Transaksi : "
                        + getTotalTransaksi()
                        + "\n\nTotal Poin Beredar : "
                        + getTotalPoin()
                        + "\n\nTotal Nasabah Aktif : "
                        + getTotalNasabahAktif();

        AlertUtil.info(
                "Dashboard Ringkasan",
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