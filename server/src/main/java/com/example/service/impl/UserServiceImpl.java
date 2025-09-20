package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.context.BaseContext;
import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.properties.JwtProperties;
import com.example.service.UserService;
import com.example.util.JwtUtil;
import com.example.util.WechatUtil;
import com.example.vo.UserInfoVO;
import com.example.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public UserVO userVO(UserDTO userDTO) {
        String code = userDTO.getCode();
        String openId = WechatUtil.getOpenId(code);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("open_id", openId);
        User user = userMapper.selectOne(userQueryWrapper);
        if(user == null){
            user = User.builder()
                    .openId(openId)
                    .createTime(Timestamp.valueOf(LocalDateTime.now()))
                    .build();
            userMapper.insert(user);
        }
        user = userMapper.selectOne(userQueryWrapper);
        UserVO userVO = UserVO.builder()
                        .id(user.getId())
                        .openId(openId)
                        .build();
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        userVO.setToken(token);
        return userVO;
    }

    @Override
    public void updateNickname(String nickname) {
        userMapper.insertOrUpdate(User.builder().nickname(nickname).id(BaseContext.getCurrentId()).build());
    }

    @Override
    public void updateIntroduction(String introduction) {
        userMapper.insertOrUpdate(User.builder().introduction(introduction).id(BaseContext.getCurrentId()).build());
    }

    @Override
    public void updateGender(boolean gender) {
        userMapper.insertOrUpdate(User.builder().gender(gender).id(BaseContext.getCurrentId()).build());
    }

    @Override
    public void updateAvatar(String avatar) {
        userMapper.insertOrUpdate(User.builder().avatar(avatar).id(BaseContext.getCurrentId()).build());
    }

    @Override
    public void updateBackground(String background) {
        userMapper.insertOrUpdate(User.builder().background(background).id(BaseContext.getCurrentId()).build());
    }

    @Override
    public UserInfoVO getUserInfo(String userId) {
        return userMapper.getUserInfo(userId);
    }
}
