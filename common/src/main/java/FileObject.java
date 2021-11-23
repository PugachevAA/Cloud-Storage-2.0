import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.nio.file.Files;
import java.nio.file.Path;

@Data
@AllArgsConstructor
@Builder
public class FileObject extends AbstractMessage {

    private String fileName;
    private int n;
    private long size;
    private byte[] bytes;
    private boolean isFirstPackage;
    private boolean isLastPackage;

    public FileObject(Path path) throws Exception {
        setType(CommandType.FILE_OBJECT);
        fileName = path.getFileName().toString();
        size = Files.size(path);
        isFirstPackage = true;
        isLastPackage = false;
        n = 0;

    }
}
