package com.wisdom.acm.dc3.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc3.common.DateUtil;
import com.wisdom.acm.dc3.common.SzxmCommonUtil;
import com.wisdom.acm.dc3.common.UUIDHexGenerator;
import com.wisdom.acm.dc3.common.officeUtils.ExcelError;
import com.wisdom.acm.dc3.common.officeUtils.ExcelUtil;
import com.wisdom.acm.dc3.common.redisUtils.RedisUtil;
import com.wisdom.acm.dc3.mapper.EnergyDailyMapper;
import com.wisdom.acm.dc3.po.EnergyDailyPo;
import com.wisdom.acm.dc3.po.EnergyDetailPo;
import com.wisdom.acm.dc3.po.EnergyMonthlyPo;
import com.wisdom.acm.dc3.service.EnergyDailyService;
import com.wisdom.acm.dc3.service.EnergyDetailService;
import com.wisdom.acm.dc3.service.EnergyMonthlyService;
import com.wisdom.acm.dc3.vo.EnergyDailyVo;
import com.wisdom.acm.dc3.vo.EnergyDetailVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.doc.CommDocService;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class EnergyDailyServiceImpl extends BaseService<EnergyDailyMapper, EnergyDailyPo> implements EnergyDailyService
{


    @Autowired
    private CommDocService commDocService;
    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EnergyDetailService energyDetailService;

    @Autowired
    private EnergyMonthlyService energyMonthlyService;



    @Override
    @AddLog(title = "????????????????????????", module = LoggerModuleEnum.ENERGY_DETAILEDSEARCH)
    public void deleteEnergyDaily(Integer id)
    {
        //????????????
        EnergyDailyPo energyDailyPo=super.selectById(id);
        AcmLogger logger = new AcmLogger();
        if(!ObjectUtils.isEmpty(energyDailyPo))
        {
            logger.setLine(energyDailyPo.getLine());
            logger.setRecordTime(DateUtil.getDateFormat(energyDailyPo.getRecordTime()));
            logger.setRecordId(String.valueOf(energyDailyPo.getId()));
            logger.setRecordStatus(energyDailyPo.getReviewStatus());
            this.addDeleteLogger(logger);
        }
            super.deleteById(id);
    }



    @Override
    public EnergyDailyVo selectById(Integer id) {
        EnergyDailyVo trainDailyVo = dozerMapper.map(super.selectById(id), EnergyDailyVo.class);//po???????????????Vo??????
        return trainDailyVo;
    }

    /**
     * ??????????????????EnergyDailyVo ??????
     * @param mapWhere
     * @return
     */
    @Override
    public List<EnergyDailyVo> selectByParams(Map<String, Object> mapWhere) {

        List<EnergyDailyVo>  trainDailyVoList= mapper.selectByParams(mapWhere);
        return trainDailyVoList;
    }

    /**
     * ????????????????????????
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @Override
    public PageInfo<EnergyDailyVo> selectEnergyDailyPageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        PageHelper.startPage(currentPageNum, pageSize);
        List<EnergyDailyVo> trainDailyVoList=mapper.selectByParams(mapWhere);
        PageInfo<EnergyDailyVo> pageInfo = new PageInfo<EnergyDailyVo>(trainDailyVoList);
        return pageInfo;
    }
    /**
     * ????????????????????????
     * @param mapWhere
     * @return
     */
    @Override
    public List<EnergyDailyVo> selectEnergyDailyList(Map<String, Object> mapWhere) {
        List<EnergyDailyVo> trainDailyVoList=mapper.selectByParams(mapWhere);
        return trainDailyVoList;
    }

    @Override
    @AddLog(title = "????????????????????????", module = LoggerModuleEnum.ENERGY_DETAILEDSEARCH)
    public String uploadEnergyDailyTemplate(Map<String, Object> mapWhere,MultipartFile file ) {
        if (file.isEmpty()) {
            throw new BaseException("??????????????????!");
        }
        String fileName = file.getOriginalFilename();//?????????
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);//????????????
        if (!"xlsx".equals(ext)) {
            throw new BaseException("?????????????????????!");
        }
        Workbook wb;
        try {
            wb = ExcelUtil.getWorkbook(file);
            Sheet sheet = ExcelUtil.getSheet(wb, 0);//???????????????????????????
            // ??????????????????????????????
            List<Map<String, Object>> dataList = ExcelUtil.getSheetValue(sheet, 1);

            //??????????????????
            ExcelError excelError = new ExcelError();
            String dailyId="";
            String recordTime="";
            String line = mapWhere.get("line").toString();// ???????????????
            String linePeriod = "";//?????????????????????,??????,??????
            String powerType = "";//0??????????????????1????????????2?????????
            String subStation = "";// ?????????/??????
            String switchgear = "";// ?????????
            String powerConsumption = "";// ?????????
            String description;// ??????
            //????????????
            List<EnergyDetailPo> energyDetailPoList = new ArrayList<EnergyDetailPo>();
            //???????????? 1???2 ???
            List<EnergyDetailPo> energyDetailPoList12 = new ArrayList<EnergyDetailPo>();
            String linePeriod12="??????,??????";
            //???????????? 3 ???
            List<EnergyDetailPo> energyDetailPoList3 = new ArrayList<EnergyDetailPo>();
            String linePeriod3="??????";
            //1????????????
            if("1".equals(line))
            {
                for (int i = 1; i < dataList.size(); i++)
                {

                    Map<String, Object> datamap = dataList.get(i);
                    //excelError.addRow(Integer.valueOf(String.valueOf(datamap.get("rowIndex"))) + 1);
                    //????????????
                    if(i == 1)
                    {
                        Date recordTimeDate = DateUtil.parseUsDateTime02(String.valueOf(datamap.get("2")));
                        recordTime=DateUtil.getDateFormat(recordTimeDate);
                        if (StringHelper.isNullAndEmpty(recordTime)) {
                            excelError.addError(2, "??????", "??????????????????");
                            return "??????????????????";
                        }
                        else
                        {
                            Map<String, Object> checkMapWhere =new HashMap<String, Object>();
                            checkMapWhere.put("recordTime",recordTime);
                            checkMapWhere.put("line",mapWhere.get("line"));
                            //checkMapWhere.put("checkReviewStatus","REJECT");
                            Boolean isInsert=  this.checkRecordTime(checkMapWhere);
                            if(!isInsert)
                            {
                                return "????????????????????????????????????????????????????????????";
                            }
                        }
                        continue;
                    }
                    //????????????
                    else if(i>=2 && i <10)
                    {
                        line=mapWhere.get("line").toString();
                        powerType="0";
                        subStation=this.getMergedRegionValue(sheet,i+1,0);
                        switchgear= String.valueOf(datamap.get("1"));
                        powerConsumption= String.valueOf(datamap.get("2"));
                        if (StringHelper.isNullAndEmpty(powerConsumption))
                            excelError.addError(2, "?????????", "?????????????????????");
                    }

                    //1??????????????????
                    //????????????
                   else if(i>11 && i<52)
                    {
                        line = mapWhere.get("line").toString();
                        linePeriod="???????????????";
                        powerType="1";
                        subStation=this.getMergedRegionValue(sheet,i+1,0);
                        switchgear= String.valueOf(datamap.get("1"));
                        powerConsumption= String.valueOf(datamap.get("2"));
                        if (StringHelper.isNullAndEmpty(powerConsumption))
                            excelError.addError(2, "?????????", "?????????????????????");
                    }
                    //????????????
                   else if(i>52 && i<69)
                    {
                        line = mapWhere.get("line").toString();
                        linePeriod="???????????????";
                        powerType="2";
                        subStation=this.getMergedRegionValue(sheet,i+1,0);
                        switchgear= String.valueOf(datamap.get("1"));
                        powerConsumption= String.valueOf(datamap.get("2"));
                        if (StringHelper.isNullAndEmpty(powerConsumption))
                            excelError.addError(2, "?????????", "?????????????????????");
                    }
                    //1????????????
                    //????????????
                   else if(i>=71 && i<83)
                    {
                        line = mapWhere.get("line").toString();
                        linePeriod="??????";
                        powerType="1";
                        //subStation=String.valueOf(datamap.get("0"));
                        subStation=this.getMergedRegionValue(sheet,i+1,0);
                        switchgear= String.valueOf(datamap.get("1"));
                        if(StringHelper.isNullAndEmpty(switchgear))
                        {
                            switchgear="";
                        }
                        powerConsumption= String.valueOf(datamap.get("2"));
                        if (StringHelper.isNullAndEmpty(powerConsumption))
                            excelError.addError(2, "?????????", "?????????????????????");
                    }
                    //????????????
                    else if(i>83 && i<90)
                    {

                        line = mapWhere.get("line").toString();
                        linePeriod="??????";
                        powerType="2";
                        //subStation=String.valueOf(datamap.get("0"));
                        subStation=this.getMergedRegionValue(sheet,i+1,0);
                        switchgear= String.valueOf(datamap.get("1"));
                        if(StringHelper.isNullAndEmpty(switchgear))
                        {
                            switchgear="";
                        }
                        powerConsumption= String.valueOf(datamap.get("2"));
                        if (StringHelper.isNullAndEmpty(powerConsumption))
                            excelError.addError(2, "?????????", "?????????????????????");
                    }
                    else
                    {
                        continue;
                    }

                    if("??????".equals(linePeriod))
                    {
                        EnergyDetailPo energyDetailPo = new EnergyDetailPo();
                        energyDetailPo.setRecordTime(DateUtil.formatDate(recordTime,"yyyy-MM-dd"));
                        energyDetailPo.setLine(line);
                        energyDetailPo.setLinePeriod(linePeriod);
                        energyDetailPo.setPowerType(powerType);
                        energyDetailPo.setSubStation(subStation);
                        energyDetailPo.setSwitchgear(switchgear);
                        energyDetailPo.setPowerConsumption(powerConsumption);
                        energyDetailPoList3.add(energyDetailPo);

                    }
                    else
                    {
                        EnergyDetailPo energyDetailPo = new EnergyDetailPo();
                        energyDetailPo.setRecordTime(DateUtil.formatDate(recordTime,"yyyy-MM-dd"));
                        energyDetailPo.setLine(line);
                        energyDetailPo.setLinePeriod(linePeriod);
                        energyDetailPo.setPowerType(powerType);
                        energyDetailPo.setSubStation(subStation);
                        energyDetailPo.setSwitchgear(switchgear);
                        energyDetailPo.setPowerConsumption(powerConsumption);
                        energyDetailPoList12.add(energyDetailPo);
                    }
                }
            }
            //3????????????
            else if("3".equals(line))
            {
                for (int i = 1; i < dataList.size(); i++)
                {

                    Map<String, Object> datamap = dataList.get(i);
                    excelError.addRow(Integer.valueOf(String.valueOf(datamap.get("rowIndex"))) + 1);
                    //????????????
                    if(i == 1)
                    {
                       // recordTime = String.valueOf(datamap.get("2"));
                        Date recordTimeDate = DateUtil.parseUsDateTime02(String.valueOf(datamap.get("2")));
                        recordTime=DateUtil.getDateFormat(recordTimeDate);
                        if (StringHelper.isNullAndEmpty(recordTime)) {
                            excelError.addError(2, "??????", "??????????????????");
                            return "??????????????????";
                        }
                        else
                        {
                            Map<String, Object> checkMapWhere =new HashMap<String, Object>();
                            checkMapWhere.put("recordTime",recordTime);
                            checkMapWhere.put("line",mapWhere.get("line"));
                            //checkMapWhere.put("checkReviewStatus",mapWhere.get("REJECT"));
                            Boolean isInsert=  this.checkRecordTime(checkMapWhere);
                            if(!isInsert)
                            {
                                return "????????????????????????????????????????????????????????????";
                            }
                        }
                        continue;
                    }
                    //????????????
                   else if(i>1 && i <4)
                    {
                        line=mapWhere.get("line").toString();
                        linePeriod="??????";
                        powerType="0";
                        subStation=this.getMergedRegionValue(sheet,i+1,0);
                        switchgear= String.valueOf(datamap.get("1"));
                        powerConsumption= String.valueOf(datamap.get("2"));
                        if (StringHelper.isNullAndEmpty(powerConsumption))
                            excelError.addError(2, "?????????", "?????????????????????");
                    }

                    //3????????????
                    //????????????
                    else if(i>=6 && i<16)
                    {
                        line = mapWhere.get("line").toString();
                        linePeriod="??????";
                        powerType="1";
                        //subStation=String.valueOf(datamap.get("0"));
                        subStation=this.getMergedRegionValue(sheet,i+1,0);
                        switchgear= String.valueOf(datamap.get("1"));
                        if(StringHelper.isNullAndEmpty(switchgear))
                        {
                            switchgear="";
                        }
                        powerConsumption= String.valueOf(datamap.get("2"));
                        if (StringHelper.isNullAndEmpty(powerConsumption))
                            excelError.addError(2, "?????????", "?????????????????????");
                    }
                    //????????????
                    else if(i>=17 && i<23)
                    {
                        line = mapWhere.get("line").toString();
                        linePeriod="??????";
                        powerType="2";
                        //subStation=String.valueOf(datamap.get("0"));
                        subStation=this.getMergedRegionValue(sheet,i+1,0);
                        switchgear= String.valueOf(datamap.get("1"));
                        if(StringHelper.isNullAndEmpty(switchgear))
                        {
                            switchgear="";
                        }
                        powerConsumption= String.valueOf(datamap.get("2"));
                        if (StringHelper.isNullAndEmpty(powerConsumption))
                            excelError.addError(2, "?????????", "?????????????????????");
                    }
                    else
                    {
                        continue;
                    }

                    EnergyDetailPo energyDetailPo = new EnergyDetailPo();
                    energyDetailPo.setRecordTime(DateUtil.formatDate(recordTime,"yyyy-MM-dd"));
                    energyDetailPo.setLine(line);
                    energyDetailPo.setLinePeriod(linePeriod);
                    energyDetailPo.setPowerType(powerType);
                    energyDetailPo.setSubStation(subStation);
                    energyDetailPo.setSwitchgear(switchgear);
                    energyDetailPo.setPowerConsumption(powerConsumption);
                    energyDetailPoList.add(energyDetailPo);
                }
            }
            if (!excelError.isHasError()) {
                if("1".equals(line))
                {
                    EnergyDailyVo energyDailyVo12= insertEnergyDaily(DateUtil.formatDate(recordTime,"yyyy-MM-dd"),mapWhere.get("line").toString(),linePeriod12);
                    dailyId=energyDailyVo12.getId();
                    for(EnergyDetailPo energyDetailPo:energyDetailPoList12)
                    {
                        energyDetailPo.setDailyId(dailyId);
                    }
                    energyDetailService.insert(energyDetailPoList12);
                    EnergyDailyVo energyDailyVo3= insertEnergyDaily(DateUtil.formatDate(recordTime,"yyyy-MM-dd"),mapWhere.get("line").toString(),linePeriod3);
                    dailyId=energyDailyVo3.getId();
                    for(EnergyDetailPo energyDetailPo3:energyDetailPoList3)
                    {
                        energyDetailPo3.setDailyId(dailyId);
                    }
                    energyDetailService.insert(energyDetailPoList3);

                }
                else
                {
                    EnergyDailyVo energyDailyVo= insertEnergyDaily(DateUtil.formatDate(recordTime,"yyyy-MM-dd"),mapWhere.get("line").toString(),linePeriod);
                    dailyId=energyDailyVo.getId();
                    for(EnergyDetailPo energyDetailPo:energyDetailPoList)
                    {
                        energyDetailPo.setDailyId(dailyId);
                    }
                    energyDetailService.insert(energyDetailPoList);
                }
                this.addLogger(recordTime,line,"","INIT");
                return "";
            } else {//????????????Excel
                String errorId = UUIDHexGenerator.generator();
                redisUtil.setxObjectValue(errorId, excelError, 120);//120????????????
                return errorId;
            }
        } catch (Exception e) {
            throw new BaseException("????????????!", e);
        }
    }

    /**
     *
     * @param mapWhere
     * @return
     */
    public Boolean checkRecordTime( Map<String, Object> mapWhere)
    {
        //????????????????????? ????????????
        List<EnergyDailyVo> checkEnergyDailyVo=mapper.selectByParams(mapWhere);
        if(ObjectUtils.isEmpty(checkEnergyDailyVo))
        {
            //??????true  ????????????
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * ???????????????????????????
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public  String getMergedRegionValue(Sheet sheet ,int row , int column){
        int sheetMergeCount = sheet.getNumMergedRegions();

        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell) ;
                }
            }
        }

        return null ;
    }

    /**
     * ?????????????????????
     * @param cell
     * @return
     */
    public  String getCellValue(Cell cell){
        if(cell == null) return "";
        return cell.getStringCellValue();
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????
     * listCombineCell ????????????????????????list
     * @param cell ????????????????????????
     * @param sheet sheet
     * @return
     */
    public  String isCombineCell(Cell cell,Sheet sheet) throws Exception
    {
        int firstC = 0;
        int lastC = 0;
        int firstR = 0;
        int lastR = 0;
        String cellValue = null;
        List<CellRangeAddress> listCombineCell = new ArrayList<>();
        //???????????? sheet ???????????????????????????
        int sheetmergerCount = sheet.getNumMergedRegions();
        //??????????????????????????????
        for(int i = 0; i<sheetmergerCount;i++)
        {
            //??????????????????????????????list???
            CellRangeAddress ca = sheet.getMergedRegion(i);
            listCombineCell.add(ca);
        }
        for(CellRangeAddress ca:listCombineCell)
        {
            //?????????????????????????????????, ?????????, ?????????, ?????????
            firstC = ca.getFirstColumn();
            lastC = ca.getLastColumn();
            firstR = ca.getFirstRow();
            lastR = ca.getLastRow();
            if(cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR)
            {
                if(cell.getColumnIndex() >= firstC && cell.getColumnIndex() <= lastC)
                {
                    Row fRow = sheet.getRow(firstR);
                    Cell fCell = fRow.getCell(firstC);
                    cellValue = getCellValue(fCell);
                    break;
                }
            }
            else
            {
                cellValue = "";
            }
        }
        return cellValue;
    }


    /**
     * ????????????????????????????????????????????????
     * @param sheet
     * @param row ?????????
     * @param column ?????????
     * @return
     */
    private  boolean isMergedRegion(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }




    /**
     * ??????
     * @param
     * @return
     */
    public EnergyDailyVo insertEnergyDaily(Date recordTime, String line,String linePeriod)
    {

        Map<String, Object> mapWhere =new HashMap<>();
        if(StringHelper.isNotNullAndEmpty(line) && recordTime !=null)
        {
            mapWhere.put("line",line);
            mapWhere.put("linePeriod",linePeriod);
            mapWhere.put("recordTime",DateUtil.getDateFormat(recordTime));
            EnergyDailyPo energyDailyPo=new EnergyDailyPo();
            energyDailyPo.setLine(line);
            energyDailyPo.setLinePeriod(linePeriod);
            energyDailyPo.setRecordTime(recordTime);
            energyDailyPo.setReviewStatus("INIT");
            //???????????????????????????approved
            //energyDailyPo.setReviewStatus("APPROVED");
            super.insert(energyDailyPo);
            return mapper.selectByParams(mapWhere).get(0);
        }
        else
        {
            return null;

        }

    }
    /**
     * ??????
     * @param
     * @return
     */
    @Override
    public EnergyDailyVo updateEnergyDaily(Integer dailyId)
    {

        //?????????????????????
        EnergyDailyPo tempEnergyDailyPo=super.selectById(dailyId);
        Map<String, Object> tempMapWhere =new HashMap<>();
        tempMapWhere.put("line",tempEnergyDailyPo.getLine());
        tempMapWhere.put("recordTime",DateUtil.getDateFormat(tempEnergyDailyPo.getRecordTime()));
        //?????????
        tempMapWhere.put("powerType","0");
        List<EnergyDailyVo> totalPowerList=mapper.select2DailyByParams(tempMapWhere);

        Map<String, Object> mapWhere =new HashMap<>();
        String totalPowerConsumption=totalPowerList.get(0).getPowerConsumption();
        String motivePowerConsumption="0";
        String towPowerConsumption="0";
        mapWhere.put("dailyId",dailyId);
        //???detail ????????????????????????????????????
        List<EnergyDailyVo> energyDailyNum=mapper.select2DailyByParams(mapWhere);
        EnergyDailyPo energyDailyPo=new EnergyDailyPo();
        energyDailyPo.setId(dailyId);
        for (EnergyDailyVo energyDailyVo :energyDailyNum)
        {
            //????????? 3?????? ???????????????????????????  ??????309 +310
            if("0".equals(energyDailyVo.getPowerType()) && "3".equals(tempEnergyDailyPo.getLine()))
            {
                totalPowerConsumption=energyDailyVo.getPowerConsumption();
                energyDailyPo.setTotalPowerConsumption(totalPowerConsumption);
            }
            //????????????
            if("1".equals(energyDailyVo.getPowerType()))
            {
                motivePowerConsumption=energyDailyVo.getPowerConsumption();
                energyDailyPo.setMotivePowerConsumption(motivePowerConsumption);
            }
            //????????????
            if("2".equals(energyDailyVo.getPowerType()))
            {
                towPowerConsumption=energyDailyVo.getPowerConsumption();
                energyDailyPo.setTowPowerConsumption(towPowerConsumption);
            }
        }

        //1?????????????????????  ????????????
        if("1".equals(tempEnergyDailyPo.getLine()))
        {
            Map<String, Object> tempWhere =new HashMap<>();
            tempWhere.put("dailyId",dailyId);
            //1?????????  ???????????????????????????
            tempWhere.put("powerType","1");
            List<EnergyDetailVo> energyDetailLinePeriodVos=energyDetailService.selectByParams(tempWhere);
            String linePeriod="";
            if(!ObjectUtils.isEmpty(energyDetailLinePeriodVos))
            {
                linePeriod=energyDetailLinePeriodVos.get(0).getLinePeriod();
            }
            //?????????????????? ?????????????????????   3???????????????????????? ????????????????????????????????????
            if("??????".equals(linePeriod))
            {
                tempWhere.remove("dailyId");
                tempWhere.put("line",tempEnergyDailyPo.getLine());
                tempWhere.put("recordTime",DateUtil.getDateFormat(tempEnergyDailyPo.getRecordTime()));
                //?????????
                tempWhere.put("powerType","0");
            }
            else
            {
                //?????????
                tempWhere.put("powerType","0");
            }
            List<EnergyDetailVo> energyDetailVos=energyDetailService.selectByParams(tempWhere);
            if (!ObjectUtils.isEmpty(energyDetailVos))
            {
                //???????????????
                totalPowerConsumption=getTotalPowerConsumption( energyDetailVos,linePeriod);

            }
        }

        //???????????? ?????? ????????????
        BigDecimal totalPowerConsumptionB= new BigDecimal(totalPowerConsumption);
        BigDecimal motivePowerConsumptionB= new BigDecimal(motivePowerConsumption);
        BigDecimal towPowerConsumptionB= new BigDecimal(towPowerConsumption);
        //?????? ?????? ?????? ?????????
        energyDailyPo.setTotalPowerConsumption(totalPowerConsumption);
        energyDailyPo.setMotivePowerConsumption(motivePowerConsumption);
        energyDailyPo.setTowPowerConsumption(towPowerConsumption);
    //???????????? ?????? ????????????
        String motivePowerRate=motivePowerConsumptionB.divide(totalPowerConsumptionB ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
        String towPowerRate=towPowerConsumptionB.divide(totalPowerConsumptionB ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
        energyDailyPo.setMotivePowerRate(motivePowerRate);
        energyDailyPo.setTowPowerRate(towPowerRate);
        //??????
        String lossPowerConsumption=totalPowerConsumptionB.subtract(motivePowerConsumptionB).subtract(towPowerConsumptionB).toString();
        energyDailyPo.setLossPowerConsumption(lossPowerConsumption);
        //????????????
        String lossPowerRate=new BigDecimal(lossPowerConsumption).divide(totalPowerConsumptionB ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
        energyDailyPo.setLossPowerRate(lossPowerRate);
        super.updateSelectiveById(energyDailyPo);
               // insert(energyDailyPo);
        return mapper.selectByParams(mapWhere).get(0);


    }


    /**
     * ??????
     * @param
     * @return
     */
    @Override
   // @AddLog(title = "????????????????????????", module = LoggerModuleEnum.ENERGY_DETAILEDSEARCH)
    public Boolean updateEnergyMonthly(Date recordTime, String line) {

        String recordTimeStr= DateUtil.getDateFormat(recordTime,"yyyy-MM");

        //recordTime  line  ??????????????????
        if(StringHelper.isNullAndEmpty(line) || StringHelper.isNullAndEmpty(recordTimeStr))
        {
            return false;
        }
        if("1".equals(line))
        {
            String linePeriod12="??????,??????";
            String linePeriod3="??????";


            //???????????????  ????????????????????????  ??????,??????
            Map<String, Object> mapWhere12=new HashMap<String, Object>();
            mapWhere12.put("up2MonthRecordTime",recordTimeStr);
            mapWhere12.put("line",line);
            mapWhere12.put("linePeriod",linePeriod12);
            EnergyDailyVo energyDailyVo12=mapper.selectDaily2MonthlyByParams(mapWhere12);

            //???????????????  ????????????????????????  ??????
            Map<String, Object> mapWhere3=new HashMap<String, Object>();
            mapWhere3.put("up2MonthRecordTime",recordTimeStr);
            mapWhere3.put("line",line);
            mapWhere3.put("linePeriod",linePeriod3);
            EnergyDailyVo energyDailyVo3=mapper.selectDaily2MonthlyByParams(mapWhere3);

            //???????????????????????? ??????,??????
            Map<String, Object> mapWhereMonth12=new HashMap<String, Object>();
            mapWhereMonth12.put("up2MonthRecordTime",recordTimeStr);
            mapWhereMonth12.put("line",line);
            mapWhereMonth12.put("linePeriod",linePeriod12);//??????bug???
            List<EnergyMonthlyPo> energyMonthlyPoList12=energyMonthlyService.selectByParams(mapWhereMonth12);
            EnergyMonthlyPo energyMonthlyPo12= dozerMapper.map(energyDailyVo12,EnergyMonthlyPo.class);

            //???????????????????????? ??????
            Map<String, Object> mapWhereMonth3=new HashMap<String, Object>();
            mapWhereMonth3.put("up2MonthRecordTime",recordTimeStr);
            mapWhereMonth3.put("line",line);
            mapWhereMonth3.put("linePeriod",linePeriod3);//??????bug???
            List<EnergyMonthlyPo> energyMonthlyPoList3=energyMonthlyService.selectByParams(mapWhereMonth3);
            EnergyMonthlyPo energyMonthlyPo3= dozerMapper.map(energyDailyVo3,EnergyMonthlyPo.class);

            //?????????????????????????????? ??????,??????
            if(ObjectUtils.isEmpty(energyMonthlyPoList12))
            {
                energyMonthlyPo12.setLine(line);
                energyMonthlyPo12.setLinePeriod(linePeriod12);
                energyMonthlyPo12.setRecordTime(DateUtil.formatDate(DateUtil.getDateFormat(recordTime),"yyyy-MM"));
                energyMonthlyService.insert(energyMonthlyPo12);
            }
            else
            {
                energyMonthlyPo12.setId(energyMonthlyPoList12.get(0).getId());
                //
                BigDecimal totalPowerConsumptionB= new BigDecimal(energyDailyVo12.getTotalPowerConsumption());
                BigDecimal motivePowerConsumptionB= new BigDecimal(energyDailyVo12.getMotivePowerConsumption());
                BigDecimal towPowerConsumptionB= new BigDecimal(energyDailyVo12.getTowPowerConsumption());
                //?????? ?????? ?????? ?????????
                energyMonthlyPo12.setTotalPowerConsumption(energyDailyVo12.getTotalPowerConsumption());
                energyMonthlyPo12.setMotivePowerConsumption(energyDailyVo12.getMotivePowerConsumption());
                energyMonthlyPo12.setTotalPowerConsumption(energyDailyVo12.getTowPowerConsumption());
                //?????????????????? ?????? ????????????
                String motivePowerRate=motivePowerConsumptionB.divide(totalPowerConsumptionB ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
                String towPowerRate=towPowerConsumptionB.divide(totalPowerConsumptionB ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
                energyMonthlyPo12.setMotivePowerRate(motivePowerRate);
                energyMonthlyPo12.setTowPowerRate(towPowerRate);
                //??????
                String lossPowerConsumption=totalPowerConsumptionB.subtract(motivePowerConsumptionB).subtract(towPowerConsumptionB).toString();
                energyMonthlyPo12.setLossPowerConsumption(lossPowerConsumption);
                //????????????
                String lossPowerRate=new BigDecimal(lossPowerConsumption).divide(totalPowerConsumptionB ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
                energyMonthlyPo12.setLossPowerRate(lossPowerRate);
                energyMonthlyService.updateSelectiveById(energyMonthlyPo12);
            }
            //?????????????????????????????? ??????
            if(ObjectUtils.isEmpty(energyMonthlyPoList3))
            {
                energyMonthlyPo3.setLine(line);
                energyMonthlyPo3.setLinePeriod(linePeriod3);
                energyMonthlyPo3.setRecordTime(DateUtil.formatDate(DateUtil.getDateFormat(recordTime),"yyyy-MM"));
                energyMonthlyService.insert(energyMonthlyPo3);
            }
            else
            {
                energyMonthlyPo3.setId(energyMonthlyPoList3.get(0).getId());
                //
                BigDecimal totalPowerConsumptionB= new BigDecimal(energyDailyVo3.getTotalPowerConsumption());
                BigDecimal motivePowerConsumptionB= new BigDecimal(energyDailyVo3.getMotivePowerConsumption());
                BigDecimal towPowerConsumptionB= new BigDecimal(energyDailyVo3.getTowPowerConsumption());
                //?????? ?????? ?????? ?????????
                energyMonthlyPo3.setTotalPowerConsumption(energyDailyVo3.getTotalPowerConsumption());
                energyMonthlyPo3.setMotivePowerConsumption(energyDailyVo3.getMotivePowerConsumption());
                energyMonthlyPo3.setTotalPowerConsumption(energyDailyVo3.getTowPowerConsumption());
                //?????????????????? ?????? ????????????
                String motivePowerRate=motivePowerConsumptionB.divide(totalPowerConsumptionB ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
                String towPowerRate=towPowerConsumptionB.divide(totalPowerConsumptionB ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
                energyMonthlyPo3.setMotivePowerRate(motivePowerRate);
                energyMonthlyPo3.setTowPowerRate(towPowerRate);
                //??????
                String lossPowerConsumption=totalPowerConsumptionB.subtract(motivePowerConsumptionB).subtract(towPowerConsumptionB).toString();
                energyMonthlyPo3.setLossPowerConsumption(lossPowerConsumption);
                //????????????
                String lossPowerRate=new BigDecimal(lossPowerConsumption).divide(totalPowerConsumptionB ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
                energyMonthlyPo3.setLossPowerRate(lossPowerRate);
                energyMonthlyService.updateSelectiveById(energyMonthlyPo3);
            }
            return true;
        }
        else
        {

            //???????????????  ????????????????????????
            Map<String, Object> mapWhere=new HashMap<String, Object>();
            mapWhere.put("up2MonthRecordTime",recordTimeStr);
            mapWhere.put("line",line);
            EnergyDailyVo energyDailyVo=mapper.selectDaily2MonthlyByParams(mapWhere);
            //????????????????????????
            Map<String, Object> mapWhereMonth=new HashMap<String, Object>();
            mapWhereMonth.put("up2MonthRecordTime",recordTimeStr);
            mapWhereMonth.put("line",line);
            List<EnergyMonthlyPo> energyMonthlyPoList=energyMonthlyService.selectByParams(mapWhereMonth);
            EnergyMonthlyPo energyMonthlyPo= dozerMapper.map(energyDailyVo,EnergyMonthlyPo.class);
            //??????????????????????????????
            if(ObjectUtils.isEmpty(energyMonthlyPoList))
            {
                energyMonthlyPo.setLine(line);
                energyMonthlyPo.setLinePeriod("??????");
                energyMonthlyPo.setRecordTime(DateUtil.formatDate(DateUtil.getDateFormat(recordTime),"yyyy-MM"));
                energyMonthlyService.insert(energyMonthlyPo);
            }
            else
            {
                energyMonthlyPo.setId(energyMonthlyPoList.get(0).getId());
                //
                BigDecimal totalPowerConsumptionB= new BigDecimal(energyDailyVo.getTotalPowerConsumption());
                BigDecimal motivePowerConsumptionB= new BigDecimal(energyDailyVo.getMotivePowerConsumption());
                BigDecimal towPowerConsumptionB= new BigDecimal(energyDailyVo.getTowPowerConsumption());
                //?????? ?????? ?????? ?????????
                energyMonthlyPo.setTotalPowerConsumption(energyDailyVo.getTotalPowerConsumption());
                energyMonthlyPo.setMotivePowerConsumption(energyDailyVo.getMotivePowerConsumption());
                energyMonthlyPo.setTotalPowerConsumption(energyDailyVo.getTowPowerConsumption());
                //?????????????????? ?????? ????????????
                String motivePowerRate=motivePowerConsumptionB.divide(totalPowerConsumptionB ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
                String towPowerRate=towPowerConsumptionB.divide(totalPowerConsumptionB ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
                energyMonthlyPo.setMotivePowerRate(motivePowerRate);
                energyMonthlyPo.setTowPowerRate(towPowerRate);
                //??????
                String lossPowerConsumption=totalPowerConsumptionB.subtract(motivePowerConsumptionB).subtract(towPowerConsumptionB).toString();
                energyMonthlyPo.setLossPowerConsumption(lossPowerConsumption);
                //????????????
                String lossPowerRate=new BigDecimal(lossPowerConsumption).divide(totalPowerConsumptionB ,2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
                energyMonthlyPo.setLossPowerRate(lossPowerRate);
                energyMonthlyService.updateSelectiveById(energyMonthlyPo);
            }
            return true;
        }
    }

    @Override

    public void approvedEnergyDaily(List<Integer> ids)
    {

        EnergyDailyPo updatePo = new EnergyDailyPo();
        updatePo.setReviewStatus("APPROVED");
        this.updateSelectiveByIds(updatePo, ids);

        List<EnergyDailyPo> tempEnergyDailyPos=super.selectByIds(ids);
        Map<String,String> map =new HashMap();
        //???????????????
        for (EnergyDailyPo tempEnergyDaily:tempEnergyDailyPos)
        {
            //???????????????
            this.updateEnergyDaily(tempEnergyDaily.getId());
        }
        //?????? ??????????????????
        for (EnergyDailyPo tempEnergyDaily:tempEnergyDailyPos)
        {
            String recordTimeStr= DateUtil.getDateFormat(tempEnergyDaily.getRecordTime(),"yyyy-MM");
            String mapKeyValue=recordTimeStr+"-"+tempEnergyDaily.getLine();
            if(!map.containsValue(mapKeyValue))
            {
                map.put(mapKeyValue,mapKeyValue);
                //?????? ??????????????????
                this.updateEnergyMonthly(tempEnergyDaily.getRecordTime(),tempEnergyDaily.getLine());
            }
        }
    }


    /**
     * ???????????????
     * @param energyDetailVos  ????????????????????????
     *  linePeriod
     * @return
     */

    public String getTotalPowerConsumption(List<EnergyDetailVo> energyDetailVos,String linePeriod)
    {

        BigDecimal totalPowerConsumption=new BigDecimal("0");
        if("??????".equals(linePeriod) )
        {
            for (EnergyDetailVo energyDetailVo:energyDetailVos)
            {
                if("101A".equals(energyDetailVo.getSwitchgear()) || "101B".equals(energyDetailVo.getSwitchgear()))
                {
                    totalPowerConsumption=totalPowerConsumption.add(new BigDecimal(energyDetailVo.getPowerConsumption()));
                }
            }

        }
        else
        {
            //?????? ???????????????
            BigDecimal subPowerConsumption=new BigDecimal("0");
            //??????????????????????????????
            BigDecimal tempPowerConsumption=new BigDecimal("0");
            for (EnergyDetailVo energyDetailVo:energyDetailVos)
            {
                if("??????".equals(energyDetailVo.getSwitchgear()) || "??????".equals(energyDetailVo.getSwitchgear()))
                {
                    subPowerConsumption=subPowerConsumption.add(new BigDecimal(energyDetailVo.getPowerConsumption()));
                }
                else
                {
                    tempPowerConsumption=tempPowerConsumption.add(new BigDecimal(energyDetailVo.getPowerConsumption()));
                }

            }
            totalPowerConsumption=subPowerConsumption.subtract(tempPowerConsumption);

        }

        return totalPowerConsumption.toString();
    }









}
