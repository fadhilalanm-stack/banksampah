package bank_sampah.modules.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

public class LoginView {
    private final BorderPane root;
    private final LoginController controller = new LoginController();

    public LoginView() {
        root = new BorderPane();
        root.getStyleClass().add("login-root");
        build();
    }

    private void build() {
        HBox mainContainer = new HBox();
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPrefSize(1000, 560);

        VBox leftPanel = createLeftPanel();
        VBox loginCard = createLoginCard();
        StackPane rightPanel = createRightPanel();

        StackPane centerWrapper = new StackPane(loginCard);
        centerWrapper.setPrefWidth(360);
        centerWrapper.setAlignment(Pos.CENTER);

        mainContainer.getChildren().addAll(leftPanel, centerWrapper, rightPanel);
        root.setCenter(mainContainer);
    }

    private VBox createLeftPanel() {
        VBox leftPanel = new VBox();
        leftPanel.getStyleClass().add("login-left-panel");
        leftPanel.setPrefWidth(340);
        leftPanel.setAlignment(Pos.BOTTOM_LEFT);
        leftPanel.setPadding(new Insets(0, 0, 20, 25));
        Label status = new Label("● System Online");
        status.getStyleClass().add("login-status");
        leftPanel.getChildren().add(status);
        return leftPanel;
    }

    private VBox createLoginCard() {
        VBox card = new VBox(15);
        card.getStyleClass().add("login-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setPrefWidth(360);
        card.setMaxWidth(360);

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

        CheckBox keepLogin = new CheckBox("Keep me logged in");
        keepLogin.getStyleClass().add("login-check");

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

        card.getChildren().addAll(icon, titleBox, usernameBox, passwordBox, keepLogin, loginButton, separator, registerBox);
        return card;
    }

    private VBox inputBox(String labelText, Control input) {
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(labelText);
        label.getStyleClass().add("input-label");
        box.getChildren().addAll(label, input);
        return box;
    }

    private StackPane createRightPanel() {
        StackPane rightPanel = new StackPane();
        rightPanel.getStyleClass().add("login-right-panel");
        rightPanel.setPrefWidth(340);

        VBox leafEffect = new VBox(20);
        leafEffect.setAlignment(Pos.CENTER);
        leafEffect.getChildren().addAll(createLeafLine(230, 7, 25), createLeafLine(260, 7, -5), createLeafLine(220, 7, -35), createLeafLine(180, 7, -60));

        Label version = new Label("v2.4.0 Stable");
        version.getStyleClass().add("login-version");
        StackPane.setAlignment(version, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(version, new Insets(0, 15, 15, 0));

        rightPanel.getChildren().addAll(leafEffect, version);
        return rightPanel;
    }

    private Rectangle createLeafLine(double width, double height, double rotate) {
        Rectangle rect = new Rectangle(width, height);
        rect.getStyleClass().add("leaf-line");
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        rect.setRotate(rotate);
        return rect;
    }

    public BorderPane getView() {
        return root;
    }
}
