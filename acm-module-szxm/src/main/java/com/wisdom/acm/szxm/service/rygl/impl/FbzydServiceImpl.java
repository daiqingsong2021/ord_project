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
import com.wisdom.acm.szxm.form.rygl.FbzydAddForm;
import com.wisdom.acm.szxm.form.rygl.FbzydUpdateForm;
import com.wisdom.acm.szxm.mapper.rygl.FbzydMapper;
import com.wisdom.acm.szxm.po.rygl.FbzydPo;
import com.wisdom.acm.szxm.service.rygl.FbzydService;
import com.wisdom.acm.szxm.vo.rygl.FbzydVo;
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
import java.util.regex.Pattern;

@Service
@Slf4j
public class FbzydServiceImpl extends BaseService<FbzydMapper, FbzydPo> implements FbzydService
{
    @Autowired
    private CommUserService commUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public PageInfo<FbzydVo> selectFbzydList(Integer projInfoId, Integer pageSize, Integer currentPageNum)
    {
        Map<String,Object> mapWhere = Maps.newHashMap();
        mapWhere.put("projInfoId",StringHelper.formattString(String.valueOf(projInfoId)));
        PageHelper.startPage(currentPageNum, pageSize);
        List<FbzydVo> fbzydVoList =mapper.selectFbzydList(mapWhere);
        PageInfo<FbzydVo> pageInfo = new PageInfo<FbzydVo>(fbzydVoList);
        return pageInfo;
    }

    @Override public FbzydVo addFbzyd(FbzydAddForm fbzydAddForm)
    {
        FbzydPo fbzydPo = dozerMapper.map(fbzydAddForm, FbzydPo.class);
        super.insert(fbzydPo);
        FbzydVo  fbzydVo= dozerMapper.map(fbzydPo, FbzydVo.class);//po???????????????Vo??????
        UserVo userVo= commUserService.getUserVoByUserId(fbzydPo.getCreator());
        fbzydVo.setCreater(userVo.getName());
        return fbzydVo;
    }

    @Override public FbzydVo updateFbzyd(FbzydUpdateForm fbzydUpdateForm)
    {
        FbzydPo fbzydUpdatePo = dozerMapper.map(fbzydUpdateForm, FbzydPo.class);
        super.updateSelectiveById(fbzydUpdatePo);//??????ID??????po?????????null??????????????????????????????null??????
        FbzydPo fbzydPo=this.selectById(fbzydUpdatePo.getId());//?????????????????????
        FbzydVo  fbzydVo= dozerMapper.map(fbzydPo, FbzydVo.class);//po???????????????Vo??????
        UserVo userVo= commUserService.getUserVoByUserId(fbzydPo.getCreator());
        fbzydVo.setCreater(userVo.getName());
        return fbzydVo;
    }

    @Override public void deleteFbzyd(List<Integer> ids)
    {
        this.deleteByIds(ids);
    }

    @Override
    public String uploadFbzydFile(MultipartFile multipartFile, Map<String, Object> paramMap) {
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
            String name = "";// ?????????????????????*
            String address = "";// ?????????????????????
            String residentNum = "";// ????????????
            String houseCharacter = "";// ????????????
            String houseArea = "";// ????????????
            List<FbzydPo> insertFbzyds = Lists.newArrayList();
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> datamap = dataList.get(i);
                excelError.addRow(Integer.valueOf(String.valueOf(datamap.get("rowIndex")))+1);

                name = String.valueOf(datamap.get("0"));
                if (StringHelper.isNullAndEmpty(name))
                    excelError.addError(0, "?????????????????????", "?????????????????????????????????");

                address = String.valueOf(datamap.get("1"));

                residentNum = String.valueOf(datamap.get("2"));

                String pattern="^\\d+$";
                boolean isMatch = Pattern.matches(pattern, residentNum);
                if (!isMatch)
                    excelError.addError(2, "????????????", "???????????????????????????");

                houseCharacter=String.valueOf(datamap.get("3"));

                houseArea=String.valueOf(datamap.get("4"));

                FbzydPo fbzydPo = new FbzydPo();
                fbzydPo.setProjInfoId(projInfoId);
                fbzydPo.setProjectId(projectId);
                fbzydPo.setSectionId(sectionId);
                fbzydPo.setName(name);
                fbzydPo.setAddress(address);
                fbzydPo.setResidentNum(Integer.valueOf(residentNum));
                fbzydPo.setHouseCharacter(houseCharacter);
                fbzydPo.setHouseArea(houseArea);

                insertFbzyds.add(fbzydPo);
            }

            if (!excelError.isHasError())
            {
                this.insert(insertFbzyds);
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

    @Override
    public PageInfo<FbzydVo> selectSectionWorkteamList(Map<String, Object> paramMap, Integer pageSize, Integer currentPageNum) {
       // Map<String,Object> mapWhere = Maps.newHashMap();
        //mapWhere.put("projInfoId",StringHelper.formattString(String.valueOf(paramMap.get("projInfoId"))));
        PageHelper.startPage(currentPageNum, pageSize);
        List<FbzydVo> fbzydVoList =mapper.selectSectionWorkteamList(paramMap);
        PageInfo<FbzydVo> pageInfo = new PageInfo<FbzydVo>(fbzydVoList);
        return pageInfo;
    }
}
