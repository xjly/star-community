package com.example.controller.user;

import com.example.util.CosUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserCommonController {
    @Autowired
    private CosUtil cosUtil;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            log.info("上传文件：{}", file.getOriginalFilename());
            String imageUrl = cosUtil.uploadFile(file);
            return imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败";
        }
    }
}
