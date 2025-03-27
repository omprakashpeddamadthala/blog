package com.dev.blog.service.impl;

import com.dev.blog.domain.PostStatus;
import com.dev.blog.domain.dtos.PostRequestDTO;
import com.dev.blog.domain.entities.Category;
import com.dev.blog.domain.entities.Post;
import com.dev.blog.domain.entities.Tag;
import com.dev.blog.domain.entities.User;
import com.dev.blog.repository.CategoryRepository;
import com.dev.blog.repository.PostRepository;
import com.dev.blog.repository.TagRepository;
import com.dev.blog.service.CategoryService;
import com.dev.blog.service.PostService;
import com.dev.blog.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private static final Integer WORDS_PER_MINUTE = 200;

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

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
        return postRepository.findAllByAuthorAndStatus( loggedInUser, PostStatus.DRAFT );
    }

    @Override
    public Post createPost(User loggedInUser, PostRequestDTO postRequestDTO) {
        Category category = categoryService.getCategoryById( postRequestDTO.getCategoryId() );
        List<Tag>  tags = tagService.getTagByIds(postRequestDTO.getTagIds());
        Post newPost = Post.builder()
                .title( postRequestDTO.getTitle() )
                .author( loggedInUser )
                .content( postRequestDTO.getContent() )
                .readingTime( calculateReadingTime( postRequestDTO.getContent() ) )
                .status( PostStatus.DRAFT )
                .category( category )
                .tags( new HashSet<>(tags) )
                .build();
        return postRepository.save( newPost );
    }

    @Override
    public Post getPostById(UUID id) {
        return postRepository.findById( id ).orElseThrow(
                () -> new EntityNotFoundException( "Post with id " + id + " not found" )
        );
    }

    @Override
    public void deletePost(UUID id) {
        Post post = getPostById( id );
        postRepository.delete( post );
    }

    @Override
    public Post updatePost(UUID id, PostRequestDTO updatedPostRequest) {
        Post existingPost = getPostById( id );
        existingPost.setTitle( updatedPostRequest.getTitle() );
        existingPost.setContent( updatedPostRequest.getContent() );
        existingPost.setReadingTime( calculateReadingTime( updatedPostRequest.getContent() ) );
        UUID updatePostRequestCategoryId = updatedPostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)) {
            Category newCategory = categoryService.getCategoryById(updatePostRequestCategoryId);
            existingPost.setCategory(newCategory);
        }

        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect( Collectors.toSet());
        Set<UUID> updatePostRequestTagIds = updatedPostRequest.getTagIds();
        if(!existingTagIds.equals(updatePostRequestTagIds)) {
            List<Tag> newTags = tagService.getTagByIds(updatePostRequestTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepository.save(existingPost);
    }

    private Integer calculateReadingTime(String content) {
        if(content == null||content.isEmpty()) return 0;
        int words = content.split("\\s+").length;
        return (int) Math.ceil((double) words / WORDS_PER_MINUTE);
    }
}
