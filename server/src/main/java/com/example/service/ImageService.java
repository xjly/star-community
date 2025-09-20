package com.example.service;

import org.springframework.stereotype.Service;

import java.util.List;

public interface ImageService {
    public List<String> getImage(String postId);
}
