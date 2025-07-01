package cristoffer85.com.projektatornserver.MAINAPP.service;

import cristoffer85.com.projektatornserver.MAINAPP.dto.SendOnlyUserNameDTO;
import cristoffer85.com.projektatornserver.MAINAPP.model.Friendship;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import cristoffer85.com.projektatornserver.MAINAPP.repository.FriendshipRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Friendship> getFriendRequests(User user) {
        return friendshipRepository.findByFriendIdAndStatus(user.getId(), "PENDING");
    }

    public List<SendOnlyUserNameDTO> getFriends(User user) {
        return friendshipRepository.findByUserIdAndStatus(user.getId(), "ACCEPTED").stream()
                .map(friendship -> new SendOnlyUserNameDTO(friendship.getFriend().getUsername()))
                .collect(Collectors.toList());
    }

    public SendOnlyUserNameDTO sendFriendRequest(String senderUsername, String receiverUsername) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Check if a friendship already exists with ACCEPTED status
        List<Friendship> existingFriendships = friendshipRepository
                .findByUserIdAndFriendIdAndStatus(sender.getId(), receiver.getId(), "ACCEPTED");
        if (!existingFriendships.isEmpty()) {
            throw new RuntimeException("You are already friends with this user");
        }

        // Check if a pending request already exists
        List<Friendship> existingRequests = friendshipRepository
                .findByUserIdAndFriendIdAndStatus(sender.getId(), receiver.getId(), "PENDING");
        if (!existingRequests.isEmpty()) {
            throw new RuntimeException("Friend request already sent");
        }

        Friendship friendship = new Friendship();
        friendship.setUser(sender);
        friendship.setFriend(receiver);
        friendship.setStatus("PENDING");
        friendshipRepository.save(friendship);

        // Return a DTO with the receiver's username
        return new SendOnlyUserNameDTO(receiver.getUsername());
    }

    public List<Friendship> getOutgoingFriendRequests(User user) {
        return friendshipRepository.findByUserIdAndStatus(user.getId(), "PENDING");
    }

    public Friendship respondToFriendRequest(String requestId, boolean accept) {
        Friendship friendship = friendshipRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        if (accept) {
            friendship.setStatus("ACCEPTED");

            // Create reciprocal friendship
            Friendship reciprocal = new Friendship();
            reciprocal.setUser(friendship.getFriend());
            reciprocal.setFriend(friendship.getUser());
            reciprocal.setStatus("ACCEPTED");
            friendshipRepository.save(reciprocal);
        } else {
            friendship.setStatus("DECLINED");
        }

        return friendshipRepository.save(friendship);
    }

    public void withdrawFriendRequest(String fromUsername, String toUsername) {
        // Find the pending friend request from fromUsername to toUsername and delete it
        // (Assuming you have a repository and model for friend requests)
        friendshipRepository.deleteByUserUsernameAndFriendUsernameAndStatus(
            fromUsername, toUsername, "PENDING"
        );
    }

    public void removeFriend(String username, String friendUsername) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        // Delete the friendship records for both users
        friendshipRepository.deleteByUserIdAndFriendId(user.getId(), friend.getId());
        friendshipRepository.deleteByUserIdAndFriendId(friend.getId(), user.getId());
    }
}