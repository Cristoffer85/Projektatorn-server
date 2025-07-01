package cristoffer85.com.projektatornserver.MAINAPP.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import cristoffer85.com.projektatornserver.MAINAPP.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    void deleteByUsername(String username);
}