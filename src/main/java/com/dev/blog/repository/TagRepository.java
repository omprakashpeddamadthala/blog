package com.dev.blog.repository;

import com.dev.blog.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    boolean existsByName(String name);

    @Query("SELECT t FROM Tag t LEFT JOIN FETCH t.posts")
    List<Tag> findAllWithPostCount();

    List<Tag> findAllByNameIn(Set<String> names);

    @Query("SELECT t FROM Tag t WHERE t.id = :id")
    Optional<Tag> findByTagId(UUID id);

    Set<Tag> findAllByIdIn(Set<UUID> tagIds);
}
