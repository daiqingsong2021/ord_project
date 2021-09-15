package com.wisdom.acm.dc4.schedule;

import com.wisdom.acm.dc4.service.ConstructionDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zll
 * 2021/2/22/022 11:26
 * Description:<从restcloud中收集数据，每天早上5点钟开始执行>
 */
//@Component
public class RcRecieveSchedule {
    @Autowired
    private ConstructionDailyService constructionDailyService;
    //@Scheduled(cron = "0 0 5 * * ?")
    public void exe() throws InterruptedException {
        constructionDailyService.addRcConstructionDaily();
    }
}
