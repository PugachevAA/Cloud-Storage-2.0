import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserObject implements Serializable {
    private String login;
    private String email;
    private String password;
    private boolean isNewUser;
    private boolean isLogOk;
}
