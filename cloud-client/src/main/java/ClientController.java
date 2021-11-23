import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
public class ClientController implements Initializable {

    public ListView serverListView;
    public ListView clientListView;

    private File clientPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientPath = App.getClientPath();
        if (!clientPath.exists()) {
            clientPath.mkdir();
        }
        updateClientListView();
        App.setClientController(this);
        App.getNetwork().writeAndFlush(new CommandObject("/getpath"));
    }

    public void sendFile() throws Exception {
        String fileName = clientListView.getSelectionModel().getSelectedItem().toString();

        if (fileName.isEmpty()) {
            return;
        }

        File sendingFile = new File(clientPath, fileName);

        FileInputStream fis = new FileInputStream(sendingFile);

        byte[] buffer = new byte[8192];
        long forReadLen = sendingFile.length();;
        int readBytes;
        int packageNum = 1;
        while (true) {
            FileObject fo = new FileObject(sendingFile.toPath());
            readBytes = fis.read(buffer);
            forReadLen = forReadLen - readBytes;
            if (readBytes == -1) {
                fis.close();
                break;
            }
            if (forReadLen == 0) {
                fo.setLastPackage(true);
            }
            if (fo.getN() > 1) {
                fo.setFirstPackage(false);
            }
            fo.setBytes(buffer);
            fo.setN(packageNum);
            App.getNetwork().writeAndFlush(fo);
            fo.setFirstPackage(false);
            packageNum ++;
            buffer = new byte[8192];
        }

    }

    public void writeToServerListView(List<String> files) {
        Platform.runLater(() -> {
            serverListView.getItems().clear();
            serverListView.getItems().addAll(files);
        });
    }
    public void updateClientListView() {
        String[] files = App.getClientPath().list();
        Platform.runLater(() -> {
            clientListView.getItems().clear();
            clientListView.getItems().addAll(files);
        });
    }

    public void refreshServerListView() {
        App.getNetwork().writeAndFlush(new CommandObject("/getpath"));
    }

    public void requestFile() {
        String fileName = serverListView.getSelectionModel().getSelectedItem().toString();
        if (fileName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Выберите файл");
            alert.showAndWait();
        } else {
            App.getNetwork().writeAndFlush(new FileRequest(fileName));
        }
    }

    public void newFolderOnClient() throws Exception {
        App.openNewFolderStage(false);
    }

    public void deleteOnClient() throws Exception {
        Files.deleteIfExists(Paths.get(App.getClientPath().toString(), clientListView.getSelectionModel().getSelectedItem().toString()));
        updateClientListView();
    }

    public void NewFolderOnServer() throws Exception {
        App.openNewFolderStage(true);
    }

    public void deleteFromServer() {
        String fileName = serverListView.getSelectionModel().getSelectedItem().toString();

        if (!fileName.isEmpty()) {
            App.getNetwork().writeAndFlush(new DeleteObject(fileName));
        }
    }

    public void goTo(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            log.debug("двойной клик еееесть");
        }
    }
}
