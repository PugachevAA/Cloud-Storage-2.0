import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileHandler extends SimpleChannelInboundHandler<FileObject> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileObject file) throws Exception {
        Path destPath = Path.of(Config.ROOT_PATH.toString(), file.getDestPath());
        Path destFile = Path.of(destPath.toString(), file.getFileName());
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
            PathObject path = new PathObject();
            path.setFilesList(FileStorage.getFiles(file.getDestPath()));
            ctx.writeAndFlush(path);
        }
    }
}
