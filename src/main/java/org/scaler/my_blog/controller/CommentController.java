package org.scaler.my_blog.controller;

import org.scaler.my_blog.payload.CommentDTO;
import org.scaler.my_blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable(value="postId") Long postId,
                                                    @RequestBody CommentDTO commentDTO) {

        return new ResponseEntity<>(commentService.createComment(postId,commentDTO), HttpStatus.CREATED);

    }
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDTO> getCommentsByPOstId(@PathVariable(value="postId") Long postId) {
          return commentService.getCommentsByPostId(postId);
    }
}
