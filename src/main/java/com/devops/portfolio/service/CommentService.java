package com.devops.portfolio.service;

import com.devops.portfolio.entity.Comment;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public Comment addComment(Long projectId, String content, String visitorName, String visitorEmail, String visitorPhone) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Comment comment = Comment.builder()
                .content(content)
                .visitorName(visitorName)
                .visitorEmail(visitorEmail)
                .visitorPhone(visitorPhone)
                .project(project)
                .isPublic(true)
                .build();

        Comment savedComment = commentRepository.save(comment);

        // Send email notification to portfolio owner
        try {
            User owner = project.getOwner();
            String emailContent = String.format(
                    "New comment on your project '%s':\n\n" +
                    "From: %s (%s)\n" +
                    "Phone: %s\n\n" +
                    "Message:\n%s",
                    project.getTitle(),
                    visitorName,
                    visitorEmail,
                    visitorPhone,
                    content
            );
            emailService.sendEmail(owner.getEmail(), "New Comment on Your Portfolio", emailContent);
        } catch (Exception e) {
            System.err.println("Failed to send email notification: " + e.getMessage());
        }

        return savedComment;
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public Page<Comment> getProjectComments(Long projectId, Pageable pageable) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return commentRepository.findByProject(project, pageable);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
