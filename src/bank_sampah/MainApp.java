package bank_sampah;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import bank_sampah.layout.AdminLayout;
import bank_sampah.modules.login.LoginView;
import bank_sampah.modules.dashboard.DashboardView;
import bank_sampah.modules.nasabah.NasabahView;
import bank_sampah.modules.jenis_sampah.JenisSampahView;
import bank_sampah.modules.transaksi_setor.TransaksiSetorView;
import bank_sampah.modules.penukaran_poin.PenukaranPoinView;
import bank_sampah.modules.laporan.LaporanView;
import bank_sampah.util.AlertUtil;
import bank_sampah.util.Session;

public class MainApp extends Application {
    private static Stage mainStage;
    private static BorderPane adminRoot;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        showLogin();
        mainStage.show();
    }

    public static void showLogin() {
        Session.clear();
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getView(), 1100, 650);
        addStylesheet(scene, "bank_sampah/resources/app.css");
        addStylesheet(scene, "bank_sampah/modules/login/login.css");
        mainStage.setTitle("Login - Sistem Informasi Bank Sampah");
        mainStage.setScene(scene);
    }

    public static void showAdminPage(String page) {
        if (!Session.isLoggedIn() && !"login".equals(page)) {
            showLogin();
            return;
        }

        AdminLayout layout = new AdminLayout(page);
        adminRoot = layout.getRoot();

        switch (page) {
            case "dashboard":
                adminRoot.setCenter(new DashboardView().getView());
                mainStage.setTitle("Dashboard - Bank Sampah");
                break;
            case "nasabah":
                adminRoot.setCenter(new NasabahView().getView());
                mainStage.setTitle("Data Nasabah - Bank Sampah");
                break;
            case "jenis_sampah":
                adminRoot.setCenter(new JenisSampahView().getView());
                mainStage.setTitle("Jenis Sampah - Bank Sampah");
                break;
            case "transaksi_setor":
                adminRoot.setCenter(new TransaksiSetorView().getView());
                mainStage.setTitle("Transaksi Setor - Bank Sampah");
                break;
            case "penukaran_poin":
                adminRoot.setCenter(new PenukaranPoinView().getView());
                mainStage.setTitle("Penukaran Poin - Bank Sampah");
                break;
            case "laporan":
                adminRoot.setCenter(new LaporanView().getView());
                mainStage.setTitle("Laporan - Bank Sampah");
                break;
            default:
                AlertUtil.info("Navigasi", "Halaman tidak ditemukan.");
                adminRoot.setCenter(new DashboardView().getView());
        }

        Scene scene = new Scene(adminRoot, 1200, 760);
        addStylesheet(scene, "bank_sampah/resources/app.css");
        addStylesheet(scene, "bank_sampah/modules/dashboard/dashboard.css");
        addStylesheet(scene, "bank_sampah/modules/nasabah/nasabah.css");
        addStylesheet(scene, "bank_sampah/modules/jenis_sampah/jenis-sampah.css");
        addStylesheet(scene, "bank_sampah/modules/transaksi_setor/transaksi-setor.css");
        addStylesheet(scene, "bank_sampah/modules/penukaran_poin/penukaran-poin.css");
        addStylesheet(scene, "bank_sampah/modules/laporan/laporan.css");
        mainStage.setScene(scene);
    }

    public static void addStylesheet(Scene scene, String path) {
        try {
            java.io.File file = new java.io.File("src/" + path);
            
            if (file.exists()) {
                scene.getStylesheets().add(file.toURI().toString());
                System.out.println("CSS loaded: " + file.getPath());
            } else {
                System.out.println("CSS not found: " + file.getPath());
            }
        
        } catch (Exception e) {
            System.out.println("Gagal load CSS: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 