package com.example.service;

import com.example.dto.PostDTO;
import com.example.entity.Post;
import com.example.vo.PostVO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PostService {
    List<PostVO> getAllPosts();

    List<PostVO> getPost(String userId);

    void createPost(PostDTO postDTO);


    List<PostVO> getHotPosts();

    PostVO getCurrentPost(String postId);

    void incrPostView(String postId);

    Boolean likePost(String postId);

    void updateStatus(String id, int status);


    List<PostVO> getAllCheckPosts();
}
