package com.example.controller.user;

import com.example.dto.PostDTO;
import com.example.result.Result;
import com.example.service.PostService;
import com.example.util.CosUtil;
import com.example.vo.PostVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/user/post")
@Slf4j
public class UserPostController {
    @Autowired
    private PostService postService;
    @GetMapping("/list")
    public Result<List<PostVO>> getAllPosts() {
        log.info("查询全部帖子{}", postService.getAllPosts());
        List<PostVO> allPosts = postService.getAllPosts();
        return Result.success(allPosts);
    }
    @PostMapping
    public Result<String> createPost(@RequestBody PostDTO postDTO) {
        log.info("创建帖子{}", postDTO);
        postService.createPost(postDTO);
        return Result.success();
    }
    @GetMapping("/hot")
    public Result<List<PostVO>> hotPost() {
        log.info("前10热帖");
        List<PostVO> hotPosts = postService.getHotPosts();
        return Result.success(hotPosts);
    }
    @GetMapping("/home")
    public Result<List<PostVO>> homePost(@RequestParam String userId) {
        log.info("查找用户{}的帖子", userId);
        List<PostVO> posts = postService.getPost(userId);
        return Result.success(posts);
    }
    @GetMapping
    public Result<PostVO> getPost(@RequestParam String id) {
        log.info("查看id{}的帖子", id);
        PostVO currentPost = postService.getCurrentPost(id);
        return Result.success(currentPost);
    }
    @GetMapping("/view")
    public Result<String> getPostView(@RequestParam String id) {
        log.info("增加帖子{}的浏览量",id);
        postService.incrPostView(id);
        return Result.success();
    }
    @GetMapping("/like")
    public Result<Boolean> likePost(@RequestParam String id) {
        log.info("点赞帖子{}",id);
        Boolean isLiked = postService.likePost(id);
        return Result.success(isLiked);
    }
    @GetMapping("/status")
    public Result<String> getPostStatus(@RequestParam String id,@RequestParam int status) {
        log.info("修改帖子{}的状态为{}",id,status);
        postService.updateStatus(id,status);
        return Result.success();
    }
    @GetMapping("/checkList")
    public Result<List<PostVO>> checkList() {
        List<PostVO> allCheckPosts = postService.getAllCheckPosts();
        return Result.success(allCheckPosts);
    }
}
