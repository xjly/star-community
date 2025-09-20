package com.example.controller.user;

import com.example.context.BaseContext;
import com.example.dto.CommentDTO;
import com.example.result.Result;
import com.example.service.CommentService;
import com.example.vo.CommentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/comment")
@Slf4j
public class UserCommentController {
    @Autowired
    private CommentService commentService;
    @GetMapping("/list")
    public Result<List<CommentVO>> getCommentList(@RequestParam(required = false,name = "postId") String postId, @RequestParam int page, @RequestParam int size) {
        List<CommentVO> comments = commentService.getCommentsById(postId, page, size);
        return Result.success(comments);
    }
    @PostMapping
    public Result<String> addComment(@RequestBody CommentDTO commentDTO) {
        log.info("发送评论:{}", commentDTO);
        commentService.addComment(commentDTO);
        return Result.success();
    }
    @GetMapping("/all")
    public Result<List<CommentVO>> getCommentList(@RequestParam int page, @RequestParam int size) {
        List<CommentVO> comments = commentService.getComments(page, size);
        return Result.success(comments);
    }
    @GetMapping("/status")
    public Result<String> getCommentStatus(@RequestParam String id, @RequestParam int status) {
        log.info("修改帖子{}的状态为{}", id, status);
        commentService.updateStatus(id, status);
        return Result.success();
    }
    @GetMapping("/like")
    public Result<Boolean> likePost(@RequestParam String id) {
        log.info("点赞评论{}",id);
        Boolean isLiked = commentService.likePost(id);
        return Result.success(isLiked);
    }
}
