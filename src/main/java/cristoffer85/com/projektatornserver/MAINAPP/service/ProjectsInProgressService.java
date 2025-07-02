package cristoffer85.com.projektatornserver.MAINAPP.service;

import cristoffer85.com.projektatornserver.MAINAPP.model.ProjectsInProgress;
import cristoffer85.com.projektatornserver.MAINAPP.repository.ProjectsInProgressRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectsInProgressService {
    @Autowired
    private ProjectsInProgressRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public List<ProjectsInProgress> getProjectsForUser(String username) {
        return repository.findByOwnerOrFriend(username, username);
    }

    public ProjectsInProgress addProject(ProjectsInProgress project) {
        ProjectsInProgress saved = repository.save(project);

        // Find friend's email
        userRepository.findByUsername(project.getFriend()).ifPresent(friendUser -> {
            emailService.sendProjectNotificationEmail(
                friendUser.getEmail(),
                project.getOwner(),
                project.getIdea()
            );
        });

        return saved;
    }

    public void removeProject(String id) {
        repository.deleteById(id);
    }
}
