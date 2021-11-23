import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class App extends Application {
    static Stage stage;
    private static Network network;
    private static UserObject user;
    private static ClientController clientController;
    private static NewFolderController folderController;

    private static File clientPath;
    private static File serverPath;
    static Stage newFolderStage = new Stage();


    @Override
    public void start(Stage primaryStage) throws Exception {
        clientPath = new File("clientroot");
        network = new Network();
        user = new UserObject();
        Parent parent = FXMLLoader.load(getClass().getResource("/auth.fxml"));
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle("Auth");
        primaryStage.show();
        stage = primaryStage;
    }

    public static void openClientStage () throws Exception {
        Parent parent = FXMLLoader.load(App.class.getResource("/client.fxml"));
        Stage newStage = new Stage();
        newStage.setScene(new Scene(parent));
        newStage.setTitle("Google disk");
        newStage.show();
        stage.close();
        stage = newStage;
    }

    public static void openNewFolderStage(boolean onServer) throws Exception {
        Parent parent = FXMLLoader.load(App.class.getResource("/newFolder.fxml"));
        newFolderStage = new Stage();
        newFolderStage.setScene(new Scene(parent));
        newFolderStage.setTitle("Create folder");
        folderController.setOnServer(onServer);
        newFolderStage.show();
    }
    public static void closeNewFolderStage() {
        newFolderStage.close();
    }

    public static Network getNetwork() {
        return network;
    }

    public static UserObject getUser() {
        return user;
    }
    public static File getClientPath() {return clientPath;}

    public static ClientController getClientController() {
        return clientController;
    }
    public static void setClientController(ClientController clientController) {App.clientController = clientController;}

    public static void setFolderController(NewFolderController folderController) {App.folderController = folderController;}
}
