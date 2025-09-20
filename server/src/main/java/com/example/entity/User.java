package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String nickname;
    private String avatar;
    private String openId;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String introduction;
    private Boolean gender;
    private String background;
}
