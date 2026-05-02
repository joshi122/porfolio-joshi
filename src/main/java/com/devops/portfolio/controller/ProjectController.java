package com.devops.portfolio.controller;

import com.devops.portfolio.dto.ProjectDTO;
import com.devops.portfolio.entity.Project;
import com.devops.portfolio.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Map<String, String> request) {
        try {
            Long userId = Long.parseLong(request.get("userId"));
            String title = request.get("title");
            String description = request.get("description");
            String technologies = request.get("technologies");

            Project project = projectService.createProject(userId, title, description, technologies);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(projectService.convertToDTO(project));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProject(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(project -> ResponseEntity.ok(projectService.convertToDTO(project)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserProjects(@PathVariable Long userId, Pageable pageable) {
        try {
            Page<Project> projects = projectService.getUserProjects(userId, pageable);
            return ResponseEntity.ok(projects.map(projectService::convertToDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects(Pageable pageable) {
        Page<Project> projects = projectService.getAllActiveProjects(pageable);
        return ResponseEntity.ok(projects.map(projectService::convertToDTO));
    }

    @GetMapping("/trending")
    public ResponseEntity<?> getTrendingProjects(Pageable pageable) {
        Page<Project> projects = projectService.getMostLikedProjects(pageable);
        return ResponseEntity.ok(projects.map(projectService::convertToDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        try {
            Project updatedProject = projectService.updateProject(id, projectDTO);
            return ResponseEntity.ok(projectService.convertToDTO(updatedProject));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.ok(Map.of("message", "Project deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/like/{userId}")
    public ResponseEntity<?> likeProject(@PathVariable Long id, @PathVariable Long userId) {
        try {
            Project project = projectService.likeProject(id, userId);
            return ResponseEntity.ok(projectService.convertToDTO(project));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/unlike/{userId}")
    public ResponseEntity<?> unlikeProject(@PathVariable Long id, @PathVariable Long userId) {
        try {
            Project project = projectService.unlikeProject(id, userId);
            return ResponseEntity.ok(projectService.convertToDTO(project));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
