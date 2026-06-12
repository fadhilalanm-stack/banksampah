package bank_sampah.modules.jenis_sampah;

import bank_sampah.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class JenisSampahView {
    private final JenisSampahController controller = new JenisSampahController();
    private TableView<Sampah> table;

    public ScrollPane getView() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(25));

        HBox titleRow = new HBox();
        VBox titleBox = new VBox(4);
        Label title = new Label("Jenis Sampah");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Kelola daftar kategori sampah dan standarisasi harga per kilogram.");
        subtitle.getStyleClass().add("page-subtitle");
        titleBox.getChildren().addAll(title, subtitle);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button tambahBtn = new Button("+ Tambah Jenis Sampah");
        tambahBtn.getStyleClass().add("green-button");
        tambahBtn.setOnAction(e -> showTambahDialog());
        titleRow.getChildren().addAll(titleBox, spacer, tambahBtn);

        HBox cards = new HBox(20);
        cards.getChildren().addAll(card("Total Kategori", String.valueOf(controller.totalKategori()), "+2 bulan ini"), card("Kategori Terpopuler", "Plastik PET", "45% dari total setoran"), card("Rata-rata Harga", "Rp 3.450", "Berdasarkan semua jenis"));

        content.getChildren().addAll(titleRow, cards, createTableBox());

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("content-scroll");
        return scrollPane;
    }

    private VBox card(String title, String value, String desc) {
        VBox card = new VBox(10);
        card.getStyleClass().add("summary-card");
        card.setPadding(new Insets(20));
        card.setPrefWidth(280);
        Label t = new Label(title.toUpperCase());
        t.getStyleClass().add("card-title");
        Label v = new Label(value);
        v.getStyleClass().add("card-value");
        Label d = new Label(desc);
        d.getStyleClass().add("small-muted");
        card.getChildren().addAll(t, v, d);
        return card;
    }

    private VBox createTableBox() {
        VBox box = new VBox(15);
        box.getStyleClass().add("panel");
        box.setPadding(new Insets(20));

        TextField search = new TextField();
        search.setPromptText("Cari nama sampah...");
        search.setPrefWidth(330);

        table = new TableView<>();
        table.setPrefHeight(360);

        TableColumn<Sampah, String> noCol = new TableColumn<>("No");
        noCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(table.getItems().indexOf(data.getValue()) + 1)));
        noCol.setPrefWidth(70);

        TableColumn<Sampah, String> namaCol = new TableColumn<>("Nama Sampah");
        namaCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaSampah()));
        namaCol.setPrefWidth(300);

        TableColumn<Sampah, String> kategoriCol = new TableColumn<>("Kategori");
        kategoriCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKategori()));
        kategoriCol.setPrefWidth(220);

        TableColumn<Sampah, String> hargaCol = new TableColumn<>("Harga per Kg");
        hargaCol.setCellValueFactory(data -> new SimpleStringProperty(FormatUtil.rupiah(data.getValue().getHargaPerKg())));
        hargaCol.setPrefWidth(200);

        table.getColumns().add(noCol);
        table.getColumns().add(namaCol);
        table.getColumns().add(kategoriCol);
        table.getColumns().add(hargaCol);
        table.setItems(controller.getData());

        search.textProperty().addListener((obs, oldVal, keyword) -> {
            String k = keyword.toLowerCase();
            if (k.isEmpty()) table.setItems(controller.getData());
            else table.setItems(controller.getData().filtered(s -> s.getNamaSampah().toLowerCase().contains(k) || s.getKategori().toLowerCase().contains(k)));
        });

        box.getChildren().addAll(search, table);
        return box;
    }

    private void showTambahDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Tambah Jenis Sampah");
        ButtonType saveButton = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setPadding(new Insets(20));

        TextField nama = new TextField();
        ComboBox<String> kategori = new ComboBox<>();
        kategori.getItems().addAll("Plastik", "Kertas/Karton", "Logam", "Kaca", "Organik");
        TextField harga = new TextField();

        form.add(new Label("Nama Sampah"), 0, 0);
        form.add(nama, 1, 0);
        form.add(new Label("Kategori"), 0, 1);
        form.add(kategori, 1, 1);
        form.add(new Label("Harga per Kg"), 0, 2);
        form.add(harga, 1, 2);

        dialog.getDialogPane().setContent(form);
        dialog.setResultConverter(btn -> {
            if (btn == saveButton) {
                controller.tambah(nama.getText(), kategori.getValue(), harga.getText());
                table.setItems(controller.getData());
            }
            return null;
        });
        dialog.showAndWait();
    }
}
