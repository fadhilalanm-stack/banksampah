package bank_sampah.modules.nasabah;

public class Nasabah {

    private int idNasabah;
    private String kodeNasabah;
    private String nama;
    private String alamat;
    private String noHp;
    private int poin;
    private String status;

    public Nasabah(
            int idNasabah,
            String kodeNasabah,
            String nama,
            String alamat,
            String noHp,
            int poin,
            String status
    ) {

        this.idNasabah = idNasabah;
        this.kodeNasabah = kodeNasabah;
        this.nama = nama;
        this.alamat = alamat;
        this.noHp = noHp;
        this.poin = poin;
        this.status = status;
    }

    // ================= GETTER =================

    public int getIdNasabah() {
        return idNasabah;
    }

    public String getKodeNasabah() {
        return kodeNasabah;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNoHp() {
        return noHp;
    }

    public int getPoin() {
        return poin;
    }

    public String getStatus() {
        return status;
    }

    // ================= SETTER =================

    public void setIdNasabah(int idNasabah) {
        this.idNasabah = idNasabah;
    }

    public void setKodeNasabah(String kodeNasabah) {
        this.kodeNasabah = kodeNasabah;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public void setPoin(int poin) {
        this.poin = poin;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}