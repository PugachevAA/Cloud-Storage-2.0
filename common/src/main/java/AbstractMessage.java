import java.io.Serializable;

public class AbstractMessage implements Serializable {
    protected CommandType type;

    protected void setType(CommandType type) {
        this.type = type;
    }

    public CommandType getType() {
        return type;
    }
}
