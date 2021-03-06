package com.wisdom.acm.szxm.service.rygl.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.UUIDHexGenerator;
import com.wisdom.acm.szxm.common.officeUtils.ExcelError;
import com.wisdom.acm.szxm.common.officeUtils.ExcelUtil;
import com.wisdom.acm.szxm.common.redisUtils.RedisUtil;
import com.wisdom.acm.szxm.form.rygl.WarnHouseAddForm;
import com.wisdom.acm.szxm.form.rygl.WarnHouseUpdateForm;
import com.wisdom.acm.szxm.mapper.rygl.WarnHouseMapper;
import com.wisdom.acm.szxm.po.rygl.WarnHousePo;
import com.wisdom.acm.szxm.service.rygl.WarnHouseService;
import com.wisdom.acm.szxm.vo.rygl.WarnHouseVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WarnHouseServiceImpl extends BaseService<WarnHouseMapper, WarnHousePo> implements WarnHouseService
{

    @Autowired
    private CommUserService commUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public PageInfo<WarnHouseVo> selectWarnHouseList(Integer projInfoId, Integer pageSize, Integer currentPageNum)
    {
        Map<String,Object> mapWhere = Maps.newHashMap();
        mapWhere.put("projInfoId", StringHelper.formattString(String.valueOf(projInfoId)));
        PageHelper.startPage(currentPageNum, pageSize);
        List<WarnHouseVo> warnHouseVoList =mapper.selectWarnHouseList(mapWhere);
        PageInfo<WarnHouseVo> pageInfo = new PageInfo<WarnHouseVo>(warnHouseVoList);
        return pageInfo;
    }

    @Override
    public List<WarnHouseVo> selectWarnHouseListByProjId(Map<String, Object> mapWhere)
    {
        List<WarnHouseVo> warnHouseVoList =mapper.selectWarnHouseListByProjId(mapWhere);
        return warnHouseVoList;
    }

    @Override public WarnHouseVo addWarnHouse(WarnHouseAddForm warnHouseAddForm)
    {
        WarnHousePo warnHousePo = dozerMapper.map(warnHouseAddForm, WarnHousePo.class);
        super.insert(warnHousePo);
        WarnHouseVo  warnHouseVo= dozerMapper.map(warnHousePo, WarnHouseVo.class);//po???????????????Vo??????
        UserVo userVo= commUserService.getUserVoByUserId(warnHousePo.getCreator());
        warnHouseVo.setCreater(userVo.getName());
        return warnHouseVo;
    }

    @Override public WarnHouseVo updateWarnHouse(WarnHouseUpdateForm warnHouseUpdateForm)
    {
        WarnHousePo warnHouseUpdatePo = dozerMapper.map(warnHouseUpdateForm, WarnHousePo.class);
        super.updateSelectiveById(warnHouseUpdatePo);//??????ID??????po?????????null??????????????????????????????null??????
        WarnHousePo warnHousePo=this.selectById(warnHouseUpdatePo.getId());//?????????????????????
        WarnHouseVo  warnHouseVo= dozerMapper.map(warnHousePo, WarnHouseVo.class);//po???????????????Vo??????
        UserVo userVo= commUserService.getUserVoByUserId(warnHousePo.getCreator());
        warnHouseVo.setCreater(userVo.getName());
        return warnHouseVo;
    }

    @Override public void deleteWarnHouse(List<Integer> ids)
    {
        this.deleteByIds(ids);
    }

    @Override
    public String uploadWarnHouseFile(MultipartFile multipartFile, Map<String, Object> paramMap) {
        if (multipartFile.isEmpty())
        {
            throw new BaseException("??????????????????!");
        }
        String fileName = multipartFile.getOriginalFilename();//?????????
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);//????????????
        if (!"xlsx".equals(ext))
        {
            throw new BaseException("?????????????????????!");
        }
        Workbook wb = null;
        try
        {
            wb = ExcelUtil.getWorkbook(multipartFile);
            Sheet sheet = ExcelUtil.getSheet(wb, 0);//???????????????????????????
            // ??????????????????????????????
            List<Map<String, Object>> dataList = ExcelUtil.getSheetValue(sheet, 1);
            List<Map<String, Object>> columnList = ExcelUtil.getSheetValue(sheet, 0, 1);

            //??????????????????
            ExcelError excelError = new ExcelError();
            Integer projInfoId = Integer.valueOf(String.valueOf(paramMap.get("projInfoId")));// ???????????????ID*
            Integer projectId = ObjectUtils.isEmpty(paramMap.get("projectId")) ? null :
                    Integer.valueOf(String.valueOf(paramMap.get("projectId")));// ??????ID
            Integer sectionId = ObjectUtils.isEmpty(paramMap.get("sectionId")) ? null :
                    Integer.valueOf(String.valueOf(paramMap.get("sectionId")));// ??????ID
            String name = "";// ??????*
            String address = "";// ????????????
            List<WarnHousePo> insertWarnHouses = Lists.newArrayList();
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> datamap = dataList.get(i);
                excelError.addRow(Integer.valueOf(String.valueOf(datamap.get("rowIndex")))+1);

                name = String.valueOf(datamap.get("0"));
                if (StringHelper.isNullAndEmpty(name))
                    excelError.addError(0, "????????????", "????????????????????????");

                address = String.valueOf(datamap.get("1"));

                WarnHousePo warnHousePo = new WarnHousePo();
                warnHousePo.setProjInfoId(projInfoId);
                warnHousePo.setProjectId(projectId);
                warnHousePo.setSectionId(sectionId);
                warnHousePo.setName(name);
                warnHousePo.setAddress(address);
                insertWarnHouses.add(warnHousePo);
            }

            if (!excelError.isHasError())
            {
                this.insert(insertWarnHouses);
                return "";
            }
            else
            {//????????????Excel
                String errorId= UUIDHexGenerator.generator();
                redisUtil.setxObjectValue(errorId,excelError,120);//120????????????
                return  errorId;
            }
        }
        catch (Exception e)
        {
            throw new BaseException("????????????!");
        }
    }

}
