package com.devops.portfolio.controller;

import com.devops.portfolio.entity.Comment;
import com.devops.portfolio.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/project/{projectId}")
    public ResponseEntity<?> addComment(
            @PathVariable Long projectId,
            @RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            String visitorName = request.get("visitorName");
            String visitorEmail = request.get("visitorEmail");
            String visitorPhone = request.get("visitorPhone");

            Comment comment = commentService.addComment(
                    projectId, content, visitorName, visitorEmail, visitorPhone
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message", "Comment added successfully",
                    "commentId", comment.getId(),
                    "createdAt", comment.getCreatedAt()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getProjectComments(@PathVariable Long projectId, Pageable pageable) {
        try {
            Page<Comment> comments = commentService.getProjectComments(projectId, pageable);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getComment(@PathVariable Long id) {
        return commentService.getCommentById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return ResponseEntity.ok(Map.of("message", "Comment deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
