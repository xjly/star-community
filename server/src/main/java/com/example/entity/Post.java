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
public class Post {
    private String id;
    private String userId;
    private String avatar;
    private String nickname;
    private Date createTime;
    private String content;
    private int shareCount;
    private int likeCount;
    private int commentCount;
    private int viewCount;
    private int status;
    private String title;
    private int topic;
}
