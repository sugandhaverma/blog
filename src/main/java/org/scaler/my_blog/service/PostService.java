package org.scaler.my_blog.service;

import org.scaler.my_blog.payload.PostDTO;
import org.scaler.my_blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);
    PostResponse getAllPosts(int pageNo, int pageSize,String sortBy);
    PostDTO getPostById(Long id);
    PostDTO updatePost(PostDTO postDTO,Long id);
    void deletePost(Long id);
}
