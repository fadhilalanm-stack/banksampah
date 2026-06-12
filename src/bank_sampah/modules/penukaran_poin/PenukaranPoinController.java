package bank_sampah.modules.penukaran_poin;

import bank_sampah.util.AlertUtil;
import javafx.collections.ObservableList;

public class PenukaranPoinController {
    private final PenukaranPoinDAO dao = new PenukaranPoinDAO();
    
public ObservableList<PenukaranPoin> getData() {
    return dao.getAll();
}
    public ObservableList<String> getNasabahList() {
    return dao.getNasabahList();
}
    public void simpan(String namaNasabah,
                   int poin,
                   String reward) {

    try {

        int idNasabah =
                dao.getIdNasabah(namaNasabah);

        dao.insert(
                idNasabah,
                poin,
                reward
        );

        AlertUtil.info(
                "Berhasil",
                "Penukaran poin berhasil disimpan."
        );

    } catch (Exception e) {

        AlertUtil.error(
                "Gagal",
                e.getMessage()
        );
    }
}
}