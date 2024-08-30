package com.yami.shop.security.api.dto;

import lombok.Data;

@Data
public class ShopLoginResult {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;
    private String role_name;
    private String license;
    private String user_id;
    private String role_id;
    private String app_zone_id;
    private String oauth_id;
    private String real_name;
    private String avatar;
    private Detail detail;
    private String dept_id;
    private String account;
    private String level;
    private String jti;
    private String nick_name;

    @Data
    public class Detail {
        private String type;
        private String phone;
        private String thirdId;
    }
}
