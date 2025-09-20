package com.example.dto;

import com.example.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private String id;
    private String title;
    private String content;
    private int status;
    private long createTime;
    private String userId;
    private List<String> imgList;
    private int likeCount;
    private int commentCount;
    private int viewCount;
    private int shareCount;
    private int topic;
}
