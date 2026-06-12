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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Separator;
import javafx.geometry.Orientation;

public class TopbarView {
    private final HBox topbar;
    private final String activeMenu;

    public TopbarView(String activeMenu) {
        this.activeMenu = activeMenu;
        topbar = new HBox(15);
        topbar.getStyleClass().add("topbar");
        topbar.setAlignment(Pos.CENTER_LEFT);
        build();
    }

    private void build() {
        TextField search = new TextField();
        search.setPromptText("Cari data...");
        search.getStyleClass().add("search-field");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ImageView notifIcon = new ImageView(
            new Image("file:/C:/ProjectJava/Praktikum/img/notifikasi.png")
        );
        notifIcon.getStyleClass().add("topbar-icon");
        
        ImageView settingIcon = new ImageView(
            new Image("file:/C:/ProjectJava/Praktikum/img/pengaturan.png")
        );
        settingIcon.getStyleClass().add("topbar-icon");

        Separator divider = new Separator();
        divider.setOrientation(Orientation.VERTICAL);
        divider.getStyleClass().add("topbar-divider");

        VBox userBox = new VBox(2);
        userBox.setAlignment(Pos.CENTER_LEFT);

        HBox.setMargin(userBox, new Insets(0, 16, 0, 0));

        Label name = new Label(Session.getNama());
        name.getStyleClass().add("topbar-name");

        Label role = new Label(Session.getRole().toUpperCase());
        role.getStyleClass().add("topbar-role");

        userBox.getChildren().addAll(name, role);

        ImageView avatar = new ImageView(
            new Image("file:/C:/ProjectJava/Praktikum/img/pp_admin.png")
        );
        
        avatar.setFitWidth(50);
        avatar.setFitHeight(50);
        avatar.setPreserveRatio(false);
        
        Circle clip = new Circle(25, 25, 25);
        avatar.setClip(clip);
        
        avatar.getStyleClass().add("avatar");

        topbar.getChildren().addAll(search, spacer, notifIcon, settingIcon, divider, avatar, userBox);
    }

    public HBox getView() {
        return topbar;
    }
}
