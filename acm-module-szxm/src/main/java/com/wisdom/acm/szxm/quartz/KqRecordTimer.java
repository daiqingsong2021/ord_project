package com.wisdom.acm.szxm.quartz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.DateUtil;
import com.wisdom.acm.szxm.common.SzxmEnumsUtil;
import com.wisdom.acm.szxm.mapper.rygl.KqRecordMapper;
import com.wisdom.acm.szxm.po.rygl.AllKqRecordPo;
import com.wisdom.acm.szxm.po.rygl.GlryKqRecordPo;
import com.wisdom.acm.szxm.po.rygl.HolidayPo;
import com.wisdom.acm.szxm.po.rygl.KqConfigPo;
import com.wisdom.acm.szxm.po.rygl.LwryKqRecordPo;
import com.wisdom.acm.szxm.service.app.EwxService;
import com.wisdom.acm.szxm.service.rygl.HolidayService;
import com.wisdom.acm.szxm.service.rygl.KqConfigService;
import com.wisdom.acm.szxm.service.rygl.KqRecordService;
import com.wisdom.acm.szxm.service.rygl.PeopleService;
import com.wisdom.acm.szxm.service.rygl.ZkService;
import com.wisdom.acm.szxm.vo.rygl.KqRecordVo;
import com.wisdom.acm.szxm.vo.rygl.PeopleVo;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.calc.calendar.CalendarUtil;
import com.wisdom.base.common.util.calc.calendar.PcCalendar;
import com.wisdom.base.common.util.calc.calendar.PmCalendar;
import com.wisdom.base.common.vo.CalendarVo;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时考勤统计定时器,统计昨天数据
 */
public class KqRecordTimer extends QuartzJobBean {
    /**
     * logback
     */
    private static final Logger logger = LoggerFactory.getLogger(KqRecordTimer.class);

    @Autowired
    private ZkService zkService;

    @Autowired
    private KqConfigService kqConfigService;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private KqRecordMapper kqRecordMapper;
    @Autowired
    private KqRecordService kqRecordService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private LeafService leafService;

    @Autowired
    private EwxService ewxService;

    /**
     * 定时任务逻辑实现方法
     * 每当触发器触发时会执行该方法逻辑
     *
     * @param jobExecutionContext 任务执行上下文
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("考勤记录任务开始执行，开始时间：{}", new Date());
        //根据标段ID 缓存标段考勤配置Map
        List<KqConfigPo> kqConfigPoList = kqConfigService.selectListAll();
        Map<Integer, KqConfigPo> kqConfigPoMap = ListUtil.listToMap(kqConfigPoList, "sectionId", Integer.class);

        //获取中控昨天人员的考勤数据
        Map<String, List<KqRecordVo>> zkRecordVoMap = this.getYesterdayKqRecord();

        PmCalendar defauPmCalendar = this.getDefaultPmCalendar();//默认日历
        Map<Integer, PmCalendar> pmCalendarMap = this.getPmCalendarMap();//获取日历缓存

        //获取请假数据缓存
        Example example = new Example(HolidayPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", SzxmEnumsUtil.StatusEnum.APPROVED.toString());
        List<HolidayPo> holidayPoList = holidayService.selectByExample(example);

        //查询所有进场人员人员表数据
        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("status", "1");
        List<PeopleVo> peoplePoList = peopleService.selectAllKqPeople(mapWhere);

        //获取企业微信昨天的考勤数据
        Map<String, List<KqRecordVo>> ewxRecordVoMap = this.getEwxYesterdayKqRecord(peoplePoList);

        List<GlryKqRecordPo> glryKqRecordPos = Lists.newArrayList();
        List<LwryKqRecordPo> lwryKqRecordPos = Lists.newArrayList();
        for (PeopleVo peopleVo : peoplePoList) {
            //考勤日历获取
            KqConfigPo kqConfigPo = kqConfigPoMap.get(peopleVo.getSectionId());
            if (ObjectUtils.isEmpty(kqConfigPo)) {//如果相关标段的考勤配置为空，则取全局考勤，全局考勤若取不到，则初始化默认考勤
                kqConfigPo = kqConfigPoMap.get(null);
                if (ObjectUtils.isEmpty(kqConfigPo)) {
                    kqConfigPo = new KqConfigPo();
                    kqConfigPo.setCalenderId(Integer.valueOf(defauPmCalendar.getId()));//获取默认日历ID
                    kqConfigPo.setType("0");
                    kqConfigPo.setMangerkq("1");
                    kqConfigPo.setWorkerkq("1");
                }

            }
            PmCalendar pmCalendar = pmCalendarMap.get(kqConfigPo.getCalenderId());

            //获取到昨天的中控考勤记录
            List<KqRecordVo> zkRecordVoList = zkRecordVoMap.get(String.valueOf(peopleVo.getId()));

            //获取到昨天的企业微信考勤记录
            List<KqRecordVo> ewkRecordVoList = ewxRecordVoMap.get(String.valueOf(peopleVo.getId()));

            //判断昨天是否工作日
            boolean isWorkDay = CalendarUtil.isWorkDate(DateUtil.getDayBefore(new Date(), 1), pmCalendar);

            //根据日历ID，判断昨天是否工作日
            if (isWorkDay && "1".equals(kqConfigPo.getMangerkq()) && "0".equals(peopleVo.getTypeVo().getCode())) {//如果是工作日 且 管理人员开启考勤
                //判断昨天是否请假
                boolean isInHoliday = this.isInHoliday(peopleVo, holidayPoList);
                List<KqRecordVo> allRecords = Lists.newArrayList();
                if (!ObjectUtils.isEmpty(zkRecordVoList)) {
                    allRecords.addAll(zkRecordVoList);
                }
                if (!ObjectUtils.isEmpty(ewkRecordVoList)) {
                    allRecords.addAll(ewkRecordVoList);
                }
                glryKqRecordPos.add(insertGlryRecord(peopleVo, pmCalendar, isInHoliday, allRecords));
            }
            if (isWorkDay && "1".equals(kqConfigPo.getWorkerkq()) && !"0".equals(peopleVo.getTypeVo().getCode())) {//如果是工作日 且 劳务人员开启考勤

                lwryKqRecordPos.add(insertLwryRecord(peopleVo, zkRecordVoList));
            }
            //记录所有人员考勤记录，不管是否工作日 和 是否开启考勤
            //insertAllKqRecord(peopleVo,jobName,zkRecordVoList);
        }
        kqRecordService.saveRykqRecord(glryKqRecordPos, lwryKqRecordPos);
        logger.info("考勤记录任务结束，结束时间：{}", new Date());
    }

    /**
     * 获取企业微信昨天的考勤数据(只统计管理人员的)
     *
     * @param peopleVoList
     * @return 人员iD ,考勤记录List 对应Map
     */
    private Map<String, List<KqRecordVo>> getEwxYesterdayKqRecord(List<PeopleVo> peopleVoList) {
        //初始化查询批次Map 次数-列表 0开始 0-99 100-199 200-299 300-399
        Map<String, List<String>> pcMap = Maps.newHashMap();
        Map<String, PeopleVo> codePeopleMap = Maps.newHashMap();
        for (int i = 0; i < peopleVoList.size(); i++) {
            PeopleVo peopleVo = peopleVoList.get(i);
            if ((i + 1) % 100 == 0 || i == 0) {//如果i+1等于100的倍数 或者 i=0
                List<String> userCodeList = Lists.newArrayList();
                userCodeList.add(peopleVo.getSysUserCode());
                pcMap.put(String.valueOf((i + 1) / 100), userCodeList);
            } else {
                List<String> userCodeList = pcMap.get(String.valueOf(i / 100));
                userCodeList.add(peopleVo.getSysUserCode());
            }
            codePeopleMap.put(peopleVo.getSysUserCode(), peopleVo);
        }
        //分批次调用接口
        String today = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");//今天凌晨0点
        String yesterDay = DateUtil.getDateFormat(DateUtil.getDayBefore(new Date(), 1), "yyyy-MM-dd");//昨天凌晨0点
        List<KqRecordVo> allEwxServiceRecords = Lists.newArrayList();
        for (Map.Entry<String, List<String>> entry : pcMap.entrySet()) {
            Map<String, Object> reqMap = Maps.newHashMap();
            reqMap.put("opencheckindatatype", "3");//全部打卡
            reqMap.put("starttime", DateUtil.getDateFormat(yesterDay).toInstant().atOffset(ZoneOffset.of("+8")).toEpochSecond());
            reqMap.put("endtime", DateUtil.getDateFormat(today).toInstant().atOffset(ZoneOffset.of("+8")).toEpochSecond());
            List<String> userCodeList = entry.getValue();
            reqMap.put("useridlist", userCodeList);
            List<KqRecordVo> ewKzkRecordVos = ewxService.getKqRecord(reqMap);
            allEwxServiceRecords.addAll(ewKzkRecordVos);
        }
        Map<String, List<KqRecordVo>> ewxRecordVoMap = Maps.newHashMap();
        for (KqRecordVo kqRecordVo : allEwxServiceRecords) {
            PeopleVo peopleVo = codePeopleMap.get(kqRecordVo.getUserCode());
            List<KqRecordVo> peoKqRecordList = ewxRecordVoMap.get(String.valueOf(peopleVo.getId()));
            if (ObjectUtils.isEmpty(peoKqRecordList)) {
                peoKqRecordList = Lists.newArrayList();
                peoKqRecordList.add(kqRecordVo);
                ewxRecordVoMap.put(String.valueOf(peopleVo.getId()), peoKqRecordList);
            } else
                peoKqRecordList.add(kqRecordVo);

        }
        return ewxRecordVoMap;
    }

    /**
     * 获取昨天考勤数据
     *
     * @return
     */
    private Map<String, List<KqRecordVo>> getYesterdayKqRecord() {
        List<KqRecordVo> allRecordList = Lists.newArrayList();
        //获取中控考勤数据
        Map<String, String> queryMap = Maps.newHashMap();
        String today = DateUtil.getDateFormat(new Date(), "yyyy-MM-dd");
        String yesterDay = DateUtil.getDateFormat(DateUtil.getDayBefore(new Date(), 1), "yyyy-MM-dd");
        queryMap.put("starttime", yesterDay + " 00:00:00");
        queryMap.put("endtime", today + " 00:00:00");

        List<KqRecordVo> zkRecordVos = zkService.getZkRecords(queryMap);
        allRecordList.addAll(zkRecordVos);

        Map<String, List<KqRecordVo>> zkRecordVoMap = Maps.newHashMap();
        for (KqRecordVo zkRecordVo : allRecordList) {
            List<KqRecordVo> valueList = zkRecordVoMap.get(zkRecordVo.getPin());
            if (ObjectUtils.isEmpty(valueList)) {
                valueList = Lists.newArrayList();
                valueList.add(zkRecordVo);
                zkRecordVoMap.put(zkRecordVo.getPin(), valueList);
            } else {
                valueList.add(zkRecordVo);
            }
        }
        return zkRecordVoMap;
    }

    /**
     * 管理人员记录插入
     *
     * @param peopleVo       人员对象
     * @param pmCalendar     考勤日历
     * @param zkRecordVoList 人员对应的考勤记录
     */
    private GlryKqRecordPo insertGlryRecord(PeopleVo peopleVo, PmCalendar pmCalendar, boolean isInHoliday,
                                            List<KqRecordVo> zkRecordVoList) {
        GlryKqRecordPo glryKqRecordPo = new GlryKqRecordPo();
        glryKqRecordPo.setId(leafService.getId());
        glryKqRecordPo.setCreatTime(new Date());
        glryKqRecordPo.setPeopleId(peopleVo.getId());
        glryKqRecordPo.setPeopleName(peopleVo.getName());
        glryKqRecordPo.setIdCard(peopleVo.getIdCard());
        glryKqRecordPo.setTelPhone(peopleVo.getTelPhone());
        glryKqRecordPo.setJob(peopleVo.getJobVo().getName());
        glryKqRecordPo.setOrgName(peopleVo.getOrgName());
        glryKqRecordPo.setOrgId(peopleVo.getOrgId());
        glryKqRecordPo.setProjectId(peopleVo.getProjectId());
        glryKqRecordPo.setSectionId(peopleVo.getSectionId());
        glryKqRecordPo.setJlRq(Integer
                .valueOf(DateUtil.getDateFormat(DateUtil.getDayBefore(new Date(), 1), "yyyyMMdd")));//记录昨天的
        glryKqRecordPo.setIsQj(0);//默认没有请假
        glryKqRecordPo.setIsQq(0);//默认没有缺勤
        glryKqRecordPo.setIsCd(0);//默认没有迟到
        glryKqRecordPo.setIsZt(0);//默认没有早退
        glryKqRecordPo.setIsYc(0);//默认没有异常

        if (isInHoliday) { //请假了
            glryKqRecordPo.setIsQj(1);
        } else {//没请假
            //查询判断是否缺勤
            if (ObjectUtils.isEmpty(zkRecordVoList))
                glryKqRecordPo.setIsQq(1);//缺勤
            else if (zkRecordVoList.size() == 1) {
                glryKqRecordPo.setIsYc(1);//异常
                glryKqRecordPo.setStartTime(zkRecordVoList.get(0).getChecktime());
                glryKqRecordPo.setStartTimeTemp(DateUtil.getDateFormat(zkRecordVoList.get(0).getChecktime(),DateUtil.DATETIME_DEFAULT_FORMAT));
            } else {
                Date calStartTime =
                        CalendarUtil.getStartWorkTime(DateUtil.getDayBefore(new Date(), 1), pmCalendar); //获取昨天的日历开始时间
                Date calEndTime =
                        CalendarUtil.getEndWorkTime(DateUtil.getDayBefore(new Date(), 1), pmCalendar); //获取昨天的日历结束时间

                //zkRecordVoList 按照checkTime排序
                Collections.sort(zkRecordVoList, new Comparator<KqRecordVo>() {
                    @Override
                    public int compare(KqRecordVo o1, KqRecordVo o2) {
                        if (o1.getChecktime().after(o2.getChecktime()))
                            return 1;
                        else if (o1.getChecktime().before(o2.getChecktime()))
                            return -1;
                        else
                            return 0;
                    }
                });
                Date checkStart = zkRecordVoList.get(0).getChecktime();
                Date checkEnd = zkRecordVoList.get(zkRecordVoList.size() - 1).getChecktime();
                glryKqRecordPo.setStartTime(checkStart);
                glryKqRecordPo.setStartTimeTemp(DateUtil.getDateFormat(checkStart,DateUtil.DATETIME_DEFAULT_FORMAT));
                glryKqRecordPo.setEndTime(checkEnd);
                glryKqRecordPo.setEndTimeTemp(DateUtil.getDateFormat(checkEnd,DateUtil.DATETIME_DEFAULT_FORMAT));

                if (checkStart.after(calStartTime)) {//迟到
                    glryKqRecordPo.setIsCd(1);
                }
                if (checkEnd.before(calEndTime)) {//早退
                    glryKqRecordPo.setIsZt(1);
                }
            }

        }
        return glryKqRecordPo;
    }

    /**
     * 劳务人员考勤记录插入
     *
     * @param peopleVo       人员对象
     * @param zkRecordVoList 人员对应的考勤记录
     */
    private LwryKqRecordPo insertLwryRecord(PeopleVo peopleVo, List<KqRecordVo> zkRecordVoList) {
        LwryKqRecordPo lwryKqRecordPo = new LwryKqRecordPo();
        lwryKqRecordPo.setId(leafService.getId());
        lwryKqRecordPo.setCreatTime(new Date());
        lwryKqRecordPo.setPeopleId(peopleVo.getId());
        lwryKqRecordPo.setPeopleName(peopleVo.getName());
        lwryKqRecordPo.setIdCard(peopleVo.getIdCard());
        lwryKqRecordPo.setTelPhone(peopleVo.getTelPhone());
        lwryKqRecordPo.setJob(peopleVo.getJobVo().getName());
        lwryKqRecordPo.setOrgName(peopleVo.getOrgName());
        lwryKqRecordPo.setOrgId(peopleVo.getOrgId());
        lwryKqRecordPo.setProjectId(peopleVo.getProjectId());
        lwryKqRecordPo.setSectionId(peopleVo.getSectionId());
        lwryKqRecordPo
                .setJlRq(Integer.valueOf(DateUtil.getDateFormat(DateUtil.getDayBefore(new Date(), 1), "yyyyMMdd")));
        lwryKqRecordPo.setIsQq(0);//默认没有缺勤

        //判断该人员是否有打卡记录，如有则代表未缺勤
        //查询判断是否缺勤
        if (ObjectUtils.isEmpty(zkRecordVoList))
            lwryKqRecordPo.setIsQq(1);//查不到记录表示缺勤

        return lwryKqRecordPo;
    }

    private PmCalendar getPmCalendar(String calendarId) {
        CalendarVo calendarVo = kqRecordMapper.getPmCalendar(calendarId);
        if (!ObjectUtils.isEmpty(calendarVo))
            return new PcCalendar(calendarVo);
        return null;
    }

    private Map<Integer, PmCalendar> getPmCalendarMap() {
        List<CalendarVo> calendarVos = kqRecordMapper.getAllPmCalendar();
        Map<Integer, CalendarVo> calendarVoMap = ListUtil.listToMap(calendarVos, "id", Integer.class);

        Map<Integer, PmCalendar> pmCalendarMap = Maps.newHashMap();
        for (Map.Entry<Integer, CalendarVo> entry : calendarVoMap.entrySet()) {
            pmCalendarMap.put(entry.getKey(), new PcCalendar(entry.getValue()));
        }
        return pmCalendarMap;
    }

    private PmCalendar getDefaultPmCalendar() {
        CalendarVo calendarVo = kqRecordMapper.getDefaultPmCalendar();
        if (!ObjectUtils.isEmpty(calendarVo))
            return new PcCalendar(calendarVo);
        return null;
    }

    private boolean isInHoliday(PeopleVo peopleVo, List<HolidayPo> holidayPoList) {
        Date yesterDay = DateUtil.getDayBefore(new Date(), 1);
        for (HolidayPo holidayPo : holidayPoList) {
            if (peopleVo.getId().equals(holidayPo.getPeopleId())) {
                if (DateUtil
                        .formatDate(DateUtil.getDateFormat(holidayPo.getStartTime()), "yyyy-MM-dd")
                        .compareTo(DateUtil.formatDate(DateUtil.getDateFormat(yesterDay), "yyyy-MM-dd")) <= 0) {

                    if (DateUtil
                            .formatDate(DateUtil.getDateFormat(holidayPo.getEndTime()), "yyyy-MM-dd")
                            .compareTo(DateUtil.formatDate(DateUtil.getDateFormat(yesterDay), "yyyy-MM-dd")) >= 0) {
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public static void main(String args[]) {
    }

    /**
     * 插入考勤详情表
     */
    private void insertAllKqRecord(PeopleVo peopleVo, String jobName, List<KqRecordVo> zkRecordVoList) {
        if (!ObjectUtils.isEmpty(zkRecordVoList)) {//如果该人员的考勤记录不为空。直接插入
            List<AllKqRecordPo> insertAllRecordList = Lists.newArrayList();
            for (KqRecordVo zkRecordVo : zkRecordVoList) {
                AllKqRecordPo allKqRecordPo = new AllKqRecordPo();
                allKqRecordPo.setId(leafService.getId());
                allKqRecordPo.setCreatTime(new Date());
                allKqRecordPo.setPeopleId(peopleVo.getId());
                allKqRecordPo.setPeopleName(peopleVo.getName());
                allKqRecordPo.setIdCard(peopleVo.getIdCard());
                allKqRecordPo.setTelPhone(peopleVo.getTelPhone());
                allKqRecordPo.setJob(jobName);
                allKqRecordPo.setType(peopleVo.getTypeVo().getCode());
                allKqRecordPo.setOrgName(peopleVo.getOrgName());
                allKqRecordPo.setOrgId(peopleVo.getOrgId());
                allKqRecordPo.setProjectId(peopleVo.getProjectId());
                allKqRecordPo.setSectionId(peopleVo.getSectionId());
                allKqRecordPo
                        .setCheckTime(Integer.valueOf(DateUtil.getDateFormat(zkRecordVo.getChecktime(), "yyyyMMdd")));
                insertAllRecordList.add(allKqRecordPo);
            }
            kqRecordMapper.addAllKqRecord(insertAllRecordList);
        }
    }
}
