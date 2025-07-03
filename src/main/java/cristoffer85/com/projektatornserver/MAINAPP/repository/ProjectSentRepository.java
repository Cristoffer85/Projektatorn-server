package cristoffer85.com.projektatornserver.MAINAPP.repository;

import cristoffer85.com.projektatornserver.MAINAPP.model.ProjectSent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectSentRepository extends MongoRepository<ProjectSent, String> {
    List<ProjectSent> findBySenderAndReceiver(String sender, String receiver);
}