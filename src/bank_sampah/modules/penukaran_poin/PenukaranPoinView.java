package bank_sampah.modules.penukaran_poin;

import bank_sampah.util.AlertUtil;
import bank_sampah.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PenukaranPoinView {
    private final PenukaranPoinController controller = new PenukaranPoinController();
    private Label totalTerimaLabel;
    private TextField jumlahPoinField;
    private TableView<PenukaranPoin> table;

    public StackPane getView() {
        StackPane wrapper = new StackPane();
        VBox content = new VBox(18);
        content.getStyleClass().add("main-content");
        HBox mainSection = new HBox(20, createFormPenukaran(), createRiwayatPenukaran());

        content.getChildren().addAll(mainSection, createKetentuanBox());

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("content-scroll");
        wrapper.getChildren().add(scrollPane);
        return wrapper;
    }

    private VBox createFormPenukaran() {
        VBox box = new VBox();
        box.getStyleClass().addAll("panel", "form-penukaran");

        Label title = new Label("↔  Form Penukaran");
        title.getStyleClass().add("panel-title");

        ComboBox<String> nasabahCombo = new ComboBox<>();
        nasabahCombo.setItems(controller.getNasabahList());
        nasabahCombo.setPromptText("Pilih Nasabah...");

        Label saldoValue = new Label("24,500 Poin");
        saldoValue.getStyleClass().add("card-value");

        ComboBox<String> rewardBox = new ComboBox<>();
        rewardBox.getItems().addAll("Tunai", "Hadiah Sembako", "Voucher");
        rewardBox.setValue("Tunai");

        jumlahPoinField = new TextField();
        jumlahPoinField.setPromptText("0");
        jumlahPoinField.textProperty().addListener((obs, oldValue, newValue) -> hitungKonversi());

        totalTerimaLabel = new Label("Rp 0");
        totalTerimaLabel.getStyleClass().add("card-value");

        Button tukarBtn = new Button("✔  Tukar Poin Sekarang");
        tukarBtn.getStyleClass().add("green-button");
        tukarBtn.setMaxWidth(Double.MAX_VALUE);
        tukarBtn.setOnAction(e -> {
            try {
                controller.simpan(
                        nasabahCombo.getValue(),
                        Integer.parseInt(jumlahPoinField.getText()),
                        rewardBox.getValue()
                );
                table.setItems(controller.getData());
            } catch (NumberFormatException ex) {
                AlertUtil.warning("Input Salah", "Jumlah poin harus berupa angka.");
            }
        });

        box.getChildren().addAll(
                title,
                new Label("Pilih Nasabah"), nasabahCombo,
                new Label("Saldo Poin Tersedia"), saldoValue,
                new Label("Jenis Penukaran"), rewardBox,
                new Label("Jumlah Poin Ditukar"), jumlahPoinField,
                new Label("Total Terima"), totalTerimaLabel,
                tukarBtn
        );
        return box;
    }

    private VBox createRiwayatPenukaran() {
        VBox box = new VBox();
        box.getStyleClass().addAll("panel", "riwayat-penukaran");

        Label title = new Label("↻  Riwayat Penukaran");
        title.getStyleClass().add("panel-title");

        table = new TableView<>();
        table.setItems(controller.getData());

        TableColumn<PenukaranPoin, String> idCol = new TableColumn<>("ID/Tanggal");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(
                "#TRX-" + data.getValue().getIdPenukaran() + "\n" + data.getValue().getTanggal()));
        idCol.setPrefWidth(110);

        TableColumn<PenukaranPoin, String> nasabahCol = new TableColumn<>("Nasabah");
        nasabahCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaNasabah()));
        nasabahCol.setPrefWidth(150);

        TableColumn<PenukaranPoin, String> poinCol = new TableColumn<>("Poin");
        poinCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getJumlahPoin())));
        poinCol.setPrefWidth(90);

        TableColumn<PenukaranPoin, String> rewardCol = new TableColumn<>("Reward");
        rewardCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getReward()));
        rewardCol.setPrefWidth(110);

        TableColumn<PenukaranPoin, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        statusCol.setPrefWidth(90);

        table.getColumns().add(idCol);
        table.getColumns().add(nasabahCol);
        table.getColumns().add(poinCol);
        table.getColumns().add(rewardCol);
        table.getColumns().add(statusCol);

        box.getChildren().addAll(title, table);
        return box;
    }

    private VBox createKetentuanBox() {
        VBox box = new VBox();
        box.getStyleClass().add("green-info-box");

        Label title = new Label("Ketentuan Penukaran");
        title.getStyleClass().add("white-bold");

        Label desc = new Label("Minimal penukaran adalah 1,000 poin. Pastikan data nasabah sudah sesuai sebelum menekan tombol Tukar.");
        desc.getStyleClass().add("white-text");
        desc.setWrapText(true);

        box.getChildren().addAll(title, desc);
        return box;
    }

    private void hitungKonversi() {
        try {
            int poin = Integer.parseInt(jumlahPoinField.getText());
            totalTerimaLabel.setText(FormatUtil.rupiah(poin * 10));
        } catch (Exception e) {
            totalTerimaLabel.setText("Rp 0");
        }
    }
}
