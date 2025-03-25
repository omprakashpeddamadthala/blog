package com.dev.blog.repository;

import com.dev.blog.domain.PostStatus;
import com.dev.blog.domain.entities.Category;
import com.dev.blog.domain.entities.Post;
import com.dev.blog.domain.entities.Tag;
import com.dev.blog.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    
    List<Post> findAllByStatusAndCategoryAndTagsContaining(PostStatus postStatus, Category category, Tag tag);

    List<Post> findAllByStatusAndCategory(PostStatus postStatus, Category category);

    List<Post> findAllByStatusAndTagsContaining(PostStatus postStatus, Tag tag);

    List<Post> findAllByStatus(PostStatus postStatus);

    List<Post> findAllByAuthorAndStatus(User loggedInUser, PostStatus postStatus);
}
