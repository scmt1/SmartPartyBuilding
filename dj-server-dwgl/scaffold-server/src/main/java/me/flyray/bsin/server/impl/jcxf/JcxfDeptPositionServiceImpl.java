package me.flyray.bsin.server.impl.jcxf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.flyray.bsin.facade.service.JcxfDeptPositionService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.jcxf.JcxfDeptPosition;
import me.flyray.bsin.server.domain.jcxf.JcxfDeptPositionPhoto;
import me.flyray.bsin.server.mapper.jcxf.JcxfDeptPositionMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfDeptPositionPhotoMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JcxfDeptPositionServiceImpl implements JcxfDeptPositionService {

    @Autowired
    private JcxfDeptPositionMapper jcxfDeptPositionMapper;

    @Autowired
    private JcxfDeptPositionPhotoMapper jcxfDeptPositionPhotoMapper;


    @Override
    public Map<String, Object> getJcxfDeptPositionByDeptId(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("dept_id", deptId);
            queryWrapper.eq("del_flag", 0);
            JcxfDeptPosition position = jcxfDeptPositionMapper.selectOne(queryWrapper);

            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("dept_id", deptId);
            List<JcxfDeptPositionPhoto> photoList = jcxfDeptPositionPhotoMapper.selectList(queryWrapper1);

            Map<String, Object> map = new HashMap<>();
            map.put("position", position);
            map.put("photoList", photoList);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public synchronized Map<String, Object> addOrUpdateJcxfDeptPosition(Map<String, Object> requestMap) {
        try {
            Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
            JcxfDeptPosition position = JSON.parseObject(JSON.toJSONString(dataMap.get("jcxfDeptPosition")), JcxfDeptPosition.class);
            List<JcxfDeptPositionPhoto> photoList = JSONArray.parseArray(JSON.toJSONString(dataMap.get("jcxfDeptPositionPhoto")), JcxfDeptPositionPhoto.class);


            if (position.getId() == null) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("dept_id", position.getDeptId());
                queryWrapper.eq("del_flag", 0);
                JcxfDeptPosition positionOld = jcxfDeptPositionMapper.selectOne(queryWrapper);

                int res = 0;
                if (positionOld != null) {
                    position.setId(positionOld.getId());
                    position.setUpdateDate(new Date());
                    res = jcxfDeptPositionMapper.updateById(position);
                } else {
                    position.setCreateDate(new Date());
                    res = jcxfDeptPositionMapper.insert(position);
                }
                if (res > 0) {
                    // 先删除旧图片
                    QueryWrapper updateWrapper = new QueryWrapper();
                    updateWrapper.eq("dept_id", position.getDeptId());
                    jcxfDeptPositionPhotoMapper.delete(updateWrapper);

                    //写入新图片
                    for (JcxfDeptPositionPhoto photo : photoList) {
                        jcxfDeptPositionPhotoMapper.insert(photo);
                    }

                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
                }
            } else {
                position.setUpdateDate(new Date());
                int res = jcxfDeptPositionMapper.updateById(position);
                if (res > 0) {
                    // 先删除旧图片
                    QueryWrapper updateWrapper = new QueryWrapper();
                    updateWrapper.eq("dept_id", position.getDeptId());
                    jcxfDeptPositionPhotoMapper.delete(updateWrapper);

                    //写入新图片
                    for (JcxfDeptPositionPhoto photo : photoList) {
                        jcxfDeptPositionPhotoMapper.insert(photo);
                    }

                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }


}
