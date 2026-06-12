import javafx.scene.layout.BorderPane;

public class AdminLayout {
    private final BorderPane root;

    public AdminLayout(String activeMenu) {
        root = new BorderPane();
        root.getStyleClass().add("root-admin");
        root.setLeft(new SidebarView(activeMenu).getView());
        root.setTop(new TopbarView(activeMenu).getView());
    }

    public BorderPane getRoot() {
        return root;
    }
}
