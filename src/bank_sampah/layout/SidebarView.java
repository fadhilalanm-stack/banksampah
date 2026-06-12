import bank_sampah.MainApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SidebarView {
    private final VBox sidebar;
    private final String activeMenu;

    public SidebarView(String activeMenu) {
        this.activeMenu = activeMenu;
        sidebar = new VBox(18);
        sidebar.getStyleClass().add("sidebar");
        build();
    }

    private void build() {
        Label logo = new Label("♻  Bank Sampah");
        logo.getStyleClass().add("sidebar-logo");

        Label role = new Label("ADMIN MANAGEMENT");
        role.getStyleClass().add("sidebar-role");
        role.setAlignment(Pos.CENTER);
        role.setMaxWidth(Double.MAX_VALUE);

        VBox logoBox = new VBox(3, logo, role);

        Button dashboard = menuButton("Dashboard", "dashboard");
        Button nasabah = menuButton("Data Nasabah", "nasabah");
        Button jenisSampah = menuButton("Jenis Sampah", "jenis_sampah");
        Button transaksi = menuButton("Transaksi Setor", "transaksi_setor");
        Button poin = menuButton("Penukaran Poin", "penukaran_poin");
        Button laporan = menuButton("Laporan", "laporan");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logout = new Button("Logout");
        logout.getStyleClass().add("logout-button");
        
        VBox.setMargin(logout, new Insets(0, 0, 50, 0));

        logout.setOnAction(e -> MainApp.showLogin());

        sidebar.getChildren().addAll(logoBox, dashboard, nasabah, jenisSampah, transaksi, poin, laporan, spacer, logout);
    }

    private Button menuButton(String text, String menuKey) {
        Button btn = new Button(text);
        btn.setPrefWidth(180);
        btn.setPrefHeight(42);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 0, 0, 15));

        if (activeMenu.equals(menuKey)) btn.getStyleClass().add("menu-active");
        else btn.getStyleClass().add("menu-default");

        btn.setOnAction(e -> MainApp.showAdminPage(menuKey));
        return btn;
    }

    public VBox getView() {
        return sidebar;
    }
}
