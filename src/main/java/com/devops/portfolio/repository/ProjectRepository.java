package com.devops.portfolio.repository;

import com.devops.portfolio.entity.Project;
import com.devops.portfolio.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findByOwner(User owner, Pageable pageable);
    Page<Project> findByStatus(Project.ProjectStatus status, Pageable pageable);
    
    @Query("SELECT p FROM Project p WHERE p.status = 'ACTIVE' ORDER BY p.likes DESC")
    Page<Project> findMostLikedProjects(Pageable pageable);
    
    List<Project> findByOwnerOrderByCreatedAtDesc(User owner);
}
