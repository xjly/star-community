package com.example.dto;

import com.example.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String postId;
    private String userId;
    private int status;
    private int commentCount;
    private long createTime;
    private String id;
    private int likeCount;
    private String content;
    private List<String> imgList; // 图片列表
    private String fatherId; // 父评论ID
    private String replyChildId; // 回复的子评论ID
}
