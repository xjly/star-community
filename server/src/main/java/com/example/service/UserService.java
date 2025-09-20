package com.example.service;

import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.vo.UserInfoVO;
import com.example.vo.UserVO;

public interface UserService {
    UserVO userVO(UserDTO userDTO);

    void updateNickname(String nickname);

    void updateIntroduction(String introduction);

    void updateGender(boolean gender);

    void updateAvatar(String avatar);

    void updateBackground(String background);

    UserInfoVO getUserInfo(String userId);
}
