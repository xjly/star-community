package com.example.service.impl;

import com.example.mapper.ImageMapper;
import com.example.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageMapper imageMapper;
    @Override
    public List<String> getImage(String postId) {
        return imageMapper.selectByPostId(postId);
    }
}
