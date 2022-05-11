package com.cos.securiy1.auth.provider;

import java.util.Map;

public class FacebookUserInfo implements OAuth2UserInfo{
    private Map<String,Object> attributes; // getAttributes;

    // 생성자
    public FacebookUserInfo(Map<String,Object> attributes){
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String)attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }
}
