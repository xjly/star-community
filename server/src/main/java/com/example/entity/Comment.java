package com.example.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private String id;
    private String userId;
    private String postId;
    private String fatherId;
    private Date createTime;
    private String content;
    private int likeCount;
    private String replyChildId;
    private int status;
    private Integer isDelete;
}
