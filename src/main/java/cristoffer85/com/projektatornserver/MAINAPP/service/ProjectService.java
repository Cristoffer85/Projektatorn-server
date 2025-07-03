package cristoffer85.com.projektatornserver.MAINAPP.service;

import cristoffer85.com.projektatornserver.MAINAPP.model.CompletedProject;
import cristoffer85.com.projektatornserver.MAINAPP.model.Project;
import cristoffer85.com.projektatornserver.MAINAPP.repository.CompletedProjectRepository;
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

    @Autowired
    private EmailService emailService;

    @Autowired
    private CompletedProjectRepository completedProjectRepository;

    public List<Project> getProjectsForUser(String username) {
        return repository.findByOwnerOrFriend(username, username);
    }

    public Project addProject(Project project) {
        return repository.save(project);
    }

    public Project sendProject(Project project) {
        Project saved = repository.save(project);

        userRepository.findByUsername(project.getFriend()).ifPresent(friendUser -> {
            emailService.sendProjectNotificationEmail(
                friendUser.getEmail(),
                project.getOwner(),
                project.getIdea(),
                saved.getId()
            );
        });

        return saved;
    }

    public CompletedProject addCompletedProject(CompletedProject completedProject) {
        return completedProjectRepository.save(completedProject);
    }

    public List<CompletedProject> getCompletedProjects(String owner) {
        return completedProjectRepository.findByOwner(owner);
    }

    public void removeProject(String id) {
        repository.deleteById(id);
    }
}
