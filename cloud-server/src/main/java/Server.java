import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Server {
    private static Network network;
    private static DB db;
    private static FileStorage fs;

    public static void main(String[] args) {

        fs = new FileStorage();
        db = new DB();
        network = new Network();
    }



    public static Network getNetwork() {
        return network;
    }

    public static DB getDB() {
        return db;
    }
}
