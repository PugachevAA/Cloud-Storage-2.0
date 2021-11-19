import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Data;

public class App extends Application {
    static Stage stage;
    private static Network network;
    private static UserObject user;
    private static ClientController clientController;
    private static String currentServerPath;

    @Override
    public void start(Stage primaryStage) throws Exception {
        network = new Network();
        user = new UserObject();
        Parent parent = FXMLLoader.load(getClass().getResource("/auth1.fxml"));
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
        stage = primaryStage;
    }

    public static void newStage (Scene scene) {
        Stage newStage = new Stage();
        newStage.setScene(scene);
        newStage.show();
        stage.close();
        stage = newStage;
    }

    public static Network getNetwork() {
        return network;
    }

    public static UserObject getUser() {
        return user;
    }
    public static void setUser(UserObject user) {
        App.user = user;
    }

    public static ClientController getClientController() {return clientController;}
    public static void setClientController(ClientController clientController) {App.clientController = clientController;}

    public static String getCurrentServerPath() {return currentServerPath;}
    public static void setCurrentServerPath(String currentServerPath) {
        App.currentServerPath = currentServerPath;
    }

}
