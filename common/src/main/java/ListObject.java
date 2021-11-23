import lombok.Data;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Data
public class ListObject extends AbstractMessage {
    private List<String> filesList;

    public ListObject(List<String> filesList) {
        setType(CommandType.FILES_LIST);
        this.filesList = filesList;
    }
    public ListObject(Path path) {
        setType(CommandType.FILES_LIST);
        this.filesList = Arrays.asList(path.toFile().list());
    }
}
