package com.wisdom.acm.processing.schedule;

import com.wisdom.acm.processing.form.FineDailyReportAddForm;
import com.wisdom.acm.processing.service.report.FineDailyReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zll
 * 2020/12/28/028 9:11
 * Description:<定时器>
 */
@Component
public class FineReportSchedule {
    private Logger log = LoggerFactory.getLogger(FineReportSchedule.class);
    @Autowired
    private FineDailyReportService fineDailyReportService;
    //每天早上七点（0 0 7 * * ?）
    @Scheduled(cron = "0 0 7 * * ?")
    public void execute1() throws InterruptedException {
        log.info("ReportSchedule thread is start,currenttime is " + new Date());
        Calendar cal  =   Calendar.getInstance();
        cal.add(Calendar.DATE,  -1);
        String yesterday =new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
        FineDailyReportAddForm form =new FineDailyReportAddForm();
        form.setDescription("定时器生成日报");
        form.setReportDay(yesterday);
        form.setReviewStatus("INIT");
        form.setReportType("0");
        form.setLine("1");
        fineDailyReportService.addFineDailyReport1(form);
        log.info("ReportSchedule thread is end,currenttime is " + new Date());
    }

    @Scheduled(cron = "0 0 7 * * ?")
    public void execute3() throws InterruptedException {
        log.info("ReportSchedule thread is start,currenttime is " + new Date());
        Calendar cal  =   Calendar.getInstance();
        cal.add(Calendar.DATE,  -1);
        String yesterday =new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());
        FineDailyReportAddForm form =new FineDailyReportAddForm();
        form.setDescription("定时器生成日报");
        form.setReportDay(yesterday);
        form.setReviewStatus("INIT");
        form.setReportType("0");
        form.setLine("3");
        fineDailyReportService.addFineDailyReport3(form);
        log.info("ReportSchedule thread is end,currenttime is " + new Date());
    }
}
