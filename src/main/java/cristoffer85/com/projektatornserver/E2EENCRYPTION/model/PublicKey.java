package cristoffer85.com.projektatornserver.E2EENCRYPTION.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/*
    Public keys are used to encrypt the messages sent to the user.
    How it’s used:
    - Others use your public key to encrypt messages to you == Hence why it is called public.
    - You cannot decrypt with a public key.
*/

@Document(collection = "public_keys")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicKey {
    @Id
    private String username;
    private String jwkJson;
}