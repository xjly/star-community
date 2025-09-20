package com.example.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.context.BaseContext;
import com.example.dto.CommentDTO;
import com.example.entity.Comment;
import com.example.entity.User;
import com.example.mapper.CommentMapper;
import com.example.mapper.UserMapper;
import com.example.service.CommentService;
import com.example.vo.CommentVO;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String COMMENT_LIKE_KEY_PREFIX = "like:comment:";
    private static final String COMMENT_LIKE_COUNT_KEY = "like:comment:count";

    @Override
    public List<CommentVO> getCommentsById(String postId, int current, int size) {
        Page<Comment> page = new Page<>(current,size);
        Page<Comment> comments = new Page<>();
        //判断帖子是否存在
        if(postId != null){
            comments = commentMapper.getPostRoot(page,postId);
        }
        List<Comment> commentList = comments.getRecords();
        ArrayList<CommentVO> commentVOS = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            //时间戳转datetime
            long time = comment.getCreateTime().getTime();
            commentVO.setCreateTime(time);
            //获取用户信息
            User user = userMapper.selectById(comment.getUserId());
            //设置用户信息(昵称和头像)
            if(user != null){
                commentVO.setNickname(user.getNickname());
                commentVO.setAvatar(user.getAvatar());
            }
            //设置子评论集合
            commentVO.setCommentList(getChildrenComments(comment.getId()));
            commentVOS.add(commentVO);
        }
        return commentVOS;
    }

    @Override
    public void addComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        //设置当前用户信息
        comment.setUserId(BaseContext.getCurrentId());
        comment.setCreateTime(new Date(commentDTO.getCreateTime()));
        comment.setIsDelete(0);
        //敏感词过滤为*
        comment.setContent(SensitiveWordHelper.replace(comment.getContent()));
        if(commentDTO.getPostId() != null){
            comment.setPostId(commentDTO.getPostId());
        }
        commentMapper.insert(comment);
    }

    @Override
    public List<CommentVO> getComments(int current, int size) {
        Page<Comment> page = new Page<>(current,size);
        Page<Comment> comments = new Page<>();
        comments = commentMapper.getComments(page);
        List<Comment> records = comments.getRecords();

        ArrayList<CommentVO> commentVOS = new ArrayList<>();
        for (Comment comment : records) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            //时间戳转datetime
            long time = comment.getCreateTime().getTime();
            commentVO.setCreateTime(time);
            //获取用户信息
            User user = userMapper.selectById(comment.getUserId());
            //设置用户信息(昵称和头像)
            if(user != null){
                commentVO.setNickname(user.getNickname());
                commentVO.setAvatar(user.getAvatar());
            }
            //设置子评论集合
            commentVO.setCommentList(getChildrenComments(comment.getId()));
            commentVOS.add(commentVO);
        }
        return commentVOS;
    }

    @Override
    public void updateStatus(String postId, int status) {
        commentMapper.updateStatus(postId,status);
    }

    @Override
    public Boolean likePost(String commentId) {
        //获取当前用户id
        String userId = BaseContext.getCurrentId();
        //redis设置set key为post_id value为user_id
        String key = COMMENT_LIKE_KEY_PREFIX + commentId;
        //查询该用户是否点赞该评论
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId);
        if(BooleanUtil.isFalse(isMember)){
            //如果不存在 说明未点赞 点赞加入set集合 点赞数+1
            Boolean isSuccess = commentMapper.incrLike(commentId);
            if(isSuccess){
                stringRedisTemplate.opsForSet().add(key, userId);
            }
        }else{
            //反之 说明已点赞 取消点赞 移除set 点赞数-1
            Boolean isSuccess = commentMapper.decrLike(commentId);
            if(isSuccess){
                stringRedisTemplate.opsForSet().remove(key, userId);
            }
        }

        return isMember;
    }

    /**
     * 递归寻找顶级评论下的子评论
     * @param fatherId
     * @return 子评论集合
     */
    private List<CommentVO> getChildrenComments(String fatherId) {
        List<Comment> children = commentMapper.getPostChildren(fatherId);
        ArrayList<CommentVO> commentVOS = new ArrayList<>();
        for (Comment comment : children) {
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(comment, commentVO);
            if(comment.getReplyChildId() != null){
                //寻找回复的子评论并设置
                CommentVO replyChildComment = new CommentVO();
                Comment replyChild = commentMapper.getReplyChildComment(comment.getReplyChildId());

                BeanUtils.copyProperties(replyChild, replyChildComment);

                User replyUser = userMapper.selectById(replyChild.getUserId());

                if(replyUser != null){
                    replyChildComment.setNickname(replyUser.getNickname());
                }
                commentVO.setReplyChildComment(replyChildComment);
            }
            //时间戳转datetime
            long time = comment.getCreateTime().getTime();
            commentVO.setCreateTime(time);
            //设置子评论集合
            commentVO.setCommentList(getChildrenComments(comment.getId()));
            //获取用户信息
            User user = userMapper.selectById(comment.getUserId());
            //设置用户信息(昵称和头像)
            if(user != null){
                commentVO.setNickname(user.getNickname());
                commentVO.setAvatar(user.getAvatar());
            }
            commentVOS.add(commentVO);
        }
        return commentVOS;
    }
}
