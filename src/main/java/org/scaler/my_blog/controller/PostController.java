package org.scaler.my_blog.controller;

import org.scaler.my_blog.payload.PostDTO;
import org.scaler.my_blog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

   private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }
    //create blog post
    @PostMapping
    public ResponseEntity<PostDTO>createPost(@RequestBody PostDTO postDTO) {
       return new ResponseEntity<>(postService.createPost(postDTO), HttpStatus.CREATED);
    }
    //get all post rest api
    @GetMapping
    public List<PostDTO> getAllPosts(){
        return postService.getAllPosts();
    }
}
