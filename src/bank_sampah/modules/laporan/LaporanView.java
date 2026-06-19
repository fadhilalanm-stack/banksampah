package bank_sampah.modules.laporan;

import bank_sampah.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LaporanView {

    private final LaporanController controller = new LaporanController();

    public ScrollPane getView() {
        VBox content = new VBox(20,
                createFilterBox(),
                createSummarySection(),
                createTransactionTable()
        );

        content.setPadding(new Insets(25));

        ScrollPane sp = new ScrollPane(content);
        sp.setFitToWidth(true);

        return sp;
    }

    private VBox createFilterBox() {
        DatePicker mulai = new DatePicker();
        DatePicker selesai = new DatePicker();

        Button btnGenerate = new Button("Generate Laporan");
        btnGenerate.getStyleClass().add("green-button");
        btnGenerate.setOnAction(e -> controller.generate());

        HBox row = new HBox(15,
                new Label("Mulai"), mulai,
                new Label("Selesai"), selesai,
                btnGenerate
        );

        VBox box = new VBox(12, row);
        box.getStyleClass().add("panel");
        box.setPadding(new Insets(20));

        return box;
    }

    private HBox createSummarySection() {
        return new HBox(20,
                createCard("Total Transaksi Hari Ini",
                        String.valueOf(controller.getTotalTransaksiHariIni())),

                createCard("Total Sampah Terkumpul",
                        FormatUtil.kg(controller.getTotalSampah())),

                createCard("Nasabah Aktif",
                        String.valueOf(controller.getTotalNasabahAktif()))
        );
    }

    private VBox createCard(String title, String value) {
        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("card-title");

        Label lblValue = new Label(value);
        lblValue.getStyleClass().add("card-value");

        VBox card = new VBox(10, lblTitle, lblValue);
        card.getStyleClass().add("summary-card");
        card.setPadding(new Insets(20));
        card.setPrefWidth(250);

        return card;
    }

    private TableColumn<Laporan, String> createColumn(
            String title,
            java.util.function.Function<Laporan, String> mapper) {

        TableColumn<Laporan, String> col = new TableColumn<>(title);

        col.setCellValueFactory(data ->
                new SimpleStringProperty(
                        mapper.apply(data.getValue())
                )
        );

        return col;
    }

    private VBox createTransactionTable() {

        Label title = new Label("Riwayat Transaksi");

        TableView<Laporan> table = new TableView<>();
        table.setPrefHeight(350);

        table.getColumns().setAll(
        createColumn("ID", Laporan::getIdTransaksi),
        createColumn("Tanggal", Laporan::getTanggal),
        createColumn("Nasabah", Laporan::getNamaNasabah),
        createColumn("Berat", l -> FormatUtil.kg(l.getBerat())),
        createColumn("Poin", l -> l.getNilaiPoin() + " Pts"),
        createAksiColumn(table)
);

        table.setItems(controller.getData());

        VBox box = new VBox(15, title, table);
        box.getStyleClass().add("panel");
        box.setPadding(new Insets(20));

        return box;
    }

    private TableColumn<Laporan, Void> createAksiColumn(
            TableView<Laporan> table) {

        TableColumn<Laporan, Void> col =
                new TableColumn<>("Aksi");

        col.setCellFactory(param -> new TableCell<>() {

            private final Button btnHapus =
                    new Button("Hapus");

            {
                btnHapus.getStyleClass()
                        .add("danger-button");

                btnHapus.setOnAction(e -> {

                    Laporan laporan =
                            getTableView()
                                    .getItems()
                                    .get(getIndex());

                    Alert alert = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Yakin ingin menghapus transaksi ini?",
                            ButtonType.OK,
                            ButtonType.CANCEL
                    );

                    alert.showAndWait()
                            .filter(r -> r == ButtonType.OK)
                            .ifPresent(r -> {
                                controller.hapusTransaksi(laporan);
                                table.setItems(controller.getData());
                            });
                });
            }

            @Override
            protected void updateItem(Void item,
                                      boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnHapus);
            }
        });

        return col;
    }
}