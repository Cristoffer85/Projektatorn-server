package cristoffer85.com.projektatornserver.MAINAPP.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {
    private String username;
    private String password;
    private String email;

    @Override
    public String toString() {
        return "Registration info: Username: " + this.username + " Password: " + this.password;
    }
}