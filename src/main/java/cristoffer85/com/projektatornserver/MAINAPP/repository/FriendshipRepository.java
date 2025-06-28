package cristoffer85.com.projektatornserver.MAINAPP.repository;

import cristoffer85.com.projektatornserver.MAINAPP.model.Friendship;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends MongoRepository<Friendship, String> {
    List<Friendship> findByUserAndStatus(User user, String status);
    List<Friendship> findByFriendAndStatus(User friend, String status);
    List<Friendship> findByUserAndFriendAndStatus(User user, User friend, String status);
    void deleteByUserAndFriend(User user, User friend);
}