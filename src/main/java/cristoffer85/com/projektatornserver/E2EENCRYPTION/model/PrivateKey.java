package cristoffer85.com.projektatornserver.E2EENCRYPTION.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/* 
    Private keys are used to decrypt the messages sent to you.
    How it’s used:
    - You use your private key to decrypt messages that were encrypted with your public key.
    - Never shared or sent to anyone else == hence why it is private.

    All private keys are encrypted before being stored, can never be read or decrypted by the server. 
    Storing the encrypted private keys here:
    - allows for better management and retrieval of the key, if user for example change device or browser,
    - or, in worst cases lose their private key on device making old messages unreadable forever

    They are purely and only decrypted by the client, making it true e2ee (i hope and think - but there are surely some experts who can prompt me otherwise..)
    ..even if the app signal have even one step harder where theres only one key per user, and not per device/browser, as i have also learned right (i also here hope).
*/

@Document(collection = "encrypted_private_keys")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivateKey {
    @Id
    private String username;
    private String ciphertext;
    private String salt;
    private String iv;
}