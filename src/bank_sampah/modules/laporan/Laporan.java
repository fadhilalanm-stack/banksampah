package bank_sampah.modules.laporan;

public class Laporan {

    private String idTransaksi;
    private String tanggal;
    private String namaNasabah;
    private double berat;
    private int nilaiPoin;

    public Laporan(
            String idTransaksi,
            String tanggal,
            String namaNasabah,
            double berat,
            int nilaiPoin
    ) {
        this.idTransaksi = idTransaksi;
        this.tanggal = tanggal;
        this.namaNasabah = namaNasabah;
        this.berat = berat;
        this.nilaiPoin = nilaiPoin;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getNamaNasabah() {
        return namaNasabah;
    }

    public double getBerat() {
        return berat;
    }

    public int getNilaiPoin() {
        return nilaiPoin;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setNamaNasabah(String namaNasabah) {
        this.namaNasabah = namaNasabah;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public void setNilaiPoin(int nilaiPoin) {
        this.nilaiPoin = nilaiPoin;
    }
}