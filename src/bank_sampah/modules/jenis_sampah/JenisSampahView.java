package bank_sampah.modules.jenis_sampah;

import bank_sampah.util.FormatUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class JenisSampahView {
    private final JenisSampahController controller = new JenisSampahController();

    private TableView<Sampah> table;
    private Pagination pagination;
    private ObservableList<Sampah> masterData;

    private final int rowsPerPage = 10;

    public ScrollPane getView() {
        VBox content = new VBox(25);
        content.getStyleClass().add("jenis-content");

        HBox titleRow = new HBox();
        titleRow.getStyleClass().add("jenis-title-row");
        titleRow.setAlignment(Pos.CENTER_LEFT);

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
        cards.getStyleClass().add("jenis-card-row");
        cards.getChildren().addAll(
                card("Total Kategori", String.valueOf(controller.totalKategori()), "+2 bulan ini"),
                card("Kategori Terpopuler", "Plastik PET", "45% dari total setoran"),
                card("Rata-rata Harga", FormatUtil.rupiah(controller.rataRataHarga()), "Berdasarkan semua jenis")
        );

        content.getChildren().addAll(titleRow, cards, createTableBox());

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("content-scroll");

        return scrollPane;
    }

    private VBox card(String title, String value, String desc) {
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

    private VBox createTableBox() {
        VBox box = new VBox(15);
        box.getStyleClass().add("jenis-table-panel");

        HBox actionBar = new HBox(10);
        actionBar.setAlignment(Pos.CENTER_LEFT);

        TextField search = new TextField();
        search.setPromptText("Cari nama sampah...");
        search.getStyleClass().add("jenis-search-field");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button filterBtn = new Button("☰ Filter");
        Button exportBtn = new Button("Export");

        filterBtn.getStyleClass().add("filter-button");
        exportBtn.getStyleClass().add("export-button");

        actionBar.getChildren().addAll(search, spacer, filterBtn, exportBtn);

        table = new TableView<>();
        table.getStyleClass().add("jenis-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Sampah, String> noCol = new TableColumn<>("No");
        noCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(
                        masterData.indexOf(data.getValue()) + 1
                ))
        );

        TableColumn<Sampah, String> namaCol = new TableColumn<>("Nama Sampah");
        namaCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNamaSampah())
        );

        TableColumn<Sampah, String> kategoriCol = new TableColumn<>("Kategori");
        kategoriCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getKategori())
        );

        TableColumn<Sampah, String> hargaCol = new TableColumn<>("Harga per Kg");
        hargaCol.setCellValueFactory(data ->
                new SimpleStringProperty(FormatUtil.rupiah(data.getValue().getHargaPerKg()))
        );

        TableColumn<Sampah, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty("Aktif"));

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

        TableColumn<Sampah, Void> actionCol = new TableColumn<>("Aksi");

        actionCol.setCellFactory(param -> new TableCell<>() {

            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Hapus");

            {
                editBtn.getStyleClass().add("edit-button");
                deleteBtn.getStyleClass().add("delete-button");

                editBtn.setOnAction(e -> {
                    Sampah sampah = getTableView()
                            .getItems()
                            .get(getIndex());

                    showEditDialog(sampah);
                });

                deleteBtn.setOnAction(e -> {
                    Sampah sampah = getTableView()
                            .getItems()
                            .get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi");
                    alert.setHeaderText(null);
                    alert.setContentText(
                            "Yakin ingin menghapus data "
                                    + sampah.getNamaSampah()
                                    + " ?"
                    );

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            controller.hapus(sampah);

                            masterData = controller.getData();
                            refreshPagination();
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
                    HBox actions = new HBox(8, editBtn, deleteBtn);
                    actions.setAlignment(Pos.CENTER);
                    setGraphic(actions);
                }
            }
        });

        table.getColumns().add(noCol);
        table.getColumns().add(namaCol);
        table.getColumns().add(kategoriCol);
        table.getColumns().add(hargaCol);
        table.getColumns().add(statusCol);
        table.getColumns().add(actionCol);

        masterData = controller.getData();

        pagination = new Pagination();
        pagination.setMaxPageIndicatorCount(5);
        refreshPagination();

        search.textProperty().addListener((obs, oldVal, keyword) -> {
            String k = keyword.toLowerCase();

            if (k.isEmpty()) {
                masterData = controller.getData();
            } else {
                masterData = FXCollections.observableArrayList(
                        controller.getData().filtered(s ->
                                s.getNamaSampah().toLowerCase().contains(k)
                                        || s.getKategori().toLowerCase().contains(k)
                        )
                );
            }

            refreshPagination();
        });

        Label footer = new Label("© 2026 Sistem Informasi Bank Sampah");
        footer.getStyleClass().add("footer-text");

        box.getChildren().addAll(actionBar, table, pagination, footer);
        return box;
    }

    private void refreshPagination() {
        int pageCount = (int) Math.ceil((double) masterData.size() / rowsPerPage);
        pagination.setPageCount(Math.max(pageCount, 1));

        pagination.setPageFactory(pageIndex -> {
            updateTablePage(pageIndex);
            return new VBox();
        });

        updateTablePage(0);
    }

    private void updateTablePage(int pageIndex) {
        if (masterData == null || masterData.isEmpty()) {
            table.setItems(FXCollections.observableArrayList());
            return;
        }

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, masterData.size());

        table.setItems(
                FXCollections.observableArrayList(
                        masterData.subList(fromIndex, toIndex)
                )
        );
    }

    private void showTambahDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Tambah Jenis Sampah");

        ButtonType saveButton = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.getStyleClass().add("jenis-dialog-form");
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));

        TextField nama = new TextField();
        nama.getStyleClass().add("dialog-input");

        ComboBox<String> kategori = new ComboBox<>();
        kategori.getItems().addAll("Plastik", "Kertas/Karton", "Logam", "Kaca", "Organik");
        kategori.getStyleClass().add("dialog-input");

        TextField harga = new TextField();
        harga.getStyleClass().add("dialog-input");

        form.add(new Label("Nama Sampah"), 0, 0);
        form.add(nama, 1, 0);

        form.add(new Label("Kategori"), 0, 1);
        form.add(kategori, 1, 1);

        form.add(new Label("Harga per Kg"), 0, 2);
        form.add(harga, 1, 2);

        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(btn -> {
            if (btn == saveButton) {
                controller.tambah(
                        nama.getText(),
                        kategori.getValue(),
                        harga.getText()
                );

                masterData = controller.getData();
                refreshPagination();
            }

            return null;
        });

        dialog.showAndWait();
    }

    private void showEditDialog(Sampah sampah) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Edit Jenis Sampah");

        ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

        GridPane form = new GridPane();
        form.getStyleClass().add("jenis-dialog-form");
        form.setHgap(15);
        form.setVgap(15);
        form.setPadding(new Insets(20));

        TextField nama = new TextField(sampah.getNamaSampah());
        nama.getStyleClass().add("dialog-input");

        ComboBox<String> kategori = new ComboBox<>();
        kategori.getItems().addAll("Plastik", "Kertas/Karton", "Logam", "Kaca", "Organik");
        kategori.setValue(sampah.getKategori());
        kategori.getStyleClass().add("dialog-input");

        TextField harga = new TextField(String.valueOf(sampah.getHargaPerKg()));
        harga.getStyleClass().add("dialog-input");

        form.add(new Label("Nama Sampah"), 0, 0);
        form.add(nama, 1, 0);

        form.add(new Label("Kategori"), 0, 1);
        form.add(kategori, 1, 1);

        form.add(new Label("Harga per Kg"), 0, 2);
        form.add(harga, 1, 2);

        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(btn -> {
            if (btn == updateButton) {
                controller.update(
                        sampah,
                        nama.getText(),
                        kategori.getValue(),
                        harga.getText()
                );

                masterData = controller.getData();
                refreshPagination();
            }

            return null;
        });

        dialog.showAndWait();
    }
}