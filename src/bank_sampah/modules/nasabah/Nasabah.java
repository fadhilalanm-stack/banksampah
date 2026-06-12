package bank_sampah.modules.nasabah;

public class Nasabah {
    private int idNasabah;
    private String kodeNasabah;
    private String nama;
    private String alamat;
    private String noHp;
    private int poin;
    private String status;

    public Nasabah(int idNasabah, String kodeNasabah, String nama, String alamat, String noHp, int poin, String status) {
        this.idNasabah = idNasabah;
        this.kodeNasabah = kodeNasabah;
        this.nama = nama;
        this.alamat = alamat;
        this.noHp = noHp;
        this.poin = poin;
        this.status = status;
    }

    public int getIdNasabah() { return idNasabah; }
    public String getKodeNasabah() { return kodeNasabah; }
    public String getNama() { return nama; }
    public String getAlamat() { return alamat; }
    public String getNoHp() { return noHp; }
    public int getPoin() { return poin; }
    public String getStatus() { return status; }
}
