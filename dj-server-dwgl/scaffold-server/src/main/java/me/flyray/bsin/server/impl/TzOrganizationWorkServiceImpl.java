package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.TzOrganizationWorkService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.PageVo;
import me.flyray.bsin.server.domain.SearchVo;
import me.flyray.bsin.server.domain.TzOrganizationWork;
import me.flyray.bsin.server.mapper.TzOrganizationWorkMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ：hxh
 * @date ：Created in 2021/11/30 16:23
 * @description：
 * @modified By：
 */

public class TzOrganizationWorkServiceImpl implements TzOrganizationWorkService {

    @Autowired
    private TzOrganizationWorkMapper tzOrganizationWorkMapper;


    /**
     * 分页查询示例
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getByPage(Map<String, Object> requestMap) {
      /*  String type = (String)requestMap.get("type");
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), PageVo.class);
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
        Map<String,Object> pagination = (Map<String,Object>)requestMap.get("pagination");
        //BsinPageUtil.pageNotNull(pagination);
        //PageHelper.startPage((Integer) pagination.get("pageNum"),(Integer) pagination.get("pageSize"));
        PageHelper.startPage(page,limit);
        List<TzOrganizationWork> organizationWorks = tzOrganizationWorkMapper.selectPageList(type);
        PageInfo<TzOrganizationWork> pageInfo = new PageInfo<TzOrganizationWork>(organizationWorks);
        return RespBodyHandler.setRespPageInfoBodyDto(pageInfo);*/

        SearchVo searchVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), SearchVo.class);
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), PageVo.class);
        TzOrganizationWork tzOrganizationWork =  JSON.parseObject(JSON.toJSONString(requestMap.get("data")), TzOrganizationWork.class);
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
        tzOrganizationWork.setDelFlag(0);
        IPage<TzOrganizationWork> pageData = new Page<>(page, limit);
        QueryWrapper<TzOrganizationWork> queryWrapper = new QueryWrapper<>();
        if (tzOrganizationWork != null) {
            queryWrapper = LikeAllField(tzOrganizationWork, searchVo);
        }

        IPage<TzOrganizationWork> result = tzOrganizationWorkMapper.selectPage(pageData, queryWrapper);
        System.out.println(result);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
    }


    /**
     * 组工添加或者修改
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> addNews(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        TzOrganizationWork tzOrganizationWork =  JSON.parseObject(JSON.toJSONString(map.get("organizationWork")), TzOrganizationWork.class);
        tzOrganizationWork.setDelFlag(0);
        tzOrganizationWork.setStatus("0");
        if(tzOrganizationWork.getId()!=null){
            TzOrganizationWork tmp = tzOrganizationWorkMapper.selectById(tzOrganizationWork.getId());
            if (tmp==null){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
            }
            tzOrganizationWork.setUpdateTime(new Date());
            int i = tzOrganizationWorkMapper.updateById(tzOrganizationWork);
            if(i==0){
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("修改成功"));
            }
        }
        tzOrganizationWork.setCreateTime(new Date());
        int insert = tzOrganizationWorkMapper.insert(tzOrganizationWork);
        if(insert==0){
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("添加失败"));
        }
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("添加成功"));
    }

    /**
     * 根据id查询组工详情数据
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getDataById(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if(StringUtils.isEmpty(id)){
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("id为空，请联系管理员！！！"));
        }
        QueryWrapper<TzOrganizationWork>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("tz_organization_work.del_flag",0).eq("tz_organization_work.id",id);
        TzOrganizationWork tzOrganizationWork = tzOrganizationWorkMapper.selectOne(queryWrapper);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzOrganizationWork));
    }

    /**
     * 根据id删除组工详情数据
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getDelById(Map<String, Object> requestMap) {
        try {
            List<String> ids = (List<String>) requestMap.get("ids");
            if(ids==null||ids.size()==0){
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("id为空，请联系管理员！！！"));
            }
            //List<String> list = Arrays.asList(ids.split(","));
            UpdateWrapper<TzOrganizationWork> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("tz_organization_work.del_flag", 1).in("tz_organization_work.id", ids);
            int update = tzOrganizationWorkMapper.update(null,updateWrapper);
            if (update > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(update));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
        }
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tzOrganizationWork 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TzOrganizationWork> LikeAllField(TzOrganizationWork tzOrganizationWork, SearchVo searchVo) {
        QueryWrapper<TzOrganizationWork> queryWrapper = new QueryWrapper<>();
        if (tzOrganizationWork.getTitle() != null) {
            queryWrapper.and(i -> i.like("tz_organization_work.title", tzOrganizationWork.getTitle()));
        }
        if (StringUtils.isNotBlank(tzOrganizationWork.getType())) {
            queryWrapper.and(i -> i.eq("tz_organization_work.type", tzOrganizationWork.getType()));
        }
        if (tzOrganizationWork.getDelFlag() != null) {
            queryWrapper.and(i -> i.eq("tz_organization_work.del_flag", tzOrganizationWork.getDelFlag()));
        }
        return queryWrapper;
    }
}

