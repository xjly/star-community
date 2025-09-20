package com.example.util;

import com.example.properties.TencentCosProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class CosUtil {
    @Autowired
    private TencentCosProperties properties;
    // 上传文件到 COS
    public String uploadFile(MultipartFile file) throws IOException {

        // 生成唯一文件名
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        // 初始化 COS 客户端
        COSCredentials cred = new BasicCOSCredentials(properties.getAccessKey(), properties.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(properties.getRegion()));
        COSClient cosClient = new COSClient(cred, clientConfig);

        // 上传文件
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        PutObjectRequest putObjectRequest = new PutObjectRequest(properties.getBucket(), fileName, tempFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);

        // 关闭客户端
        cosClient.shutdown();

        // 返回文件访问链接
        return properties.getBaseUrl() + "/" + fileName;
    }
}
