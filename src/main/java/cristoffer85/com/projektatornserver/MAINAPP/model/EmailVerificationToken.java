package cristoffer85.com.projektatornserver.MAINAPP.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "email_verification_tokens")
public class EmailVerificationToken {
    @Id
    private String id;
    private String token;
    private String username;
    private Instant expiry;
}