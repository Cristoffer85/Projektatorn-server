package cristoffer85.com.projektatornserver.MAINAPP.repository;

import cristoffer85.com.projektatornserver.MAINAPP.model.PendingProject;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PendingProjectRepository extends MongoRepository<PendingProject, String> {
}