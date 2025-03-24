package com.dev.blog.service.impl;

import com.dev.blog.domain.entities.Tag;
import com.dev.blog.repository.TagRepository;
import com.dev.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository repository;


    @Override
    public List<Tag> createTags(Set<String> tagNames) {
        log.info( "Create new tag with names {}", tagNames );
        List<Tag> existingTags = repository.findAllByNameIn( tagNames );

        Set<String> existingTagNames = existingTags.stream()
                .map( Tag::getName )
                .collect( Collectors.toSet() );

        List<Tag> newTags = tagNames.stream()
                .filter( tag -> !existingTagNames.contains( tag ) )
                .map( name -> Tag.builder()
                        .name( name )
                        .posts( new HashSet<>() )
                        .build() )
                .toList();

        List<Tag> savedTags = new ArrayList<>();

        if (!newTags.isEmpty()) {
            savedTags = repository.saveAll( newTags );
        }
        savedTags.addAll( existingTags );
        return savedTags;

    }

    @Override
    public List<Tag> getAllTags() {
        log.info( "Get all tags from service" );
        return repository.findAllWithPostCount();
    }

    @Override
    public void deleteTag(UUID id) {
        log.info( "Delete tag with id {}", id );
       Optional<Tag> tags = repository.findByTagId( id );
        if(tags.isPresent()){
            if(!tags.get().getPosts().isEmpty()) {
                throw new IllegalStateException("Tag has posts associated with it");
            }
            repository.deleteById( id );
        }
    }
}
