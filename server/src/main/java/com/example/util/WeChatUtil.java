package com.example.util;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.properties.WeChatPropertites;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;


@Component
public class WeChatUtil {
    //	配置自己的app_id、app_secret
    @Autowired
    private  WeChatPropertites weChatPropertites;

    public  String getOpenId(String loginCode) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("appid",weChatPropertites.getAppId())
                .queryParam("secret",weChatPropertites.getAppSecret())
                .queryParam("js_code", loginCode)
                .queryParam("grant_type", "authorization_code")
                .toUriString();

        HttpResponse response = HttpUtil.createGet(requestUrl).execute();

        // 获取 session_key 和 openid
        JSONObject parseObj = JSONUtil.parseObj(response.body());

        String openid = (String) parseObj.get("openid");
        return openid;
    }
}
