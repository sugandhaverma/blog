package org.scaler.my_blog.service;

import org.scaler.my_blog.payload.PostDTO;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    List<PostDTO> getAllPosts();
    PostDTO getPostById(Long id);
    PostDTO updatePost(PostDTO postDTO,Long id);
    void deletePost(Long id);
}
