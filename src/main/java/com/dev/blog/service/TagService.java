package com.dev.blog.service;

import com.dev.blog.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface  TagService {

    List<Tag> createTags(Set<String> tagNames);

    List<Tag> getAllTags();

    void deleteTag(UUID id);

    List<Tag> getTagByIds(Set<UUID> tagIds);
}
