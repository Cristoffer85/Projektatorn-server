package cristoffer85.com.projektatornserver.MAINAPP.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "password_reset_tokens")
public class PasswordReset {
    @Id
    private String token;
    private String username;
    private Instant expiry;
}
