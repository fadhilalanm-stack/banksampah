package bank_sampah.modules.nasabah;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class NasabahView {
    private final NasabahController controller = new NasabahController();
    private TableView<Nasabah> table;

    public ScrollPane getView() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(25));

        HBox titleRow = new HBox();
        titleRow.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(4);
        Label title = new Label("Kelola Nasabah");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Data nasabah yang terdaftar dalam sistem Bank Sampah.");
        subtitle.getStyleClass().add("page-subtitle");
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBtn = new Button("+ Tambah Nasabah");
        addBtn.getStyleClass().add("green-button");
        addBtn.setOnAction(e -> showTambahDialog());

        titleRow.getChildren().addAll(titleBox, spacer, addBtn);

        HBox cards = new HBox(20);
        cards.getChildren().addAll(
                card("Total Nasabah", String.valueOf(controller.totalNasabah()), "Semua data nasabah"),
                card("Nasabah Aktif", "934", "Terdaftar aktif"),
                card("Nasabah Baru", "48", "Bulan ini")
        );

        content.getChildren().addAll(titleRow, cards, createNasabahTable());

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.getStyleClass().add("content-scroll");
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private VBox card(String title, String value, String desc) {
        VBox card = new VBox(10);
        card.getStyleClass().add("summary-card");
        card.setPadding(new Insets(20));
        card.setPrefWidth(250);
        Label t = new Label(title.toUpperCase());
        t.getStyleClass().add("card-title");
        Label v = new Label(value);
        v.getStyleClass().add("card-value");
        Label d = new Label(desc);
        d.getStyleClass().add("small-muted");
        card.getChildren().addAll(t, v, d);
        return card;
    }

    private VBox createNasabahTable() {
        VBox box = new VBox(15);
        box.getStyleClass().add("panel");
        box.setPadding(new Insets(20));

        TextField search = new TextField();
        search.setPromptText("Cari nama nasabah...");
        search.setPrefWidth(300);

        table = new TableView<>();
        table.setPrefHeight(360);

        TableColumn<Nasabah, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKodeNasabah()));
        idCol.setPrefWidth(90);

        TableColumn<Nasabah, String> namaCol = new TableColumn<>("Nama Nasabah");
        namaCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNama()));
        namaCol.setPrefWidth(220);

        TableColumn<Nasabah, String> alamatCol = new TableColumn<>("Alamat");
        alamatCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAlamat()));
        alamatCol.setPrefWidth(280);

        TableColumn<Nasabah, String> hpCol = new TableColumn<>("No HP");
        hpCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNoHp()));
        hpCol.setPrefWidth(160);

        TableColumn<Nasabah, String> poinCol = new TableColumn<>("Poin");
        poinCol.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPoin())));
        poinCol.setPrefWidth(120);

        TableColumn<Nasabah, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        statusCol.setPrefWidth(120);

        table.getColumns().add(idCol);
        table.getColumns().add(namaCol);
        table.getColumns().add(alamatCol);
        table.getColumns().add(poinCol);
        table.getColumns().add(statusCol);
        table.setItems(controller.getData());

        search.textProperty().addListener((obs, oldVal, keyword) -> {
            String k = keyword.toLowerCase();
            if (k.isEmpty()) table.setItems(controller.getData());
            else table.setItems(controller.getData().filtered(n -> n.getNama().toLowerCase().contains(k) || n.getKodeNasabah().toLowerCase().contains(k)));
        });

        box.getChildren().addAll(search, table);
        return box;
    }

    private void showTambahDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Tambah Nasabah Baru");
        ButtonType saveButton = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setPadding(new Insets(20));

        TextField nama = new TextField();
        TextField hp = new TextField();
        TextArea alamat = new TextArea();
        alamat.setPrefRowCount(3);

        form.add(new Label("Nama Lengkap"), 0, 0);
        form.add(nama, 1, 0);
        form.add(new Label("Alamat"), 0, 1);
        form.add(alamat, 1, 1);
        form.add(new Label("No HP"), 0, 2);
        form.add(hp, 1, 2);

        dialog.getDialogPane().setContent(form);
        dialog.setResultConverter(btn -> {
            if (btn == saveButton) {
                controller.tambahNasabah(nama.getText(), alamat.getText(), hp.getText());
                if (table != null) table.setItems(controller.getData());
            }
            return null;
        });
        dialog.showAndWait();
    }
}
