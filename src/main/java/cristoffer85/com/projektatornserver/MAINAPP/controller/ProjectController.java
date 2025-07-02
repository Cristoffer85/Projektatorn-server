package cristoffer85.com.projektatornserver.MAINAPP.controller;

import cristoffer85.com.projektatornserver.MAINAPP.model.ProjectsInProgress;
import cristoffer85.com.projektatornserver.MAINAPP.service.ProjectsInProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects-in-progress")
public class ProjectController {

    @Autowired
    private ProjectsInProgressService service;

    @GetMapping("/projects-in-progress/load")
    public List<ProjectsInProgress> loadProjects(@RequestParam String username) {
        return service.getProjectsForUser(username);
    }

    @PostMapping("/add")
    public ProjectsInProgress addProject(@RequestBody ProjectsInProgress project) {
        return service.addProject(project);
    }

    @DeleteMapping("/remove/{id}")
    public void removeProject(@PathVariable String id) {
        service.removeProject(id);
    }
}
