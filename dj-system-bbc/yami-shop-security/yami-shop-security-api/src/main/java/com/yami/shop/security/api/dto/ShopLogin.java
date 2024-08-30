package com.yami.shop.security.api.dto;

import lombok.Data;

@Data
public class ShopLogin {
    private String accessToken;
    private String appId;

    private String money;
}
