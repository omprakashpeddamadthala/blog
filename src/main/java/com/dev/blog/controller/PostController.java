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
        List<Post> posts = postService.getAllPosts( categoryId, tagId );
        List<PostDTO> postDTOS = posts.stream().map( postMapper::toDto ).collect( Collectors.toList() );
        return new ResponseEntity<>( postDTOS, HttpStatus.OK );
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<PostDTO>> getAllDraftsPosts(@RequestAttribute UUID userId) {
        User loggedInUser = userService.getUserById( userId );
        List<Post> posts = postService.getAllDraftsPosts(loggedInUser);
        List<PostDTO> postDTOS = posts.stream().map( postMapper::toDto ).collect( Collectors.toList() );
        return new ResponseEntity<>( postDTOS, HttpStatus.OK );
    }

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@Valid  @RequestAttribute UUID userId, @RequestBody PostRequestDTO postRequestDTO) {
        User loggedInUser = userService.getUserById( userId );
        Post createdPost = postService.createPost( loggedInUser, postRequestDTO );
        return new ResponseEntity<>( postMapper.toDto( createdPost ), HttpStatus.CREATED );
    }



}
