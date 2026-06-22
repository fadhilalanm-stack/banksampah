package bank_sampah.modules.penukaran_poin;

import bank_sampah.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PenukaranPoinView {
    private final PenukaranPoinController controller = new PenukaranPoinController();

    private Label totalTerimaLabel;
    private TextField jumlahPoinField;
    private TableView<PenukaranPoin> table;

    public StackPane getView() {
        
        StackPane wrapper = new StackPane();
        
        VBox content = new VBox(20);
        content.getStyleClass().add("poin-content");

        VBox formBox = createFormPenukaran();
        VBox riwayatBox = createRiwayatPenukaran();

        HBox.setHgrow(riwayatBox, Priority.ALWAYS);

        HBox mainSection = new HBox(
                20,
                formBox,
                riwayatBox
        );

        mainSection.getStyleClass().add("poin-main-section");

        content.getChildren().addAll(
                mainSection,
                createKetentuanBox()
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("content-scroll");

        wrapper.getChildren().add(scrollPane);

        return wrapper;
    }

    private VBox createFormPenukaran() {
        VBox box = new VBox(18);
        box.getStyleClass().add("poin-panel");
        box.setPrefWidth(400);

        Label title = new Label("Form Penukaran");
        title.getStyleClass().add("panel-title");

        ComboBox<String> nasabahCombo = new ComboBox<>();
        nasabahCombo.getItems().addAll(
                "Ahmad Sulaiman - NSB001",
                "Siti Aminah - NSB002",
                "Budi Santoso - NSB003"
        );
        nasabahCombo.setPromptText("Cari Nama atau ID Nasabah...");
        nasabahCombo.getStyleClass().add("poin-input");

        Label saldoValue = new Label("24,500 Poin");
        saldoValue.getStyleClass().add("card-value");

        ComboBox<String> rewardBox = new ComboBox<>();
        rewardBox.getItems().addAll("Tunai", "Hadiah Sembako", "Voucher");
        rewardBox.setValue("Tunai");
        rewardBox.getStyleClass().add("poin-input");

        jumlahPoinField = new TextField();
        jumlahPoinField.setPromptText("0");
        jumlahPoinField.getStyleClass().add("poin-input");
        jumlahPoinField.textProperty().addListener(
                (obs, oldValue, newValue) -> hitungKonversi()
        );

        totalTerimaLabel = new Label("Rp 0");
        totalTerimaLabel.getStyleClass().add("card-value");

        Button tukarBtn = new Button("✔  Tukar Poin Sekarang");
        tukarBtn.getStyleClass().add("green-button");
        tukarBtn.setMaxWidth(Double.MAX_VALUE);

        tukarBtn.setOnAction(e -> {
            controller.tukar(jumlahPoinField.getText(), rewardBox.getValue());
            table.setItems(controller.getData());
        });

        box.getChildren().addAll(
                title,
                label("Pilih Nasabah"),
                nasabahCombo,
                label("Saldo Poin Tersedia"),
                saldoValue,
                label("Jenis Penukaran"),
                rewardBox,
                label("Jumlah Poin Ditukar"),
                jumlahPoinField,
                label("Total Terima"),
                totalTerimaLabel,
                tukarBtn
        );

        return box;
    }
    
    private VBox createRiwayatPenukaran() {
        
        VBox box = new VBox(15);

        box.getStyleClass().addAll(
                "poin-panel",
                "poin-riwayat-panel"
        );

        box.setPadding(new Insets(20));

        box.setPrefWidth(1050);
        box.setMinWidth(950);

        Label title = new Label("Riwayat Penukaran");
        title.getStyleClass().add("panel-title");

        HBox actionBar = new HBox(10);
        actionBar.setAlignment(Pos.CENTER_LEFT);

        TextField search = new TextField();
        search.setPromptText("Cari penukaran...");
        search.getStyleClass().add("laporan-search-field");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button exportBtn = new Button("Export");
        exportBtn.getStyleClass().add("export-button");

        actionBar.getChildren().addAll(
                search,
                spacer,
                exportBtn
        );

        table = new TableView<>();
        table.getStyleClass().add("poin-table");

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );

        table.setPrefHeight(550);

        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<PenukaranPoin, String> idCol =
                new TableColumn<>("ID");

        idCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        "#TRX-" + data.getValue().getIdPenukaran()
                )
        );

        TableColumn<PenukaranPoin, String> tanggalCol =
                new TableColumn<>("Tanggal");

        tanggalCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getTanggal()
                )
        );

        TableColumn<PenukaranPoin, String> nasabahCol =
                new TableColumn<>("Nasabah");

        nasabahCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getNamaNasabah()
                )
        );

        TableColumn<PenukaranPoin, String> poinCol =
                new TableColumn<>("Poin");

        poinCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getJumlahPoin() + " Pts"
                )
        );

        TableColumn<PenukaranPoin, String> rewardCol =
                new TableColumn<>("Reward");

        rewardCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getReward()
                )
        );

        TableColumn<PenukaranPoin, String> statusCol =
                new TableColumn<>("Status");

        statusCol.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getStatus()
                )
        );

        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {

                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label badge = new Label(item);
                    badge.getStyleClass().add("status-active");
                    setGraphic(badge);
                }
            }
        });

        TableColumn<PenukaranPoin, Void> aksiCol =
                createAksiColumn();

        table.getColumns().setAll(
                idCol,
                tanggalCol,
                nasabahCol,
                poinCol,
                rewardCol,
                statusCol,
                aksiCol
        );

        table.setItems(controller.getData());

        search.textProperty().addListener(
                (obs, oldVal, keyword) -> {

                    String k = keyword.toLowerCase();

                    if (k.isEmpty()) {
                        table.setItems(controller.getData());
                    } else {
                        table.setItems(
                                FXCollections.observableArrayList(
                                        controller.getData().filtered(
                                                p ->
                                                        p.getNamaNasabah().toLowerCase().contains(k)
                                                                || p.getTanggal().toLowerCase().contains(k)
                                                                || p.getReward().toLowerCase().contains(k)
                                        )
                                )
                        );
                    }
                }
        );

        Label footer =
                new Label("© 2026 Sistem Informasi Bank Sampah");

        footer.getStyleClass().add("footer-text");

        box.getChildren().addAll(
                title,
                actionBar,
                table,
                footer
        );

        return box;
    }

    private TableColumn<PenukaranPoin, Void> createAksiColumn() {
        TableColumn<PenukaranPoin, Void> col = new TableColumn<>("Aksi");

        col.setCellFactory(param -> new TableCell<>() {

            private final Button btnHapus = new Button("Hapus");

            {
                btnHapus.getStyleClass().add("delete-button");

                btnHapus.setOnAction(e -> {
                    PenukaranPoin data = getTableView()
                            .getItems()
                            .get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi");
                    alert.setHeaderText(null);
                    alert.setContentText("Yakin ingin menghapus data ini?");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            controller.hapus(data);
                            table.setItems(controller.getData());
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox boxBtn = new HBox(btnHapus);
                    boxBtn.setAlignment(Pos.CENTER);
                    setGraphic(boxBtn);
                }
            }
        });

        return col;
    }

    private VBox createKetentuanBox() {
        VBox box = new VBox(5);
        box.getStyleClass().add("green-info-box");

        Label title = new Label("Ketentuan Penukaran");
        title.getStyleClass().add("white-bold");

        Label desc = new Label(
                "Minimal penukaran adalah 1,000 poin. Pastikan data nasabah sudah sesuai sebelum menekan tombol Tukar."
        );
        desc.getStyleClass().add("white-text");
        desc.setWrapText(true);

        box.getChildren().addAll(title, desc);

        return box;
    }

    private Label label(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("poin-label");
        return label;
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