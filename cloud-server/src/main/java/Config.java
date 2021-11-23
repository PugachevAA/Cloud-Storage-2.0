import java.nio.file.Path;

public class Config {
    public static final int PORT = 8989;
    public static final String SQL_URL = "jdbc:mysql://localhost:3306/cloud-storage";
    public static final String SQL_USER = "sa";
    public static final String SQL_PASS = "Adrenalin123";
    public static final Path ROOT_PATH = Path.of("root");
}
