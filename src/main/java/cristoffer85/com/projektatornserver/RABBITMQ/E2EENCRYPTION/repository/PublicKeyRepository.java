package cristoffer85.com.projektatornserver.RABBITMQ.E2EENCRYPTION.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import cristoffer85.com.projektatornserver.RABBITMQ.E2EENCRYPTION.model.PublicKeyEntity;

public interface PublicKeyRepository extends MongoRepository<PublicKeyEntity, String> {
}