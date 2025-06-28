package cristoffer85.com.projektatornserver.RABBITMQ.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import cristoffer85.com.projektatornserver.RABBITMQ.dto.MsgDto;
import cristoffer85.com.projektatornserver.RABBITMQ.model.ChatMessage;
import cristoffer85.com.projektatornserver.RABBITMQ.repository.ChatMessageRepository;
import cristoffer85.com.projektatornserver.RABBITMQ.service.MsgConsumer;
import cristoffer85.com.projektatornserver.RABBITMQ.service.MsgProducer;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;
import java.util.Map;

@RestController
@RequestMapping("/rabbitmq")
public class MessageController {

    @Autowired
    private MsgProducer msgProducer;

    @Autowired
    private MsgConsumer msgConsumer;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @PostMapping("/publish")
    public String pushMsgIntoQueue(@RequestBody MsgDto msgDTO) {
        msgProducer.sendMsg(msgDTO);
        return "Message sent from " + msgDTO.getSender() + " to " + msgDTO.getReceiver() + ": " + msgDTO.getMessage();
    }

    @GetMapping("/subscribe/{user1}/{user2}")
    public List<String> getMessagesBetweenUsers(
            @PathVariable String user1, 
            @PathVariable String user2, 
            @RequestHeader("X-Username") String loggedInUsername,
            @RequestParam(name = "markAsRead", defaultValue = "true") boolean markAsRead) {
        if (!user1.equals(loggedInUsername) && !user2.equals(loggedInUsername)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, 
                "You can only view messages involving yourself.");
        }

        String queueKey = user1.compareTo(user2) < 0 
            ? user1 + "_" + user2 
            : user2 + "_" + user1;

        List<String> messages = msgConsumer.getMessages(queueKey);
        
        if (markAsRead) {
            msgConsumer.markMessagesAsRead(queueKey, loggedInUsername);
        }
        
        return messages;
    }

    @GetMapping("/history")
    public List<ChatMessage> getChatHistory(
        @RequestParam String user1,
        @RequestParam String user2
    ) {
        return chatMessageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderByTimestamp(
            user1, user2, user1, user2
        );
    }

    @GetMapping("/unread/{username}")
    public int getUnreadMessagesCount(@PathVariable String username) {
        return msgConsumer.getUnreadMessagesCount(username);
    }

    @GetMapping("/unread/senders/{username}")
    public Set<String> getUnreadMessagesSenders(@PathVariable String username) {
        return msgConsumer.getUnreadMessagesSenders(username);
    }

    @PostMapping("/mark-read")
    public void markMessagesAsRead(@RequestBody Map<String, String> payload) {
        String user1 = payload.get("user1");
        String user2 = payload.get("user2");
        if (user1 == null || user2 == null) return;
        String queueKey = user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
        msgConsumer.markMessagesAsRead(queueKey, user1);
    }
}