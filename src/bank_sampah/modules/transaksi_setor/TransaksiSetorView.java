package bank_sampah.modules.transaksi_setor;

import bank_sampah.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
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
        content.setPadding(new Insets(25));
        HBox topSection = new HBox(20, createFormSetoran(), createKalkulasiBox());
        content.getChildren().addAll(topSection, createRiwayatTable());
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("content-scroll");
        return scrollPane;
    }

    private VBox createFormSetoran() {
        VBox box = new VBox(18);
        box.getStyleClass().add("panel");
        box.setPadding(new Insets(22));
        box.setPrefWidth(620);
        Label title = new Label("⟳  Entri Setoran Baru");
        title.getStyleClass().add("panel-title");

        ComboBox<String> nasabahCombo = new ComboBox<>();
nasabahCombo.setItems(controller.getNasabahList());
nasabahCombo.setPromptText("Pilih Nasabah...");

        jenisSampahBox = new ComboBox<>();

jenisSampahBox.setItems(
        controller.getJenisSampahList()
);

jenisSampahBox.setPromptText(
        "Pilih Kategori Sampah..."
);

jenisSampahBox.setOnAction(
        e -> hitungOtomatis()
);

        beratField = new TextField();
        beratField.setPromptText("0.0");
        beratField.textProperty().addListener((obs, oldValue, newValue) -> hitungOtomatis());

        DatePicker tanggalPicker = new DatePicker(LocalDate.now());

        GridPane form = new GridPane();
        form.setHgap(18);
        form.setVgap(14);
        form.add(new Label("Pilih Nasabah"), 0, 0);
        form.add(new Label("Jenis Sampah"), 1, 0);
        form.add(nasabahCombo, 0, 1);
        form.add(jenisSampahBox, 1, 1);
        form.add(new Label("Berat (kg)"), 0, 2);
        form.add(new Label("Tanggal Transaksi"), 1, 2);
        form.add(beratField, 0, 3);
        form.add(tanggalPicker, 1, 3);

        Button simpanBtn = new Button("▣  Simpan Transaksi");
        simpanBtn.getStyleClass().add("green-button");
        simpanBtn.setOnAction(e -> {

    try {

        controller.simpan(
                nasabahCombo.getValue(),
                Double.parseDouble(beratField.getText()),
                jenisSampahBox.getValue(),
                tanggalPicker.getValue().toString()
        );

        table.setItems(
                controller.getData()
        );

        table.refresh();

        beratField.clear();
        jenisSampahBox.getSelectionModel().clearSelection();
        nasabahCombo.getSelectionModel().clearSelection();

        estimasiNilaiLabel.setText("Rp 0");
        poinDidapatLabel.setText("0 Poin");

    } catch (NumberFormatException ex) {

        bank_sampah.util.AlertUtil.warning(
                "Input Salah",
                "Berat harus berupa angka."
        );

    } catch (Exception ex) {

        bank_sampah.util.AlertUtil.error(
                "Error",
                ex.getMessage()
        );
    }
});

        box.getChildren().addAll(title, form, simpanBtn);
        return box;
    }

    private VBox createKalkulasiBox() {
        VBox box = new VBox(18);
        box.getStyleClass().add("green-panel");
        box.setPadding(new Insets(22));
        box.setPrefWidth(290);
        Label title = new Label("Kalkulasi Otomatis");
        title.getStyleClass().add("panel-title");
        estimasiNilaiLabel = new Label("Rp 0");
        poinDidapatLabel = new Label("0 Poin");
        box.getChildren().addAll(title, row("Estimasi Nilai", estimasiNilaiLabel), row("Poin Didapat", poinDidapatLabel), new Label("Informasi Petugas: Admin Utama"));
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
        box.getStyleClass().add("panel");
        box.setPadding(new Insets(20));
        Label title = new Label("↻  Riwayat Setoran");
        title.getStyleClass().add("panel-title");

        table = new TableView<>();
        table.setPrefHeight(300);

        TableColumn<TransaksiSetor, String> tanggalCol = new TableColumn<>("Tanggal");
        tanggalCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTanggal()));
        tanggalCol.setPrefWidth(130);
        TableColumn<TransaksiSetor, String> namaCol = new TableColumn<>("Nama Nasabah");
        namaCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaNasabah()));
        namaCol.setPrefWidth(180);
        TableColumn<TransaksiSetor, String> jenisCol = new TableColumn<>("Jenis Sampah");
        jenisCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaSampah()));
        jenisCol.setPrefWidth(150);
        TableColumn<TransaksiSetor, String> beratCol = new TableColumn<>("Berat");
        beratCol.setCellValueFactory(data -> new SimpleStringProperty(FormatUtil.kg(data.getValue().getBerat())));
        beratCol.setPrefWidth(110);
        TableColumn<TransaksiSetor, String> nilaiCol = new TableColumn<>("Nilai");
        nilaiCol.setCellValueFactory(data -> new SimpleStringProperty(FormatUtil.rupiah(data.getValue().getTotalHarga())));
        nilaiCol.setPrefWidth(120);
        TableColumn<TransaksiSetor, String> poinCol = new TableColumn<>("Poin");
        poinCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPoin())));
        poinCol.setPrefWidth(90);

        table.getColumns().add(tanggalCol);
        table.getColumns().add(namaCol);
        table.getColumns().add(jenisCol);
        table.getColumns().add(beratCol);
        table.getColumns().add(nilaiCol);
        table.getColumns().add(poinCol);
        table.setItems(controller.getData());
        box.getChildren().addAll(title, table);
        return box;
    }

    private void hitungOtomatis() {
        try {
            double berat = Double.parseDouble(beratField.getText());
            int total = (int)(berat * controller.getHargaPerKg(jenisSampahBox.getValue()));
            int poin = (int)(berat * 10);
            estimasiNilaiLabel.setText(FormatUtil.rupiah(total));
            poinDidapatLabel.setText(poin + " Poin");
        } catch (Exception e) {
            estimasiNilaiLabel.setText("Rp 0");
            poinDidapatLabel.setText("0 Poin");
        }
    }
}
