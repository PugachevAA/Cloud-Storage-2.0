import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class FileObject implements Serializable {
    private String fileName;
    private String destPath;
    private int n;
    private long size;
    private byte[] bytes;
    private boolean isFirstPackage;
    private boolean isLastPackage;
}
