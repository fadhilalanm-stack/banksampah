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
                createSummaryCard("Total Nasabah", String.valueOf(controller.getTotalNasabah()), "+12%", "👥"),
                createSummaryCard("Total Sampah Hari Ini", FormatUtil.kg(controller.getTotalSampahHariIni()), "Baru", "♻"),
                createSummaryCard("Total Transaksi Bulan Ini", String.valueOf(controller.getTotalTransaksi()), "+5.4%", "▣"),
                createSummaryCard("Total Poin Beredar", String.valueOf(controller.getTotalPoin()), "Aktif", "▣")
        );

        HBox middleSection = new HBox(20, createStatisticBox(), createTargetBox());
        VBox activityBox = createActivityBox();

        content.getChildren().addAll(new VBox(3, title, subtitle), cards, middleSection, activityBox);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("content-scroll");
        return scrollPane;
    }

    private VBox createSummaryCard(String title, String value, String badge, String icon) {
        VBox card = new VBox(10);
        card.getStyleClass().add("summary-card");
        card.setPadding(new Insets(18));
        card.setPrefWidth(220);
        card.setPrefHeight(125);

        HBox top = new HBox();
        top.setAlignment(Pos.CENTER_LEFT);
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("card-icon");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label badgeLabel = new Label(badge);
        badgeLabel.getStyleClass().add("badge");
        top.getChildren().addAll(iconLabel, spacer, badgeLabel);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");
        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("card-value");
        card.getChildren().addAll(top, titleLabel, valueLabel);
        return card;
    }

    private VBox createStatisticBox() {
        VBox box = new VBox(15);
        box.getStyleClass().add("panel");
        box.setPadding(new Insets(20));
        box.setPrefWidth(560);
        box.setPrefHeight(300);

        Label title = new Label("Statistik Transaksi Bulanan");
        title.getStyleClass().add("panel-title");
        Label subtitle = new Label("Data transaksi 6 bulan terakhir");
        subtitle.getStyleClass().add("small-muted");

        Pane chartArea = new Pane();
        chartArea.setPrefHeight(200);
        String[] months = {"Jan", "Feb", "Mar", "Apr", "Mei", "Jun"};
        for (int i = 0; i < months.length; i++) {
            Label month = new Label(months[i]);
            month.getStyleClass().add("small-muted");
            month.setLayoutX(45 + i * 75);
            month.setLayoutY(170);
            chartArea.getChildren().add(month);
        }
        Label emptyChart = new Label("Area grafik transaksi");
        emptyChart.getStyleClass().add("empty-chart");
        emptyChart.setLayoutX(190);
        emptyChart.setLayoutY(80);
        chartArea.getChildren().add(emptyChart);

        box.getChildren().addAll(new VBox(3, title, subtitle), chartArea);
        return box;
    }

    private VBox createTargetBox() {
        VBox box = new VBox(18);
        box.getStyleClass().add("panel");
        box.setPadding(new Insets(20));
        box.setPrefWidth(280);
        box.setPrefHeight(300);

        Label title = new Label("Target Setoran");
        title.getStyleClass().add("panel-title");

        StackPane circleBox = new StackPane();
        circleBox.setPrefHeight(150);

        Circle baseCircle = new Circle(60);
        baseCircle.getStyleClass().add("base-circle");
        Arc progressArc = new Arc();
        progressArc.setRadiusX(60);
        progressArc.setRadiusY(60);
        progressArc.setStartAngle(90);
        progressArc.setLength(-270);
        progressArc.setType(ArcType.OPEN);
        progressArc.getStyleClass().add("progress-arc");

        VBox percentBox = new VBox(2);
        percentBox.setAlignment(Pos.CENTER);
        Label percent = new Label("75%");
        percent.getStyleClass().add("percent-label");
        Label status = new Label("Tercapai");
        status.getStyleClass().add("small-muted");
        percentBox.getChildren().addAll(percent, status);

        circleBox.getChildren().addAll(baseCircle, progressArc, percentBox);
        box.getChildren().addAll(title, circleBox, legendRow("Anorganik", "312 Kg"), legendRow("Organik", "140 Kg"));
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
        box.getStyleClass().add("panel");
        box.setPadding(new Insets(20));

        Label title = new Label("Aktivitas Terbaru");
        title.getStyleClass().add("panel-title");

        TableView<TransaksiSetor> table = new TableView<>();
        table.setPrefHeight(210);

        TableColumn<TransaksiSetor, String> nasabahCol = new TableColumn<>("Nasabah");
        nasabahCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaNasabah()));
        nasabahCol.setPrefWidth(220);

        TableColumn<TransaksiSetor, String> tipeCol = new TableColumn<>("Tipe Sampah");
        tipeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaSampah()));
        tipeCol.setPrefWidth(200);

        TableColumn<TransaksiSetor, String> beratCol = new TableColumn<>("Berat");
        beratCol.setCellValueFactory(data -> new SimpleStringProperty(FormatUtil.kg(data.getValue().getBerat())));
        beratCol.setPrefWidth(150);

        TableColumn<TransaksiSetor, String> poinCol = new TableColumn<>("Poin");
        poinCol.setCellValueFactory(data -> new SimpleStringProperty("+" + data.getValue().getPoin()));
        poinCol.setPrefWidth(150);

        TableColumn<TransaksiSetor, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        statusCol.setPrefWidth(150);

        table.getColumns().add(nasabahCol);
        table.getColumns().add(tipeCol);
        table.getColumns().add(beratCol);
        table.getColumns().add(poinCol);
        table.getColumns().add(statusCol);
        table.getItems().addAll(new TransaksiSetorDAO().getAll());

        box.getChildren().addAll(title, table);
        return box;
    }
}
