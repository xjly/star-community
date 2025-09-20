package com.example.service;

import com.example.dto.CommentDTO;
import com.example.vo.CommentVO;

import java.util.List;

public interface CommentService {
    List<CommentVO> getCommentsById(String postId, int current, int size);

    void addComment(CommentDTO commentDTO);

    List<CommentVO> getComments(int current, int size);

    void updateStatus(String postId, int status);

    Boolean likePost(String commentId);
}
