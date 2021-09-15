package com.wisdom.acm.szxm.service.rygl.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.*;
import com.wisdom.acm.szxm.mapper.rygl.KqRecordMapper;
import com.wisdom.acm.szxm.po.rygl.PeoplePo;
import com.wisdom.acm.szxm.service.app.EwxService;
import com.wisdom.acm.szxm.service.rygl.HolidayService;
import com.wisdom.acm.szxm.service.rygl.KqReportService;
import com.wisdom.acm.szxm.service.rygl.PeopleService;
import com.wisdom.acm.szxm.service.rygl.ZkService;
import com.wisdom.acm.szxm.vo.rygl.*;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.text.NumberFormat;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KqReportServiceImpl implements KqReportService {

    @Autowired
    private KqRecordMapper kqRecordMapper;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private ZkService zkService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private EwxService ewxService;


    /**
     * 查询管理人员考勤报表
     *
     * @param mapWhere
     * @return
     */
    @Override
    public List<Map<String, Object>> selectGlRyReport(Map<String, Object> mapWhere) {
        return kqRecordMapper.selectGlRyReport(mapWhere);
    }

    /**
     * 查询劳务人员考勤报表
     *
     * @param mapWhere
     * @return
     */
    @Override
    public List<Map<String, Object>> selectLwRyReport(Map<String, Object> mapWhere) {
        return kqRecordMapper.selectLwRyReport(mapWhere);
    }


    @Override
    public Map<String, Object> getSectionKqRecord(Map<String, Object> mapWhere, Integer pageSize,
                                                  Integer currentPageNum) {
        Map<String, Object> returnMap = Maps.newHashMap();

        List<String> typeList = Lists.newArrayList();
        String type = String.valueOf(mapWhere.get("type"));//人员类型，0 管理人员 1普通人员 2全部
        if (type.equals("0")) {
            typeList.add("0");
        } else if (type.equals("1")) {
            typeList.add("1");
            typeList.add("2");
            typeList.add("3");
            typeList.add("4");
        } else if (type.equals("2") || StringHelper.isNullAndEmpty(type)) {
            typeList.add("0");
            typeList.add("1");
            typeList.add("2");
            typeList.add("3");
            typeList.add("4");
        }
        Date date = new Date();//默认取当前日期
        if (StringHelper.isNotNullAndEmpty(String.valueOf(mapWhere.get("date"))))
            date = DateUtil.formatDate(String.valueOf(mapWhere.get("date")), DateUtil.DATE_DEFAULT_FORMAT);
        //查询相关标段的进场人员
        /**
         * 去除传入type值
         */
        mapWhere.remove("type");
        mapWhere.put("typeList", typeList);//人员分类
        mapWhere.put("status", "1");//进场人员
        mapWhere.put("projectId", StringHelper.formattString(String.valueOf(mapWhere.get("projectId"))));
        List<String> sectionList = (List<String>) mapWhere.get("sectionList");
        if (!ObjectUtils.isEmpty(sectionList)) {
            mapWhere.remove("sectionId");
        } else {
            mapWhere.put("sectionId", StringHelper.formattString(String.valueOf(mapWhere.get("sectionId"))));
        }
        mapWhere.put("ryId", StringHelper.formattString(String.valueOf(mapWhere.get("ryId"))));
        mapWhere.put("searcher", StringHelper.formattString(String.valueOf(mapWhere.get("searcher"))));
        List<PeopleVo> peopleVoList = peopleService.selectAllKqPeople(mapWhere);

        List<Map<String, Object>> resultList = Lists.newArrayList();
        Map<String, List<KqRecordVo>> zkRecordMap = this.getZkKqRecord(date);//获取中控考勤数据
        Map<String, List<KqRecordVo>> ewxRecordMap = this.getEwxKqRecord(date, peopleVoList);//获取企业微信考勤数据

        //获取请假数据缓存
        Map<String, Object> holiQueryMap = Maps.newHashMap();
        holiQueryMap.put("status", SzxmEnumsUtil.StatusEnum.APPROVED.toString());
        holiQueryMap.put("projectId", StringHelper.formattString(String.valueOf(mapWhere.get("projectId"))));
        if (!ObjectUtils.isEmpty(sectionList)) {
            holiQueryMap.put("sectionList", sectionList);
        } else {
            holiQueryMap.put("sectionId", StringHelper.formattString(String.valueOf(mapWhere.get("sectionId"))));
        }
        holiQueryMap.put("date", DateUtil.getDateFormat(date));
        List<HolidayVo> holidayVoList = holidayService.selectInOneDate(holiQueryMap);
        Map<Integer, HolidayVo> holidayMap = ListUtil.listToMap(holidayVoList, "peopleId", Integer.class);

        int cqrs = 0;//出勤人数
        int qjrs = 0;//请假人数
        List<String> peopleList_cq = Lists.newArrayList();
        //请假人详情
        List<PeopleVo> qjDetailInfo = new ArrayList<>();
        //出勤人详情
        List<PeopleVo> cqDetailInfo = new ArrayList<>();
        //缺勤人详情
        List<PeopleVo> qqDetailInfo = new ArrayList<>();
        for (PeopleVo peopleVo : peopleVoList) {
            //判断此人当天是否请假
            HolidayVo holidayVo = holidayMap.get(peopleVo.getId());
            if (!ObjectUtils.isEmpty(holidayVo)) {//不为空说明这天是请假的
                qjDetailInfo.add(peopleVo);
                qjrs++;//请假人数自增
            } else {
                //根据主键ID查询中控的数据
                List<KqRecordVo> allKqRecords = Lists.newArrayList();
                List<KqRecordVo> zkRecords = zkRecordMap.get(String.valueOf(peopleVo.getId()));
                if (!ObjectUtils.isEmpty(zkRecords))
                    allKqRecords.addAll(zkRecords);
                //根据ID查询企业微信的数据
                List<KqRecordVo> ewxRecords = ewxRecordMap.get(String.valueOf(peopleVo.getId()));
                if (!ObjectUtils.isEmpty(ewxRecords))
                    allKqRecords.addAll(ewxRecords);
                if (!ObjectUtils.isEmpty(allKqRecords)) {
                    cqDetailInfo.add(peopleVo);
                    cqrs++;//出勤人数自增
                    peopleList_cq.add(peopleVo.getName());
                }
                for (KqRecordVo kqRecordVo : allKqRecords) {
                    Map<String, Object> data = Maps.newHashMap();
                    data.put("id", UUIDHexGenerator.generator());
                    data.put("peopleId", peopleVo.getId());
                    data.put("name", peopleVo.getName());
                    data.put("typeName", peopleVo.getTypeVo().getName());

                    data.put("jobName", peopleVo.getJobVo().getName());
                    data.put("checkTime", DateUtil.getDateFormat(kqRecordVo.getChecktime(), "yyyy-MM-dd HH:mm"));
                    data.put("checkType", "1".equals(kqRecordVo.getCheckType()) ? "微信考勤" : "考勤机");
                    data.put("projectName", peopleVo.getProjectName());
                    data.put("locationAddress", StringHelper.formattString(kqRecordVo.getLocationAddress()));
                    data.put("sectionName", peopleVo.getSectionName());
                    data.put("projectId", peopleVo.getProjectId());
                    data.put("sectionCode", peopleVo.getSectionCode());
                    resultList.add(data);
                }
            }

        }
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>();
        pageInfo.setPageNum(currentPageNum);
        pageInfo.setPageSize(pageSize);
        Page<Map<String, Object>> newPage = new PageInfoUtiil<Map<String, Object>>().generatePageList(pageInfo, resultList);
        PageInfo newPageInfo = new PageInfo<Map<String, Object>>(newPage);
        newPageInfo.setTotal(pageInfo.getTotal());

        returnMap.put("data", new TableResultResponse(newPageInfo));
        returnMap.put("jcrysl", peopleVoList.size());//进场人员数量
        returnMap.put("cqrs", cqrs);//出勤人数
        returnMap.put("qjrs", qjrs);//请假人数
        returnMap.put("qqrs", peopleVoList.size() - cqrs - qjrs);//缺勤人数数量
        returnMap.put("peopleVoList_cq", peopleList_cq);
        //获取请假人详情，缺勤人详情
        returnMap.put("qjDatailInfo", qjDetailInfo);//请假人详情
        //缺勤人详情计算
        qjDetailInfo.addAll(cqDetailInfo);
        List<Integer> peopleIds = qjDetailInfo.stream().map(PeopleVo::getId).distinct().collect(Collectors.toList());
        for(PeopleVo peopleVo : peopleVoList){
            if(!peopleIds.contains(peopleVo.getId())){
                qqDetailInfo.add(peopleVo);
            }
        }
        returnMap.put("qqDatailInfo", qqDetailInfo);//缺勤人详情
        return returnMap;
    }

    @Override
    public Map<String, Object> getAttendanceRecords(Map<String, Object> mapWhere) {
        Map<String, Object> returnMap = Maps.newHashMap();

        List<String> typeList = Lists.newArrayList();
        String type = String.valueOf(mapWhere.get("type"));//人员类型，0 管理人员 1普通人员 2全部
        if (type.equals("0")) {
            typeList.add("0");
        } else if (type.equals("1")) {
            typeList.add("1");
            typeList.add("2");
            typeList.add("3");
            typeList.add("4");
        } else if (type.equals("2") || StringHelper.isNullAndEmpty(type)) {
            typeList.add("0");
            typeList.add("1");
            typeList.add("2");
            typeList.add("3");
            typeList.add("4");
        }
        //查询sql使用typelist
        mapWhere.remove("type");

        Date date = new Date();//默认取当前日期
        if (StringHelper.isNotNullAndEmpty(String.valueOf(mapWhere.get("date"))))
            date = DateUtil.formatDate(String.valueOf(mapWhere.get("date")), DateUtil.DATE_DEFAULT_FORMAT);
        //查询相关标段的进场人员
        mapWhere.put("typeList", typeList);//人员分类
        mapWhere.put("status", "1");//进场人员
        mapWhere.put("projectId", StringHelper.formattString(String.valueOf(mapWhere.get("projectId"))));
        List<PeopleVo> peopleVoList = peopleService.selectAllKqPeopleAllSection(mapWhere);
        log.info("一共有人员{}人", peopleVoList.size());
        Map<String, List<KqRecordVo>> zkRecordMap = this.getZkKqRecord(date);//获取中控考勤数据
        Map<String, List<KqRecordVo>> ewxRecordMap = this.getEwxKqRecord(date, peopleVoList);//获取企业微信考勤数据

        //获取请假数据缓存
        Map<String, Object> holiQueryMap = Maps.newHashMap();
        holiQueryMap.put("status", SzxmEnumsUtil.StatusEnum.APPROVED.toString());
        holiQueryMap.put("projectId", StringHelper.formattString(String.valueOf(mapWhere.get("projectId"))));
        holiQueryMap.put("date", DateUtil.getDateFormat(date));
        List<HolidayVo> holidayVoList = holidayService.selectInOneDate(holiQueryMap);
        Map<Integer, HolidayVo> holidayMap = ListUtil.listToMap(holidayVoList, "peopleId", Integer.class);
        log.info("请假人员{}人", holidayVoList.size());
        Map<String, List<PeopleVo>> peopleSectionMap = Maps.newHashMap();
        Map<String, Integer> kqSectionMap = Maps.newHashMap();
        Map<String, Integer> qjSectionMap = Maps.newHashMap();

        int cqrs = 0;//出勤人数
        int qjrs = 0;//请假人数
        for (PeopleVo peopleVo : peopleVoList) {
            //判断此人当天是否请假
            HolidayVo holidayVo = holidayMap.get(peopleVo.getId());
            if (!ObjectUtils.isEmpty(holidayVo)) {//不为空说明这天是请假的
                qjrs++;//请假人数自增
                if (ObjectUtils.isEmpty(qjSectionMap.get(peopleVo.getSectionCode()))) {
                    qjSectionMap.put(peopleVo.getSectionCode(), 1);
                } else {
                    Integer qjNums = qjSectionMap.get(peopleVo.getSectionCode());
                    qjNums++;
                    qjSectionMap.put(peopleVo.getSectionCode(), qjNums);
                }
            } else {
                //根据主键ID查询中控的数据
                List<KqRecordVo> allKqRecords = Lists.newArrayList();
                List<KqRecordVo> zkRecords = zkRecordMap.get(String.valueOf(peopleVo.getId()));
                if (!ObjectUtils.isEmpty(zkRecords))
                    allKqRecords.addAll(zkRecords);
                //根据ID查询企业微信的数据
                List<KqRecordVo> ewxRecords = ewxRecordMap.get(String.valueOf(peopleVo.getId()));
                if (!ObjectUtils.isEmpty(ewxRecords))
                    allKqRecords.addAll(ewxRecords);
                if (!ObjectUtils.isEmpty(allKqRecords)) {
                    cqrs++;//出勤人数自增
                    if (ObjectUtils.isEmpty(kqSectionMap.get(peopleVo.getSectionCode()))) {
                        kqSectionMap.put(peopleVo.getSectionCode(), 1);
                    } else {
                        Integer kqNum = kqSectionMap.get(peopleVo.getSectionCode());
                        kqNum++;
                        kqSectionMap.put(peopleVo.getSectionCode(), kqNum);
                    }
                }
                if (ObjectUtils.isEmpty(peopleSectionMap.get(peopleVo.getSectionCode()))) {
                    List<PeopleVo> peopleVos = Lists.newArrayList();
                    peopleVos.add(peopleVo);
                    peopleSectionMap.put(peopleVo.getSectionCode(), peopleVos);
                } else {
                    peopleSectionMap.get(peopleVo.getSectionCode()).add(peopleVo);
                }
            }

        }
        List<SectionKqVo> sectionKqVos = Lists.newArrayList();
        if (!ObjectUtils.isEmpty(peopleSectionMap)) {
            for (Map.Entry<String, List<PeopleVo>> entry : peopleSectionMap.entrySet()) {
                int attendanceQuantity = ObjectUtils.isEmpty(kqSectionMap.get(entry.getKey())) ? 0 : kqSectionMap.get(entry.getKey());
                int leaveNumber = ObjectUtils.isEmpty(qjSectionMap.get(entry.getKey())) ? 0 : qjSectionMap.get(entry.getKey());
                List<PeopleVo> value = entry.getValue();

                SectionKqVo sectionKqVo = new SectionKqVo();
                sectionKqVo.setAllNums(value.size());
                sectionKqVo.setSectionId(value.get(0).getSectionId());
                sectionKqVo.setSectionCode(value.get(0).getSectionCode());
                sectionKqVo.setAttendanceQuantity(attendanceQuantity);
                sectionKqVo.setLeaveNumber(leaveNumber);
                sectionKqVo.setAbsenceNumber(sectionKqVo.getAllNums() - attendanceQuantity - leaveNumber);

                sectionKqVos.add(sectionKqVo);
            }
            Collections.sort(sectionKqVos);
        }

        returnMap.put("list", sectionKqVos);
        returnMap.put("jcrysl", peopleVoList.size());//进场人员数量
        returnMap.put("cqrs", cqrs);//出勤人数
        returnMap.put("qjrs", qjrs);//请假人数
        returnMap.put("qqrs", peopleVoList.size() - cqrs - qjrs);//缺勤人数数量

        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后0位
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float) (cqrs) / (float) peopleVoList.size() * 100);

        returnMap.put("attendance", result);//出勤率
        log.info("出勤率：{}", result);
        return returnMap;
    }

    /**
     * 获取中控考勤指定日期的数据，按人员ID，组成Map
     *
     * @param date
     * @return
     */
    private Map<String, List<KqRecordVo>> getZkKqRecord(Date date) {
        //获取中控考勤数据
        Map<String, String> queryMap = Maps.newHashMap();
        String startTime = DateUtil.getDateFormat(date, "yyyy-MM-dd") + " 00:00:00";//今天凌晨0点
        String endTime = DateUtil.getDateFormat(DateUtil.getDayAfter(date, 1), "yyyy-MM-dd") + " 00:00:00";//明天凌晨0点
        queryMap.put("starttime", startTime);
        queryMap.put("endtime", endTime);
        List<KqRecordVo> zkRecordVos = zkService.getZkRecords(queryMap);
        Map<String, List<KqRecordVo>> zkRecordVoMap = Maps.newHashMap();
        for (KqRecordVo zkRecordVo : zkRecordVos) {
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
     * 获取企业微信考勤指定日期的数据，按人员ID，组成Map
     *
     * @param date
     * @return
     */
    private Map<String, List<KqRecordVo>> getEwxKqRecord(Date date, List<PeopleVo> peopleVoList) {
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
        String today = DateUtil.getDateFormat(date, "yyyy-MM-dd");//今天凌晨0点
        String tomorrow = DateUtil.getDateFormat(DateUtil.getDayAfter(date, 1), "yyyy-MM-dd");//明天凌晨0点
        List<KqRecordVo> allEwxServiceRecords = Lists.newArrayList();
        for (Map.Entry<String, List<String>> entry : pcMap.entrySet()) {
            Map<String, Object> reqMap = Maps.newHashMap();
            reqMap.put("opencheckindatatype", "3");//全部打卡
            reqMap.put("starttime", DateUtil.getDateFormat(today).toInstant().atOffset(ZoneOffset.of("+8")).toEpochSecond());
            reqMap.put("endtime", DateUtil.getDateFormat(tomorrow).toInstant().atOffset(ZoneOffset.of("+8")).toEpochSecond());
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

    @Override
    public Map<String, Object> getPeopleMonthKqRecord(Map<String, Object> mapWhere) {
        Map<String, Object> resultMap = new HashMap<>();
        String peopleId = String.valueOf(mapWhere.get("peopleId"));
        String type = String.valueOf(mapWhere.get("type"));//人员类型，0 管理人员 1 劳务人员
        String startTime = StringHelper.formattString(String.valueOf(mapWhere.get("startTime")));
        String endTime = StringHelper.formattString(String.valueOf(mapWhere.get("endTime")));
        //查询人员信息
        PeoplePo peoplePo = peopleService.selectById(peopleId);
        resultMap.put("peopleInfo",peoplePo);
        if (StringHelper.isNullAndEmpty(peopleId) || StringHelper.isNullAndEmpty(type) ||
                StringHelper.isNullAndEmpty(startTime) || StringHelper.isNullAndEmpty(endTime)) {
            return null;
        }

        mapWhere.put("startTime", DateUtil.getDateFormat(DateUtil.formatDate(startTime, "yyyy-MM-dd"), "yyyyMMdd"));
        mapWhere.put("endTime", DateUtil.getDateFormat(DateUtil.formatDate(endTime, "yyyy-MM-dd"), "yyyyMMdd"));
        mapWhere.put("peopleId", peopleId);
        List<KqDayReportDetailVo> kqReportDetail = new ArrayList<>();
        if ("0".equals(type)) {//管理人员
            kqReportDetail = kqRecordMapper.selectGlRyReportDetail(mapWhere);
            //处理获取状态
            getPeopleDayStatus(kqReportDetail);
        } else {
            //查询劳务人员在固定时间段里面的请假情况
            //缺勤的记录
            kqReportDetail = kqRecordMapper.selectLwRyReportDetail(mapWhere);
            //请假的记录
            List<HolidayVo> holidayVos = holidayService.queryLwryHolidayRecord(mapWhere);
            //获取当前月份的所有日历
            List<Date> everyDay = DateUtil.getEveryDay(DateUtil.formatDate(startTime, "yyyy-MM-dd"), DateUtil.formatDate(endTime, "yyyy-MM-dd"));
            this.getLwryPeopleDayStatus(kqReportDetail,holidayVos,everyDay);
        }
        long qingJiaDays = kqReportDetail.stream().filter(item -> StringUtils.equals(item.getStaus(), "qingJia")).count();
        long queQinDays = kqReportDetail.stream().filter(item -> StringUtils.equals(item.getStaus(), "queQin")).count();
        long chuQinDays = kqReportDetail.stream().filter(item -> StringUtils.equals(item.getStaus(), "chuQin")).count();
        resultMap.put("qingJiaDays",qingJiaDays);
        resultMap.put("queQinDays",queQinDays);
        resultMap.put("chuQinDays",chuQinDays);
        resultMap.put("kqReportDetail",kqReportDetail);
        return resultMap;
    }

    private void getPeopleDayStatus(List<KqDayReportDetailVo> kqDayReportDetailVoList){
        for (KqDayReportDetailVo kqDayReportDetailVo : kqDayReportDetailVoList) {
            if(StringUtils.equals(kqDayReportDetailVo.getQingJia(),"1")){
                kqDayReportDetailVo.setStaus("qingJia");
                continue;
            } else if(StringUtils.equals(kqDayReportDetailVo.getQueQin(),"1")){
                kqDayReportDetailVo.setStaus("queQin");
                continue;
            } else {
                //异常，迟到，早退都算做出勤了
                /*if(StringUtils.equals(kqDayReportDetailVo.getYiChang(),"1") || StringUtils.equals(kqDayReportDetailVo.getChiDao(),"1")
                    || StringUtils.equals(kqDayReportDetailVo.getZaoTui(),"1")){
                kqDayReportDetailVo.setStaus("chuQin");
                continue;
            }*/
                kqDayReportDetailVo.setStaus("chuQin");
            }

        }

    }

    private void getLwryPeopleDayStatus(List<KqDayReportDetailVo> kqDayReportDetailVoList,List<HolidayVo> holidayVos, List<Date> everyDay){
        //判断外部人员缺勤状态
        for (KqDayReportDetailVo kqDayReportDetailVo : kqDayReportDetailVoList) {
            if(StringUtils.equals(kqDayReportDetailVo.getQueQin(),"1")){
                kqDayReportDetailVo.setStaus("queQin");
                continue;
            }
        }
        for(Date nowDate : everyDay){
            KqDayReportDetailVo kqDayReportDetailVo = new KqDayReportDetailVo();
            kqDayReportDetailVo.setDay(DateUtil.getDateFormat(nowDate,"yyyyMMdd"));
            //判断日历里面的当前时间是否在请假的时间里面
            for(HolidayVo holidayVo : holidayVos){
                if(DateUtil.betweenCalendar(nowDate,holidayVo.getStartTime(),holidayVo.getEndTime())){
                    kqDayReportDetailVo.setStaus("qingJia");
                    continue;
                }
            }
            if(StringUtils.isBlank(kqDayReportDetailVo.getStaus())){
                kqDayReportDetailVo.setStaus("chuQin");
            }
            kqDayReportDetailVoList.add(kqDayReportDetailVo);
        }
    }
}
