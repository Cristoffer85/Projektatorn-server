package cristoffer85.com.projektatornserver.MAINAPP.service;

import cristoffer85.com.projektatornserver.MAINAPP.model.Project;
import cristoffer85.com.projektatornserver.MAINAPP.model.ProjectSent;
import cristoffer85.com.projektatornserver.MAINAPP.repository.ProjectSentRepository;
import cristoffer85.com.projektatornserver.MAINAPP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectSentService {

    @Autowired
    private ProjectSentRepository projectSentRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public ProjectSent sendProject(ProjectSent sent) {
        sent.setSentAt(new java.util.Date());
        ProjectSent saved = projectSentRepository.save(sent);

        Project project = projectService.getProjectById(sent.getProjectId());
        if (project != null) {
            userRepository.findByUsername(sent.getReceiver()).ifPresent(friendUser -> {
                emailService.sendProjectNotificationEmail(
                    friendUser.getEmail(),
                    sent.getSender(),
                    project.getIdea(),
                    project.getId()
                );
            });
        }
        return saved;
    }
}