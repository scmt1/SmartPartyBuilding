package me.flyray.bsin.server.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.TzStudyUser;

@DS(value = "dj_party")
public interface TzStudyUserMapper extends BaseMapper<TzStudyUser> {
}
