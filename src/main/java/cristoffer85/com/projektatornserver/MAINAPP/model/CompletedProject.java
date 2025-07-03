package cristoffer85.com.projektatornserver.MAINAPP.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "completed_projects")
public class CompletedProject {
    @Id
    private String id;
    private String friend;
    private String idea;
    private String owner;
    private long completedAt;
}