import lombok.extern.slf4j.Slf4j;

public class FileRequest extends AbstractMessage{
    private String name;

    public FileRequest(String name) {
        setType(CommandType.FILE_REQUEST);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
