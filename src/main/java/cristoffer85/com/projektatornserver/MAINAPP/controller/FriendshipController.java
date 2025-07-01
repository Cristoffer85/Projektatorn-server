package cristoffer85.com.projektatornserver.MAINAPP.controller;

import cristoffer85.com.projektatornserver.MAINAPP.dto.FriendRequestDTO;
import cristoffer85.com.projektatornserver.MAINAPP.dto.SendOnlyUserNameDTO;
import cristoffer85.com.projektatornserver.MAINAPP.model.Friendship;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import cristoffer85.com.projektatornserver.MAINAPP.service.FriendshipService;
import cristoffer85.com.projektatornserver.MAINAPP.service.UserService;
import cristoffer85.com.projektatornserver.RABBITMQ.repository.ChatMessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/friendship")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @GetMapping("/friend-requests/{username}")
    public List<FriendRequestDTO> getFriendRequests(@PathVariable String username) {
        User user = userService.getOneUser(username);
        return friendshipService.getFriendRequests(user).stream()
                .map(friendship -> new FriendRequestDTO(
                    friendship.getId(),
                    friendship.getUser().getUsername()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/friends/{username}")
    public List<SendOnlyUserNameDTO> getFriends(@PathVariable String username) {
        User user = userService.getOneUser(username);
        return friendshipService.getFriends(user);
    }

    @PostMapping("/send-request")
    public SendOnlyUserNameDTO sendFriendRequest(@RequestParam String senderUsername, @RequestParam String receiverUsername) {
        return friendshipService.sendFriendRequest(senderUsername, receiverUsername);
    }

    @GetMapping("/outgoing-requests/{username}")
    public List<SendOnlyUserNameDTO> getOutgoingRequests(@PathVariable String username) {
        User user = userService.getOneUser(username);
        return friendshipService.getOutgoingFriendRequests(user).stream()
            .map(friendship -> new SendOnlyUserNameDTO(friendship.getFriend().getUsername()))
            .collect(Collectors.toList());
    }

    @PutMapping("/respond-request/{requestId}")
    public Friendship respondToFriendRequest(@PathVariable String requestId, @RequestParam boolean accept) {
        return friendshipService.respondToFriendRequest(requestId, accept);
    }

    @DeleteMapping("/remove-friend")
    public void removeFriend(@RequestParam String username, @RequestParam String friendUsername) {
        friendshipService.removeFriend(username, friendUsername);
        chatMessageRepository.deleteBySenderAndReceiverOrReceiverAndSender(username, friendUsername, username, friendUsername);
    }
}