package bank_sampah.modules.transaksi_setor;

import bank_sampah.util.AlertUtil;
import bank_sampah.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;

public class TransaksiSetorView {
    private final TransaksiSetorController controller = new TransaksiSetorController();

    private Label estimasiNilaiLabel;
    private Label poinDidapatLabel;
    private TextField beratField;
    private ComboBox<String> jenisSampahBox;
    private TableView<TransaksiSetor> table;

    public ScrollPane getView() {
        VBox content = new VBox(22);
        content.getStyleClass().add("setor-content");

        HBox topSection = new HBox(20);
        topSection.getStyleClass().add("setor-top-section");
        topSection.getChildren().addAll(
                createFormSetoran(),
                createKalkulasiBox()
        );

        content.getChildren().addAll(topSection, createRiwayatTable());

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("content-scroll");

        return scrollPane;
    }

    private VBox createFormSetoran() {
        VBox box = new VBox(18);
        box.getStyleClass().add("setor-panel");
        box.setPrefWidth(620);

        Label title = new Label("Entri Setoran Baru");
        title.getStyleClass().add("panel-title");

        ComboBox<String> nasabahCombo = new ComboBox<>();
        nasabahCombo.getItems().addAll("Ahmad Sulistyo", "Siti Aminah", "Bambang Wijaya");
        nasabahCombo.setPromptText("Cari Nama atau ID Nasabah...");
        nasabahCombo.getStyleClass().add("setor-input");

        jenisSampahBox = new ComboBox<>();
        jenisSampahBox.getItems().addAll("Plastik PET", "Kertas", "Logam", "Kaca", "Kardus");
        jenisSampahBox.setPromptText("Pilih Kategori Sampah...");
        jenisSampahBox.getStyleClass().add("setor-input");
        jenisSampahBox.setOnAction(e -> hitungOtomatis());

        beratField = new TextField();
        beratField.setPromptText("0.0");
        beratField.getStyleClass().add("setor-input");
        beratField.textProperty().addListener((obs, oldValue, newValue) -> hitungOtomatis());

        DatePicker tanggalPicker = new DatePicker(LocalDate.now());
        tanggalPicker.getStyleClass().add("setor-input");

        GridPane form = new GridPane();
        form.getStyleClass().add("setor-form");

        form.add(new Label("Pilih Nasabah"), 0, 0);
        form.add(new Label("Jenis Sampah"), 1, 0);
        form.add(nasabahCombo, 0, 1);
        form.add(jenisSampahBox, 1, 1);

        form.add(new Label("Berat (kg)"), 0, 2);
        form.add(new Label("Tanggal Transaksi"), 1, 2);
        form.add(beratField, 0, 3);
        form.add(tanggalPicker, 1, 3);

        Button simpanBtn = new Button("Simpan Transaksi");
        simpanBtn.getStyleClass().add("green-button");

        simpanBtn.setOnAction(e -> {
            try {
                controller.simpan(
                        Double.parseDouble(beratField.getText()),
                        jenisSampahBox.getValue(),
                        tanggalPicker.getValue().toString()
                );

                table.setItems(controller.getData());
                hitungOtomatis();

            } catch (Exception ex) {
                AlertUtil.warning("Input Salah", "Berat harus berupa angka.");
            }
        });

        box.getChildren().addAll(title, form, simpanBtn);

        return box;
    }

    private VBox createKalkulasiBox() {
        VBox box = new VBox(18);
        box.getStyleClass().add("setor-green-panel");
        box.setPrefWidth(290);

        Label title = new Label("Kalkulasi Otomatis");
        title.getStyleClass().add("panel-title");

        estimasiNilaiLabel = new Label("Rp 0");
        poinDidapatLabel = new Label("0 Poin");

        Label info = new Label("Informasi Petugas: Admin Utama");
        info.getStyleClass().add("small-muted");

        box.getChildren().addAll(
                title,
                row("Estimasi Nilai", estimasiNilaiLabel),
                row("Poin Didapat", poinDidapatLabel),
                info
        );

        return box;
    }

    private HBox row(String label, Label value) {
        HBox row = new HBox();

        Label l = new Label(label);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        value.getStyleClass().add("green-bold");

        row.getChildren().addAll(l, spacer, value);

        return row;
    }

    private VBox createRiwayatTable() {
        VBox box = new VBox(15);
        box.getStyleClass().add("laporan-table-panel");

        Label title = new Label("Riwayat Setoran");
        title.getStyleClass().add("panel-title");

        HBox actionBar = new HBox(10);
        actionBar.setAlignment(Pos.CENTER_LEFT);

        TextField search = new TextField();
        search.setPromptText("Cari transaksi...");
        search.getStyleClass().add("laporan-search-field");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        actionBar.getChildren().addAll(search, spacer);

        table = new TableView<>();
        table.getStyleClass().add("laporan-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPrefHeight(350);

        TableColumn<TransaksiSetor, String> tanggalCol = new TableColumn<>("Tanggal");
        tanggalCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getTanggal()));

        TableColumn<TransaksiSetor, String> namaCol = new TableColumn<>("Nama Nasabah");
        namaCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNamaNasabah()));

        TableColumn<TransaksiSetor, String> jenisCol = new TableColumn<>("Jenis Sampah");
        jenisCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNamaSampah()));

        TableColumn<TransaksiSetor, String> beratCol = new TableColumn<>("Berat");
        beratCol.setCellValueFactory(data ->
                new SimpleStringProperty(FormatUtil.kg(data.getValue().getBerat())));

        TableColumn<TransaksiSetor, String> nilaiCol = new TableColumn<>("Nilai");
        nilaiCol.setCellValueFactory(data ->
                new SimpleStringProperty(FormatUtil.rupiah(data.getValue().getTotalHarga())));

        TableColumn<TransaksiSetor, String> poinCol = new TableColumn<>("Poin");
        poinCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getPoin())));

        TableColumn<TransaksiSetor, String> statusCol = new TableColumn<>("Status");
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

        TableColumn<TransaksiSetor, Void> aksiCol = createAksiColumn();

        table.getColumns().setAll(
                tanggalCol,
                namaCol,
                jenisCol,
                beratCol,
                nilaiCol,
                poinCol,
                statusCol,
                aksiCol
        );

        table.setItems(controller.getData());

        search.textProperty().addListener((obs, oldVal, keyword) -> {
            String k = keyword.toLowerCase();

            if (k.isEmpty()) {
                table.setItems(controller.getData());
            } else {
                table.setItems(
                        controller.getData().filtered(t ->
                                t.getTanggal().toLowerCase().contains(k)
                                        || t.getNamaNasabah().toLowerCase().contains(k)
                                        || t.getNamaSampah().toLowerCase().contains(k)
                        )
                );
            }
        });

        Label footer = new Label("© 2026 Sistem Informasi Bank Sampah");
        footer.getStyleClass().add("footer-text");

        box.getChildren().addAll(title, actionBar, table, footer);

        return box;
    }

    private TableColumn<TransaksiSetor, Void> createAksiColumn() {
        TableColumn<TransaksiSetor, Void> col = new TableColumn<>("Aksi");

        col.setCellFactory(param -> new TableCell<>() {

            private final Button btnHapus = new Button("Hapus");

            {
                btnHapus.getStyleClass().add("delete-button");

                btnHapus.setOnAction(e -> {
                    TransaksiSetor transaksi = getTableView()
                            .getItems()
                            .get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi");
                    alert.setHeaderText(null);
                    alert.setContentText("Yakin ingin menghapus transaksi ini?");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            controller.hapus(transaksi);
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

    private void hitungOtomatis() {
        try {
            double berat = Double.parseDouble(beratField.getText());

            int total = (int) (berat * controller.getHargaPerKg(jenisSampahBox.getValue()));
            int poin = (int) (berat * 10);

            estimasiNilaiLabel.setText(FormatUtil.rupiah(total));
            poinDidapatLabel.setText(poin + " Poin");

        } catch (Exception e) {
            estimasiNilaiLabel.setText("Rp 0");
            poinDidapatLabel.setText("0 Poin");
        }
    }
}