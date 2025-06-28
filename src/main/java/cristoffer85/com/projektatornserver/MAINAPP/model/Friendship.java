package cristoffer85.com.projektatornserver.MAINAPP.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "friendships")
public class Friendship {

    @Id
    private String id;

    @DBRef
    private User user;

    @DBRef
    private User friend;

    private String status; // PENDING, ACCEPTED, DECLINED
}