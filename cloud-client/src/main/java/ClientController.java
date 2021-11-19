import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Slf4j
public class ClientController implements Initializable {

    public ListView serverListView;
    public ListView clientListView;
    public Button refresh;

    private File clientPath;
    private File serverPath;

    private File[] clientFilesList;
    private File[] serverFilesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientPath = new File("clientroot");

        if (!clientPath.exists()) {
            clientPath.mkdir();
        }
        clientFilesList = clientPath.listFiles();
        for (File file : clientFilesList) {
            clientListView.getItems().add(file.getName());
        }
        App.setClientController(this);
    }

    public void sendFile(MouseEvent mouseEvent) throws IOException {
        String fileName = clientListView.getSelectionModel().getSelectedItem().toString();
        if (fileName.isEmpty()) {
            return;
        }

        File sendingFile = new File(clientPath, fileName);
        long size = sendingFile.length();

        FileInputStream fis = new FileInputStream(sendingFile);


        byte[] buffer = new byte[8192];
        long forReadLen = size;
        int readBytes;
        int packageNum = 1;
        while (true) {
            FileObject fo = FileObject.builder()
                    .fileName(fileName)
                    .destPath(App.getUser().getLogin())
                    .size(size)
                    .n(0)
                    .isFirstPackage(packageNum == 1)
                    .isLastPackage(false)
                    .build();
            readBytes = fis.read(buffer);
            forReadLen = forReadLen - readBytes;
            if (readBytes == -1) {
                fis.close();
                break;
            }
            if (forReadLen == 0) {
                fo.setLastPackage(true);
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
            for (String file : files) {
                serverListView.getItems().add(file);
            }
        });
    }

    public void refreshServerListView(MouseEvent mouseEvent) {
        App.getNetwork().writeAndFlush("/getpath");
    }
}
