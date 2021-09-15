package com.wisdom.acm.dc1.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.dc1.common.DateUtil;
import com.wisdom.acm.dc1.common.DcCommonUtil;
import com.wisdom.acm.dc1.common.officeUtils.ExcelError;
import com.wisdom.acm.dc1.common.officeUtils.ExcelUtil;
import com.wisdom.acm.dc1.mapper.TrafficMainMapper;
import com.wisdom.acm.dc1.po.*;
import com.wisdom.acm.dc1.service.*;
import com.wisdom.acm.dc1.vo.*;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.EnumsUtil;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zll
 * @date 2020/7/21/021 18:46
 * Description：<描述>
 */
@Slf4j
@Service
public class TrafficMainServiceImpl extends BaseService<TrafficMainMapper,TrafficMainPo> implements TrafficMainService {
    @Autowired
    TrafficStationMonthlyService trafficStationMonthlyService;
    @Autowired
    TrafficStationDailyService trafficStationDailyService;
    @Autowired
    TrafficPeakStationMonthlyService trafficPeakStationMonthlyService;
    @Autowired
    TrafficPeakStationDailyService trafficPeakStationDailyService;
    @Autowired
    TrafficLineDailyService trafficLineDailyService;
    @Autowired
    TrafficLineTicketMonthlyService trafficLineTicketMonthlyService;
    @Autowired
    TrafficLineMonthlyService trafficLineMonthlyService;
    @Autowired
    TrafficLineTicketDailyService trafficLineTicketDailyService;
    @Autowired
    TrafficPeakLineDailyService trafficPeakLineDailyService;
    @Autowired
    private DcCommonUtil dcCommonUtil;
    @Override
    public PageInfo<TrafficMainVo> queryTrafficMainList(Map<String, Object>
                    mapWhere, Integer pageSize, Integer currentPageNum)
    {
        mapWhere.put("period", StringHelper.formattString(String.valueOf(mapWhere.get("period"))));
        mapWhere.put("startTime", StringHelper.formattString(String.valueOf(mapWhere.get("startTime"))));
        mapWhere.put("endTime", StringHelper.formattString(String.valueOf(mapWhere.get("endTime"))));
        PageHelper.startPage(currentPageNum, pageSize);
        List<TrafficMainVo> trafficMainVoList = mapper.queryTrafficMainList(mapWhere);
        PageInfo<TrafficMainVo> pageInfo = new PageInfo<TrafficMainVo>(trafficMainVoList);
        if (!ObjectUtils.isEmpty(pageInfo.getList()))
        {
            for(TrafficMainVo trafficMainVo : pageInfo.getList()){
                trafficMainVo.setWeek(DateUtil.getWeek(trafficMainVo.getCreatTime()));
                String statusName="新建";
                if("APPROVED".equals(trafficMainVo.getStatusVo().getCode()))
                {
                    statusName="已发布";
                }
                //返回前端查看状态
                //trafficMainVo.getStatusVo().setName(EnumsUtil.StatusEnum.valueOf(trafficMainVo.getStatusVo().getCode()).getName());
                trafficMainVo.getStatusVo().setName(statusName);
            }
        }
        return pageInfo;
    }

    @Override
    public UploadVo queryIsHaveTrafficMain(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BaseException("文件不能为空!");
        }
        String fileName = file.getOriginalFilename();//文件名
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);//文件类型
        if (!"xlsx".equals(ext)) {
            throw new BaseException("文件格式不支持!");
        }
        String date="";
        Workbook wb = null;
        try {
            wb = ExcelUtil.getWorkbook(file);
            Sheet sheet = ExcelUtil.getSheet(wb, 0);//获取第一页的工作簿
            //定义错误日志
            ExcelError excelError = new ExcelError();
            //交易日期
            date = generateDate(excelError, sheet);
            //从数据库中查询交易日期是本日的返回不可以导入
            List<TrafficMainVo> ids = mapper.queryTrafficMainByDate(date);
            if (ids.size() != 0) {
                UploadVo vo =new UploadVo();
                vo.setStatus("0");
                vo.setContent("交易日期为： " + date + "已经导入，是否需要重新导入？");
                return vo;
            }
        } catch (Exception e) {
            throw new BaseException("导入错误!", e);
        }
        UploadVo vo =new UploadVo();
        vo.setStatus("1");
        vo.setContent("交易日期为： " + date + "暂没导入，请导入表格数据");
        return vo;
    }

    @Override
    public void approvedTraffic(List<Integer> ids) {
        //1 把业务数据的状态变更为已批准.
        TrafficMainPo updatePo = new TrafficMainPo();
        updatePo.setStatus(EnumsUtil.StatusEnum.APPROVED.getCode());
        this.updateSelectiveByIds(updatePo, ids);
    }

    /**
     * 主表数据删除之后，子表数据跟着删除
     * @param mapList
     */
    @Override
    @AddLog(title = "删除本条客运数据，连带着子表数据", module = LoggerModuleEnum.TRAFFIC_DAILYREPORT)
    public void delTrafficMain(List<Map<String, Object>> mapList) {
        List<Integer>listId= Lists.newArrayList();
        List<String>recordTimeList=Lists.newArrayList();
        for(Map<String, Object> map: mapList){
            String id =StringHelper.formattString(String.valueOf(map.get("id")));
            String recordTime=StringHelper.formattString(String.valueOf(map.get("recordTime")));
            //保存时间
            recordTimeList.add(recordTime);
            //保存主键id
            listId.add(Integer.valueOf(id));
        }
        List<Integer>ids=mapList.stream().map(e->(Integer) e.get("id")).collect(Collectors.toList());
        List<TrafficMainPo>pos=this.selectByIds(ids);
        for(TrafficMainPo po :pos){
            this.setAcmLogger(new AcmLogger("1,3号线"+DateUtil.getDateFormat(po.getRecordTime())+"号客运数据删除"
                    ,DateUtil.getDateFormat(po.getRecordTime()),"1,3",
                    po.getId().toString(),"INIT"));
        }
        delAllTrafficSheet(listId,recordTimeList);
    }

    /**
     * 查询子表本日期的相关所有数据信息
     * @param recordTime
     * @return
     */
    @Override
    public List<Map<String, List<?>>> queryTrafficChildrenList(String recordTime) {
        List<Map<String, List<?>>> allMapList=new ArrayList<>();
        Map<String, List<?>> mapList= new HashMap<>();
        //取全线网客运量情况数据
        List<TrafficLineDailyVo> trafficLineDailyVos =trafficLineDailyService.trafficLineDailyVoList(recordTime);
        //全线网各车站进出站客运量统计情况（客运量单位：人次）
        List<TrafficStationDailyVo> trafficStationDailyVos=trafficStationDailyService.trafficStationDailyVos(recordTime);
        //全线网客运量高峰统计情况
        List<TrafficPeakLineDailyVo> trafficPeakLineDailyVos=trafficPeakLineDailyService.trafficStationDailyVos(recordTime);
        //全线网客运量分类统计情况
        List<TrafficLineTicketDailyVo> trafficLineTicketDailyVos=trafficLineTicketDailyService.trafficLineTicketDailyVoList(recordTime);
        mapList.put("table1", trafficLineDailyVos);
        mapList.put("table2",trafficStationDailyVos);
        mapList.put("table3",trafficPeakLineDailyVos);
        mapList.put("table4",trafficLineTicketDailyVos);
        allMapList.add(mapList);
        return allMapList;
    }

    /**
     * 修改子表数据
     * @param trafficChildrenList
     * @return
     */
    @Override
    @AddLog(title = "修改客运子表数据", module = LoggerModuleEnum.TRAFFIC_DAILYREPORT)
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<Map<String, List<?>>> updateTrafficChildrenList(List<Map<String, List<?>>> trafficChildrenList,Integer id)
    {
        //第四个table的日期
        Map m = (Map) trafficChildrenList.get(3).get("table4").get(0);
        Date recordTime=DateUtil.getDateFormat((String) m.get("recordTime"));
        super.setAcmLogger(new AcmLogger("修改客运子表数据，交易日期为： " + DateUtil.getDateFormat(recordTime)));
        List<TrafficLineTicketDailyVo>trafficLineTicketDailyVos=trafficLineTicketDailyService.queryTrafficLineTicketDailys(null,DateUtil.getDateFormat(recordTime));
        List<TrafficLineDailyPo> trafficLineDailyPos=Lists.newArrayList();
        List<TrafficLineTicketDailyPo> trafficLineTicketDailyPos=Lists.newArrayList();
        List<TrafficPeakLineDailyPo> trafficPeakLineDailyPos=Lists.newArrayList();
        List<TrafficStationDailyPo> trafficStationDailyPos=Lists.newArrayList();
        List<String>ls=Lists.newArrayList();
        for(Map<String, List<?>> mapList:trafficChildrenList){
            if(!ObjectUtils.isEmpty(mapList.get("table1"))){
                for(int i=0;i<mapList.get("table1").size()-1;i++){
                    Map map= (Map) mapList.get("table1").get(i);
                    map.put("recordTime",DateUtil.getDateFormat((String)map.get("recordTime")));
                    map.put("creatTime",DateUtil.getDateFormat((String)map.get("creatTime")));
                    map.put("lastUpdTime",DateUtil.getDateFormat(DateUtil.getDateFormat(new Date())));
                    TrafficLineDailyPo trafficLineDailyPo=dozerMapper.map(map,TrafficLineDailyPo.class);
                    ls.add(DateUtil.getDateFormat(trafficLineDailyPo.getRecordTime()));
                    trafficLineDailyPos.add(trafficLineDailyPo);
                }
            }
            if(!ObjectUtils.isEmpty(mapList.get("table2"))){
                for(int i=0;i<mapList.get("table2").size();i++){
                    Map map= (Map) mapList.get("table2").get(i);
                    map.put("recordTime",DateUtil.getDateFormat((String)map.get("recordTime")));
                    map.remove("creatTime");
                    map.put("lastUpdTime",DateUtil.getDateFormat(DateUtil.getDateFormat(new Date())));
                    TrafficStationDailyPo trafficStationDailyPo=dozerMapper.map(mapList.get("table2").get(i),TrafficStationDailyPo.class);
                    trafficStationDailyPos.add(trafficStationDailyPo);
                   ls.add(DateUtil.getDateFormat(trafficStationDailyPo.getRecordTime()));
                }
            }
            if(!ObjectUtils.isEmpty(mapList.get("table3"))){
                for(int i=0;i<mapList.get("table3").size();i++){
                    Map map= (Map) mapList.get("table3").get(i);
                    map.put("recordTime",DateUtil.getDateFormat((String)map.get("recordTime")));
                    map.remove("creatTime");
                    map.put("lastUpdTime",DateUtil.getDateFormat(DateUtil.getDateFormat(new Date())));
                    TrafficPeakLineDailyPo trafficPeakLineDailyPo=dozerMapper.map(mapList.get("table3").get(i),TrafficPeakLineDailyPo.class);
                    trafficPeakLineDailyPos.add(trafficPeakLineDailyPo);
                    ls.add(DateUtil.getDateFormat(trafficPeakLineDailyPo.getRecordTime()));
                }
            }
            if(!ObjectUtils.isEmpty(mapList.get("table4"))){
                List<List<String>> lists=Lists.newArrayList();
                for(int i=0;i<mapList.get("table4").size();i++){
                    List<String>list=Lists.newArrayList();
                    Map map= (Map) mapList.get("table4").get(i);
                    //获取几号线
                    String line=map.get("line").toString();
                    //获取单程票数据类型
                    String oneWayTicket=map.get("oneWayTicket").toString();//0
                    //获取城市通数据类型
                    String cityAccess=map.get("cityAccess").toString();//1
                    //获取工作票数据类型
                    String workTicket=map.get("workTicket").toString();//2
                    //获取计次票数据类型
                    String countTicket=map.get("countTicket").toString();//3
                    //获取储蓄票数据类型
                    String storedTicket=map.get("storedTicket").toString();//4
                    //获取人脸识别数据类型
                    String facePay=map.get("facePay").toString();//5
                    list.add(line);list.add(oneWayTicket);list.add(cityAccess);list.add(workTicket);list.add(countTicket);
                    list.add(storedTicket);list.add(facePay);lists.add(list);
                }
                for(TrafficLineTicketDailyVo vo:trafficLineTicketDailyVos){
                    //一号线数据
                    if(vo.getLine().equals("1")){
                        if(vo.getTicketType().equals(new BigDecimal("0"))){//单行票
                            vo.setTrafficVolume(new BigDecimal(lists.get(0).get(1)));
                            vo.setRate(new BigDecimal(lists.get(1).get(1)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("1"))){//城市通
                            vo.setTrafficVolume(new BigDecimal(lists.get(0).get(2)));
                            vo.setRate(new BigDecimal(lists.get(1).get(2)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("2"))){//工作票
                            vo.setTrafficVolume(new BigDecimal(lists.get(0).get(3)));
                            vo.setRate(new BigDecimal(lists.get(1).get(3)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("3"))){//计次纪念票
                            vo.setTrafficVolume(new BigDecimal(lists.get(0).get(4)));
                            vo.setRate(new BigDecimal(lists.get(1).get(4)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("4"))){//储值票
                            vo.setTrafficVolume(new BigDecimal(lists.get(0).get(5)));
                            vo.setRate(new BigDecimal(lists.get(1).get(5)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("5"))){//人脸支付
                            vo.setTrafficVolume(new BigDecimal(lists.get(0).get(6)));
                            vo.setRate(new BigDecimal(lists.get(1).get(6)));
                        }
                    }
                    //三号线数据
                    if(vo.getLine().equals("3")){
                        if(vo.getTicketType().equals(new BigDecimal("0"))){
                            vo.setTrafficVolume(new BigDecimal(lists.get(2).get(1)));
                            vo.setRate(new BigDecimal(lists.get(3).get(1)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("1"))){
                            vo.setTrafficVolume(new BigDecimal(lists.get(2).get(2)));
                            vo.setRate(new BigDecimal(lists.get(3).get(2)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("2"))){
                            vo.setTrafficVolume(new BigDecimal(lists.get(2).get(3)));
                            vo.setRate(new BigDecimal(lists.get(3).get(3)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("3"))){
                            vo.setTrafficVolume(new BigDecimal(lists.get(2).get(4)));
                            vo.setRate(new BigDecimal(lists.get(3).get(4)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("4"))){
                            vo.setTrafficVolume(new BigDecimal(lists.get(2).get(5)));
                            vo.setRate(new BigDecimal(lists.get(3).get(5)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("5"))){
                            vo.setTrafficVolume(new BigDecimal(lists.get(2).get(6)));
                            vo.setRate(new BigDecimal(lists.get(3).get(6)));
                        }
                    }
                    //线网数据
                    if(vo.getLine().equals("0")){
                        if(vo.getTicketType().equals(new BigDecimal("0"))){
                            vo.setTrafficVolume(new BigDecimal(lists.get(4).get(1)));
                            vo.setRate(new BigDecimal(lists.get(5).get(1)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("1"))){
                            vo.setTrafficVolume(new BigDecimal(lists.get(4).get(2)));
                            vo.setRate(new BigDecimal(lists.get(5).get(2)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("2"))){
                            vo.setTrafficVolume(new BigDecimal(lists.get(4).get(3)));
                            vo.setRate(new BigDecimal(lists.get(5).get(3)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("3"))){
                            vo.setTrafficVolume(new BigDecimal(lists.get(4).get(4)));
                            vo.setRate(new BigDecimal(lists.get(5).get(4)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("4"))){
                            vo.setTrafficVolume(new BigDecimal(lists.get(4).get(5)));
                            vo.setRate(new BigDecimal(lists.get(5).get(5)));
                        }
                        if(vo.getTicketType().equals(new BigDecimal("5"))){
                            vo.setTrafficVolume(new BigDecimal(lists.get(4).get(6)));
                            vo.setRate(new BigDecimal(lists.get(5).get(6)));
                        }
                    }
                    TrafficLineTicketDailyPo trafficLineTicketDailyPo=dozerMapper.map(vo,TrafficLineTicketDailyPo.class);
                    trafficLineTicketDailyPos.add(trafficLineTicketDailyPo);
                    ls.add(DateUtil.getDateFormat(trafficLineTicketDailyPo.getRecordTime()));
                }
            }
        }
        if(trafficLineDailyPos.size()!=0){
            trafficLineDailyService.updateTrafficLineDailyPos(trafficLineDailyPos,id);
        }
        if(trafficStationDailyPos.size()!=0){
            trafficStationDailyService.updateStationDailyPos(trafficStationDailyPos,DateUtil.getDateFormat(recordTime));
        }
        if(trafficPeakLineDailyPos.size()!=0){
            trafficPeakLineDailyService.updatePeakLineDailyPos(trafficPeakLineDailyPos);
        }
        if(trafficLineTicketDailyPos.size()!=0){
            trafficLineTicketDailyService.updateLineTicketDailyPos(trafficLineTicketDailyPos,DateUtil.getDateFormat(recordTime));
        }
        this.setAcmLogger(new AcmLogger("1,3号线"+ls.get(0)+"号客运数据修改",ls.get(0),"1,3",
                String.valueOf(id),"INIT"));
        return queryTrafficChildrenList(ls.get(0));
    }

    @Override
    public void updateTrafficMainPo(TrafficMainPo trafficMainPo,Date recordTime) {
        trafficMainPo.setRecordTime(recordTime);
        super.updateSelectiveById(trafficMainPo);
    }

    //删除固定日期（交易日期）以及本表数据
    private void delAllTrafficSheet(List<Integer> listId, List<String> recordTimeList) {
        //根据recordTime删除众多字表数据
        //子表、odr_traffic_station_daily
        trafficStationDailyService.delTrafficStationDailyByDates(recordTimeList);
        //子表、odr_traffic_station_monthly
        trafficStationMonthlyService.delTrafficStationMonthlyByDates(recordTimeList);
        //子表、odr_traffic_peak_station_daily
        //trafficPeakStationDailyService.delTrafficPeakStationDailyByDates(recordTimeList);
        //子表、odr_traffic_peak_station_monthly,暂时没有月度表
        //trafficPeakStationMonthlyService.delTrafficPeakStationMonthlyByDates(recordTimeList);
        //子表、odr_traffic_peak_station_daily
        trafficPeakLineDailyService.delTrafficPeakLineDailyByDates(recordTimeList);
        //子表、odr_traffic_line_ticket_daily
        trafficLineTicketDailyService.delTrafficLineTicketDailyByDates(recordTimeList);
        //子表、odr_traffic_line_ticket_monthly
        trafficLineTicketMonthlyService.delTrafficLineTicketMonthlyByDates(recordTimeList);
        //子表、odr_traffic_line_daily
        trafficLineDailyService.delTrafficLineByDates(recordTimeList);
        //子表、odr_traffic_line_monthly
        trafficLineMonthlyService.delTrafficLineMonthlyByDates(recordTimeList);
        if(listId!=null){
            //删除主表数据
            this.deleteByIds(listId);
        }else{
            //删除主表也可以根据日期
            mapper.delTrafficMainByDates(recordTimeList);
        }
    }

    /**
     * 导入数据来源excel
     * @param multipartFile
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @AddLog(title = "导入客运日况数据", module = LoggerModuleEnum.TRAFFIC_DAILYREPORT)
    public String uploadTrafficMainFile(MultipartFile multipartFile,Integer userId) {
        if (multipartFile.isEmpty()) {
            throw new BaseException("文件不能为空!");
        }
        String fileName = multipartFile.getOriginalFilename();//文件名
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);//文件类型
        if (!"xlsx".equals(ext)) {
            throw new BaseException("文件格式不支持!");
        }
        Workbook wb = null;
        try{
            wb = ExcelUtil.getWorkbook(multipartFile);
            Sheet sheet = ExcelUtil.getSheet(wb, 0);//获取第一页的工作簿
            //定义错误日志
            ExcelError excelError = new ExcelError();
            //交易日期
            String date=generateDate(excelError,sheet);
            //从数据库中查询若非新建不可以导入
            List<TrafficMainVo> vos=mapper.queryTrafficMainByDate(date);
            if(vos.size()!=0){
                for(TrafficMainVo vo:vos){
                    if(!vo.getStatusVo().getCode().equals("INIT")){
                        return "交易日期为： "+date+"数据已经添加，且已审批，不可以再次添加！";
                    }
                }
            }
            //插入之前首先删除本日的数据
            delAllTrafficSheet(null, Collections.singletonList(date));
            //从全线网客运量高峰统计情况中获取数据
            generateTrafficMains(excelError,sheet,userId,date);
            //从全线网各车站进出站客运量统计情况（客运量单位：人次）中获取数据
            generateTrafficStations(excelError,sheet,userId,date);
            //从全线网客运量高峰统计情况中获取数据
            generateTrafficPeakLines(excelError,sheet,userId,date);
            //全线网客运量分类统计情况
            generateTrafficLineTickets(excelError,sheet,userId,date);
            Example example = new Example(TrafficMainPo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("recordTime", date);
            this.setAcmLogger(new AcmLogger("1,3号线"+date+"号客运数据导入",date,"1,3",
                    this.selectOneByExample(example).getId().toString(),"INIT"));
        } catch (Exception e) {
            throw new BaseException("导入错误!", e);
        }
        return "成功";
    }

    private String generateDate(ExcelError excelError, Sheet sheet) {
        List<Map<String, Object>> dataList = ExcelUtil.getSheetValue(sheet, 5,6);
        String date="";
        for (int i = 0; i < dataList.size(); i++)
        {
            Map<String, Object> datamap = dataList.get(i);
            excelError.addRow(Integer.valueOf(String.valueOf(datamap.get("rowIndex")))+1);

            date = String.valueOf(datamap.get("12"));
            if (StringHelper.isNullAndEmpty(date))
                excelError.addError(0, "交易日期", "不能为空");
        }
        if(date.indexOf(":") >1)
        {
            date=date.substring(date.indexOf(":") + 1);
        }
        else
        {
            date=date.substring(date.indexOf("：") + 1);
        }

        return date;
    }

    //全线网客运量分类统计情况,取列数据
    private void generateTrafficLineTickets(ExcelError excelError, Sheet sheet, Integer userId, String date)
    {
        List<TrafficLineTicketDailyPo>trafficLineTicketDailyPos=Lists.newArrayList();
        //取出来缓存之后再存进数据库
        List<List<List<BigDecimal>>>lsBig=Lists.newArrayList();
        List<BigDecimal>listB=Lists.newArrayList();
        List<BigDecimal>listOnly=Lists.newArrayList();
        List<BigDecimal>listCity=Lists.newArrayList();
        List<BigDecimal>listWork=Lists.newArrayList();
        List<BigDecimal>listCount=Lists.newArrayList();
        List<BigDecimal>listSave=Lists.newArrayList();
        List<BigDecimal>listFace=Lists.newArrayList();
        for(int i=64;i<=69;i++){
            //存储所有取出的数据
            List<Map<String, Object>> dataList = new ArrayList<>();
            BigDecimal onlyTraffic=new BigDecimal("0");
            if(i==64){
                dataList =ExcelUtil.getSheetValue(sheet, i,i+1);;
                // ExcelUtil.getSheetValue(sheet, i,70);
                //取每一行数据之和
                Map<String, Object> datamap = dataList.get(0);
                onlyTraffic =new BigDecimal(String.valueOf(datamap.get("14")));
                BigDecimal cityTraffic =new BigDecimal(String.valueOf(datamap.get("19")));
                BigDecimal workTraffic =new BigDecimal(String.valueOf(datamap.get("25")));
                BigDecimal countTraffic =new BigDecimal(String.valueOf(datamap.get("33")));
                BigDecimal saveTraffic =new BigDecimal(String.valueOf(datamap.get("41")));
                BigDecimal faceTraffic =new BigDecimal("0");
                if(!StringUtils.isEmpty(datamap.get("45")))
                {
                    faceTraffic =new BigDecimal(String.valueOf(datamap.get("45")));
                }
                listB.add(onlyTraffic.add(cityTraffic).add(workTraffic).add(countTraffic).add(saveTraffic).add(faceTraffic));
            }
            if(i==66){
                dataList =ExcelUtil.getSheetValue(sheet, i,i+1);;
                // ExcelUtil.getSheetValue(sheet, i,70);
                //取每一行数据之和
                Map<String, Object> datamap = dataList.get(0);
                onlyTraffic =new BigDecimal(String.valueOf(datamap.get("14")));

                BigDecimal cityTraffic =new BigDecimal(String.valueOf(datamap.get("19")));
                BigDecimal workTraffic =new BigDecimal(String.valueOf(datamap.get("25")));
                BigDecimal countTraffic =new BigDecimal(String.valueOf(datamap.get("33")));
                BigDecimal saveTraffic =new BigDecimal(String.valueOf(datamap.get("41")));
                BigDecimal faceTraffic =new BigDecimal("0");
                if(!StringUtils.isEmpty(datamap.get("45")))
                {
                    faceTraffic =new BigDecimal(String.valueOf(datamap.get("45")));
                }
                listB.add(onlyTraffic.add(cityTraffic).add(workTraffic).add(countTraffic).add(saveTraffic).add(faceTraffic));
            }
            if(i==68){
                dataList =ExcelUtil.getSheetValue(sheet, i,i+1);;
                // ExcelUtil.getSheetValue(sheet, i,70);
                //取每一行数据之和
                Map<String, Object> datamap = dataList.get(0);
                onlyTraffic =new BigDecimal(String.valueOf(datamap.get("14")));

                BigDecimal cityTraffic =new BigDecimal(String.valueOf(datamap.get("19")));
                BigDecimal workTraffic =new BigDecimal(String.valueOf(datamap.get("25")));
                BigDecimal countTraffic =new BigDecimal(String.valueOf(datamap.get("33")));
                BigDecimal saveTraffic =new BigDecimal(String.valueOf(datamap.get("41")));
                BigDecimal faceTraffic =new BigDecimal("0");
                if(!StringUtils.isEmpty(datamap.get("45")))
                {
                    faceTraffic =new BigDecimal(String.valueOf(datamap.get("45")));
                }
                listB.add(onlyTraffic.add(cityTraffic).add(workTraffic).add(countTraffic).add(saveTraffic).add(faceTraffic));
            }
            listOnly.add(onlyTraffic);
            if(i==64 ||i==66 ||i==68  )
            {
                dataList =ExcelUtil.getSheetValue(sheet, i,i+1);;
                // ExcelUtil.getSheetValue(sheet, i,70);
                //取每一行数据之和
                Map<String, Object> datamap = dataList.get(0);
                onlyTraffic =new BigDecimal(String.valueOf(datamap.get("14")));
                //城市通数据缓存
                BigDecimal cityTraffic =new BigDecimal(String.valueOf(datamap.get("19")));
                listCity.add(cityTraffic);
                //工作票数据缓存
                BigDecimal workTraffic =new BigDecimal(String.valueOf(datamap.get("25")));
                listWork.add(workTraffic);
                //计次纪念票
                BigDecimal countTraffic =new BigDecimal(String.valueOf(datamap.get("33")));
                listCount.add(countTraffic);
                //储值票
                BigDecimal saveTraffic =new BigDecimal(String.valueOf(datamap.get("41")));
                listSave.add(saveTraffic);
                //人脸支付票
                BigDecimal faceTraffic =new BigDecimal("0");
                if(!StringUtils.isEmpty(datamap.get("45")))
                {
                    faceTraffic =new BigDecimal(String.valueOf(datamap.get("45")));
                }
                listFace.add(faceTraffic);
            }
            else
            {
                //城市通数据缓存
                BigDecimal cityTraffic =new BigDecimal("0");
                listCity.add(cityTraffic);
                //工作票数据缓存
                BigDecimal workTraffic =new BigDecimal("0");
                listWork.add(workTraffic);
                //计次纪念票
                BigDecimal countTraffic =new BigDecimal("0");
                listCount.add(countTraffic);
                //储值票
                BigDecimal saveTraffic =new BigDecimal("0");
                listSave.add(saveTraffic);
                //人脸支付票
                BigDecimal faceTraffic =new BigDecimal("0");
                listFace.add(faceTraffic);
            }

            if(i==69){
                List<List<BigDecimal>> ls=Lists.newArrayList();
                ls.add(listOnly);
                ls.add(listCity);
                ls.add(listWork);
                ls.add(listCount);
                ls.add(listSave);
                ls.add(listFace);
                lsBig.add(ls);
            }
        }
        //从缓存中取出数据存入数据库中
        for(List<List<BigDecimal>> ls : lsBig){
            int k = 0;
            for(List<BigDecimal> ks :ls ) {
                for(int i=0;i<=ks.size();i++){
                    if(i==0){//哈尔滨地铁1号线票卡
                        TrafficLineTicketDailyPo trafficLineTicketDailyPo1=new TrafficLineTicketDailyPo();
                        trafficLineTicketDailyPo1.setTicketType(new BigDecimal(String.valueOf(k)));
                        trafficLineTicketDailyPo1.setCreator(userId);
                        trafficLineTicketDailyPo1.setCreatTime(new Date());
                        trafficLineTicketDailyPo1.setLine("1");
                        trafficLineTicketDailyPo1.setTrafficVolume(new BigDecimal(String.valueOf(ks.get(0))));
                        //trafficLineTicketDailyPo1.setRate(new BigDecimal(String.valueOf(ks.get(1).multiply(new BigDecimal("100")))));
                        //计算比例
                        if(!StringUtils.isEmpty(listB.get(0)))
                        {
                            BigDecimal total= new BigDecimal(String.valueOf(String.valueOf(ks.get(0))));
                            BigDecimal rate = total.divide(new BigDecimal(String.valueOf(listB.get(0))), 4, BigDecimal.ROUND_HALF_UP);
                            trafficLineTicketDailyPo1.setRate(rate.multiply(new BigDecimal("100")));
                        }
                        else
                        {
                            trafficLineTicketDailyPo1.setRate(new BigDecimal("0"));
                        }
                        trafficLineTicketDailyPo1.setTotalTrafficVolume(listB.get(0));
                        trafficLineTicketDailyPo1.setRecordTime(DateUtil.getDateFormat(date));
                        trafficLineTicketDailyPos.add(trafficLineTicketDailyPo1);
                    }
                    if(i==2){//哈尔滨地铁3号线票卡
                        TrafficLineTicketDailyPo trafficLineTicketDailyPo2=new TrafficLineTicketDailyPo();
                        trafficLineTicketDailyPo2.setTicketType(new BigDecimal(String.valueOf(k)));
                        trafficLineTicketDailyPo2.setCreator(userId);
                        trafficLineTicketDailyPo2.setCreatTime(new Date());
                        trafficLineTicketDailyPo2.setLine("3");
                        trafficLineTicketDailyPo2.setTrafficVolume(new BigDecimal(String.valueOf(ks.get(2))));
                        //trafficLineTicketDailyPo2.setRate(new BigDecimal(String.valueOf(ks.get(3).multiply(new BigDecimal("100")))));
                        //计算比例
                        if(!StringUtils.isEmpty(listB.get(1)))
                        {
                            BigDecimal total= new BigDecimal(String.valueOf(String.valueOf(ks.get(2))));
                            BigDecimal rate = total.divide(new BigDecimal(String.valueOf(listB.get(1))), 4, BigDecimal.ROUND_HALF_UP);
                            trafficLineTicketDailyPo2.setRate(rate.multiply(new BigDecimal("100")));
                        }
                        else
                        {
                            trafficLineTicketDailyPo2.setRate(new BigDecimal("0"));
                        }
                        trafficLineTicketDailyPo2.setTotalTrafficVolume(listB.get(1));
                        trafficLineTicketDailyPo2.setRecordTime(DateUtil.getDateFormat(date));
                        trafficLineTicketDailyPos.add(trafficLineTicketDailyPo2);
                    }
                    if(i==4){//哈尔滨地铁线网
                        TrafficLineTicketDailyPo trafficLineTicketDailyPo3=new TrafficLineTicketDailyPo();
                        trafficLineTicketDailyPo3.setTicketType(new BigDecimal(String.valueOf(k)));
                        trafficLineTicketDailyPo3.setCreator(userId);
                        trafficLineTicketDailyPo3.setCreatTime(new Date());
                        trafficLineTicketDailyPo3.setLine("0");
                        trafficLineTicketDailyPo3.setTrafficVolume(new BigDecimal(String.valueOf(ks.get(4))));
                        //trafficLineTicketDailyPo3.setRate(new BigDecimal(String.valueOf(ks.get(5).multiply(new BigDecimal("100")))));
                        //计算比例
                        if(!StringUtils.isEmpty(listB.get(2)))
                        {
                            BigDecimal total= new BigDecimal(String.valueOf(String.valueOf(ks.get(4))));
                            BigDecimal rate = total.divide(new BigDecimal(String.valueOf(listB.get(2))), 4, BigDecimal.ROUND_HALF_UP);
                            trafficLineTicketDailyPo3.setRate(rate.multiply(new BigDecimal("100")));
                        }
                        else
                        {
                            trafficLineTicketDailyPo3.setRate(new BigDecimal("0"));
                        }
                        trafficLineTicketDailyPo3.setTotalTrafficVolume(listB.get(2));
                        trafficLineTicketDailyPo3.setRecordTime(DateUtil.getDateFormat(date));
                        trafficLineTicketDailyPos.add(trafficLineTicketDailyPo3);
                    }

                }
                k++;
            }
        }
        trafficLineTicketDailyService.insertTrafficLineTicketDaily(trafficLineTicketDailyPos);
        //月累计统计
        String yearMonth= DateUtil.getDateFormat(DateUtil.getDateFormat(date),"yyyy-MM");
        List<TrafficLineTicketDailyVo> trafficLineTicketDailyVoList=
                trafficLineTicketDailyService.queryTrafficLineTicketDailys(null,yearMonth);
        List<TrafficLineTicketMonthlyPo> trafficLineTicketMonthlyPos=Lists.newArrayList();
        if(!ObjectUtils.isEmpty(trafficLineTicketDailyVoList)){
            for(TrafficLineTicketDailyVo vo :trafficLineTicketDailyVoList){
                TrafficLineTicketMonthlyPo trafficLineTicketMonthlyPo=new TrafficLineTicketMonthlyPo();
                trafficLineTicketMonthlyPo.setCreator(userId);
                trafficLineTicketMonthlyPo.setRecordTime(DateUtil.getDateFormat(date));
                trafficLineTicketMonthlyPo.setLine(vo.getLine());
                trafficLineTicketMonthlyPo.setLinePeriod(vo.getLinePeriod());
                trafficLineTicketMonthlyPo.setRate(vo.getRate());
                trafficLineTicketMonthlyPo.setTicketType(vo.getTicketType());
                trafficLineTicketMonthlyPo.setTotalTrafficVolume(vo.getTotalTrafficVolume());
                trafficLineTicketMonthlyPo.setTrafficVolume(vo.getTrafficVolume());
                trafficLineTicketMonthlyPos.add(trafficLineTicketMonthlyPo);
            }
        }else{
            for(TrafficLineTicketDailyPo po :trafficLineTicketDailyPos){
                TrafficLineTicketMonthlyPo trafficLineTicketMonthlyPo=new TrafficLineTicketMonthlyPo();
                trafficLineTicketMonthlyPo.setCreator(userId);
                trafficLineTicketMonthlyPo.setRecordTime(DateUtil.getDateFormat(date));
                trafficLineTicketMonthlyPo.setLine(po.getLine());
                trafficLineTicketMonthlyPo.setLinePeriod(po.getLinePeriod());
                trafficLineTicketMonthlyPo.setRate(po.getRate());
                trafficLineTicketMonthlyPo.setTicketType(po.getTicketType());
                trafficLineTicketMonthlyPo.setTotalTrafficVolume(po.getTotalTrafficVolume());
                trafficLineTicketMonthlyPo.setTrafficVolume(po.getTrafficVolume());
                trafficLineTicketMonthlyPos.add(trafficLineTicketMonthlyPo);
            }
        }

        //重做逻辑
        List<TrafficLineTicketMonthlyVo>listVos=trafficLineTicketMonthlyService.queryTrafficLineTicketMonthlyVo(yearMonth);
        if(!ObjectUtils.isEmpty(listVos)){
            trafficLineTicketMonthlyService.updateLineTicketMonthly(listVos,trafficLineTicketMonthlyPos);
        }else{
            trafficLineTicketMonthlyService.insertTrafficLineTicketMonthlys(trafficLineTicketMonthlyPos);
        }
    }

    //从全线网客运量高峰统计情况中获取数据
    private void generateTrafficPeakLines(ExcelError excelError, Sheet sheet, Integer userId, String date) {
        TrafficPeakLineDailyPo trafficPeakLineDailyPo =new TrafficPeakLineDailyPo();
        //获取客运线网高峰数据(目前数据中只有线网高峰数据，不存在)
        String line = "";// 线路
        String morningPeakTrafficVolume ="";// 早高峰客运量（人次）
        String eveningPeakTrafficVolume = "";// 晚高峰客运量（人次）
        String transectUpVolume = "";// 高峰小时断面客流（上行）
        String transectDownVolume = "";// 高峰小时断面客流（下行）
        for(int i=57;i<=58;i++){
            List<Map<String, Object>> dataList = ExcelUtil.getSheetValue(sheet, i,59);
            //哈尔滨1号线数据
            Map<String, Object> datamap = dataList.get(0);
                excelError.addRow(Integer.valueOf(String.valueOf(datamap.get("rowIndex"))) + 1);
            if(i==57){
                String lineRead =String.valueOf(datamap.get("3"));
                if("哈尔滨1号线".equals(lineRead)){
                    line="1";
                }else{
                    line="3";
                }
                if (StringHelper.isNullAndEmpty(lineRead))
                    excelError.addError(0, "哈尔滨"+line+ "号线","线路名称不能为空");
                morningPeakTrafficVolume =String.valueOf(datamap.get("5"));
                if (StringHelper.isNullAndEmpty(morningPeakTrafficVolume))
                        excelError.addError(0, "哈尔滨"+line+ "号线", "早高峰客运量不能为空");
                eveningPeakTrafficVolume =String.valueOf(datamap.get("16"));
                if (StringHelper.isNullAndEmpty(eveningPeakTrafficVolume))
                        excelError.addError(0, "哈尔滨"+line+ "号线", "晚高峰客运量不能为空");

                transectUpVolume =String.valueOf(datamap.get("23"));
                if (StringHelper.isNullAndEmpty(transectUpVolume))
                        excelError.addError(0, "哈尔滨"+line+ "号线", "高峰小时断面客流不能为空");

                trafficPeakLineDailyPo.setMorningPeakTrafficVolume(new BigDecimal(morningPeakTrafficVolume));
                trafficPeakLineDailyPo.setEveningPeakTrafficVolume(new BigDecimal(eveningPeakTrafficVolume));
                trafficPeakLineDailyPo.setCreator(userId);
                trafficPeakLineDailyPo.setLine(line);
                trafficPeakLineDailyPo.setRecordTime(DateUtil.getDateFormat(date));
                trafficPeakLineDailyPo.setCreatTime(new Date());
                Matcher m1 =Pattern.compile(":(.*)\\(").matcher(transectUpVolume);
             while (m1.find()){
                 trafficPeakLineDailyPo.setTransectUpVolume(new BigDecimal(m1.group(1).trim()));
                 }
             }
            else {
                transectDownVolume =String.valueOf(datamap.get("23"));
                if (StringHelper.isNullAndEmpty(transectDownVolume))
                        excelError.addError(0, "哈尔滨"+line+ "号线", "高峰小时断面客流不能为空");
                 Matcher m2 =Pattern.compile(":(.*)\\(").matcher(transectDownVolume);
                while (m2.find()){
                    trafficPeakLineDailyPo.setTransectDownVolume(new BigDecimal(m2.group(1).trim()));
                }
                trafficPeakLineDailyService.insertTrafficPeakLineDaily(trafficPeakLineDailyPo);
                //逻辑查出本月数据
                String yearMonth= DateUtil.getDateFormat(DateUtil.getDateFormat(date),"yyyy-MM");
                List<TrafficPeakLineDailyVo> queryTrafficPeakLineDailyVos=
                        trafficPeakLineDailyService.queryTrafficPeakLineDailyVos(yearMonth);
                //缺乏高峰值月度表，需要与李洋讨论


             }
        }
    }

    public static void main(String[]args){
//        //截取中间数字
//        String str="下行:9192 (17时~18时哈工大至西大桥)";
//        Matcher matcher =Pattern.compile(":(.*)\\(").matcher(str);
//        while (matcher.find()){
//            System.out.println(matcher.group(1).trim());
//        }
//        String strR="交易日期 :2019-12-18";
//        String newStr = strR.substring(str.indexOf(":") + 1);
//        System.out.println(newStr);
        List list = Arrays.asList("one Two three Four five six one three Four".split(" "));
        System.out.println("List :"+list);
        Collections.replaceAll(list, "one", "hundrea");
        System.out.println("replaceAll: " + list);
    }

    //全线网各车站进出站客运量统计情况（客运量单位：人次）
    private void generateTrafficStations(ExcelError excelError, Sheet sheet, Integer userId, String date) {
        //客站表数据
        List<TrafficStationDailyPo> insertTrafficStationDailys = Lists.newArrayList();
        List<TrafficStationMonthlyPo> insertTrafficStationMonthlys = Lists.newArrayList();
        for(int i=22;i<=49;i++){
            String line = "1";// 线路
            String stationNum="";//车站编号
            String station = "";//车站名称
            String arrivalVolume ="";// 进站（万人次）
            String outboundVolume = "";// 出站（万人次）
            String totalVolume = "";// 进站量+出站量（人次）
            List<Map<String, Object>> dataList = ExcelUtil.getSheetValue(sheet, i,50);
            if(i==45){
                continue;
            }
            Map<String, Object> datamap = dataList.get(0);
            excelError.addRow(Integer.valueOf(String.valueOf(datamap.get("rowIndex"))) + 1);
            stationNum =String.valueOf(datamap.get("7"));
            if (StringHelper.isNullAndEmpty(stationNum))
                excelError.addError(0, "车站编号", "不能为空");
            station =String.valueOf(datamap.get("13"));
            if (StringHelper.isNullAndEmpty(station))
                    excelError.addError(0, "站名", "不能为空");
            arrivalVolume =String.valueOf(datamap.get("21"));
            if (StringHelper.isNullAndEmpty(arrivalVolume))
                    excelError.addError(0, "进站量", "不能为空");
            outboundVolume =String.valueOf(datamap.get("29"));
            if (StringHelper.isNullAndEmpty(outboundVolume))
                    excelError.addError(0, "出站量", "不能为空");
            totalVolume =String.valueOf(datamap.get("36"));
            if (StringHelper.isNullAndEmpty(totalVolume))
                    excelError.addError(0, "进站量+出站量", "不能为空");

            //客站表数据
            TrafficStationDailyPo trafficStationDailyPo =new TrafficStationDailyPo();
            trafficStationDailyPo.setArrivalVolume(new BigDecimal(arrivalVolume));
            line="1";
            if(i>=46){
                if(i<=49){
                    line="3";
                }
            }
            trafficStationDailyPo.setLine(line);
            trafficStationDailyPo.setOutboundVolume(new BigDecimal(outboundVolume));
            trafficStationDailyPo.setTotalVolume(new BigDecimal(totalVolume));
            trafficStationDailyPo.setRecordTime(DateUtil.getDateFormat(date));
            trafficStationDailyPo.setCreator(userId);
            trafficStationDailyPo.setCreatTime(new Date());
            trafficStationDailyPo.setStation(station);
            trafficStationDailyPo.setStationNum(stationNum);
            insertTrafficStationDailys.add(trafficStationDailyPo);
        }
        trafficStationDailyService.insertStationDaily(insertTrafficStationDailys);
        //查询出每个站对应月的汇总，累计，日均量，然后插入odr_traffic_station_monthly表中
        String yearMonth= DateUtil.getDateFormat(DateUtil.getDateFormat(date),"yyyy-MM");
        List<TrafficStationDailyVo> queryTrafficStationDailyVoList=
                trafficStationDailyService.queryTrafficStationDailys(yearMonth);
        if(!ObjectUtils.isEmpty(queryTrafficStationDailyVoList)){
            for(TrafficStationDailyVo vo:queryTrafficStationDailyVoList){
                TrafficStationMonthlyPo trafficStationMonthlyPo=new TrafficStationMonthlyPo();
                trafficStationMonthlyPo.setArrivalVolume(vo.getArrivalVolume());
                trafficStationMonthlyPo.setArrivalVolumeAverage(vo.getArrivalVolumeAverage());
                trafficStationMonthlyPo.setLine(vo.getLine());
                trafficStationMonthlyPo.setLinePeriod(vo.getLinePeriod());
                trafficStationMonthlyPo.setOutboundVolume(vo.getOutboundVolume());
                trafficStationMonthlyPo.setOutboundVolumeAverage(vo.getOutboundVolumeAverage());
                trafficStationMonthlyPo.setTotalVolume(vo.getTotalVolume());
                trafficStationMonthlyPo.setStation(vo.getStation());
                trafficStationMonthlyPo.setRecordTime(vo.getRecordTime());
                trafficStationMonthlyPo.setStationNum(vo.getStationNum());
                insertTrafficStationMonthlys.add(trafficStationMonthlyPo);
            }
        }else{
            for(TrafficStationDailyPo po:insertTrafficStationDailys){
                TrafficStationMonthlyPo trafficStationMonthlyPo=new TrafficStationMonthlyPo();
                trafficStationMonthlyPo.setArrivalVolume(po.getArrivalVolume());
                trafficStationMonthlyPo.setArrivalVolumeAverage(po.getArrivalVolume());
                trafficStationMonthlyPo.setLine(po.getLine());
                trafficStationMonthlyPo.setLinePeriod(po.getLinePeriod());
                trafficStationMonthlyPo.setOutboundVolume(po.getOutboundVolume());
                trafficStationMonthlyPo.setOutboundVolumeAverage(po.getOutboundVolume());
                trafficStationMonthlyPo.setTotalVolume(po.getTotalVolume());
                trafficStationMonthlyPo.setStation(po.getStation());
                trafficStationMonthlyPo.setRecordTime(po.getRecordTime());
                trafficStationMonthlyPo.setStationNum(po.getStationNum());
                insertTrafficStationMonthlys.add(trafficStationMonthlyPo);
            }
        }

        //查询本月累计数据
        //本月27条数据
        List<TrafficStationMonthlyVo>listVos=trafficStationMonthlyService.queryTrafficStationMonthly(yearMonth);
        if(!ObjectUtils.isEmpty(listVos)){
            trafficStationMonthlyService.updateTrafficStationMonthly(listVos,insertTrafficStationMonthlys);
        }else{
            trafficStationMonthlyService.insertStationMonthly(insertTrafficStationMonthlys);
        }
    }

    //全线网客运量情况
    private void generateTrafficMains(ExcelError excelError, Sheet sheet, Integer userId, String date){
        //主表数据
        List<TrafficMainPo> insertTrafficMainPos = Lists.newArrayList();
        //客运线路表数据
        List<TrafficLineDailyPo> insertTrafficLineDailyPos = Lists.newArrayList();
        //客运线路月累计数据
        List<TrafficLineMonthlyPo> insertTrafficLineMonthlyPos = Lists.newArrayList();
        //16行是哈1号线数据，17行是3线数据，18行为主表数据
        for(int i = 15; i <= 17; i++){
            String line = "";// 线路
            String arrivalVolume ="";// 进站（万人次）
            String outboundVolume = "";// 出站（万人次）
            String transferVolume ="";// 换入量（万人次）
            String trafficVolume = "";// 客运量（万人次）
            String trafficVolumeMonth = "";// 月累计客运量（万人次）
            String trafficVolumeMonthAverage ="";// 本月日均客运量（万人次）
            String trafficVolumeYear = "";// 年累计客运量（万人次）
            String trafficVolumeYearAverage = "";// 年累计日均客运量（万人次）
            List<Map<String, Object>> dataList = ExcelUtil.getSheetValue(sheet, i,18);
            Map<String, Object> datamap = dataList.get(0);
            arrivalVolume =String.valueOf(datamap.get("7"));
            if (StringHelper.isNullAndEmpty(arrivalVolume))
                    excelError.addError(0, "进站量", "进站量数据不能为空");
            outboundVolume =String.valueOf(datamap.get("13"));
            if (StringHelper.isNullAndEmpty(outboundVolume))
                    excelError.addError(0, "出站站", "数据不能为空");
            transferVolume =String.valueOf(datamap.get("18"));
            if (StringHelper.isNullAndEmpty(transferVolume))
                    excelError.addError(0, "换人量", "数据不能为空");
            trafficVolume =String.valueOf(datamap.get("21"));
            if (StringHelper.isNullAndEmpty(trafficVolume))
                    excelError.addError(0, "客运量", "数据不能为空");
            trafficVolumeMonth =String.valueOf(datamap.get("26"));
            if (StringHelper.isNullAndEmpty(trafficVolumeMonth))
                    excelError.addError(0, "月累计客运量", "数据不能为空");
            trafficVolumeMonthAverage =String.valueOf(datamap.get("31"));
            if (StringHelper.isNullAndEmpty(trafficVolumeMonthAverage))
                    excelError.addError(0, "本月日均客运量", "数据不能为空");
            trafficVolumeYear =String.valueOf(datamap.get("35"));
            if (StringHelper.isNullAndEmpty(trafficVolumeYear))
                    excelError.addError(0, "年累计客运量", "数据不能为空");
            trafficVolumeYearAverage =String.valueOf(datamap.get("43"));
            if (StringHelper.isNullAndEmpty(trafficVolumeYearAverage))
                    excelError.addError(0, "年累计日均客运量", "数据不能为空");
            if(i==15){
                    line="1";
            }
            if(i==16){
                    line="3";
            }
            if(i==17){
                    line="0";//线网
            }

            //插入主表数据main
            if(i==17){
                TrafficMainPo trafficMainPo=new TrafficMainPo();
                trafficMainPo.setCreator(userId);
                trafficMainPo.setCreatTime(new Date());
                trafficMainPo.setRecordTime(DateUtil.getDateFormat(date));
                trafficMainPo.setTrafficVolumeToday(new BigDecimal(trafficVolume));
                trafficMainPo.setTrafficVolumeMonth(new BigDecimal(trafficVolumeMonth));
                trafficMainPo.setTrafficVolumeMonthAverage(new BigDecimal(trafficVolumeMonthAverage));
                trafficMainPo.setTrafficVolumeYear(new BigDecimal(trafficVolumeYear));
                trafficMainPo.setTrafficVolumeYearAverage(new BigDecimal(trafficVolumeYearAverage));
                trafficMainPo.setTrafficVolumeOpen(new BigDecimal("0"));
                trafficMainPo.setTrafficVolumeOpenAverage(new BigDecimal("0"));
                trafficMainPo.setStatus("INIT");
                //开通至今累计
                insertTrafficMainPos.add(trafficMainPo);
            }
                //注入线路客运量，按车站汇总
                TrafficLineDailyPo trafficLineDailyPo =new TrafficLineDailyPo();
                trafficLineDailyPo.setLine(line);
                trafficLineDailyPo.setRecordTime(DateUtil.getDateFormat(date));
                trafficLineDailyPo.setArrivalVolume(new BigDecimal(arrivalVolume));
                trafficLineDailyPo.setOutboundVolume(new BigDecimal(outboundVolume));
                trafficLineDailyPo.setTransferVolume(new BigDecimal(transferVolume));
                trafficLineDailyPo.setTrafficVolume(new BigDecimal(trafficVolume));
                trafficLineDailyPo.setTrafficVolumeMonth(new BigDecimal(trafficVolumeMonth));
                trafficLineDailyPo.setTrafficVolumeMonthAverage(new BigDecimal(trafficVolumeMonthAverage));
                trafficLineDailyPo.setTrafficVolumeYear(new BigDecimal(trafficVolumeYear));
                trafficLineDailyPo.setTrafficVolumeYearAverage(new BigDecimal(trafficVolumeYearAverage));
                trafficLineDailyPo.setTrafficVolumeOpen(new BigDecimal("0"));
                trafficLineDailyPo.setTrafficVolumeOpenAverage(new BigDecimal("0"));
                trafficLineDailyPo.setCreator(userId);
                trafficLineDailyPo.setCreatTime(new Date());
                insertTrafficLineDailyPos.add(trafficLineDailyPo);

                //注入月累计线路客运量，按日汇总
                TrafficLineMonthlyPo trafficLineMonthlyPo =new TrafficLineMonthlyPo();
                trafficLineMonthlyPo.setLine(line);
                trafficLineMonthlyPo.setRecordTime(DateUtil.getDateFormat(date));
                trafficLineMonthlyPo.setTrafficVolumeMonth(new BigDecimal(trafficVolumeMonth));
                trafficLineMonthlyPo.setTrafficVolumeMonthAverage(new BigDecimal(trafficVolumeMonthAverage));
                trafficLineMonthlyPo.setCreator(userId);
                insertTrafficLineMonthlyPos.add(trafficLineMonthlyPo);
        }

        this.insert(insertTrafficMainPos);
        trafficLineDailyService.insertTrafficLines(insertTrafficLineDailyPos);
        Map<String,Object>map= Maps.newHashMap();
        String yearMonth= DateUtil.getDateFormat(DateUtil.getDateFormat(date),"yyyy-MM");
        map.put("yearMonth",yearMonth);
        map.put("lines",Arrays.asList("1","3","0"));
        List<TrafficLineDailyVo> vos=trafficLineDailyService.queryMaxTrafficVolumeList(map);
        Map<String ,TrafficLineDailyVo>mapList= ListUtil.listToMap(vos,"line",String.class);
        for(TrafficLineMonthlyPo po:insertTrafficLineMonthlyPos){
            if(po.getLine().equals("1")){
                po.setLine("1");
                po.setMaxTrafficVolume(mapList.get("1").getMaxVolume());
                po.setMaxTrafficDate(mapList.get("1").getRecordTime());
            }
            if(po.getLine().equals("3")){
                po.setLine("3");
                po.setMaxTrafficVolume(mapList.get("3").getMaxVolume());
                po.setMaxTrafficDate(mapList.get("3").getRecordTime());
            }
            if(po.getLine().equals("0")){
                po.setLine("0");
                po.setMaxTrafficVolume(mapList.get("0").getMaxVolume());
                po.setMaxTrafficDate(mapList.get("0").getRecordTime());
            }
        }
        //获取线网客运中月数据
        List<TrafficLineMonthlyVo> monthlyVoList=trafficLineMonthlyService.queryTrafficLineMonthlyVo(map);
        if(!ObjectUtils.isEmpty(monthlyVoList)){
            //更新月表
            trafficLineMonthlyService.updateTrafficLineMonthly(monthlyVoList,insertTrafficLineMonthlyPos);
        }else{
            trafficLineMonthlyService.insertTrafficLineMonthly(insertTrafficLineMonthlyPos);
        }
        //获取主表数据，然后写入日志
        Example example = new Example(TrafficMainPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("recordTime", date);
        this.setAcmLogger(new AcmLogger(date,"1,3,0",this.selectByExample(example).get(0).getId().toString(),"INIT"));
    }
}
