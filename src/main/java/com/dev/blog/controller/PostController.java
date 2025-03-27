package com.dev.blog.controller;

import com.dev.blog.domain.dtos.PostRequestDTO;
import com.dev.blog.domain.dtos.PostDTO;
import com.dev.blog.domain.entities.Post;
import com.dev.blog.domain.entities.User;
import com.dev.blog.mappers.PostMapper;
import com.dev.blog.service.UserService;
import com.dev.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final PostMapper postMapper;


    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts(@RequestParam(required = false) UUID categoryId,
                                                     @RequestParam(required = false) UUID tagId) {
        log.info( "Get all posts with category id {} and tag id {}", categoryId, tagId );
        List<Post> posts = postService.getAllPosts( categoryId, tagId );
        List<PostDTO> postDTOS = posts.stream().map( postMapper::toDto ).collect( Collectors.toList() );
        return new ResponseEntity<>( postDTOS, HttpStatus.OK );
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<PostDTO>> getAllDraftsPosts(@RequestAttribute UUID userId) {
        log.info( "Get all drafts posts with user id {}", userId );
        User loggedInUser = userService.getUserById( userId );
        List<Post> posts = postService.getAllDraftsPosts(loggedInUser);
        List<PostDTO> postDTOS = posts.stream().map( postMapper::toDto ).collect( Collectors.toList() );
        return new ResponseEntity<>( postDTOS, HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid  @RequestAttribute UUID userId, @RequestBody PostRequestDTO postRequestDTO) {
        log.info( "Create new post with title {}", postRequestDTO.getTitle());
        User loggedInUser = userService.getUserById( userId );
        Post createdPost = postService.createPost( loggedInUser, postRequestDTO );
        return new ResponseEntity<>( postMapper.toDto( createdPost ), HttpStatus.CREATED );
    }

    public ResponseEntity<PostDTO> updatePost(@PathVariable UUID id, @RequestBody PostRequestDTO postRequestDTO) {
     log.info( "Update post with id {}", id );
        Post updatedPost = postService.updatePost( id, postRequestDTO );
        return new ResponseEntity<>( postMapper.toDto( updatedPost ), HttpStatus.OK );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable UUID id) {
        log.info("Get post with id {}", id);
        Post post = postService.getPostById( id );
        return new ResponseEntity<>( postMapper.toDto( post ), HttpStatus.OK );
    }

    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        log.info( "Delete post with id {}", id);
        postService.deletePost( id );
        return new ResponseEntity<>( HttpStatus.NO_CONTENT );
    }


}
