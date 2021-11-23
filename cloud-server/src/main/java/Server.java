import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class Server {
    private static Network network;
    private static DB db;
    private static HashMap<String, UserObject> activeUsers = new HashMap<>();

    public static void main(String[] args) {

        new FileStorage();
        db = new DB();
        network = new Network();
    }

    public static DB getDB() {return db;}

    public static HashMap<String, UserObject> getActiveUsers() {return activeUsers;}

    public static String getCurrentUserLogin (ChannelHandlerContext ctx) {
        return activeUsers.get(ctx.channel().id().toString()).getLogin();
    }
}
