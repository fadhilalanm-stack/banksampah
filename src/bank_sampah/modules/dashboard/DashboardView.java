package bank_sampah.modules.dashboard;

import bank_sampah.modules.transaksi_setor.TransaksiSetor;
import bank_sampah.modules.transaksi_setor.TransaksiSetorDAO;
import bank_sampah.util.FormatUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;

public class DashboardView {

    private final DashboardController controller = new DashboardController();

    public ScrollPane getView() {
        VBox content = new VBox(22);
        content.setPadding(new Insets(25));

        Label title = new Label("Dashboard Ringkasan");
        title.getStyleClass().add("page-title");

        Label subtitle = new Label("Selamat datang kembali, berikut adalah performa Bank Sampah hari ini.");
        subtitle.getStyleClass().add("page-subtitle");

        HBox cards = new HBox(15);
        cards.getChildren().addAll(
                createSummaryCard("Total Nasabah", String.valueOf(controller.getTotalNasabah()), "+12%"),
                createSummaryCard("Total Sampah Hari Ini", FormatUtil.kg(controller.getTotalSampahHariIni()), "Baru"),
                createSummaryCard("Total Transaksi Bulan Ini", String.valueOf(controller.getTotalTransaksi()), "+5.4%"),
                createSummaryCard("Total Poin Beredar", String.valueOf(controller.getTotalPoin()), "Aktif")
        );

        HBox middleSection = new HBox(20);
        middleSection.getChildren().addAll(
                createStatisticBox(),
                createTargetBox()
        );

        VBox activityBox = createActivityBox();

        content.getChildren().addAll(
                new VBox(3, title, subtitle),
                cards,
                middleSection,
                activityBox
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("content-scroll");

        return scrollPane;
    }

    private VBox createSummaryCard(String title, String value, String badge) {
        VBox card = new VBox(15);
        card.getStyleClass().add("summary-card");
        card.setPadding(new Insets(20));
        card.setPrefWidth(290);
        card.setPrefHeight(145);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badgeLabel = new Label(badge);
        badgeLabel.getStyleClass().add("badge");

        header.getChildren().addAll(titleLabel, spacer, badgeLabel);

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("card-value");

        card.getChildren().addAll(header, valueLabel);

        return card;
    }

    private VBox createStatisticBox() {
        VBox box = new VBox(15);
        box.getStyleClass().add("panel");
        box.setPadding(new Insets(20));
        box.setPrefWidth(1000);
        box.setPrefHeight(500);

        Label title = new Label("Statistik Transaksi Bulanan");
        title.getStyleClass().add("panel-title");

        Label subtitle = new Label("Data transaksi 6 bulan terakhir");
        subtitle.getStyleClass().add("small-muted");

        Pane chartArea = new Pane();
        chartArea.setPrefHeight(390);

        String[] months = {"Jan", "Feb", "Mar", "Apr", "Mei", "Jun"};

        for (int i = 0; i < months.length; i++) {
            Label month = new Label(months[i]);
            month.getStyleClass().add("small-muted");
            month.setLayoutX(80 + i * 120);
            month.setLayoutY(330);
            chartArea.getChildren().add(month);
        }

        Label emptyChart = new Label("Area grafik transaksi");
        emptyChart.getStyleClass().add("empty-chart");
        emptyChart.setLayoutX(420);
        emptyChart.setLayoutY(180);
        chartArea.getChildren().add(emptyChart);

        box.getChildren().addAll(new VBox(3, title, subtitle), chartArea);

        return box;
    }

    private VBox createTargetBox() {
        VBox box = new VBox(24);
        box.getStyleClass().add("panel");
        box.setPadding(new Insets(20));
        box.setPrefWidth(700);
        box.setPrefHeight(500);

        Label title = new Label("Target Setoran");
        title.getStyleClass().add("panel-title");

        StackPane circleBox = new StackPane();
        circleBox.setPrefHeight(260);

        Circle baseCircle = new Circle(90);
        baseCircle.getStyleClass().add("base-circle");

        Arc progressArc = new Arc();
        progressArc.setRadiusX(90);
        progressArc.setRadiusY(90);
        progressArc.setStartAngle(90);
        progressArc.setLength(-270);
        progressArc.setType(ArcType.OPEN);
        progressArc.getStyleClass().add("progress-arc");

        VBox percentBox = new VBox(4);
        percentBox.setAlignment(Pos.CENTER);

        Label percent = new Label("75%");
        percent.getStyleClass().add("percent-label");

        Label status = new Label("Tercapai");
        status.getStyleClass().add("small-muted");

        percentBox.getChildren().addAll(percent, status);
        circleBox.getChildren().addAll(baseCircle, progressArc, percentBox);

        box.getChildren().addAll(
                title,
                circleBox,
                legendRow("Anorganik", "312 Kg"),
                legendRow("Organik", "140 Kg")
        );

        return box;
    }

    private HBox legendRow(String name, String value) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        Circle dot = new Circle(5);
        dot.getStyleClass().add("green-dot");

        Label nameLabel = new Label(name);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("bold-text");

        row.getChildren().addAll(dot, nameLabel, spacer, valueLabel);

        return row;
    }

    private VBox createActivityBox() {
        VBox box = new VBox(15);
        box.getStyleClass().add("laporan-table-panel");

        Label title = new Label("Aktivitas Terbaru");
        title.getStyleClass().add("panel-title");

        HBox actionBar = new HBox(10);
        actionBar.setAlignment(Pos.CENTER_LEFT);

        TextField search = new TextField();
        search.setPromptText("Cari aktivitas...");
        search.getStyleClass().add("laporan-search-field");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        actionBar.getChildren().addAll(search, spacer);

        TableView<TransaksiSetor> table = new TableView<>();
        table.getStyleClass().add("laporan-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPrefHeight(380);

        TableColumn<TransaksiSetor, String> nasabahCol = new TableColumn<>("Nasabah");
        nasabahCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNamaNasabah())
        );

        TableColumn<TransaksiSetor, String> tipeCol = new TableColumn<>("Jenis Sampah");
        tipeCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getNamaSampah())
        );

        TableColumn<TransaksiSetor, String> beratCol = new TableColumn<>("Berat");
        beratCol.setCellValueFactory(data ->
                new SimpleStringProperty(FormatUtil.kg(data.getValue().getBerat()))
        );

        TableColumn<TransaksiSetor, String> poinCol = new TableColumn<>("Poin");
        poinCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPoin() + " Pts")
        );

        TableColumn<TransaksiSetor, String> statusCol = new TableColumn<>("Status");
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

        table.getColumns().setAll(
                nasabahCol,
                tipeCol,
                beratCol,
                poinCol,
                statusCol
        );

        TransaksiSetorDAO dao = new TransaksiSetorDAO();
        table.setItems(dao.getAll());

        search.textProperty().addListener((obs, oldVal, keyword) -> {
            String k = keyword.toLowerCase();

            if (k.isEmpty()) {
                table.setItems(dao.getAll());
            } else {
                table.setItems(
                        dao.getAll().filtered(t ->
                                t.getNamaNasabah().toLowerCase().contains(k)
                                        || t.getNamaSampah().toLowerCase().contains(k)
                                        || t.getTanggal().toLowerCase().contains(k)
                        )
                );
            }
        });

        Label footer = new Label("© 2026 Sistem Informasi Bank Sampah");
        footer.getStyleClass().add("footer-text");

        box.getChildren().addAll(title, actionBar, table, footer);

        return box;
    }
}