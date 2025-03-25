package com.dev.blog.mappers;

import com.dev.blog.domain.dtos.PostDTO;
import com.dev.blog.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "authorDTO", source = "author")
    @Mapping(target = "categoryDTO", source = "category")
    @Mapping(target = "tagDTOs", source = "tags")
    PostDTO toDto(Post post);
}
