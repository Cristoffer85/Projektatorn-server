package cristoffer85.com.projektatornserver.RABBITMQ.E2EENCRYPTION.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "public_keys")
public class PublicKeyEntity {
    @Id
    private String username;
    private String jwkJson;

    public PublicKeyEntity() {
    }

    public PublicKeyEntity(String username, String jwkJson) {
        this.username = username;
        this.jwkJson = jwkJson;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJwkJson() {
        return jwkJson;
    }

    public void setJwkJson(String jwkJson) {
        this.jwkJson = jwkJson;
    }
}