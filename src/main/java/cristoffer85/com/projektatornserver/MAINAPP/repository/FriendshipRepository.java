package cristoffer85.com.projektatornserver.MAINAPP.repository;

import cristoffer85.com.projektatornserver.MAINAPP.model.Friendship;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends MongoRepository<Friendship, String> {
    List<Friendship> findByUserIdAndStatus(String userId, String status);
    List<Friendship> findByFriendIdAndStatus(String friendId, String status);
    List<Friendship> findByUserIdAndFriendIdAndStatus(String userId, String friendId, String status);
    void deleteByUserIdAndFriendId(String userId, String friendId);
    void deleteByUserIdAndFriendIdAndStatus(String userId, String friendId, String status);
}