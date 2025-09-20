package com.example.vo;

import com.example.entity.Image;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostVO {
    private String id;
    private String userId;
    private String avatar;
    private String nickname;
    private long createTime;
    private String content;
    private int shareCount;
    private int likeCount;
    private int commentCount;
    private int viewCount;
    private List<String> imgList;
    private String title;
    private int status;
    private int topic;
}
