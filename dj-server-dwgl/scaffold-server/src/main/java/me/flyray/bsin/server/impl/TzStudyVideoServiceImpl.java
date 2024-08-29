package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.TzStudyVideoService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.TzStudyUser;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.impl.TzMessageServiceImpl;
import me.flyray.bsin.server.mapper.*;
import me.flyray.bsin.server.mapper.jcxf.*;
import me.flyray.bsin.server.utils.RespBodyHandler;
import me.flyray.bsin.server.utils.ShortMessageResult;
import me.flyray.bsin.server.utils.ShortMessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.*;

public class TzStudyVideoServiceImpl implements TzStudyVideoService {
    @Autowired
    private TzMessageServiceImpl tzMessageService;

    @Autowired
    private TzMessageMapper tzMessageMapper;
    @Autowired
    private AttachFileMapper attachFileMapper;

    @Autowired
    private TzStudyUserMapper tzStudyUserMapper;

    @Autowired
    private TzStudyVideoMapper tzStudyVideoMapper;

    @Autowired
    private TzVideoColumnMapper tzVideoColumnMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzStudyViewMapper tzStudyViewMapper;

    @Autowired
    private TzMessageAutoMapper tzMessageAutoMapper;

    @Autowired
    private TzMessageAutoRoleMapper tzMessageAutoRoleMapper;

    @Autowired
    private TzMessageAutoContentMapper tzMessageAutoContentMapper;

    @Autowired
    private TzMessageDetailMapper tzMessageDetailMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @DS("dj_party")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> deleteTzStudyVideo(Map<String, Object> requestMap) {
        List<String> ids = (List<String>) requestMap.get("ids");

        if (ids == null || ids.size() == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空，请联系管理员！");
        }
        try {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.in("id", ids);
            updateWrapper.set("del_flag", 1);
            tzStudyVideoMapper.update(null, updateWrapper);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("study_id", ids);
            tzStudyViewMapper.delete(queryWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());

        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }

    @Override
    public Map<String, Object> postVideo(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        String status = (String) requestMap.get("status");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空，请联系管理员");
        }
        try {
            QueryWrapper<TzStudyVideo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_study_video.id", id).eq("tz_study_video.del_flag", 0);
            TzStudyVideo tzStudyVideo = tzStudyVideoMapper.selectOne(queryWrapper);
            if (tzStudyVideo == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"数据为空，发布失败");
            }
            tzStudyVideo.setStatus(status);
            tzStudyVideo.setPostTime(new Date());
            int updateById = tzStudyVideoMapper.updateById(tzStudyVideo);
            if (updateById > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"发布失败");
            }
        } catch (Exception e) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"发布失败");
        }
    }

    @Override
    public Map<String, Object> queryTzStudyVideoList(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

        TzStudyVideo tzStudyVideo = JSON.parseObject(JSON.toJSONString(map.get("tzStudyVideo")), TzStudyVideo.class);
        SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);

        try {
            Long deptId = tzStudyVideo.getDeptId();
            QueryWrapper<TzVideoColumn> queryWrapper = new QueryWrapper<>();
            if (tzStudyVideo.getColumnId() != null) {
                queryWrapper.eq("tz_video_column.id", tzStudyVideo.getColumnId());
            }
            queryWrapper.eq("tz_video_column.dept_id", deptId);
            queryWrapper.eq("tz_video_column.del_flag", 0);
            List<TzVideoColumn> list = tzVideoColumnMapper.selectList(queryWrapper);
            if (list == null || list.size() == 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new Page()));
            }

            List<Integer> colunmIds = new ArrayList<>();
            for (TzVideoColumn tzVideoColumn : list) {
                colunmIds.add(tzVideoColumn.getId());
            }
            IPage<TzStudyVideo> result = queryTzStudyVideoListByPage(tzStudyVideo, searchVo, pageVo, colunmIds);

//            for (TzStudyVideo video : result.getRecords()) {
//                video.setTzVideoColumn(tzVideoColumnMapper.selectById(video.getColumnId()));
//
//                QueryWrapper q2 = new QueryWrapper();
//                q2.eq("study_id", video.getId());
//                video.setTzStudyView(tzStudyViewMapper.selectOne(q2));
//            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> saveVideo(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            String deptId = (String) map.get("deptId");
            TzStudyVideo tzStudyVideo = JSON.parseObject(JSON.toJSONString(map.get("tzStudyVideo")), TzStudyVideo.class);

            if (StringUtils.isBlank(deptId)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"部门id为空，请联系管理员");
            }
            tzStudyVideo.setDeptId(Long.valueOf(deptId));
            if (tzStudyVideo.getId() != null) {
                tzStudyVideo.setStatus("0");
                tzStudyVideo.setUpdateTime(new Date());
                int updateById = tzStudyVideoMapper.updateById(tzStudyVideo);
                if (updateById > 0) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
                }

            } else {
                tzStudyVideo.setDelFlag(0);
                // 新增时，判断一下栏目是否停用
                TzVideoColumn column = tzVideoColumnMapper.selectById(tzStudyVideo.getColumnId());
                // 启用状态 1、启用 2、停用
                if ("2".equals(column.getUseStatus())) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"该栏目已停用");
                }

                tzStudyVideo.setStatus("0"); //状态为已新建
                tzStudyVideo.setCreateTime(new Date());

                int res = tzStudyVideoMapper.insert(tzStudyVideo);
                if (res > 0) {
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

    @Override
    public Map<String, Object> getTzStudyVideo(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
        }
        try {
            TzStudyVideo studyVideo = tzStudyVideoMapper.selectById(id);
            if (studyVideo != null && studyVideo.getDelFlag().intValue() == 0) {
                QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tz_attach_file.foreign_key", studyVideo.getId());
                queryWrapper.eq("tz_attach_file.table_type", "tz_study_video");
                List<AttachFile> list = attachFileMapper.selectList(queryWrapper);
                Map<String, Object> map = new HashMap<>();
                for (AttachFile attachFile : list) {
                    if (attachFile.getType().intValue() == 2 || attachFile.getType().intValue() == 3) {
                        map.put("video", attachFile);
                    } else if (attachFile.getType().intValue() == 1) {
                        map.put("image", attachFile);
                    }
                }
                map.put("studyVideo", studyVideo);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> getTzStudyVideoByApp(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }

            String partyMemberId = (String) requestMap.get("partyMemberId");

            TzStudyVideo studyVideo = tzStudyVideoMapper.selectById(id);
            if (studyVideo != null) {
                // 判断是否删除
                if (studyVideo.getDelFlag().intValue() == 1) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"该课程已删除");
                }

                // 判断发布状态 审核状态(0未发布 1 已发布)
                if ("0".equals(studyVideo.getStatus())) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"课程尚未发布");
                }

                // 判断是否可见时间内 查看类型 1、时间内可看 2、永久查看
                if ("1".equals(studyVideo.getViewType())) {
                    long nowTime = new Date().getTime();
                    if (nowTime < studyVideo.getStartTime().getTime() || nowTime > studyVideo.getEndTime().getTime()) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"该课程已过学习时间");
                    }
                }

                // 判断所属类型是否隐藏
                TzVideoColumn column = tzVideoColumnMapper.selectById(studyVideo.getColumnId());
                if (column == null || !"1".equals(column.getShowStatus()) || column.getDelFlag().intValue() != 0) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"该课程不可查看");
                }

                // 判断可见范围
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("study_id", studyVideo.getId());
                TzStudyView view = tzStudyViewMapper.selectOne(queryWrapper);
                if (view == null) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂无权限查看该课程");
                }
                JcxfPartyMember partyMember = jcxfPartyMemberMapper.selectById(partyMemberId);
                if (partyMember == null || partyMember.getDelFlag() == true) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到党员信息");
                }

                // 当前及其下级部门可看
                if ("1".equals(view.getType())) {
                    Boolean viewFlag = false;
                    for (String deptId: view.getDeptIds().split(",")) {
                        if (StringUtils.isNotBlank(deptId)) {
                            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
                            deptIds.add(Long.valueOf(deptId));
                            if (deptIds.contains(partyMember.getDeptId())) {
                                viewFlag = true;
                                break;
                            }
                        }
                    }

                    if (!viewFlag) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂无权限查看该课程");
                    }

                } else if ("2".equals(view.getType())) {
                    // 当前部门可看
                    if (!view.getDeptIds().contains("," + partyMember.getDeptId() + ",")) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂无权限查看该课程");
                    }

                } else if ("3".equals(view.getType())) {
                    // 当前职务可看-当前及其下级部门
                    if (!view.getPositionIds().contains("," + partyMember.getPosition() + ",")) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂无权限查看该课程");
                    }

                    Boolean viewFlag = false;
                    for (String deptId: view.getDeptIds().split(",")) {
                        if (StringUtils.isNotBlank(deptId)) {
                            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
                            deptIds.add(Long.valueOf(deptId));
                            if (deptIds.contains(partyMember.getDeptId())) {
                                viewFlag = true;
                                break;
                            }
                        }
                    }
                    if (!viewFlag) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂无权限查看该课程");
                    }

                } else if ("4".equals(view.getType())) {
                    // 当前职务可看-仅当前部门
                    if (!view.getDeptIds().contains("," + partyMember.getDeptId() + ",")) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂无权限查看该课程");
                    }

                    if (!view.getPositionIds().contains("," + partyMember.getPosition() + ",")) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂无权限查看该课程");
                    }
                } else if ("5".equals(view.getType())) {
                    // 选中党员可看
                    if (!view.getPartyMemberIds().contains("," + partyMember.getId() + ",")) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂无权限查看该课程");
                    }
                }


                QueryWrapper<AttachFile> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("tz_attach_file.foreign_key", studyVideo.getId());
                queryWrapper1.eq("tz_attach_file.table_type", "tz_study_video");
                List<AttachFile> list = attachFileMapper.selectList(queryWrapper1);
                Map<String, Object> map = new HashMap<>();
                for (AttachFile attachFile : list) {
                    if (attachFile.getType() == 2 || attachFile.getType() == 3)
                        map.put("video", attachFile);
                    else if (attachFile.getType() == 1)
                        map.put("image", attachFile);
                }
                map.put("studyVideo", studyVideo);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> getTzStudyVideoListByType(Map<String, Object> requestMap) {
        try {
            String type = (String) requestMap.get("type");
            String userId = (String) requestMap.get("userId");

            QueryWrapper q1 = new QueryWrapper();
            q1.eq("id", userId);
            q1.eq("del_flag", 0);
            JcxfPartyMember partyMember = jcxfPartyMemberMapper.selectOne(q1);
            if (partyMember == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到党员信息");
            }
            List<Integer> studyIds = getStudyIdListByPartyMember(partyMember, null);

            if (studyIds.size() == 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new ArrayList()));
            }

            if (!"top".equals(type)) {
                QueryWrapper queryWrapper = new QueryWrapper();
                //queryWrapper.eq("dept_id", deptId);
                queryWrapper.eq("del_flag", 0);
                queryWrapper.eq("type", type); // 1图文栏目，2视频栏目
                queryWrapper.eq("show_status", "1");
                List<TzVideoColumn> imgColumn = tzVideoColumnMapper.selectList(queryWrapper);
                List<Integer> imgColumnIds = new ArrayList<>();
                for (TzVideoColumn tzVideoColumn : imgColumn) {
                    imgColumnIds.add(tzVideoColumn.getId());
                }
                List<TzStudyVideo> list = new ArrayList<>();
                if (imgColumnIds.size() > 0) {
                    QueryWrapper<TzStudyVideo> q = new QueryWrapper();
                    q.eq("del_flag", 0);
                    // q.eq("dept_id", deptId);
                    q.eq("status", 1);
                    q.in("column_id", imgColumnIds);
                    q.in("id", studyIds);
                    q.eq("show_home", 1);
                    q.and(n -> n.and(i -> i.eq("view_type", "1").le("start_time", new Date()).ge("end_time", new Date()))
                            .or().eq("view_type", "2"));
                    q.orderByAsc("show_home_sort");

                    list = tzStudyVideoMapper.selectList(q);

                    for (TzStudyVideo video : list) {
                        QueryWrapper qa = new QueryWrapper<>();
                        qa.eq("tz_attach_file.foreign_key", video.getId());
                        qa.eq("tz_attach_file.table_type", "tz_study_video");
                        qa.eq("tz_attach_file.type", 1);
                        video.setAttachFileList(attachFileMapper.selectList(qa));
                        video.setTzVideoColumn(tzVideoColumnMapper.selectById(video.getColumnId()));
                    }
                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));

            } else {
                String topType = (String) requestMap.get("topType");

                QueryWrapper<TzStudyVideo> qTop = new QueryWrapper();
                qTop.eq("del_flag", 0);
                // qTop.eq("dept_id", deptId);
                qTop.eq("status", 1);
                qTop.eq("top", 1);
                if (StringUtils.isNotBlank(topType)) {
                    /*QueryWrapper queryWrapper = new QueryWrapper();
                    //queryWrapper.eq("dept_id", deptId);
                    queryWrapper.eq("del_flag", 0);
                    queryWrapper.eq("type", topType); // 1图文栏目，2视频栏目
                    queryWrapper.eq("show_status", "1");
                    List<TzVideoColumn> imgColumn = tzVideoColumnMapper.selectList(queryWrapper);

                    if (imgColumn == null || imgColumn.size() == 0) {
                        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new ArrayList()));
                    }

                    List<Integer> imgColumnIds = new ArrayList<>();
                    for (TzVideoColumn tzVideoColumn : imgColumn) {
                        imgColumnIds.add(tzVideoColumn.getId());
                    }
                    qTop.in("column_id", imgColumnIds);*/

                    qTop.inSql("column_id", "select id from tz_video_column where del_flag = 0 and show_status = 1 and type="+ type+"");
                } else {
                    /*QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("del_flag", 0);
                    queryWrapper.eq("show_status", "1");
                    List<TzVideoColumn> imgColumn = tzVideoColumnMapper.selectList(queryWrapper);
                    if (imgColumn == null || imgColumn.size() == 0) {
                        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new ArrayList()));
                    }
                    List<Integer> imgColumnIds = new ArrayList<>();
                    for (TzVideoColumn tzVideoColumn : imgColumn) {
                        imgColumnIds.add(tzVideoColumn.getId());
                    }
                    qTop.in("column_id", imgColumnIds);*/
                    qTop.inSql("column_id", "select id from tz_video_column where del_flag = 0 and show_status = 1");
                }
                qTop.in("id", studyIds);
                qTop.and(n -> n.and(i -> i.eq("view_type", "1").le("start_time", new Date()).ge("end_time", new Date()))
                        .or().eq("view_type", "2"));
                qTop.orderByAsc("top_sort");
                List<TzStudyVideo> topList = tzStudyVideoMapper.selectList(qTop);
                for (TzStudyVideo video : topList) {
                    QueryWrapper<AttachFile> q = new QueryWrapper<>();
                    q.eq("tz_attach_file.foreign_key", video.getId());
                    q.eq("tz_attach_file.table_type", "tz_study_video");
                    q.eq("tz_attach_file.type", 1);
                    video.setAttachFileList(attachFileMapper.selectList(q));
                    video.setTzVideoColumn(tzVideoColumnMapper.selectById(video.getColumnId()));
                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(topList));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> addTzStudyUser(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            TzStudyUser tzStudyUser = JSON.parseObject(JSON.toJSONString(map.get("tzStudyUser")), TzStudyUser.class);
            if (tzStudyUser == null || tzStudyUser.getUserId() == null || tzStudyUser.getStudyId() == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"用户id或学习id为空，请联系管理员！");
            }
            Long userId = tzStudyUser.getUserId();
            Integer studyId = tzStudyUser.getStudyId();
            //查询是否有记录
            QueryWrapper<TzStudyUser> q = new QueryWrapper<>();
            q.eq("user_id", userId).eq("study_id", studyId);
            List<TzStudyUser> tzStudyUsers = tzStudyUserMapper.selectList(q);
            if (tzStudyUsers != null && tzStudyUsers.size() > 0) { //如果有记录则修改
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("id", tzStudyUsers.get(0).getId());
                updateWrapper.set("update_time", new Date());
                updateWrapper.set("last_time", new Date());
                int insert = tzStudyUserMapper.update(null, updateWrapper);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("已更新原记录访问时间"));
            }
            //添加记录
            String userStudy = userId.toString() + "," + studyId.toString();
            tzStudyUser.setUserStudy(userStudy);
            tzStudyUser.setCreateTime(new Date());
            int insert = tzStudyUserMapper.insert(tzStudyUser);
            if (insert > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("已新增记录"));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"添加记录失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"添加记录失败");
        }
    }

    @Override
    public Map<String, Object> queryStudyStatus(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            TzStudyUser tzStudyUser = JSON.parseObject(JSON.toJSONString(map.get("tzStudyUser")), TzStudyUser.class);
            if (tzStudyUser == null || tzStudyUser.getUserId() == null || tzStudyUser.getStudyId() == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"用户id或学习id为空，请联系管理员！");
            }
            Long userId = tzStudyUser.getUserId();
            Integer studyId = tzStudyUser.getStudyId();
            QueryWrapper<TzStudyUser> q = new QueryWrapper<>();
            q.eq("user_id", userId).eq("study_id", studyId);
            List<TzStudyUser> tzStudyUsers = tzStudyUserMapper.selectList(q);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzStudyUsers));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> queryStudyCount(Map<String, Object> requestMap) {
        try {
            String studyId = (String) requestMap.get("studyId");
            if (StringUtils.isBlank(studyId)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }
            QueryWrapper<TzStudyUser> q = new QueryWrapper<>();
            q.eq("study_id", studyId);
            Integer selectCount = tzStudyUserMapper.selectCount(q);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(selectCount));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }

    }

    @Override
    public Map<String, Object> getLastStudyByUserId(Map<String, Object> requestMap) {
        try {
            String userId = (String) requestMap.get("userId");
            QueryWrapper<TzStudyUser> queryWrapper = new QueryWrapper();
            queryWrapper.eq("user_id", userId);
            queryWrapper.orderByDesc("last_time");
            queryWrapper.last("limit 1");
            TzStudyUser tzStudyUser = tzStudyUserMapper.selectOne(queryWrapper);
            if (tzStudyUser != null) {
                TzStudyVideo studyVideo = tzStudyVideoMapper.selectById(tzStudyUser.getStudyId());

                // 如果栏目已隐藏，则不显示
                TzVideoColumn column = tzVideoColumnMapper.selectById(studyVideo.getColumnId());
                // 显示状态 1、显示 2、隐藏
                if (column == null || column.getDelFlag().intValue() != 0 || "2".equals(column.getShowStatus())) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(null));
                }

                // 审核状态(0未发布 1 已发布)
                if (studyVideo != null && studyVideo.getDelFlag().intValue() == 0 && "1".equals(studyVideo.getStatus())) {
                    QueryWrapper<AttachFile> q = new QueryWrapper<>();
                    q.eq("tz_attach_file.foreign_key", studyVideo.getId());
                    q.eq("tz_attach_file.table_type", "tz_study_video");
                    q.eq("tz_attach_file.type", 1);
                    studyVideo.setAttachFileList(attachFileMapper.selectList(q));

                    // 获取栏目类型，以作跳转
                    if (column.getDelFlag().intValue() == 0) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("studyVideo", studyVideo);
                        map.put("column", column);

                        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
                    } else {
                        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(null));
                    }

                } else {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(null));
                }
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> getVideoColumnListByPage(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            TzStudyVideo tzStudyVideo = JSON.parseObject(JSON.toJSONString(map.get("tzStudyVideo")), TzStudyVideo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);

            // 查询查看权限
            String userId = (String) map.get("userId");
            QueryWrapper q1 = new QueryWrapper();
            q1.eq("id", userId);
            q1.eq("del_flag", 0);
            JcxfPartyMember partyMember = jcxfPartyMemberMapper.selectOne(q1);
            if (partyMember == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到党员信息");
            }

            Integer columnId = null;
            if (tzStudyVideo != null) {
                columnId = tzStudyVideo.getColumnId();
            }
            List<Integer> studyIds = getStudyIdListByPartyMember(partyMember, columnId);

            if (studyIds.size() == 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new Page<>()));
            }

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
            Page<TzStudyVideo> pageData = new Page<>(page, limit);

            QueryWrapper<TzStudyVideo> queryWrapper = new QueryWrapper();

            if (tzStudyVideo != null) {
                if (tzStudyVideo.getColumnId() != null) {
                    queryWrapper.eq("column_id", tzStudyVideo.getColumnId());
                }
            }

            String columnType = (String) map.get("columnType");
            if (StringUtils.isNotBlank(columnType)) {
                /*QueryWrapper q2 = new QueryWrapper();
                q2.select("id");
                q2.eq("del_flag", 0);
                q2.eq("type", columnType);
                q2.eq("show_status", "1");
                List<TzVideoColumn> columnList = tzVideoColumnMapper.selectList(q2);
                if (columnList == null || columnList.size() == 0) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new Page()));
                }
                List<Integer> ids = new ArrayList<>();
                for (TzVideoColumn column: columnList) {
                    ids.add(column.getId());
                }
                queryWrapper.in("column_id", ids);*/

                queryWrapper.inSql("column_id", "select id from tz_video_column where del_flag = 0 and show_status = '1' and type = "+ columnType +"");
            } else {
                queryWrapper.inSql("column_id", "select id from tz_video_column where del_flag = 0 and show_status = '1'");
            }

            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("status", "1"); //审核状态(0未发布 1 已发布)
            queryWrapper.in("id", studyIds);
            queryWrapper.and(q -> q.and(i -> i.eq("view_type", "1").le("start_time", new Date()).ge("end_time", new Date()))
                    .or().eq("view_type", "2"));
            queryWrapper.orderByDesc("post_time");

            IPage<TzStudyVideo> result = tzStudyVideoMapper.selectPage(pageData, queryWrapper);
            for (TzStudyVideo video : result.getRecords()) {
                QueryWrapper<AttachFile> q = new QueryWrapper<>();
                q.eq("tz_attach_file.foreign_key", video.getId());
                q.eq("tz_attach_file.table_type", "tz_study_video");
                q.eq("tz_attach_file.type", 1);
                video.setAttachFileList(attachFileMapper.selectList(q));

                //查询当前用户的是否学习过
                QueryWrapper q2 = new QueryWrapper();
                q2.eq("user_id", userId);
                q2.eq("study_id", video.getId());
                int count = tzStudyUserMapper.selectCount(q2);
                if (count > 0) {
                    video.setIsStudy(true);
                } else {
                    video.setIsStudy(false);
                }

                TzVideoColumn column = tzVideoColumnMapper.selectById(video.getColumnId());
                if (column != null) {
                    video.setTzVideoColumn(column);
                }

                QueryWrapper q3 = new QueryWrapper();
                q3.eq("study_id", video.getId());
                video.setStudyNum(tzStudyUserMapper.selectCount(q3));
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> getPastVideoColumnList(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String limit = (String) requestMap.get("limit");
            TzStudyVideo video = tzStudyVideoMapper.selectById(id);

            String userId = (String) requestMap.get("userId");
            QueryWrapper q1 = new QueryWrapper();
            q1.eq("id", userId);
            q1.eq("del_flag", 0);
            JcxfPartyMember partyMember = jcxfPartyMemberMapper.selectOne(q1);
            if (partyMember == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到党员信息");
            }

            List<Integer> studyIds = getStudyIdListByPartyMember(partyMember, video.getColumnId());

            if (studyIds == null || studyIds.size() == 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new ArrayList()));
            }

            QueryWrapper<TzStudyVideo> queryWrapper = new QueryWrapper();
            queryWrapper.eq("column_id", video.getColumnId());
            queryWrapper.eq("status", 1);
            queryWrapper.lt("post_time", video.getPostTime());
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("id", studyIds);
            queryWrapper.and(q -> q.and(i -> i.eq("view_type", "1").le("start_time", new Date()).ge("end_time", new Date()))
                    .or().eq("view_type", "2"));
            queryWrapper.orderByDesc("post_time");
            queryWrapper.last("limit " + limit);

            List<TzStudyVideo> videoList = tzStudyVideoMapper.selectList(queryWrapper);
            for (TzStudyVideo studyVideo : videoList) {
                QueryWrapper<AttachFile> q = new QueryWrapper<>();
                q.eq("tz_attach_file.foreign_key", video.getId());
                q.eq("tz_attach_file.table_type", "tz_study_video");
                q.eq("tz_attach_file.type", 1);
                studyVideo.setAttachFileList(attachFileMapper.selectList(q));
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(videoList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> sendStudyMessage(Map<String, Object> requestMap) {
        try {
            String tenantId = (String) requestMap.get("bizTenantId");

            String studyId = (String) requestMap.get("studyId");
            TzStudyVideo tzStudyVideo = tzStudyVideoMapper.selectById(studyId);


            // 先查询权限和开关情况
            QueryWrapper<TzMessageAuto> autoQueryWrapper = new QueryWrapper<>();
            // 查看类型 1、时间内可看 2、永久查看
            if ("1".equals(tzStudyVideo.getViewType())) {
                autoQueryWrapper.eq("tz_message_auto.type", "4");// 党建学习短信模板
            } else if ("2".equals(tzStudyVideo.getViewType())) {
                autoQueryWrapper.eq("tz_message_auto.type", "5");// 党建学习短信模板
            }
            TzMessageAuto tzMessageAuto = tzMessageAutoMapper.selectOne(autoQueryWrapper);
            if (tzMessageAuto.getIsOpen() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"该业务已停用");
            }

            // 获取本部门信息
//            JcxfSysDept dept = jcxfSysDeptMapper.selectById(tzStudyVideo.getDeptId());
//
//            List<String> roleDeptIds = new ArrayList<>(Arrays.asList(dept.getParentIds().split(",")));
//            roleDeptIds.add(String.valueOf(dept.getId()));
//            // 查询有此权限的部门
//            QueryWrapper qRole = new QueryWrapper();
//            qRole.in("dept_id", roleDeptIds);
//            qRole.eq("auto_id", tzMessageAuto.getId());
//            qRole.eq("status", "1"); // 用户任务开启
//            int count = tzMessageAutoRoleMapper.selectCount(qRole);
//            if (count == 0) {
//                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂无该业务权限或业务已关闭");
//            }


            QueryWrapper q = new QueryWrapper();
            q.eq("study_id", studyId);
            TzStudyView tzStudyView = tzStudyViewMapper.selectOne(q);

            if (StringUtils.isNotBlank(tzStudyView.getDeptIds())) {
                tzStudyView.setDeptIds(tzStudyView.getDeptIds().substring(1, tzStudyView.getDeptIds().length() - 1));
            }
            if (StringUtils.isNotBlank(tzStudyView.getPartyMemberIds())) {
                tzStudyView.setPartyMemberIds(tzStudyView.getPartyMemberIds().substring(1, tzStudyView.getPartyMemberIds().length() - 1));
            }
            if (StringUtils.isNotBlank(tzStudyView.getPositionIds())) {
                tzStudyView.setPositionIds(tzStudyView.getPositionIds().substring(1, tzStudyView.getPositionIds().length() - 1));
            }

            Set<JcxfPartyMember> partyMemberList = new HashSet<>();
            String[] viewDeptIds = tzStudyView.getDeptIds() != null? tzStudyView.getDeptIds().split(","): new String[0];
            if ("1".equals(tzStudyView.getType()) || "2".equals(tzStudyView.getType())) {
                for (String deptId : viewDeptIds) {
                    if ("1".equals(tzStudyView.getType())) {
                        List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
                        deptIds.add(Long.valueOf(deptId));

                        QueryWrapper qParty = new QueryWrapper();
                        qParty.in("dept_id", deptIds);
                        qParty.eq("del_flag", 0);
                        partyMemberList.addAll(jcxfPartyMemberMapper.selectList(qParty));
                    } else if ("2".equals(tzStudyView.getType())) {
                        QueryWrapper qParty = new QueryWrapper();
                        qParty.eq("dept_id", deptId);
                        qParty.eq("del_flag", 0);
                        partyMemberList.addAll(jcxfPartyMemberMapper.selectList(qParty));
                    }
                }

            } else if ("3".equals(tzStudyView.getType())) {
                //  当前职务可看-当前及其下级部门
                String[] positionIds = tzStudyView.getPositionIds()!=null? tzStudyView.getPositionIds().split(","):new String[0];
                if (viewDeptIds.length > 0 && positionIds.length > 0) {
                    Set<Long> deptIds = new HashSet<>();
                    for (String deptId : viewDeptIds) {
                        deptIds.addAll(jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId)));
                        deptIds.add(Long.valueOf(deptId));
                    }
                    QueryWrapper qParty = new QueryWrapper();
                    qParty.in("dept_id", deptIds);
                    qParty.in("position", positionIds);
                    qParty.eq("del_flag", 0);
                    partyMemberList.addAll(jcxfPartyMemberMapper.selectList(qParty));
                }

            } else if ("4".equals(tzStudyView.getType())) {
                //	当前职务可看-仅当前部门
                String[] positionIds = tzStudyView.getPositionIds() !=null?tzStudyView.getPositionIds().split(","): new String[0];
                if (viewDeptIds.length > 0 && positionIds.length > 0) {
                    QueryWrapper qParty = new QueryWrapper();
                    qParty.in("dept_id", viewDeptIds);
                    qParty.in("position", positionIds);
                    qParty.eq("del_flag", 0);
                    partyMemberList.addAll(jcxfPartyMemberMapper.selectList(qParty));
                }

            } else if ("5".equals(tzStudyView.getType())) {
                //	选中党员可看
                String[] partyMemberIds = tzStudyView.getPartyMemberIds() !=null? tzStudyView.getPartyMemberIds().split(","): new String[0];
                if (partyMemberIds.length > 0) {
                    QueryWrapper qParty = new QueryWrapper();
                    qParty.in("id", partyMemberIds);
                    qParty.eq("del_flag", 0);
                    partyMemberList.addAll(jcxfPartyMemberMapper.selectList(qParty));
                }
            }

            if (partyMemberList == null || partyMemberList.size() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到党员信息");
            }

            List<String> names = new ArrayList<>();
            List<String> phones = new ArrayList<>();
            String sendObject = "";
            String sign = tzMessageAuto.getSign();
            for (JcxfPartyMember partyMember : partyMemberList) {
                names.add(partyMember.getRealName());
                phones.add(partyMember.getPhone());
                sendObject = sendObject + partyMember.getPhone() + ",";
            }
            if (names.size() == 0 || phones.size() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"名字或电话为空");
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // 注意月份是从0开始计数的
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            TzMessage tzMessage = new TzMessage();
            tzMessage.setMissionName("学习课程通知" + year + month + day +"：" + tzStudyVideo.getId());
            tzMessage.setSendTimeType(1);
            tzMessage.setSendMod(1);
            tzMessage.setSendType(1);

            //view_type 查看类型 1、时间内可看 2、永久查看
            String content = tzMessageAuto.getTemContent();
            if ("1".equals(tzStudyVideo.getViewType())) {
                String startTime = dateFormat.format(tzStudyVideo.getStartTime());
                String endTime = dateFormat.format(tzStudyVideo.getEndTime());
                String title = tzStudyVideo.getTitle();
                content = content.replace("{startTime}", startTime).replace("endTime", endTime).replace("{title}", title);
                tzMessage.setContent(sign + content);
            } else {
                content = content.replace("{title}", tzStudyVideo.getTitle());
                tzMessage.setContent(sign + content);
            }

            tzMessage.setSign(sign);

            if (sendObject.length() > 0) {
                sendObject = sendObject.substring(0, sendObject.length() - 1);
                tzMessage.setSendObject(sendObject);
            }

            tzMessage.setSendTime(new Date());
            tzMessage.setCreateTime(new Date());
            tzMessage.setTenantId(tenantId);
            tzMessage.setStatus("1"); // （1、发送结束，2、发送失败，3、待返回,4、已取消）
            int count2 = tzMessageMapper.insert(tzMessage);
            if (count2 == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"发送短信错误");
            }

            // 查看类型 1、时间内可看 2、永久查看
            String startTime = "";
            String endTime = "";
            if ("1".equals(tzStudyVideo.getViewType())) {
                startTime = dateFormat.format(tzStudyVideo.getStartTime());
                endTime = dateFormat.format(tzStudyVideo.getEndTime());
            }

            int success = 0;
            int error = 0;

            String title = tzStudyVideo.getTitle();

            List<TzMessageDetail> detailList = new ArrayList<>();

            for (JcxfPartyMember partyMember : partyMemberList) {
                String temContent = tzMessageAuto.getTemContent();

                // 如果该部门有自己的设置
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("dept_id", partyMember.getDeptId());
                queryWrapper.eq("auto_id", tzMessageAuto.getId());
                TzMessageAutoContent tzMessageAutoContent = tzMessageAutoContentMapper.selectOne(queryWrapper);
                if (tzMessageAutoContent != null) {
                    temContent = tzMessageAutoContent.getTemContent();
                }

                //查看类型 1、时间内可看 2、永久查看
                if ("1".equals(tzStudyVideo.getViewType())) {

                    temContent = temContent.replace("{name}", partyMember.getRealName())
                            .replace("{startTime}", startTime)
                            .replace("{endTime}", endTime)
                            .replace("{title}", title);
                } else {
                    temContent = temContent.replace("{name}", partyMember.getRealName())
                            .replace("{title}", title);
                }

                ShortMessageResult shortMessageResult = null;
                if (tzMessage.getSendTimeType() == 1) {
                    shortMessageResult = ShortMessageUtil.sendMessageBySign(partyMember.getPhone(), temContent, tzMessage.getSign());
                } else {
                    shortMessageResult = ShortMessageUtil.sendMessageBySignByTime(partyMember.getPhone(), temContent, tzMessage.getSign(), tzMessage.getSendTime());
                }

                String status = "0";
                if (shortMessageResult != null && "200".equals(shortMessageResult.getCode())) {
                    success++;
                    status = "1";
                } else {
                    error++;
                    status = "2";
                }
                TzMessageDetail tzMessageDetail = new TzMessageDetail();
                //自定义发送不要模板id
                //tzMessageDetail.setTemplateId(tzMessage.getId().toString());
                tzMessageDetail.setMissionName(tzMessage.getMissionName());
                if (shortMessageResult != null) {
                    tzMessageDetail.setMsgId(shortMessageResult.getMsgId());
                    tzMessageDetail.setFeeCount(shortMessageResult.getContNum());
                }
                tzMessageDetail.setPhone(partyMember.getPhone());
                tzMessageDetail.setSendStatus(status);
                tzMessageDetail.setSendContent(tzMessage.getSign() + temContent);
                tzMessageDetail.setPostTime(tzMessage.getSendTime());
                tzMessageDetail.setTenantId(tenantId);
                tzMessageDetail.setSendType("5");
                tzMessageDetail.setDeptId(partyMember.getDeptId());
                tzMessageDetail.setTemplateId(String.valueOf(tzMessageAuto.getId()));

                if (shortMessageResult != null) {
                    tzMessageDetail.setResultCode(shortMessageResult.getCode());
                    tzMessageDetail.setResultMsg(shortMessageResult.getMsg());
                }

                detailList.add(tzMessageDetail);
            }
            // 记录短信发送时间
            UpdateWrapper up = new UpdateWrapper();
            up.eq("id", tzStudyView.getId());
            up.set("message_time", new Date());
            tzStudyViewMapper.update(null, up);

            tzMessage.setSuccessCount(success);
            tzMessage.setErrorCount(error);

            tzMessageMapper.updateById(tzMessage);
            if (count2 > 0) {
                //保存短信详情
                for (TzMessageDetail detail: detailList) {
                    detail.setMessageId(tzMessage.getId());
                    tzMessageDetailMapper.insert(detail);
                }
            }

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("study_id", studyId);
            updateWrapper.set("message_time", new Date());
            tzStudyViewMapper.update(null, updateWrapper);

            Map<String, Integer> map = new HashMap<>();
            map.put("successCount", success);
            map.put("errorCount", error);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("发送成功"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"发送异常");
        }
    }

    public List<Integer> getStudyIdListByPartyMember(JcxfPartyMember partyMember, Integer columnId) {
        JcxfSysDept dept = jcxfSysDeptMapper.selectById(partyMember.getDeptId());
        String[] parentIds = dept.getParentIds().split(",");

        ArrayList<String> parentIdList = new ArrayList<>(parentIds.length);
        Collections.addAll(parentIdList, parentIds);
        parentIdList.add(partyMember.getDeptId().toString());
        List<Integer> studyIds = tzStudyViewMapper.getStudyIdsByPartMember(
                parentIdList, String.valueOf(partyMember.getDeptId()), String.valueOf(partyMember.getPosition()), String.valueOf(partyMember.getId()),
                columnId);
        return studyIds;
    }

    public IPage<TzStudyVideo> queryTzStudyVideoListByPage(TzStudyVideo tzStudyVideo, SearchVo searchVo, PageVo pageVo, List<Integer> colunmIds) {
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
        Page<TzStudyVideo> pageData = new Page<>(page, limit);
        QueryWrapper<TzStudyVideo> queryWrapper = new QueryWrapper<>();
        if (tzStudyVideo != null) {
            queryWrapper = LikeAllField(tzStudyVideo, searchVo);
        }
        if (colunmIds != null && colunmIds.size() > 0) {
            queryWrapper.in("tz_study_video.column_id", colunmIds);
        }
        queryWrapper.orderByDesc("tz_study_video.create_time");
        IPage<TzStudyVideo> result = tzStudyVideoMapper.getStudyVideoByPage(pageData, queryWrapper);
        return result;
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tzStudyVideo 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TzStudyVideo> LikeAllField(TzStudyVideo tzStudyVideo, SearchVo searchVo) {
        QueryWrapper<TzStudyVideo> queryWrapper = new QueryWrapper<>();
        if (tzStudyVideo.getId() != null) {
            queryWrapper.and(i -> i.eq("tz_study_video.id", tzStudyVideo.getId()));
        }
        //if(tzStudyVideo.getColumnId() != null){
        //	queryWrapper.and(i -> i.eq("tz_study_video.column_id", tzStudyVideo.getColumnId()));
        //}
        if (StringUtils.isNotBlank(tzStudyVideo.getColumnName())) {
            queryWrapper.and(i -> i.like("tz_study_video.column_name", tzStudyVideo.getColumnName()));
        }
        if (StringUtils.isNotBlank(tzStudyVideo.getTitle())) {
            queryWrapper.and(i -> i.like("tz_study_video.title", tzStudyVideo.getTitle()));
        }
        if (StringUtils.isNotBlank(tzStudyVideo.getStatus())) {
            queryWrapper.and(i -> i.eq("tz_study_video.status", tzStudyVideo.getStatus()));
        }
        if (tzStudyVideo.getPlayCount() != null) {
            queryWrapper.and(i -> i.like("tz_study_video.play_count", tzStudyVideo.getPlayCount()));
        }
        if (tzStudyVideo.getLikeCount() != null) {
            queryWrapper.and(i -> i.like("tz_study_video.like_count", tzStudyVideo.getLikeCount()));
        }
        if (tzStudyVideo.getCollectCount() != null) {
            queryWrapper.and(i -> i.like("tz_study_video.collect_count", tzStudyVideo.getCollectCount()));
        }
        if (StringUtils.isNotBlank(tzStudyVideo.getCreateBy())) {
            queryWrapper.and(i -> i.like("tz_study_video.create_by", tzStudyVideo.getCreateBy()));
        }
        if (tzStudyVideo.getCreateTime() != null) {
            queryWrapper.and(i -> i.like("tz_study_video.create_time", tzStudyVideo.getCreateTime()));
        }
        if (tzStudyVideo.getUpdateTime() != null) {
            queryWrapper.and(i -> i.like("tz_study_video.update_time", tzStudyVideo.getUpdateTime()));
        }
        if (tzStudyVideo.getUpdateBy() != null) {
            queryWrapper.and(i -> i.like("tz_study_video.update_by", tzStudyVideo.getUpdateBy()));
        }
        if (tzStudyVideo.getHours() != null) {
            queryWrapper.and(i -> i.like("tz_study_video.hours", tzStudyVideo.getHours()));
        }
        if (tzStudyVideo.getScores() != null) {
            queryWrapper.and(i -> i.like("tz_study_video.scores", tzStudyVideo.getScores()));
        }
        if (StringUtils.isNotBlank(tzStudyVideo.getSummary())) {
            queryWrapper.and(i -> i.like("tz_study_video.summary", tzStudyVideo.getSummary()));
        }
        if (StringUtils.isNotBlank(tzStudyVideo.getKeyword())) {
            queryWrapper.and(i -> i.like("tz_study_video.keyword", tzStudyVideo.getKeyword()));
        }
        if (StringUtils.isNotBlank(tzStudyVideo.getSource())) {
            queryWrapper.and(i -> i.like("tz_study_video.source", tzStudyVideo.getSource()));
        }
        if (StringUtils.isNotBlank(tzStudyVideo.getIntroduce())) {
            queryWrapper.and(i -> i.like("tz_study_video.introduce", tzStudyVideo.getIntroduce()));
        }
        if (tzStudyVideo.getDelFlag() != null) {
            queryWrapper.and(i -> i.eq("tz_study_video.del_flag", tzStudyVideo.getDelFlag()));
        }
        if (tzStudyVideo.getShowHome() != null) {
            queryWrapper.and(i -> i.like("tz_study_video.show_home", tzStudyVideo.getShowHome()));
        }
        if (tzStudyVideo.getTop() != null) {
            queryWrapper.and(i -> i.like("tz_study_video.top", tzStudyVideo.getTop()));
        }
        if (StringUtils.isNotBlank(tzStudyVideo.getViewType())) {
            queryWrapper.and(i -> i.eq("tz_study_video.view_type", tzStudyVideo.getViewType()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.and(i -> i.between("tz_study_video.create_time", searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        queryWrapper.and(i -> i.eq("tz_study_video.del_flag", 0));

        return queryWrapper;

    }
}
