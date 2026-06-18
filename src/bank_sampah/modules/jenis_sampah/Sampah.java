package bank_sampah.modules.jenis_sampah;

public class Sampah {

    private int idSampah;
    private String namaSampah;
    private String kategori;
    private int hargaPerKg;

    public Sampah(
            int idSampah,
            String namaSampah,
            String kategori,
            int hargaPerKg
    ) {

        this.idSampah = idSampah;
        this.namaSampah = namaSampah;
        this.kategori = kategori;
        this.hargaPerKg = hargaPerKg;
    }

    // ================= GETTER =================

    public int getIdSampah() {
        return idSampah;
    }

    public String getNamaSampah() {
        return namaSampah;
    }

    public String getKategori() {
        return kategori;
    }

    public int getHargaPerKg() {
        return hargaPerKg;
    }

    // ================= SETTER =================

    public void setIdSampah(int idSampah) {
        this.idSampah = idSampah;
    }

    public void setNamaSampah(String namaSampah) {
        this.namaSampah = namaSampah;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setHargaPerKg(int hargaPerKg) {
        this.hargaPerKg = hargaPerKg;
    }
}