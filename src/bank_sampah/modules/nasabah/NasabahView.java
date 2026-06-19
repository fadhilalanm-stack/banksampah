package bank_sampah.modules.nasabah;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class NasabahView {
    private final NasabahController controller = new NasabahController();

    private TableView<Nasabah> table;
    private Pagination pagination;
    private ObservableList<Nasabah> masterData;

    private Label totalValue;
    private Label aktifValue;
    private Label baruValue;

    private final int rowsPerPage = 10;

    public ScrollPane getView() {
        VBox content = new VBox(25);
        content.getStyleClass().add("nasabah-content");

        HBox titleRow = new HBox();
        titleRow.getStyleClass().add("nasabah-title-row");
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

        content.getChildren().addAll(titleRow, createCards(), createNasabahTable());

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("content-scroll");

        return scrollPane;
    }

    private HBox createCards() {
        HBox cards = new HBox(20);
        cards.getStyleClass().add("nasabah-card-row");

        VBox totalCard = card("Total Nasabah", "0", "Semua data nasabah");
        VBox aktifCard = card("Nasabah Aktif", "0", "Terdaftar aktif");
        VBox baruCard = card("Nasabah Baru", "0", "Bulan ini");

        totalValue = (Label) totalCard.getChildren().get(1);
        aktifValue = (Label) aktifCard.getChildren().get(1);
        baruValue = (Label) baruCard.getChildren().get(1);

        refreshSummary();

        cards.getChildren().addAll(totalCard, aktifCard, baruCard);
        return cards;
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

    private void refreshSummary() {
        ObservableList<Nasabah> data = controller.getData();

        int total = data.size();

        long aktif = data.stream()
                .filter(n -> n.getStatus() != null
                        && n.getStatus().equalsIgnoreCase("Aktif"))
                .count();

        int baru = total;

        if (totalValue != null) totalValue.setText(String.valueOf(total));
        if (aktifValue != null) aktifValue.setText(String.valueOf(aktif));
        if (baruValue != null) baruValue.setText(String.valueOf(baru));
    }

    private VBox createNasabahTable() {
        VBox box = new VBox(15);
        box.getStyleClass().add("nasabah-table-panel");

        HBox actionBar = new HBox(10);
        actionBar.setAlignment(Pos.CENTER_LEFT);

        TextField search = new TextField();
        search.setPromptText("Cari nama nasabah...");
        search.getStyleClass().add("nasabah-search-field");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button filterBtn = new Button("☰ Filter");
        Button exportBtn = new Button("Export");

        filterBtn.getStyleClass().add("filter-button");
        exportBtn.getStyleClass().add("export-button");

        actionBar.getChildren().addAll(search, spacer, filterBtn, exportBtn);

        table = new TableView<>();
        table.getStyleClass().add("nasabah-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableColumn<Nasabah, String> noCol = new TableColumn<>("No");
        noCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(
                        masterData.indexOf(data.getValue()) + 1
                ))
        );

        TableColumn<Nasabah, String> kodeCol = new TableColumn<>("ID");
        kodeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getKodeNasabah())
        );

        TableColumn<Nasabah, String> namaCol = new TableColumn<>("Nama Nasabah");
        namaCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNama())
        );

        TableColumn<Nasabah, String> alamatCol = new TableColumn<>("Alamat");
        alamatCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAlamat())
        );

        TableColumn<Nasabah, String> hpCol = new TableColumn<>("No HP");
        hpCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNoHp())
        );

        TableColumn<Nasabah, String> poinCol = new TableColumn<>("Poin");
        poinCol.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getPoin()))
        );

        TableColumn<Nasabah, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStatus())
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

        TableColumn<Nasabah, Void> actionCol = new TableColumn<>("Aksi");

        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Hapus");

            {
                editBtn.getStyleClass().add("edit-button");
                deleteBtn.getStyleClass().add("delete-button");

                editBtn.setOnAction(e -> {
                    Nasabah nasabah = getTableView().getItems().get(getIndex());
                    showEditDialog(nasabah);
                });

                deleteBtn.setOnAction(e -> {
                    Nasabah nasabah = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Konfirmasi");
                    alert.setHeaderText(null);
                    alert.setContentText("Yakin ingin menghapus data " + nasabah.getNama() + " ?");

                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            controller.hapus(nasabah);

                            masterData = controller.getData();
                            refreshPagination();
                            refreshSummary();
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

        table.getColumns().setAll(
                noCol,
                kodeCol,
                namaCol,
                alamatCol,
                hpCol,
                poinCol,
                statusCol,
                actionCol
        );

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
                        controller.getData().filtered(n ->
                                n.getNama().toLowerCase().contains(k)
                                        || n.getKodeNasabah().toLowerCase().contains(k)
                                        || n.getNoHp().toLowerCase().contains(k)
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
                controller.tambahNasabah(
                        nama.getText(),
                        alamat.getText(),
                        hp.getText()
                );

                masterData = controller.getData();
                refreshPagination();
                refreshSummary();
            }

            return null;
        });

        dialog.showAndWait();
    }

    private void showEditDialog(Nasabah nasabah) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Edit Nasabah");

        ButtonType updateButton = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButton, ButtonType.CANCEL);

        dialog.getDialogPane().setPrefWidth(450);
        dialog.getDialogPane().setPrefHeight(250);

        GridPane form = new GridPane();
        form.getStyleClass().add("nasabah-dialog-form");
        form.setHgap(12);
        form.setVgap(12);
        form.setPadding(new Insets(20));

        TextField nama = new TextField(nasabah.getNama());
        TextField hp = new TextField(nasabah.getNoHp());

        TextArea alamat = new TextArea(nasabah.getAlamat());
        alamat.setPrefRowCount(3);

        nama.getStyleClass().add("dialog-input");
        hp.getStyleClass().add("dialog-input");
        alamat.getStyleClass().add("dialog-input");

        form.add(new Label("Nama Lengkap"), 0, 0);
        form.add(nama, 1, 0);

        form.add(new Label("Alamat"), 0, 1);
        form.add(alamat, 1, 1);

        form.add(new Label("No HP"), 0, 2);
        form.add(hp, 1, 2);

        dialog.getDialogPane().setContent(form);

        dialog.setResultConverter(btn -> {
            if (btn == updateButton) {
                controller.update(
                        nasabah,
                        nama.getText(),
                        alamat.getText(),
                        hp.getText()
                );

                masterData = controller.getData();
                refreshPagination();
                refreshSummary();
            }

            return null;
        });

        dialog.showAndWait();
    }
}