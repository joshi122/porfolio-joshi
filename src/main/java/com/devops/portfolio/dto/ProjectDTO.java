package com.devops.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Long id;
    private String title;
    private String description;
    private String technologies;
    private String projectUrl;
    private String githubUrl;
    private String imageUrl;
    private String status;   // ✅ ADD THIS
}







