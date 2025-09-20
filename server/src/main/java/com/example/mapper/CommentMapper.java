package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("select * from comment where post_id = #{postId} and father_id is null order by create_time desc")
    Page<Comment> getPostRoot(Page<Comment> page, String postId);
    @Select("select * from comment where father_id = #{fatherId} order by create_time desc")
    List<Comment> getPostChildren(String fatherId);
    @Select("select * from comment where id = #{replyChildId}")
    Comment getReplyChildComment(String replyChildId);
    @Select("select * from comment where status = 0 order by create_time desc")
    Page<Comment> getComments(Page<Comment> page);
    @Update("update comment set status = #{status} where id = #{postId}")
    void updateStatus(String postId, int status);
    @Update("update comment set like_count = like_count + 1 where id = #{commentId}")
    Boolean incrLike(String commentId);
    @Update("update comment set like_count = like_count - 1 where id = #{commentId}")
    Boolean decrLike(String commentId);
}
