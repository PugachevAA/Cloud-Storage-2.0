import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class AbstractHandler extends SimpleChannelInboundHandler<AbstractMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) throws Exception {
        switch (msg.getType()) {
            case FILE_OBJECT:
                requestFile((FileObject) msg);
                break;
            case FILES_LIST:
                updateList((ListObject) msg);
                break;
            case COMMAND:
                readCommand((CommandObject) msg);
                break;
        }


    }

    private void readCommand(CommandObject command) {
        if (command.getCommand().equals("/authok") || command.getCommand().equals("/regok")) {
            Platform.runLater(() -> {
                try {
                    App.openClientStage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void requestFile(FileObject file) throws Exception {
        Path destPath = Paths.get(App.getClientPath().getPath());
        Path destFile = Paths.get(destPath.toString(), file.getFileName());
        log.debug("Принимаем файл " + file.getFileName() + " в " + destPath);
        if (file.isFirstPackage()) {
            log.debug("Получен первый пакет " + file.getFileName() );
            Files.deleteIfExists(destFile);
        }

        try (FileOutputStream fos = new FileOutputStream(destFile.toFile(),true)) {
            fos.write(file.getBytes());
        }

        if (file.isLastPackage()) {
            log.debug("Получен последний пакет " + file.getFileName() + ", ");
            App.getClientController().updateClientListView();
        }
    }

    private void updateList(ListObject path) {
        log.debug("Приняли PathObject");
        App.getClientController().writeToServerListView(path.getFilesList());
    }
}
