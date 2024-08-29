package me.flyray.bsin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.TDict;

public interface TDictMapper extends BaseMapper<TDict> {
    TDict findByType(String type);
}
