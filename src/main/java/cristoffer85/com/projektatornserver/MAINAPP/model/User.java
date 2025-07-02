package cristoffer85.com.projektatornserver.MAINAPP.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;
    private String username;
    private String password;
    private boolean verified = false;

    private String email;
    private String pendingEmail;
    
    private String forestanimal;
    private String soursnack;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthday;

    private String avatar;

    //------ Authority/roles ------
    @DBRef
    private Set<Role> authorities = new HashSet<>();

    @Override
    @JsonIgnore
    public Set<Role> getAuthorities() {
        return this.authorities;
    }
    // -----------------------------

    // ------ Account status methods ------
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}