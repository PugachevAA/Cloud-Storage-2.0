import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class PathHandler extends SimpleChannelInboundHandler<PathObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, PathObject pathObject){
        log.debug("Приняли PathObject");
        //App.setCurrentServerPath(pathObject.getServerPath());
        App.getClientController().writeToServerListView(pathObject.getFilesList());


    }
}
