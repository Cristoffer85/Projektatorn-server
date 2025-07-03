package cristoffer85.com.projektatornserver.MAINAPP.controller;

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
}
