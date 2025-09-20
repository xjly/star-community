package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Post;
import com.example.vo.PostVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    @Select("select * from post order by create_time desc ")
    List<Post> getAllPosts();
    @Select("select count(id) from comment where post_id = #{postId} and status = 1")
    int getCommentCount(String postId);
    @Select("select * from post order by hot_factor desc limit 10")
    List<Post> getHotPosts();
    @Select("select * from post where user_id = #{userId} order by view_count desc")
    List<Post> selectUser(String userId);
    @Select("select * from post where id = #{postId}")
    Post getCurrentPost(String postId);
    @Update("update post set view_count = view_count + 1 where id = #{postId}")

    Boolean incrLike(String postId);
    @Update("update post set like_count = like_count - 1 where id = #{postId}")
    Boolean decrLike(String postId);
    @Update("update post set status = #{status} where id = #{id}")
    void updateStatus(String id, int status);
    @Select("select * from post where status = 0 order by create_time desc")
    List<Post> getAllCheckPosts();
    @Update("update post set view_count = #{viewCount} where id = #{postId}")
    void incrView(String postId,Integer viewCount);
    @Update("update post set comment_count = #{commentCount} where id = #{postId}")
    void updateCommentCount(String postId, int commentCount);
    @Select("select (p.view_count * 0.5 + p.comment_count * 0.3 + (SELECT COUNT(DISTINCT c.user_id)\n" +
            "FROM comment c\n" +
            "WHERE c.post_id = #{postId}) * 0.2 ) from post p where id = #{postId}")
    Double getPostFactor(String postId);
    @Update("update post set hot_factor = #{factor} where id = #{postId}")
    void updatePostFactor(String postId,double factor);
}
