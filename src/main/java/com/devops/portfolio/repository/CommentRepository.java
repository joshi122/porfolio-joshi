package com.devops.portfolio.repository;

import com.devops.portfolio.entity.Comment;
import com.devops.portfolio.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByProject(Project project, Pageable pageable);
    List<Comment> findByProjectOrderByCreatedAtDesc(Project project);
    long countByProject(Project project);
}
