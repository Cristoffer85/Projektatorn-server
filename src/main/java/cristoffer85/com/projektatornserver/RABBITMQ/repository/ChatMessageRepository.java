package cristoffer85.com.projektatornserver.RABBITMQ.repository;

import cristoffer85.com.projektatornserver.RABBITMQ.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestamp(
        String sender1, String receiver1, String sender2, String receiver2
    );
}