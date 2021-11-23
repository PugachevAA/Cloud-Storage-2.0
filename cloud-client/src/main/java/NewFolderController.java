import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class NewFolderController implements Initializable {
    public TextField nameTextField;

    public void setOnServer(boolean onServer) {
        this.onServer = onServer;
    }

    private boolean onServer = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.setFolderController(this);
    }

    public void create() throws Exception {
        if (!onServer) {
            createOnClient();
        } else {
            createOnServer();
        }
    }
    public void createOnClient() throws Exception {
        if (!nameTextField.getText().isEmpty()) {
            Files.createDirectory(Paths.get(App.getClientPath().toString(), nameTextField.getText()));
            App.getClientController().updateClientListView();
            App.closeNewFolderStage();
        }

    }
    public void createOnServer() {
        if (!nameTextField.getText().isEmpty()) {
            App.getNetwork().writeAndFlush(new NewFolder(nameTextField.getText()));
            App.closeNewFolderStage();
        }
    }


    public void cancel() {
        App.closeNewFolderStage();
    }
}
