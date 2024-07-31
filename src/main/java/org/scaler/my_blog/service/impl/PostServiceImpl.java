package org.scaler.my_blog.service.impl;

import org.scaler.my_blog.entity.Post;
import org.scaler.my_blog.exception.ResourceNotFoundException;
import org.scaler.my_blog.payload.PostDTO;
import org.scaler.my_blog.payload.PostResponse;
import org.scaler.my_blog.repository.PostRepository;
import org.scaler.my_blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PostDTO createPost(PostDTO postDTO) {
        //convert DTO to entity
//
        Post post = mapToEntity(postDTO);

      Post newPost = postRepository.save(post);

      //convert entity to DTO
        PostDTO postResponse = mapToDTO(newPost);
//        PostDTO postResponse = new PostDTO();
//        postResponse.setId(newPost.getId());
//        postResponse.setTitle(newPost.getTitle());
//        postResponse.setContent(newPost.getContent());
//        postResponse.setDescription(newPost.getDescription());

        return postResponse;
    }

    @Override
    public  PostResponse getAllPosts(int pageNo, int pageSize,String sortBy) {
        //create page instance
        PageRequest pageable = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));

  Page<Post> posts = postRepository.findAll(pageable);
  //get content for page object
        List<Post>lisofPosts = posts.getContent();
        List<PostDTO> content = lisofPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
        PostResponse postResponse = new  PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
      return postResponse;
    }

    @Override
    public PostDTO getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new  ResourceNotFoundException("Post","id",id));
        return mapToDTO(post);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Long id) {
        //get post by id from database
        Post post = postRepository.findById(id).orElseThrow(()->new  ResourceNotFoundException("Post","id",id));

       post.setTitle(postDTO.getTitle());
       post.setDescription(postDTO.getDescription());
       post.setContent(postDTO.getContent());

       Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new  ResourceNotFoundException("Post","id",id));
   postRepository.delete(post);
    }

    //converted entity into DTO
    private PostDTO mapToDTO(Post post){
        PostDTO postdto = new PostDTO();
        postdto.setContent(post.getContent());
        postdto.setId(post.getId());
        postdto.setDescription(post.getDescription());
        postdto.setTitle(post.getTitle());
        return  postdto;

    }
    private Post mapToEntity(PostDTO postDTO){
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setDescription(postDTO.getDescription());
          return post;
    }
}
