package cristoffer85.com.projektatornserver.MAINAPP.dto;

import cristoffer85.com.projektatornserver.MAINAPP.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private Object user;
    private String token;
    private Role role;
}
