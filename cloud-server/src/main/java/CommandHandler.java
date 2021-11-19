import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class CommandHandler extends SimpleChannelInboundHandler<String> {

    private final SimpleDateFormat format;

    public CommandHandler() {
        format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        log.debug("StringHandler. received: {}", s);
        PathObject pathObject = new PathObject();
        pathObject.setServerPath(AuthHandler.getActiveUsers().get(ctx.channel().id().toString()).getLogin());
        pathObject.setFilesList(FileStorage.getFiles(AuthHandler.getActiveUsers().get(ctx.channel().id().toString()).getLogin()));
        if (s.equals("/getpath")) {
            ctx.writeAndFlush(pathObject);
            log.debug(pathObject.getFilesList().toString());
        }
    }
}
