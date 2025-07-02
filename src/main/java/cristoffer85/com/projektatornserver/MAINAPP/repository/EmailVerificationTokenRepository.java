package cristoffer85.com.projektatornserver.MAINAPP.repository;

import cristoffer85.com.projektatornserver.MAINAPP.model.EmailVerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.Optional;

public interface EmailVerificationTokenRepository extends MongoRepository<EmailVerificationToken, String> {
    Optional<EmailVerificationToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteByExpiryBefore(Instant expiry);
}