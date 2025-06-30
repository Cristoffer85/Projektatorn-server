package cristoffer85.com.projektatornserver.RABBITMQ.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cristoffer85.com.projektatornserver.RABBITMQ.dto.MsgDto;
import cristoffer85.com.projektatornserver.RABBITMQ.model.ChatMessage;
import cristoffer85.com.projektatornserver.RABBITMQ.repository.ChatMessageRepository;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;

@Service
public class MsgConsumer {
    private final Map<String, List<String>> messages = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Integer>> unreadMessagesCountBySender = new ConcurrentHashMap<>();

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @RabbitListener(queues = "chatQueue")
        public void receiveMessage(MsgDto msgDTO) {
            try {
                // Save to DB
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setSender(msgDTO.getSender());
                chatMessage.setReceiver(msgDTO.getReceiver());
                chatMessage.setContentForReceiver(msgDTO.getContentForReceiver());
                chatMessage.setContentForSender(msgDTO.getContentForSender());
                chatMessage.setTimestamp(new Date());
                chatMessageRepository.save(chatMessage);

                String queueKey = msgDTO.getSender().compareTo(msgDTO.getReceiver()) < 0
                    ? msgDTO.getSender() + "_" + msgDTO.getReceiver()
                    : msgDTO.getReceiver() + "_" + msgDTO.getSender();

                messages.computeIfAbsent(queueKey, k -> new ArrayList<>())
                        .add(msgDTO.getSender() + ": [encrypted]");

                unreadMessagesCountBySender.computeIfAbsent(msgDTO.getReceiver(), k -> new ConcurrentHashMap<>())
                        .merge(msgDTO.getSender(), 1, Integer::sum);

                System.out.println("Received message for " + queueKey + ": " + msgDTO.getSender() + " -> " + msgDTO.getReceiver());
            } catch (Exception e) {
                System.out.println("Failed to process message: " + e.getMessage());
            }
        }

    public List<String> getMessages(String queueKey) {
        return new ArrayList<>(messages.getOrDefault(queueKey, new ArrayList<>()));
    }

    // Sum all unread messages across all senders for this user.
    public int getUnreadMessagesCount(String username) {
        Map<String, Integer> map = unreadMessagesCountBySender.get(username);
        if (map == null) return 0;
        return map.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Return the set of senders who have unread messages.
    public Set<String> getUnreadMessagesSenders(String username) {
        Map<String, Integer> map = unreadMessagesCountBySender.get(username);
        if (map == null) return ConcurrentHashMap.newKeySet();
        return map.keySet();
    }

    // Mark messages as read only for a specific conversation.
    public void markMessagesAsRead(String queueKey, String username) {
        // Extract the other participant from the queueKey.
        String[] parts = queueKey.split("_");
        if (parts.length != 2) return;
        String otherUser = parts[0].equals(username) ? parts[1] : parts[0];

        Map<String, Integer> map = unreadMessagesCountBySender.get(username);
        if (map != null) {
            map.remove(otherUser);
            if (map.isEmpty()) {
                unreadMessagesCountBySender.remove(username);
            }
        }
    }
}