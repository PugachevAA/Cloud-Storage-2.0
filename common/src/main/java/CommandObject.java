import lombok.Data;

@Data
public class CommandObject extends AbstractMessage {
    private String command;

    public CommandObject(String command) {
        setType(CommandType.COMMAND);
        this.command = command;
    }
}
