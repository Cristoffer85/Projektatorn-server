package cristoffer85.com.projektatornserver.MAINAPP.repository;

import cristoffer85.com.projektatornserver.MAINAPP.model.PasswordReset;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordResetRepository extends MongoRepository<PasswordReset, String> {
    Optional<PasswordReset> findByToken(String token);
}