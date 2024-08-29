package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tz_user_tag_user")
public class TzUserTagUser {
    private Integer userTagUserId;
    private Integer userTagId;
    private String userId;
}
