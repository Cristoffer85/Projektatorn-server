package cristoffer85.com.projektatornserver.MAINAPP.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import cristoffer85.com.projektatornserver.MAINAPP.model.Admin;

import java.util.Optional;

public interface AdminRepository extends MongoRepository<Admin, String> {
    Optional<Admin> findByUsername(String username);
}