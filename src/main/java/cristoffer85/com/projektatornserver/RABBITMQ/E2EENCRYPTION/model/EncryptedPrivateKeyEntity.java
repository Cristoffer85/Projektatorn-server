package cristoffer85.com.projektatornserver.RABBITMQ.E2EENCRYPTION.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "encrypted_private_keys")
public class EncryptedPrivateKeyEntity {
    @Id
    private String username;
    private String ciphertext;
    private String salt;
    private String iv;

    public EncryptedPrivateKeyEntity() {
    }

    public EncryptedPrivateKeyEntity(String username, String ciphertext, String salt, String iv) {
        this.username = username;
        this.ciphertext = ciphertext;
        this.salt = salt;
        this.iv = iv;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getCiphertext() {
        return ciphertext;
    }
    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getIv() {
        return iv;
    }
    public void setIv(String iv) {
        this.iv = iv;
    }
}