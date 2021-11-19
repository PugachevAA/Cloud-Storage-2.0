import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandHandler extends SimpleChannelInboundHandler<CommandObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, CommandObject command) throws Exception {
        log.debug("Зашли в хендлер команд");
        if (command.getCommand().equals("/authok") || command.getCommand().equals("/regok")) {
            Parent parent = FXMLLoader.load(getClass().getResource("/client.fxml"));
            Platform.runLater(() -> {
                App.newStage(new Scene(parent));
            });
        }
    }
}
