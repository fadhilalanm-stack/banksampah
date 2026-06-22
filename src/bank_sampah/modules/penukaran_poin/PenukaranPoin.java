package bank_sampah.modules.penukaran_poin;

public class PenukaranPoin {

    private int idPenukaran;
    private String tanggal;
    private String namaNasabah;
    private int jumlahPoin;
    private String reward;
    private String status;

    public PenukaranPoin(
            int idPenukaran,
            String tanggal,
            String namaNasabah,
            int jumlahPoin,
            String reward,
            String status
    ) {
        this.idPenukaran = idPenukaran;
        this.tanggal = tanggal;
        this.namaNasabah = namaNasabah;
        this.jumlahPoin = jumlahPoin;
        this.reward = reward;
        this.status = status;
    }

    public int getIdPenukaran() {
        return idPenukaran;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getNamaNasabah() {
        return namaNasabah;
    }

    public int getJumlahPoin() {
        return jumlahPoin;
    }

    public String getReward() {
        return reward;
    }

    public String getStatus() {
        return status;
    }

    public void setIdPenukaran(int idPenukaran) {
        this.idPenukaran = idPenukaran;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setNamaNasabah(String namaNasabah) {
        this.namaNasabah = namaNasabah;
    }

    public void setJumlahPoin(int jumlahPoin) {
        this.jumlahPoin = jumlahPoin;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}