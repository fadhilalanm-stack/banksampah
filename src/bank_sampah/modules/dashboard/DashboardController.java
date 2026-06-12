package bank_sampah.modules.dashboard;

public class DashboardController {
    private final DashboardDAO dao = new DashboardDAO();

    public int getTotalNasabah() { return dao.countNasabah(); }
    public double getTotalSampahHariIni() { return dao.totalSampahHariIni(); }
    public int getTotalTransaksi() { return dao.countTransaksi(); }
    public int getTotalPoin() { return dao.totalPoin(); }
}
