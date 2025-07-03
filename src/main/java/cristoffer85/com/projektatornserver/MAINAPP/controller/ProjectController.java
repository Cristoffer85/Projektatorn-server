package cristoffer85.com.projektatornserver.MAINAPP.controller;

import cristoffer85.com.projektatornserver.MAINAPP.model.CompletedProject;
import cristoffer85.com.projektatornserver.MAINAPP.model.PendingProject;
import cristoffer85.com.projektatornserver.MAINAPP.model.Project;
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
    private ProjectService projectService;

    // ######### Regular Projects endpoints ##############
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

    // ######### Pending Projects endpoints ##############
    @PostMapping("/pending")
    public PendingProject sendProject(@RequestBody PendingProject pendingProject) {
        return projectService.sendProject(pendingProject);
    }

    @GetMapping("/pending/{username}")
    public List<PendingProject> getPendingProjects(@PathVariable String username) {
        return projectService.getPendingProjectsForUser(username);
    }

    @DeleteMapping("/pending/{id}")
    public void removePendingProject(@PathVariable String id) {
        projectService.removePendingProject(id);
    }

    // ######### Completed Projects endpoints ##############
    @PostMapping("/history")
    public CompletedProject addCompletedProject(@RequestBody CompletedProject completedProject) {
        return service.addCompletedProject(completedProject);
    }

    @GetMapping("/history")
    public List<CompletedProject> getCompletedProjects(@RequestParam String owner) {
        return service.getCompletedProjects(owner);
    }
}
