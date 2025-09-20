package com.example.util;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.web.util.UriComponentsBuilder;

public class WechatUtil {
    //	配置自己的app_id、app_secret
    private static final String APP_ID = "wx768ed27d22d3dc81";
    private static final String APP_SECRET = "1584a9762750b89d11de7109dd0e6c1b";

    public static String getOpenId(String loginCode) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        String requestUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("appid", APP_ID)
                .queryParam("secret", APP_SECRET)
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
