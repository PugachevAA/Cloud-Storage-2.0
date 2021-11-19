import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@Slf4j
public class AuthHandler extends SimpleChannelInboundHandler<UserObject> {

    private DB db;
    private Path userPath;
    private static HashMap<String, UserObject> activeUsers = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        activeUsers.put(ctx.channel().id().toString(), new UserObject());
        log.debug("Подключился клиент.");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UserObject userObject) throws Exception {
        db = Server.getDB();
        db.getConnection();
        activeUsers.replace(ctx.channel().id().toString(), userObject);
        boolean logOk = false;
        if (userObject.isNewUser()) {       //Если регистрируется
            if (register(userObject)) {         //Если регистрация прошла
                CommandObject command = new CommandObject("/regok");
                ctx.writeAndFlush(command);
                logOk = true;
            }
        } else {                            //Если авторизуется
            if (auth(userObject)) {             //Если авторизоватся
                CommandObject command = new CommandObject("/authok");
                ctx.writeAndFlush(command);
                logOk = true;
            }
        }

        if (logOk) {    //Если регистрация или авторизация успешны
            activeUsers.get(ctx.channel().id().toString()).setLogOk(true);
            userPath = Path.of(activeUsers.get(ctx.channel().id().toString()).getLogin());//Path.of(Config.ROOT_PATH.toString(),activeUsers.get(ctx.channel().id().toString()).getLogin());
            PathObject pathObject = new PathObject();
            pathObject.setServerPath(userPath.toString());
            FileStorage.createDirectory(userPath.toString());
            pathObject.setFilesList(FileStorage.getFiles(userPath.toString()));
            ctx.writeAndFlush(pathObject);
        }

        db.closeConnection();
    }
    public boolean auth(UserObject user) throws SQLException {
        log.debug("Пробует залогиниться " + user.getLogin());
        ResultSet result = db.read("SELECT * FROM `users` WHERE `login` like '" + user.getLogin() + "'");
        result.next();
        String dbPass = result.getString("password");
        if (dbPass.equals(user.getPassword())) {
            return true;
        }
        return false;
    }
    public boolean register(UserObject newUser) {
        log.debug("Пробует зарегаться " + newUser.getLogin());
        int result = db.update("INSERT INTO `users` (`login`,`password`,`email`)" +
                "VALUES ('" + newUser.getLogin() + "', '" + newUser.getPassword() + "','" + newUser.getEmail() + "');");
        log.debug("insert db status: " + result);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        activeUsers.remove(ctx.channel().id().toString());
    }

    public static HashMap<String, UserObject> getActiveUsers() {
        return activeUsers;
    }
}
