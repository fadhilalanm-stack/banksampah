package bank_sampah.layout;

import bank_sampah.util.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class TopbarView {
    private final HBox topbar;
    public TopbarView(String activeMenu) {
        topbar = new HBox(15);
        topbar.getStyleClass().add("topbar");
        topbar.setPadding(new Insets(15, 25, 15, 25));
        topbar.setAlignment(Pos.CENTER_LEFT);
        build();
    }

    private void build() {
        TextField search = new TextField();
        search.setPromptText("Cari data...");
        search.setPrefWidth(260);
        search.getStyleClass().add("search-field");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label notif = new Label("🔔");
        notif.getStyleClass().add("topbar-icon");

        Label setting = new Label("⚙");
        setting.getStyleClass().add("topbar-icon");

        VBox userBox = new VBox(2);
        userBox.setAlignment(Pos.CENTER_RIGHT);

        Label name = new Label(Session.getNama());
        name.getStyleClass().add("topbar-name");

        Label role = new Label(Session.getRole().toUpperCase());
        role.getStyleClass().add("topbar-role");

        userBox.getChildren().addAll(name, role);

        Circle avatar = new Circle(18);
        avatar.getStyleClass().add("avatar");

        topbar.getChildren().addAll(search, spacer, notif, setting, userBox, avatar);
    }

    public HBox getView() {
        return topbar;
    }
}
