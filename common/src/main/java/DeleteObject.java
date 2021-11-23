import lombok.Data;

@Data
public class DeleteObject extends AbstractMessage{
    private String name;

    public DeleteObject(String name) {
        setType(CommandType.DELETE);
        this.name = name;
    }
}
