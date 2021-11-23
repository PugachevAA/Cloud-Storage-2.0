import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class AuthHandler extends SimpleChannelInboundHandler<UserObject> {

    private DB db;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Server.getActiveUsers().put(ctx.channel().id().toString(), new UserObject());
        log.debug("Подключился клиент.");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UserObject userObject) throws Exception {
        db = Server.getDB();
        db.getConnection();
        Server.getActiveUsers().replace(ctx.channel().id().toString(), userObject);
        boolean logOk = false;
        if (userObject.isNewUser()) {       //Если регистрируется
            if (register(userObject)) {         //Если регистрация прошла
                ctx.writeAndFlush(new CommandObject("/regok"));
                logOk = true;
            }
        } else {                            //Если авторизуется
            if (auth(userObject)) {             //Если авторизоватся
                ctx.writeAndFlush(new CommandObject("/authok"));
                logOk = true;
            }
        }

        if (logOk) {    //Если регистрация или авторизация успешны
            Server.getActiveUsers().get(ctx.channel().id().toString()).setLogOk(true);
            String userPath = Server.getActiveUsers().get(ctx.channel().id().toString()).getLogin();
            FileStorage.createDirectory(userPath);
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
    public void channelInactive(ChannelHandlerContext ctx) {
        Server.getActiveUsers().remove(ctx.channel().id().toString());
    }

}
