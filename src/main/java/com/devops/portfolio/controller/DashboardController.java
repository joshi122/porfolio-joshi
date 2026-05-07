package com.devops.portfolio.controller;

import com.devops.portfolio.entity.User;
import com.devops.portfolio.service.ProjectService;
import com.devops.portfolio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final ProjectService projectService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        model.addAttribute("projects", projectService.getUserProjects(user.getId(), PageRequest.of(0, 10)));
        model.addAttribute("allProjects", projectService.getAllActiveProjects(PageRequest.of(0, 10)));
        return "dashboard";
    }

    @GetMapping("/projects/new")
    public String newProjectPage() {
        return "new-project";
    }

    @PostMapping("/projects/new")
    public String createProject(@RequestParam String title,
                                @RequestParam String description,
                                @RequestParam String technologies,
                                Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        projectService.createProject(user.getId(), title, description, technologies);
        return "redirect:/dashboard";
    }

    @PostMapping("/projects/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/dashboard";
    }
}
