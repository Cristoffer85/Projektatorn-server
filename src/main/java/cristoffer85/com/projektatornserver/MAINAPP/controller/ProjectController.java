package cristoffer85.com.projektatornserver.MAINAPP.controller;

import cristoffer85.com.projektatornserver.MAINAPP.model.Project;
import cristoffer85.com.projektatornserver.MAINAPP.model.ProjectSent;
import cristoffer85.com.projektatornserver.MAINAPP.repository.ProjectSentRepository;
import cristoffer85.com.projektatornserver.MAINAPP.service.ProjectSentService;
import cristoffer85.com.projektatornserver.MAINAPP.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService service;

    @Autowired
    private ProjectSentRepository projectSentRepository;

    @Autowired
    private ProjectSentService projectSentService;

    @GetMapping("/load")
    public List<Project> loadProjects(@RequestParam String username) {
        return service.getProjectsForUser(username);
    }

    @PostMapping("/add")
    public Project addProject(@RequestBody Project project) {
        return service.addProject(project);
    }

    @DeleteMapping("/remove/{id}")
    public void removeProject(@PathVariable String id) {
        service.removeProject(id);
    }

    @PostMapping("/sent")
    public ProjectSent sendProjects(@RequestBody ProjectSent sent) {
        return projectSentService.sendProject(sent);
    }

    @GetMapping("/sent")
    public List<ProjectSent> getSentProjects(
            @RequestParam String sender,
            @RequestParam String receiver
    ) {
        return projectSentRepository.findBySenderAndReceiver(sender, receiver);
    }
}
