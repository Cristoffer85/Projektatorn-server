package cristoffer85.com.projektatornserver.MAINAPP.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "projects_sent")
public class ProjectSent {
    @Id
    private String projectId;
    private String sender;
    private String receiver;
    private List<String> ideas;
    private Date sentAt;
}