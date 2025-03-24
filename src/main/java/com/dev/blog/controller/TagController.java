package com.dev.blog.controller;

import com.dev.blog.domain.dtos.TagRequest;
import com.dev.blog.domain.dtos.TagResponse;
import com.dev.blog.domain.entities.Tag;
import com.dev.blog.mappers.TagMapper;
import com.dev.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
@Slf4j
public class TagController {

    private final TagService tagService;

    private final TagMapper tagMapper;

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTag(@RequestBody TagRequest tagRequest) {
        log.info( "Create new tag" );
        List<Tag> savedTags = tagService.createTags(tagRequest.getNames());
        List<TagResponse> createdTagResponse = savedTags.stream().map(tagMapper::toTagResponse).toList();
        return new ResponseEntity<>( createdTagResponse, HttpStatus.CREATED );
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        log.info( "Get all tags" );
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok( tagMapper.toTagResponseList( tags ) );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@PathVariable UUID id) {
        log.info( "Delete tag with id {}", id );
        tagService.deleteTag( id );
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
