package com.dev.blog.service;

import com.dev.blog.domain.dtos.PostRequestDTO;
import com.dev.blog.domain.entities.Post;
import com.dev.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<Post> getAllPosts(UUID categoryId, UUID tagId);

    List<Post> getAllDraftsPosts(User loggedInUser);

    Post createPost(User loggedInUser, PostRequestDTO postRequestDTO);
}
