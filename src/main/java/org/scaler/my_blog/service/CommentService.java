package org.scaler.my_blog.service;

import org.scaler.my_blog.payload.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(Long postId, CommentDTO commentDTO);
    List<CommentDTO> getCommentsByPostId(Long postId);
}
