package org.scaler.my_blog.service.impl;

import org.scaler.my_blog.entity.Comments;
import org.scaler.my_blog.entity.Post;
import org.scaler.my_blog.exception.ResourceNotFoundException;
import org.scaler.my_blog.payload.CommentDTO;
import org.scaler.my_blog.repository.CommentRepository;
import org.scaler.my_blog.repository.PostRepository;
import org.scaler.my_blog.service.CommentService;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.Comment;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
        Comments comments = mapToEntity(commentDTO);
        //retrive post entity by id

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));
        //set post to comments entity
        comments.setPost(post);

        //comment entity to DB
        Comments newComment = commentRepository.save(comments);
        return mapToDTO(newComment);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        List<Comments> comments = commentRepository.findByPostId(postId);

        //convert list of comment entities to list of comment dto
        return comments.stream().map(comments1 -> mapToDTO(comments1)).collect(Collectors.toList());
    }

    private CommentDTO mapToDTO(Comments comment) {
        CommentDTO commentss = new CommentDTO();
        commentss.setId(comment.getId());
        commentss.setName(comment.getName());
        commentss.setBody(comment.getBody());
        commentss.setEmail(comment.getEmail());
        return commentss;
    }

    private Comments mapToEntity(CommentDTO commentDTO) {
        Comments comments = new Comments();
        comments.setId(commentDTO.getId());
        comments.setName(commentDTO.getName());
        comments.setBody(commentDTO.getBody());
        comments.setEmail(commentDTO.getEmail());
        return comments;
    }
}
