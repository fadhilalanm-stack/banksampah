package bank_sampah.modules.register;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class RegisterView {
    private final BorderPane root;
    private final RegisterController controller = new RegisterController();

    public RegisterView() {
        root = new BorderPane();
        root.getStyleClass().add("login-root");
        build();
    }

    private void build() {
        StackPane mainContainer = new StackPane();
        mainContainer.getStyleClass().add("login-main-container");

        HBox backgroundBox = new HBox();
        backgroundBox.setMaxWidth(Double.MAX_VALUE);
        backgroundBox.setMaxHeight(Double.MAX_VALUE);

        VBox leftPanel = createLeftPanel();
        StackPane rightPanel = createRightPanel();

        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        backgroundBox.getChildren().addAll(leftPanel, rightPanel);

        VBox registerCard = createRegisterCard();
        StackPane.setAlignment(registerCard, Pos.CENTER);

        mainContainer.getChildren().addAll(backgroundBox, registerCard);
        root.setCenter(mainContainer);
    }

    private VBox createLeftPanel() {
        VBox leftPanel = new VBox();
        leftPanel.getStyleClass().add("login-left-panel");
        leftPanel.setMaxWidth(Double.MAX_VALUE);
        leftPanel.setAlignment(Pos.BOTTOM_LEFT);
        leftPanel.setPadding(new Insets(0, 0, 20, 25));

        return leftPanel;
    }

    private VBox createRegisterCard() {
        VBox card = new VBox(10);
        card.getStyleClass().add("login-card");

        card.setAlignment(Pos.CENTER);

        card.setPrefWidth(380);
        card.setMinWidth(380);
        card.setMaxWidth(380);

        card.setPrefHeight(650);
        card.setMinHeight(650);
        card.setMaxHeight(650);

        Label icon = new Label("♻");
        icon.getStyleClass().add("login-icon");

        Label title1 = new Label("Daftar Akun");
        title1.getStyleClass().add("login-title");

        Label title2 = new Label("Bank Sampah");
        title2.getStyleClass().add("login-title");

        Label subtitle = new Label("Waste Management Administration");
        subtitle.getStyleClass().add("login-subtitle");

        VBox titleBox = new VBox(0, title1, title2, subtitle);
        titleBox.setAlignment(Pos.CENTER);

        TextField namaField = new TextField();
        namaField.setPromptText("Masukkan nama lengkap");
        namaField.getStyleClass().add("login-input");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Buat username");
        usernameField.getStyleClass().add("login-input");

        TextField emailField = new TextField();
        emailField.setPromptText("Masukkan email");
        emailField.getStyleClass().add("login-input");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Buat password");
        passwordField.getStyleClass().add("login-input");

        PasswordField konfirmasiField = new PasswordField();
        konfirmasiField.setPromptText("Ulangi password");
        konfirmasiField.getStyleClass().add("login-input");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("admin", "petugas");
        roleBox.setPromptText("Pilih role");
        roleBox.getStyleClass().add("login-input");
        roleBox.setMaxWidth(Double.MAX_VALUE);

        VBox namaBox = inputBox("NAMA LENGKAP", namaField);
        VBox usernameBox = inputBox("USERNAME", usernameField);
        VBox emailBox = inputBox("EMAIL", emailField);
        VBox passwordBox = inputBox("PASSWORD", passwordField);
        VBox konfirmasiBox = inputBox("KONFIRMASI PASSWORD", konfirmasiField);
        VBox roleBoxWrap = inputBox("ROLE", roleBox);

        Button registerButton = new Button("Daftar  ➜");
        registerButton.getStyleClass().add("login-button");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setOnAction(e -> controller.register(
                namaField.getText(),
                usernameField.getText(),
                emailField.getText(),
                passwordField.getText(),
                konfirmasiField.getText(),
                roleBox.getValue()
        ));

        Separator separator = new Separator();

        HBox loginBox = new HBox(4);
        loginBox.setAlignment(Pos.CENTER);

        Label sudahAkun = new Label("Sudah punya akun?");
        sudahAkun.getStyleClass().add("small-muted");

        Label kembaliLogin = new Label("Kembali ke Login");
        kembaliLogin.getStyleClass().add("green-bold");
        kembaliLogin.setStyle("-fx-cursor: hand;");
        kembaliLogin.setOnMouseClicked(e -> controller.goToLogin());

        loginBox.getChildren().addAll(sudahAkun, kembaliLogin);

        card.getChildren().addAll(
                icon,
                titleBox,
                namaBox,
                usernameBox,
                emailBox,
                passwordBox,
                konfirmasiBox,
                roleBoxWrap,
                registerButton,
                separator,
                loginBox
        );

        return card;
    }

    private VBox inputBox(String labelText, Control input) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(Double.MAX_VALUE);

        Label label = new Label(labelText);
        label.getStyleClass().add("input-label");

        input.setMaxWidth(Double.MAX_VALUE);

        box.getChildren().addAll(label, input);

        return box;
    }

    private StackPane createRightPanel() {
        StackPane rightPanel = new StackPane();
        rightPanel.getStyleClass().add("login-right-panel");
        rightPanel.setMaxWidth(Double.MAX_VALUE);

        Label version = new Label("v2.4.0 Stable");
        version.getStyleClass().add("login-version");

        StackPane.setAlignment(version, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(version, new Insets(0, 15, 15, 0));

        rightPanel.getChildren().add(version);

        return rightPanel;
    }

    public BorderPane getView() {
        return root;
    }
}