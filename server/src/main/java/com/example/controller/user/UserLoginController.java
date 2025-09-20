package com.example.controller.user;

import com.example.context.BaseContext;
import com.example.dto.UserDTO;
import com.example.entity.User;
import com.example.result.Result;
import com.example.service.UserService;
import com.example.util.WechatUtil;
import com.example.vo.UserInfoVO;
import com.example.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserLoginController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public Result<UserVO> wxLogin(@RequestBody UserDTO userDTO) {
        UserVO userVO = userService.userVO(userDTO);
        log.info("微信登录:{}", userVO);
        return Result.success(userVO);
    }
    @GetMapping("/nickname")
    public Result<String> updateNickname(@RequestParam String nickname) {
        log.info("更新用户{}名:{}",BaseContext.getCurrentId(), nickname);
        userService.updateNickname(nickname);
        return Result.success();
    }
    @GetMapping("/introduction")
    public Result<String> updateIntroduction(@RequestParam String introduction) {
        log.info("更新用户{}简介:{}",BaseContext.getCurrentId(), introduction);
        userService.updateIntroduction(introduction);
        return Result.success();
    }
    @GetMapping("/gender")
    public Result<String> updateGender(@RequestParam boolean gender) {
        log.info("更新用户{}性别:{}",BaseContext.getCurrentId(), gender);
        userService.updateGender(gender);
        return Result.success();
    }
    @GetMapping("/avatar")
    public Result<String> updateAvatar(@RequestParam String avatar) {
        log.info("更新用户{}头像：{}",BaseContext.getCurrentId(), avatar);
        userService.updateAvatar(avatar);
        return Result.success();
    }
    @GetMapping("/background")
    public Result<String> updateBackground(@RequestParam String background) {
        log.info("更新用户{}背景图：{}",BaseContext.getCurrentId(), background);
        userService.updateBackground(background);
        return Result.success();
    }
    @GetMapping("/info")
    public Result<UserInfoVO> updateInfo() {
        log.info("查找用户{}信息",BaseContext.getCurrentId());
        UserInfoVO userInfo = userService.getUserInfo(BaseContext.getCurrentId());
        return Result.success(userInfo);
    }
    @GetMapping
    public Result<UserInfoVO> getUserInfo(@RequestParam String userId) {
        return Result.success(userService.getUserInfo(userId));
    }
}
