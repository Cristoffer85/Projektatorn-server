package cristoffer85.com.projektatornserver.MAINAPP.repository;

import cristoffer85.com.projektatornserver.MAINAPP.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> findByOwnerOrFriend(String owner, String friend);
}