package cristoffer85.com.projektatornserver.E2EENCRYPTION.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cristoffer85.com.projektatornserver.E2EENCRYPTION.model.PrivateKey;

public interface PrivateKeyRepository extends MongoRepository<PrivateKey, String> {
}