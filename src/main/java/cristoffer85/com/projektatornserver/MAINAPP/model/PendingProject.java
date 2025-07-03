package cristoffer85.com.projektatornserver.MAINAPP.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "pending_projects")
public class PendingProject {
    @Id
    private String id;
    private String owner;
    private String friend;
    private String idea;
}