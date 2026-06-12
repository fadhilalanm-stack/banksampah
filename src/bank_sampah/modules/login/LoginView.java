package bank_sampah.modules.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginView {
    private final BorderPane root;
    private final LoginController controller = new LoginController();

    public LoginView() {
        root = new BorderPane();
        root.getStyleClass().add("login-root");
        build();
    }

    private void build() {
        StackPane mainContainer = new StackPane();
        mainContainer.getStyleClass().add("login-main-container");

        // Background dibagi 2: kiri dan kanan
        HBox backgroundBox = new HBox();
        backgroundBox.setMaxWidth(Double.MAX_VALUE);
        backgroundBox.setMaxHeight(Double.MAX_VALUE);

        VBox leftPanel = createLeftPanel();
        StackPane rightPanel = createRightPanel();

        HBox.setHgrow(leftPanel, Priority.ALWAYS);
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        leftPanel.setMaxWidth(Double.MAX_VALUE);
        rightPanel.setMaxWidth(Double.MAX_VALUE);

        backgroundBox.getChildren().addAll(leftPanel, rightPanel);

        // Login card ditaruh di depan background
        VBox loginCard = createLoginCard();
        StackPane.setAlignment(loginCard, Pos.CENTER);

        mainContainer.getChildren().addAll(backgroundBox, loginCard);

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

    private VBox createLoginCard() {
        VBox card = new VBox(15);
        card.getStyleClass().add("login-card");

        card.setAlignment(Pos.CENTER);

        card.setPrefWidth(360);
        card.setMinWidth(360);
        card.setMaxWidth(360);

        card.setPrefHeight(600);
        card.setMinHeight(600);
        card.setMaxHeight(600);

        Label icon = new Label("♻");
        icon.getStyleClass().add("login-icon");

        Label title1 = new Label("Sistem Informasi");
        title1.getStyleClass().add("login-title");

        Label title2 = new Label("Bank Sampah");
        title2.getStyleClass().add("login-title");

        Label subtitle = new Label("Waste Management Administration");
        subtitle.getStyleClass().add("login-subtitle");

        VBox titleBox = new VBox(0, title1, title2, subtitle);
        titleBox.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("login-input");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("login-input");

        VBox usernameBox = inputBox("USERNAME", usernameField);
        VBox passwordBox = inputBox("PASSWORD", passwordField);

        Button loginButton = new Button("Login  ➜");
        loginButton.getStyleClass().add("login-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(e -> controller.login(usernameField.getText(), passwordField.getText()));

        Separator separator = new Separator();

        HBox registerBox = new HBox(4);
        registerBox.setAlignment(Pos.CENTER);

        Label dontHave = new Label("Don't have an account?");
        dontHave.getStyleClass().add("small-muted");

        Label contact = new Label("Contact Administrator");
        contact.getStyleClass().add("green-bold");

        registerBox.getChildren().addAll(dontHave, contact);

        card.getChildren().addAll(
                icon,
                titleBox,
                usernameBox,
                passwordBox,
                loginButton,
                separator,
                registerBox
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

        rightPanel.getChildren().addAll(version);

        return rightPanel;
    }

    public BorderPane getView() {
        return root;
    }
}