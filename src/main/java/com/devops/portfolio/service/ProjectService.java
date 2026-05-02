package com.devops.portfolio.service;

import com.devops.portfolio.dto.ProjectDTO;
import com.devops.portfolio.entity.Project;
import com.devops.portfolio.entity.User;
import com.devops.portfolio.repository.CommentRepository;
import com.devops.portfolio.repository.ProjectRepository;
import com.devops.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public Project createProject(Long userId, String title, String description, String technologies) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = Project.builder()
                .title(title)
                .description(description)
                .technologies(technologies)
                .owner(owner)
                .status(Project.ProjectStatus.ACTIVE)
                .likes(0L)
                .build();

        return projectRepository.save(project);
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Page<Project> getUserProjects(Long userId, Pageable pageable) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return projectRepository.findByOwner(owner, pageable);
    }

    public Page<Project> getAllActiveProjects(Pageable pageable) {
        return projectRepository.findByStatus(Project.ProjectStatus.ACTIVE, pageable);
    }

    public Page<Project> getMostLikedProjects(Pageable pageable) {
        return projectRepository.findMostLikedProjects(pageable);
    }

    public Project updateProject(Long projectId, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (projectDTO.getTitle() != null) {
            project.setTitle(projectDTO.getTitle());
        }
        if (projectDTO.getDescription() != null) {
            project.setDescription(projectDTO.getDescription());
        }
        if (projectDTO.getTechnologies() != null) {
            project.setTechnologies(projectDTO.getTechnologies());
        }
        if (projectDTO.getProjectUrl() != null) {
            project.setProjectUrl(projectDTO.getProjectUrl());
        }
        if (projectDTO.getGithubUrl() != null) {
            project.setGithubUrl(projectDTO.getGithubUrl());
        }
        if (projectDTO.getImageUrl() != null) {
            project.setImageUrl(projectDTO.getImageUrl());
        }

        return projectRepository.save(project);
    }

    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    public Project likeProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!project.getLikedByUsers().contains(user)) {
            project.getLikedByUsers().add(user);
            user.getLikedProjects().add(project);
            project.setLikes(project.getLikes() + 1);
            userRepository.save(user);
        }

        return projectRepository.save(project);
    }

    public Project unlikeProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (project.getLikedByUsers().contains(user)) {
            project.getLikedByUsers().remove(user);
            user.getLikedProjects().remove(project);
            project.setLikes(Math.max(0, project.getLikes() - 1));
            userRepository.save(user);
        }

        return projectRepository.save(project);
    }

    public ProjectDTO convertToDTO(Project project) {
        long commentCount = commentRepository.countByProject(project);
        return ProjectDTO.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .technologies(project.getTechnologies())
                .imageUrl(project.getImageUrl())
                .projectUrl(project.getProjectUrl())
                .githubUrl(project.getGithubUrl())
                .status(project.getStatus().toString())
                .likes(project.getLikes())
                .commentCount(commentCount)
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
