package cristoffer85.com.projektatornserver.RABBITMQ.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@Document(collection = "chat_messages")
public class ChatMessage {
    @Id
    private String id;

    private String sender;
    private String receiver;
    private String content;
    private Date timestamp = new Date();
}