package me.flyray.bsin.server.impl.jcxf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.JcxfAndDjService;
import me.flyray.bsin.server.domain.jcxf.JcxfOrganizationLife;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMeeting;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMeetingMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.PasswordManager;
import me.flyray.bsin.server.utils.jcxf.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class JcxfAndDjServiceImpl implements JcxfAndDjService {

//    @Autowired
//    private TzSysDeptMapper tzSysDeptMapper;
//
//    @Autowired
//    private TzOrganizationLifeMapper tzOrganizationLifeMapper;

    @Autowired
    private JcxfPartyMeetingMapper jcxfPartyMeetingMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private PasswordManager passwordManager;

    //private CountDownLatch latch = new CountDownLatch(1);

    AtomicInteger count = new AtomicInteger(0);

    @Override
    public Map<String, Object> syncLife(Map<String, Object> requestMap) {
//        try {
//            QueryWrapper q = new QueryWrapper();
//            q.eq("id", "8009");
//            q.or();
//            q.like("parent_ids", "%,8009,%");
//            List<JcxfSysDept> deptList = jcxfSysDeptMapper.selectList(q);
//
//            List<Integer> ids = new ArrayList<>();
//            for (JcxfSysDept dept: deptList) {
//                ids.add(dept.getId());
//            }
//
//            int MAX_THREADS = 30;//最大线程数
//            ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
//
//
//            int page = 0;
//            int size = 1000;
//            List<JcxfPartyMeeting> results = new ArrayList<>();
//            while (true) {
//                QueryWrapper queryWrapper = new QueryWrapper();
//                queryWrapper.eq("del_flag", 0);
//                queryWrapper.in("dept_id", ids);
//                queryWrapper.last("limit " + page * size + "," + size);
//
//                List<JcxfPartyMeeting> result = jcxfPartyMeetingMapper.selectList(queryWrapper);
//                if (result.size() >0) {
//                    page++;
//                    results.addAll(result);
//                } else {
//                    break;
//                }
//            }
//
//            latch = new CountDownLatch(results.size());
//
//            for (JcxfPartyMeeting result: results) {
//                Runnable p = new Salesman(result);
//                Thread t = new Thread(p);
//                executorService.submit(t);
//            }
//
//            //写入部门数据
//            executorService.shutdown();//for循环结束后停止ExecutorService
//            latch.await();
//
//            System.out.println("导入完成，数量"+count.get());
//            System.out.println(page);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    public Map<String, Object> resetAllMemberPassword(Map<String, Object> requestMap) {
        try {
            Integer pageNumber = 1;
            Integer pageSize = 300;

            int MAX_THREADS = 30;//最大线程数
            ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

            while (true) {
                Page<JcxfPartyMember> pageData = new Page<>(pageNumber, pageSize);
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("del_flag", false);
                IPage<JcxfPartyMember> list = jcxfPartyMemberMapper.selectPage(pageData, queryWrapper);

                if (list.getRecords().size() == 0) {
                    break;
                }
                pageNumber++;
                Runnable p = new setPassword(list.getRecords());
                Thread t = new Thread(p);
                executorService.submit(t);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    class setPassword implements Runnable {

        private List<JcxfPartyMember> partyMemberList;
        setPassword (List<JcxfPartyMember> list) {
            this.partyMemberList = list;
        }


        @Override
        public void run() {
            try {
                for (JcxfPartyMember partyMember : partyMemberList) {
                    //默认密码
                    String idcard = partyMember.getIdcard();
                    String substring = null;
                    //初始密码默认为身份证号后6位
                    if (idcard != null && idcard != "") {
                        substring = idcard.substring(12);
                    }
                    //密码加密
                    long time = new Date().getTime();
                    String result = passwordManager.encryptPassword(time + substring);
                    // partyMember.setPassword(result);

                    UpdateWrapper updateWrapper = new UpdateWrapper();
                    updateWrapper.eq("id", partyMember.getId());
                    updateWrapper.set("password", result);
                    jcxfPartyMemberMapper.update(null, updateWrapper);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Salesman implements Runnable{

        private JcxfPartyMeeting meeting;
        Salesman(JcxfPartyMeeting result) {
            this.meeting = result;
        }
        @Override
        public void run() {
//            try {
//                TzOrganizationLife life = new TzOrganizationLife();
//                TzSysDept dept = tzSysDeptMapper.selectById(meeting.getDeptId());
//                if (dept != null) {
//                    life.setDeptId(meeting.getDeptId());
//                    life.setDeptName(dept.getName());
//                    life.setMeetingName(meeting.getMeetingTopic());
//                    life.setMeetingType(String.valueOf(meeting.getMeetingType()));
//                    life.setStartTime(meeting.getStartTime());
//                    life.setEndTime(meeting.getEndTime());
//                    life.setMeetingStatus(meeting.getMeetingStatus());
//                    life.setCreateTime(meeting.getCreateDate());
//                    life.setMeetingContent(meeting.getMeetingContent());
//                    life.setAddr(meeting.getMeetingAddress());
//                    life.setDelFlag(0);
//
//                    tzOrganizationLifeMapper.insert(life);
//                    count.getAndDecrement();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                latch.countDown();
//            }
        }

    }

}
