package cristoffer85.com.projektatornserver.MAINAPP.service;

import cristoffer85.com.projektatornserver.MAINAPP.model.ProjectsInProgress;
import cristoffer85.com.projektatornserver.MAINAPP.repository.ProjectsInProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectsInProgressService {
    @Autowired
    private ProjectsInProgressRepository repository;

    public List<ProjectsInProgress> loadProjects(String username) {
        return repository.findByOwner(username);
    }

    public ProjectsInProgress addProject(ProjectsInProgress project) {
        return repository.save(project);
    }

    public void removeProject(String id) {
        repository.deleteById(id);
    }
}
