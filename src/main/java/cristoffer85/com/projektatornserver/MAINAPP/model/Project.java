package cristoffer85.com.projektatornserver.MAINAPP.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "projects_in_progress")
public class Project {
    @Id
    private String id;
    private String friend;
    private String idea;
    private String owner;
}
