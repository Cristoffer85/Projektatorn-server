package cristoffer85.com.projektatornserver.E2EENCRYPTION.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cristoffer85.com.projektatornserver.E2EENCRYPTION.model.PublicKey;

public interface PublicKeyRepository extends MongoRepository<PublicKey, String> {
}