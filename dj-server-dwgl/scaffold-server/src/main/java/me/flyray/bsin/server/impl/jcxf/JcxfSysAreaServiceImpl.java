package me.flyray.bsin.server.impl.jcxf;

import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.flyray.bsin.facade.service.JcxfSysAreaService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.jcxf.AreaTree;
import me.flyray.bsin.server.domain.jcxf.JcxfSysArea;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysAreaMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JcxfSysAreaServiceImpl implements JcxfSysAreaService {

    @Autowired
    private JcxfSysAreaMapper jcxfSysAreaMapper;

    @Override
    public Map<String, Object> getAreaList(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String level = (String) requestMap.get("level");

            QueryWrapper queryWrapper = new QueryWrapper();

            if (StringUtils.isNotBlank(id)) {
                queryWrapper.eq("pId", id);
            }
            queryWrapper.eq("level", level);
            queryWrapper.eq("del_flag", false);
            List<JcxfSysArea> areasList = jcxfSysAreaMapper.selectList(queryWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(areasList));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getAreaTree(Map<String, Object> requestMap) {
        try {
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("del_flag", false);
            queryWrapper1.eq("level", 1);
            List<JcxfSysArea> jcxfSysAreaList1 = jcxfSysAreaMapper.selectList(queryWrapper1);

            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("del_flag", false);
            queryWrapper2.eq("level", 2);
            List<JcxfSysArea> jcxfSysAreaList2 = jcxfSysAreaMapper.selectList(queryWrapper2);

            QueryWrapper queryWrapper3 = new QueryWrapper();
            queryWrapper3.eq("del_flag", false);
            queryWrapper3.eq("level", 3);
            List<JcxfSysArea> jcxfSysAreaList3 = jcxfSysAreaMapper.selectList(queryWrapper3);


            Iterator<JcxfSysArea> iterator1 = jcxfSysAreaList1.iterator();

            while (iterator1.hasNext()) {
                JcxfSysArea JcxfSysArea1 = iterator1.next();
                List<JcxfSysArea> children1 = new ArrayList<>();

                Iterator<JcxfSysArea> iterator2 = jcxfSysAreaList2.iterator();
                while (iterator2.hasNext()) {
                    JcxfSysArea JcxfSysArea2 = iterator2.next();
                    List<JcxfSysArea> children2 = new ArrayList<>();

                    Iterator<JcxfSysArea> iterator3 = jcxfSysAreaList3.iterator();
                    while (iterator3.hasNext()) {
                        JcxfSysArea JcxfSysArea3 = iterator3.next();
                        if (JcxfSysArea3.getPId().equals(JcxfSysArea2.getId())) {
                            children2.add(JcxfSysArea3);
                        }
                    }
                    if (JcxfSysArea2.getPId().equals(JcxfSysArea1.getId())) {
                        children1.add(JcxfSysArea2);
                    }
                    JcxfSysArea2.setChildren(children2);

                }
                JcxfSysArea1.setChildren(children1);
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jcxfSysAreaList1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
