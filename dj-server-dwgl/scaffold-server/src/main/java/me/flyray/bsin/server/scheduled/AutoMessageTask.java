package me.flyray.bsin.server.scheduled;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import me.flyray.bsin.facade.service.TzMessageAutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AutoMessageTask {

    @Autowired
    private TzMessageAutoService tzMessageAutoService;

    /**
     * 发给政治生日短信
     */
    @XxlJob("sendPolicyBirthdayMsg")
    public void sendPolicyBirthdayMsg(){
        log.info("发送政治生日短信>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        try {
            tzMessageAutoService.queryPolicyBirthdayToday();
        }catch (Exception e) {
            log.info("发送政治生日短信异常>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            e.printStackTrace();
        }
    }


    @XxlJob("sendBirthdayMsg")
    public void sendBirthdayMsg(){
        log.info("发送党员生日短信>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        try {
            tzMessageAutoService.queryBirthdayToday();
        }catch (Exception e) {
            log.info("发送党员生日短信异常>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            e.printStackTrace();
        }
    }

}
