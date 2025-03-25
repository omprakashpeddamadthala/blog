package com.dev.blog.domain.dtos;

import com.dev.blog.domain.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private UUID id;
    private String title;
    private String content;
    private PostStatus status;
    private Integer readingTime;
    private AuthorDTO authorDTO;
    private CategoryDTO categoryDTO;
    private Set<TagDTO> tagDTOs = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
