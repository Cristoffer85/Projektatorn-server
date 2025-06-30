package cristoffer85.com.projektatornserver.RABBITMQ.E2EENCRYPTION.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cristoffer85.com.projektatornserver.RABBITMQ.E2EENCRYPTION.model.EncryptedPrivateKeyEntity;

public interface EncryptedPrivateKeyRepository extends MongoRepository<EncryptedPrivateKeyEntity, String> {
}