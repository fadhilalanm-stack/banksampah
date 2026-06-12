package bank_sampah.modules.laporan;

import bank_sampah.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LaporanView {

    private final LaporanController controller =
            new LaporanController();

    public ScrollPane getView() {

        VBox content = new VBox(20);
        content.setPadding(new Insets(25));

        content.getChildren().addAll(
                createFilterBox(),
                createSummarySection(),
                createTransactionTable()
        );

        ScrollPane scrollPane =
                new ScrollPane(content);

        scrollPane.setFitToWidth(true);

        return scrollPane;
    }

    private VBox createFilterBox() {

        VBox box = new VBox(12);

        box.getStyleClass().add("panel");
        box.setPadding(new Insets(20));

        HBox row = new HBox(15);

        DatePicker mulai = new DatePicker();
        DatePicker selesai = new DatePicker();

        Button generateBtn =
                new Button("Generate Laporan");

        generateBtn.getStyleClass()
                .add("green-button");

        generateBtn.setOnAction(
                e -> controller.generate()
        );

        Button exportBtn =
                new Button("Export PDF");

        exportBtn.setOnAction(
                e -> controller.exportPdf()
        );

        row.getChildren().addAll(
                new Label("Mulai"),
                mulai,
                new Label("Selesai"),
                selesai,
                generateBtn,
                exportBtn
        );

        box.getChildren().add(row);

        return box;
    }

    private HBox createSummarySection() {

        HBox section = new HBox(20);

        section.getChildren().addAll(

                createCard(
                        "Total Transaksi Hari Ini",
                        String.valueOf(
                                controller.getTotalTransaksiHariIni()
                        )
                ),

                createCard(
                        "Total Sampah Terkumpul",
                        FormatUtil.kg(
                                controller.getTotalSampah()
                        )
                ),

                createCard(
                        "Nasabah Aktif",
                        String.valueOf(
                                controller.getTotalNasabahAktif()
                        )
                )
        );

        return section;
    }

    private VBox createCard(String title,
                            String value) {

        VBox card = new VBox(10);

        card.getStyleClass()
                .add("summary-card");

        card.setPadding(
                new Insets(20)
        );

        card.setPrefWidth(250);

        Label lblTitle =
                new Label(title);

        lblTitle.getStyleClass()
                .add("card-title");

        Label lblValue =
                new Label(value);

        lblValue.getStyleClass()
                .add("card-value");

        card.getChildren().addAll(
                lblTitle,
                lblValue
        );

        return card;
    }

    private VBox createTransactionTable() {

        VBox box = new VBox(15);

        box.getStyleClass().add("panel");
        box.setPadding(new Insets(20));

        Label title =
                new Label("Riwayat Transaksi");

        TableView<Laporan> table =
                new TableView<>();

        table.setPrefHeight(350);

        TableColumn<Laporan, String> idCol =
                new TableColumn<>("ID");

        idCol.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getIdTransaksi()
                )
        );

        TableColumn<Laporan, String> tanggalCol =
                new TableColumn<>("Tanggal");

        tanggalCol.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getTanggal()
                )
        );

        TableColumn<Laporan, String> namaCol =
                new TableColumn<>("Nasabah");

        namaCol.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getNamaNasabah()
                )
        );

        TableColumn<Laporan, String> beratCol =
                new TableColumn<>("Berat");

        beratCol.setCellValueFactory(
                data -> new SimpleStringProperty(
                        FormatUtil.kg(
                                data.getValue().getBerat()
                        )
                )
        );

        TableColumn<Laporan, String> poinCol =
                new TableColumn<>("Poin");

        poinCol.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue()
                                .getNilaiPoin() + " Pts"
                )
        );

        
        table.getColumns().add(idCol);
        table.getColumns().add(tanggalCol);
        table.getColumns().add(namaCol);
        table.getColumns().add(beratCol);
        table.getColumns().add(poinCol);

        table.setItems(
                controller.getData()
        );

        box.getChildren().addAll(
                title,
                table
        );

        return box;
    }
}