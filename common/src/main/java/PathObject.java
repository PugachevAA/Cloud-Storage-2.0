import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PathObject implements Serializable {
    private String serverPath;
    private List<String> filesList;
}
