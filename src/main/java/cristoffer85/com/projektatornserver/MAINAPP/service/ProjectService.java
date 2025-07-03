package cristoffer85.com.projektatornserver.MAINAPP.service;

import cristoffer85.com.projektatornserver.MAINAPP.model.CompletedProject;
import cristoffer85.com.projektatornserver.MAINAPP.model.PendingProject;
import cristoffer85.com.projektatornserver.MAINAPP.model.Project;
import cristoffer85.com.projektatornserver.MAINAPP.repository.CompletedProjectRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.PendingProjectRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.ProjectRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PendingProjectRepository pendingProjectRepository;

    @Autowired
    private CompletedProjectRepository completedProjectRepository;

    // ########### Regular Projects repo ##############
    public List<Project> getProjectsForUser(String username) {
        return repository.findByOwnerOrFriend(username, username);
    }

    public Project addProject(Project project) {
        return repository.save(project);
    }

    public void removeProject(String id) {
        repository.deleteById(id);
    }

    // ########### Pending Projects repo ##############
    public PendingProject sendProject(PendingProject pendingProject) {
        PendingProject saved = pendingProjectRepository.save(pendingProject);
        userRepository.findByUsername(saved.getFriend()).ifPresent(friendUser -> {
            emailService.sendProjectNotificationEmail(
                friendUser.getEmail(),
                saved.getOwner(),
                saved.getIdea(),
                saved.getId()
            );
        });
        return saved;
    }

    public List<PendingProject> getPendingProjectsForUser(String username) {
        return pendingProjectRepository.findAll()
            .stream()
            .filter(p -> p.getFriend().equals(username))
            .collect(Collectors.toList());
    }

    public void removePendingProject(String id) {
        pendingProjectRepository.deleteById(id);
    }

    // ########### Completed Projects repo ##############
    public CompletedProject addCompletedProject(CompletedProject completedProject) {
        return completedProjectRepository.save(completedProject);
    }

    public List<CompletedProject> getCompletedProjects(String owner) {
        return completedProjectRepository.findByOwner(owner);
    }
}