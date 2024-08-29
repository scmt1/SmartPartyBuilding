package me.flyray.bsin.server.impl.cq;

import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.flyray.bsin.facade.service.CqSysOrgService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.CqSysOrg;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.CqSysOrgMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class CqSysOrgServiceImpl implements CqSysOrgService {

    @Autowired
    private CqSysOrgMapper cqSysOrgMapper;



    @Override
    public Map<String, Object> getTzSysOrgList(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        List<CqSysOrg> list = cqSysOrgMapper.getParentDeptListById(deptId);
        return RespBodyHandler.setRespBodyListDto(list);
    }

    @Override
    public Map<String, Object> getTzSysOrg(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空，请联系管理员！");
        }
        try {
            CqSysOrg res = cqSysOrgMapper.selectById(id);
            if (res != null) {
                // 判断是否叶子节点
                QueryWrapper q = new QueryWrapper();
                q.eq("parent_id", id);
                int count = cqSysOrgMapper.selectCount(q);
                if (count > 0) {
                    res.setLeaf(false);
                } else {
                    res.setLeaf(true);
                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(res));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getOrgListByName(Map<String, Object> requestMap) {
        try {
            String name = (String) requestMap.get("deptName");
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = cqSysOrgMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.like("name", name);
            queryWrapper.in("id", ids);
            List<CqSysOrg> list = cqSysOrgMapper.selectList(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }
}
