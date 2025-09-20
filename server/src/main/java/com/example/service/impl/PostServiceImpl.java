package com.example.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import com.example.context.BaseContext;
import com.example.dto.PostDTO;
import com.example.entity.Image;
import com.example.entity.Post;
import com.example.entity.User;
import com.example.mapper.ImageMapper;
import com.example.mapper.PostMapper;
import com.example.mapper.UserMapper;
import com.example.service.PostService;
import com.example.vo.PostVO;
import com.github.houbb.sensitive.word.core.SensitiveWordHelper;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    private static final String POST_LIKE_KEY_PREFIX = "like:post:";
    private static final String POST_LIKE_COUNT_KEY = "like:post:count";

    private static final String POST_VIEW_KEY_PREFIX = "view:post:";


    @Autowired
    private PostMapper postMapper;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<PostVO> getAllPosts() {
        List<PostVO> postVOS = new ArrayList<>();
        List<Post> posts = postMapper.getAllPosts();
        for (Post post : posts) {
            Double factor1 = postMapper.getPostFactor(post.getId());
            Date date = DateUtil.date();
            Date createTime = post.getCreateTime();
            long gap = DateUtil.between(createTime, date, DateUnit.DAY, false);
            Double factor2 = Math.pow(Math.E, gap);
            postMapper.updatePostFactor(post.getId(),factor1 * factor2);
            postVOS.add(getPostVO(post));
        }
        return postVOS;
    }

    @Override
    public List<PostVO> getPost(String userId) {
        List<PostVO> postVOS = new ArrayList<>();
        List<Post> posts = postMapper.selectUser(userId);
        for (Post post : posts) {
            postVOS.add(getPostVO(post));
        }
        return postVOS;
    }

    @Override
    public void createPost(PostDTO postDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        post.setUserId(BaseContext.getCurrentId());
        post.setCreateTime(new Date((postDTO.getCreateTime())));
        User user = userMapper.selectById(BaseContext.getCurrentId());
        post.setAvatar(user.getAvatar());
        post.setNickname(user.getNickname());
        //敏感词过滤为*
        post.setContent(SensitiveWordHelper.replace(post.getContent()));
        postMapper.insert(post);
        postDTO.getImgList().forEach(url -> {

            Image image = new Image();
            image.setPostId(post.getId());
            image.setImageUrl(url);
            imageMapper.insert(image);
        });
    }


    @Override
    public List<PostVO> getHotPosts() {
        List<PostVO> postVOS = new ArrayList<>();
        List<Post> posts = postMapper.getHotPosts();
        for (Post post : posts) {
            postVOS.add(getPostVO(post));
        }
        return postVOS;
    }

    @Override
    public PostVO getCurrentPost(String postId) {
        Post currentPost = postMapper.getCurrentPost(postId);
        return getPostVO(currentPost);
    }


    //公共的转VO方法 改变其他表字段的值
    private PostVO getPostVO(Post post) {
        PostVO postVO = new PostVO();
        long time = post.getCreateTime().getTime();
        postVO.setCreateTime(time);
        List<String> Images = imageMapper.selectByPostId(post.getId());
        BeanUtils.copyProperties(post, postVO);
        postVO.setImgList(Images);
        int commentCount = postMapper.getCommentCount(post.getId());
        //更新post表中的comment_count字段
        postMapper.updateCommentCount(post.getId(),commentCount);
        postVO.setCommentCount(commentCount);
        return postVO;
    }

    @Override
    public void incrPostView(String postId) {
        String key = POST_VIEW_KEY_PREFIX + postId;
        stringRedisTemplate.opsForValue().increment(key, 1);
        String value = stringRedisTemplate.opsForValue().get(key);
        if(value != null) {
            Integer viewCount = Integer.valueOf(value);
            postMapper.incrView(postId,viewCount);
        }
    }


    @Override
    public Boolean likePost(String postId) {
        //获取当前用户id
        String userId = BaseContext.getCurrentId();
        //redis设置set key为post_id value为user_id
        String key = POST_LIKE_KEY_PREFIX + postId;
        //查询该用户是否点赞该帖子
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId);
        if(BooleanUtil.isFalse(isMember)){
            //如果不存在 说明未点赞 点赞加入set集合 点赞数+1
            Boolean isSuccess = postMapper.incrLike(postId);
            if(isSuccess){
                stringRedisTemplate.opsForSet().add(key, userId);
            }
        }else{
            //反之 说明已点赞 取消点赞 移除set 点赞数-1
            Boolean isSuccess = postMapper.decrLike(postId);
            if(isSuccess){
                stringRedisTemplate.opsForSet().remove(key, userId);
            }
        }

        return isMember;
    }

    @Override
    public void updateStatus(String id, int status) {
        postMapper.updateStatus(id,status);
    }

    @Override
    public List<PostVO> getAllCheckPosts() {
        List<PostVO> postVOS = new ArrayList<>();
        List<Post> posts = postMapper.getAllCheckPosts();
        for (Post post : posts) {
            postVOS.add(getPostVO(post));
        }
        return postVOS;
    }
}
