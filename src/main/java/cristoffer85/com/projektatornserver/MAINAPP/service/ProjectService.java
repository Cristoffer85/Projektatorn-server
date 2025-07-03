package cristoffer85.com.projektatornserver.MAINAPP.service;

import cristoffer85.com.projektatornserver.MAINAPP.model.Project;
import cristoffer85.com.projektatornserver.MAINAPP.repository.ProjectRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository repository;

    @Autowired
    private UserRepository userRepository;

    public List<Project> getProjectsForUser(String username) {
        return repository.findByOwnerOrFriend(username, username);
    }

    public Project addProject(Project project) {
        return repository.save(project);
    }

    public Project getProjectById(String id) {
        return repository.findById(id).orElse(null);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void removeProject(String id) {
        repository.deleteById(id);
    }
}