package cristoffer85.com.projektatornserver.MAINAPP.repository;

import cristoffer85.com.projektatornserver.MAINAPP.model.CompletedProject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CompletedProjectRepository extends MongoRepository<CompletedProject, String> {
    List<CompletedProject> findByOwner(String owner);
}