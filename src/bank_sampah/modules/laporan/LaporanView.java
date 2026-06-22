package bank_sampah.modules.laporan;

import bank_sampah.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LaporanView {

    private final LaporanController controller = new LaporanController();

    private TableView<Laporan> table;
    private DatePicker mulaiPicker;
    private DatePicker selesaiPicker;

    public ScrollPane getView() {
        VBox content = new VBox(25);
        content.getStyleClass().add("laporan-content");

        HBox titleRow = new HBox();
        titleRow.getStyleClass().add("laporan-title-row");
        titleRow.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);

        Label title = new Label("Laporan");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Kelola dan pantau seluruh riwayat transaksi bank sampah.");
        subtitle.getStyleClass().add("page-subtitle");

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleRow.getChildren().addAll(titleBox, spacer);

        content.getChildren().addAll(
                titleRow,
                createFilterBox(),
                createSummarySection(),
                createTransactionTable()
        );

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);
        sp.getStyleClass().add("content-scroll");

        return sp;
    }

    private VBox createFilterBox() {
        VBox box = new VBox(15);
        box.getStyleClass().add("laporan-table-panel");

        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);

        mulaiPicker = new DatePicker();
        selesaiPicker = new DatePicker();

        mulaiPicker.getStyleClass().add("laporan-date-picker");
        selesaiPicker.getStyleClass().add("laporan-date-picker");

        Button btnGenerate = new Button("Generate Laporan");
        btnGenerate.getStyleClass().add("green-button");

        btnGenerate.setOnAction(e -> {
            table.setItems(controller.getDataByTanggal(
                    mulaiPicker.getValue(),
                    selesaiPicker.getValue()
            ));
            controller.generate(
                    mulaiPicker.getValue(),
                    selesaiPicker.getValue()
            );
        });

        Button btnReset = new Button("Reset");
        btnReset.getStyleClass().add("export-button");

        btnReset.setOnAction(e -> {
            mulaiPicker.setValue(null);
            selesaiPicker.setValue(null);
            table.setItems(controller.getData());
        });

        row.getChildren().addAll(
                new Label("Mulai"),
                mulaiPicker,
                new Label("Selesai"),
                selesaiPicker,
                btnGenerate,
                btnReset
        );

        box.getChildren().add(row);
        return box;
    }

    private HBox createSummarySection() {
        HBox cards = new HBox(20);
        cards.getStyleClass().add("laporan-card-row");

        cards.getChildren().addAll(
                createCard(
                        "Total Transaksi Hari Ini",
                        String.valueOf(controller.getTotalTransaksiHariIni()),
                        "Transaksi berhasil"
                ),
                createCard(
                        "Total Sampah Terkumpul",
                        FormatUtil.kg(controller.getTotalSampah()),
                        "Akumulasi seluruh setoran"
                ),
                createCard(
                        "Nasabah Aktif",
                        String.valueOf(controller.getTotalNasabahAktif()),
                        "Nasabah yang bertransaksi"
                )
        );

        return cards;
    }

    private VBox createCard(String title, String value, String desc) {
        VBox card = new VBox(10);
        card.getStyleClass().add("summary-card");

        Label t = new Label(title.toUpperCase());
        t.getStyleClass().add("card-title");

        Label v = new Label(value);
        v.getStyleClass().add("card-value");

        Label d = new Label(desc);
        d.getStyleClass().add("small-muted");

        card.getChildren().addAll(t, v, d);

        return card;
    }

    private TableColumn<Laporan, String> createColumn(
            String title,
            java.util.function.Function<Laporan, String> mapper
    ) {
        TableColumn<Laporan, String> col = new TableColumn<>(title);
        col.setCellValueFactory(data ->
                new SimpleStringProperty(mapper.apply(data.getValue()))
        );
        return col;
    }

    private VBox createTransactionTable() {
        VBox box = new VBox(15);
        box.getStyleClass().add("laporan-table-panel");

        HBox actionBar = new HBox(10);
        actionBar.setAlignment(Pos.CENTER_LEFT);

        TextField search = new TextField();
        search.setPromptText("Cari transaksi...");
        search.getStyleClass().add("laporan-search-field");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button exportBtn = new Button("Export");
        exportBtn.getStyleClass().add("export-button");
        exportBtn.setOnAction(e -> controller.exportPdf());

        actionBar.getChildren().addAll(search, spacer, exportBtn);

        table = new TableView<>();
        table.getStyleClass().add("laporan-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Laporan, String> idCol =
                createColumn("ID", Laporan::getIdTransaksi);

        TableColumn<Laporan, String> tanggalCol =
                createColumn("Tanggal", Laporan::getTanggal);

        TableColumn<Laporan, String> nasabahCol =
                createColumn("Nasabah", Laporan::getNamaNasabah);

        TableColumn<Laporan, String> beratCol =
                createColumn("Berat", l -> FormatUtil.kg(l.getBerat()));

        TableColumn<Laporan, String> poinCol =
                createColumn("Poin", l -> l.getNilaiPoin() + " Pts");

        TableColumn<Laporan, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty("Selesai"));

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

        TableColumn<Laporan, Void> actionCol = createAksiColumn();

        table.getColumns().setAll(
                idCol,
                tanggalCol,
                nasabahCol,
                beratCol,
                poinCol,
                statusCol,
                actionCol
        );

        table.setItems(controller.getData());

        search.textProperty().addListener((obs, oldVal, keyword) -> {
            String k = keyword.toLowerCase();

            if (k.isEmpty()) {
                table.setItems(controller.getData());
            } else {
                table.setItems(
                        FXCollections.observableArrayList(
                                controller.getData().filtered(l ->
                                        l.getIdTransaksi().toLowerCase().contains(k)
                                                || l.getTanggal().toLowerCase().contains(k)
                                                || l.getNamaNasabah().toLowerCase().contains(k)
                                )
                        )
                );
            }
        });

        Label footer = new Label("© 2026 Sistem Informasi Bank Sampah");
        footer.getStyleClass().add("footer-text");

        box.getChildren().addAll(actionBar, table, footer);

        return box;
    }

    private TableColumn<Laporan, Void> createAksiColumn() {
        TableColumn<Laporan, Void> col = new TableColumn<>("Aksi");

        col.setCellFactory(param -> new TableCell<>() {

            private final Button btnHapus = new Button("Hapus");

            {
                btnHapus.getStyleClass().add("delete-button");

                btnHapus.setOnAction(e -> {
                    Laporan laporan = getTableView()
                            .getItems()
                            .get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi");
                    alert.setHeaderText(null);
                    alert.setContentText("Yakin ingin menghapus transaksi ini?");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            controller.hapus(laporan);
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
                    HBox box = new HBox(btnHapus);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });

        return col;
    }
}