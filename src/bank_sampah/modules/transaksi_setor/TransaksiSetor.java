package bank_sampah.modules.transaksi_setor;

public class TransaksiSetor {

    private int idTransaksi;
    private String tanggal;
    private String namaNasabah;
    private String namaSampah;
    private double berat;
    private int totalHarga;
    private int poin;
    private String status;

    public TransaksiSetor(
            int idTransaksi,
            String tanggal,
            String namaNasabah,
            String namaSampah,
            double berat,
            int totalHarga,
            int poin,
            String status
    ) {
        this.idTransaksi = idTransaksi;
        this.tanggal = tanggal;
        this.namaNasabah = namaNasabah;
        this.namaSampah = namaSampah;
        this.berat = berat;
        this.totalHarga = totalHarga;
        this.poin = poin;
        this.status = status;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getNamaNasabah() {
        return namaNasabah;
    }

    public String getNamaSampah() {
        return namaSampah;
    }

    public double getBerat() {
        return berat;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public int getPoin() {
        return poin;
    }

    public String getStatus() {
        return status;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setNamaNasabah(String namaNasabah) {
        this.namaNasabah = namaNasabah;
    }

    public void setNamaSampah(String namaSampah) {
        this.namaSampah = namaSampah;
    }

    public void setBerat(double berat) {
        this.berat = berat;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

    public void setPoin(int poin) {
        this.poin = poin;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}