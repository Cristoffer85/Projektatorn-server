package cristoffer85.com.projektatornserver.MAINAPP.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Data
@Document(collection = "roles")
public class Role implements GrantedAuthority {

    @Id
    private String id;

    private String authority;

    public Role(String authority) {
        this.authority = authority;
    }

    public Role() {
        super();
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}