import lombok.Data;

@Data
public class NewFolder extends AbstractMessage{
    private String name;

    public NewFolder(String name) {
        setType(CommandType.NEW_FOLDER);
        this.name = name;
    }
}
