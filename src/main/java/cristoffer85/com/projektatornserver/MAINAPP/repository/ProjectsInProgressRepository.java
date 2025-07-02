package cristoffer85.com.projektatornserver.MAINAPP.repository;

import cristoffer85.com.projektatornserver.MAINAPP.model.ProjectsInProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProjectsInProgressRepository extends MongoRepository<ProjectsInProgress, String> {
    List<ProjectsInProgress> findByOwner(String owner);
}