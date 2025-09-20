package com.example.vo;

import com.example.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO {
    private String id;
    private String postId;
    private String userId;
    private String content;
    private long createTime;
    private int likeCount;
    private String fatherId;
    private String replyChildId;
    private String avatar;
    private String nickname;
    private List<CommentVO> commentList; // 子评论列表
    private int status;
    private Integer isDelete;
    private CommentVO ReplyChildComment;
}
