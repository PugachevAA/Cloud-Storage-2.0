import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class AbstractHandler extends SimpleChannelInboundHandler<AbstractMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractMessage msg) throws Exception {
        switch (msg.getType()) {
            case FILE_OBJECT:
                processFile((FileObject) msg, ctx);
                break;
            case FILE_REQUEST:
                sendFile((FileRequest) msg, ctx);
                break;
            case COMMAND:
                commandReader((CommandObject) msg, ctx);
                break;
            case NEW_FOLDER:
                createFolder((NewFolder) msg, ctx);
                break;
            case DELETE:
                deletePath((DeleteObject) msg, ctx);
                break;
        }
    }

    private void deletePath(DeleteObject msg, ChannelHandlerContext ctx) throws IOException {
        Path delPath = Path.of(Config.ROOT_PATH.toString(), Server.getActiveUsers().get(ctx.channel().id().toString()).getLogin(), msg.getName());
        Files.deleteIfExists(delPath);
        ctx.writeAndFlush(new ListObject(FileStorage.getFiles(Server.getCurrentUserLogin(ctx))));
    }

    private void createFolder(NewFolder msg, ChannelHandlerContext ctx) throws IOException {
        Path newDir = Path.of(Config.ROOT_PATH.toString(), Server.getActiveUsers().get(ctx.channel().id().toString()).getLogin(), msg.getName());
        Files.createDirectory(newDir);
        ctx.writeAndFlush(new ListObject(FileStorage.getFiles(Server.getCurrentUserLogin(ctx))));
    }

    private void commandReader(CommandObject msg, ChannelHandlerContext ctx) {
        switch (msg.getCommand()) {
            case "/getpath":
                ctx.writeAndFlush(new ListObject(FileStorage.getFiles(Server.getCurrentUserLogin(ctx))));
                break;
        }
    }

    private void sendFile(FileRequest msg, ChannelHandlerContext ctx) throws Exception {
        String fileName =  msg.getName();
        String clientPath = Server.getCurrentUserLogin(ctx);

        if (fileName.isEmpty()) {
            return;
        }

        File sendingFile = new File(Path.of(Config.ROOT_PATH.toString(), clientPath, fileName).toString());

        FileInputStream fis = new FileInputStream(sendingFile);

        byte[] buffer = new byte[8192];
        long forReadLen = sendingFile.length();
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
            ctx.writeAndFlush(fo);
            fo.setFirstPackage(false);
            packageNum ++;
            buffer = new byte[8192];
        }
    }

    private void processFile(FileObject file, ChannelHandlerContext ctx) throws Exception {
        Path destPath = Path.of(Server.getCurrentUserLogin(ctx));
        Path destFile = Path.of(destPath.toString(), file.getFileName());
        log.debug("Принимаем файл " + file.getFileName() + " в " + destPath);
        if (file.isFirstPackage()) {
            log.debug("Получен первый пакет " + file.getFileName() );
            Files.deleteIfExists(destFile);
        }

        try (FileOutputStream fos = new FileOutputStream(Paths.get(Config.ROOT_PATH.toString(), destFile.toString()).toFile(),true)) {
            fos.write(file.getBytes());
        }

        if (file.isLastPackage()) {
            log.debug("Получен последний пакет " + file.getFileName() + ", ");
            ListObject path = new ListObject(FileStorage.getFiles(destPath.toString()));
            ctx.writeAndFlush(path);
        }
    }
}
