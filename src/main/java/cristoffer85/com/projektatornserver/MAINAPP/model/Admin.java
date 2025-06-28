package cristoffer85.com.projektatornserver.MAINAPP.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "admins")
public class Admin implements UserDetails {

    @Id
    private String id;

    private String username;
    private String password;
    private String position;
    private String name;

    @DBRef
    private Set<Role> authorities = new HashSet<>();

    @JsonIgnore
    @Override
    public Set<Role> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}