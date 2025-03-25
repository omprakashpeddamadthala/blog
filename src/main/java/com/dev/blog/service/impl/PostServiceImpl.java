package com.dev.blog.service.impl;

import com.dev.blog.domain.PostStatus;
import com.dev.blog.domain.entities.Category;
import com.dev.blog.domain.entities.Post;
import com.dev.blog.domain.entities.Tag;
import com.dev.blog.domain.entities.User;
import com.dev.blog.repository.CategoryRepository;
import com.dev.blog.repository.PostRepository;
import com.dev.blog.repository.TagRepository;
import com.dev.blog.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {

        log.info( "Get all posts from service" );
        if (categoryId != null && tagId != null) {
            Category category = categoryRepository.findById( categoryId ).orElseThrow(
                    () -> new EntityNotFoundException( "Category with id " + categoryId + " not found" ) );
            Tag tag = tagRepository.findById( tagId ).orElseThrow(
                    () -> new EntityNotFoundException( "Tag with id " + tagId + " not found" ) );
            return postRepository.findAllByStatusAndCategoryAndTagsContaining( PostStatus.PUBLISHED, category, tag );
        }

        if (categoryId != null) {
            Category category = categoryRepository.findById( categoryId ).orElseThrow(
                    () -> new EntityNotFoundException( "Category with id " + categoryId + " not found" ) );
            return postRepository.findAllByStatusAndCategory( PostStatus.PUBLISHED, category );
        }

        if (tagId != null) {
            Tag tag = tagRepository.findById( tagId ).orElseThrow(
                    () -> new EntityNotFoundException( "Tag with id " + tagId + " not found" ) );
            return postRepository.findAllByStatusAndTagsContaining( PostStatus.PUBLISHED, tag );
        }
        return postRepository.findAllByStatus( PostStatus.PUBLISHED );
    }

    @Override
    public List<Post> getAllDraftsPosts(User loggedInUser) {
        return postRepository.findAllByAuthorAndStatus(loggedInUser, PostStatus.DRAFT);
    }
}
