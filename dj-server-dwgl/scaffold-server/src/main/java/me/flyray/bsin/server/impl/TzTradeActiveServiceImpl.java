package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import me.flyray.bsin.facade.service.TzTradeActiveService;
import me.flyray.bsin.facade.service.TzTradeUnionService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.AttachFile;
import me.flyray.bsin.server.domain.PageVo;
import me.flyray.bsin.server.domain.TzTradeActive;
import me.flyray.bsin.server.domain.TzTradeUnion;
import me.flyray.bsin.server.mapper.AttachFileMapper;
import me.flyray.bsin.server.mapper.TzTradeActiveMapper;
import me.flyray.bsin.server.mapper.TzTradeUnionMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 具体投票项 服务实现类
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
public class TzTradeActiveServiceImpl extends ServiceImpl<TzTradeActiveMapper, TzTradeActive> implements TzTradeActiveService {
    @Autowired
    private TzTradeActiveMapper tzTradeActiveMapper;

    @Autowired
    private AttachFileMapper attachFileMapper;

    @Autowired
    private TzTradeUnionMapper tzTradeUnionMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> addTradeActive(Map<String, Object> requestMap) {
        try {
            TzTradeActive tzTradeActive = JSON.parseObject(JSON.toJSONString(requestMap.get("active")), TzTradeActive.class);
            List<AttachFile> attachFileList = tzTradeActive.getAttachFileList();
            List<AttachFile> imageFileList = tzTradeActive.getImageFileList();

            if (tzTradeActive.getId() == null) {
                tzTradeActive.setDelFlag(0);
                tzTradeActive.setCreateTime(new Date());
                int insert = tzTradeActiveMapper.insert(tzTradeActive);
                if (insert <= 0) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"添加失败");
                }

            } else {
                tzTradeActive.setUpdateTime(new Date());
                tzTradeActiveMapper.updateById(tzTradeActive);
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"添加失败");
        }

    }

    @Override
    public Map<String, Object> queryTradeActiveList(Map<String, Object> requestMap) {
        Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
        TzTradeActive tzTradeActive = JSON.parseObject(JSON.toJSONString(dataMap.get("tzActive")), TzTradeActive.class);
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);

        try {
            int page = 1;
            int limit = 10;
            if (pageVo != null) {
                if (pageVo.getPageNumber() != 0) {
                    page = pageVo.getPageNumber();
                }
                if (pageVo.getPageSize() != 0) {
                    limit = pageVo.getPageSize();
                }
            }

            IPage<TzTradeActive> pageData = new Page<>(page, limit);

            // 查询所有下级部门id
            QueryWrapper q1 = new QueryWrapper();
            q1.select("id");
            q1.like("parent_ids", tzTradeActive.getTradeId());
            List<TzTradeUnion> tzTradeUnionList = tzTradeUnionMapper.selectList(q1);
            List<Integer> ids = tzTradeUnionList.stream().map(tradeUnion -> tradeUnion.getId()).collect(Collectors.toList());
            ids.add(tzTradeActive.getTradeId());

            if (ids.size() == 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new Page()));
            }
            QueryWrapper<TzTradeActive> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("trade_id", ids);
            queryWrapper.eq("del_flag", 0);
            IPage<TzTradeActive> result = tzTradeActiveMapper.selectPage(pageData, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> getTradeActiveById(Map<String, Object> requestMap) {
        try {
            Integer id = (Integer) requestMap.get("id");
            if (id == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }
            TzTradeActive tzTradeActive = tzTradeActiveMapper.selectById(id);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTradeActive));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> deleteTradeActive(Map<String, Object> requestMap) {
        try {
            //List<String> ids = (List<String>) requestMap.get("ids");
            List<String> ids = new ArrayList<>();
            for (Object id : (List<?>) requestMap.get("ids")) {
                ids.add(String.valueOf(id));
            }
            if (ids == null || ids.size() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }

            UpdateWrapper<TzTradeActive> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("tz_trade_active.id", ids);
            updateWrapper.set("del_flag", 1);
            int update = tzTradeActiveMapper.update(null, updateWrapper);
            if (update > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(update));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }
}
