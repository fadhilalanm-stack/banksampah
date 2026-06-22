package bank_sampah.modules.penukaran_poin;

import bank_sampah.util.AlertUtil;
import javafx.collections.ObservableList;

public class PenukaranPoinController {

    private final PenukaranPoinDAO dao = new PenukaranPoinDAO();

    public ObservableList<PenukaranPoin> getData() {
        return dao.getAll();
    }

    public void tukar(String poinText, String reward) {

        if (poinText == null || poinText.isEmpty()) {
            AlertUtil.warning(
                    "Input Kosong",
                    "Jumlah poin harus diisi."
            );
            return;
        }

        if (reward == null || reward.isEmpty()) {
            AlertUtil.warning(
                    "Input Kosong",
                    "Jenis penukaran harus dipilih."
            );
            return;
        }

        try {
            int poin = Integer.parseInt(poinText);

            if (poin < 1000) {
                AlertUtil.warning(
                        "Poin Tidak Cukup",
                        "Minimal penukaran adalah 1.000 poin."
                );
                return;
            }

            dao.insert(1, poin, reward);

            AlertUtil.info(
                    "Berhasil",
                    "Penukaran poin berhasil diproses."
            );

        } catch (NumberFormatException e) {

            AlertUtil.warning(
                    "Input Salah",
                    "Jumlah poin harus berupa angka."
            );

        } catch (Exception e) {

            AlertUtil.error(
                    "Gagal",
                    "Penukaran gagal disimpan: " + e.getMessage()
            );
        }
    }

    public void hapus(PenukaranPoin penukaran) {

        if (penukaran == null) {

            AlertUtil.warning(
                    "Peringatan",
                    "Pilih data penukaran terlebih dahulu!"
            );

            return;
        }

        try {

            boolean sukses =
                    dao.hapusPenukaran(
                            penukaran.getIdPenukaran()
                    );

            if (sukses) {

                AlertUtil.info(
                        "Berhasil",
                        "Data penukaran berhasil dihapus."
                );

            } else {

                AlertUtil.error(
                        "Gagal",
                        "Data penukaran gagal dihapus."
                );
            }

        } catch (Exception e) {

            AlertUtil.error(
                    "Gagal",
                    "Data gagal dihapus: " + e.getMessage()
            );
        }
    }

    public int totalPenukaran() {
        return dao.countAll();
    }
}