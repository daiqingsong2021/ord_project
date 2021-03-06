package com.wisdom.acm.szxm.service.rygl.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.common.UUIDHexGenerator;
import com.wisdom.acm.szxm.common.officeUtils.ExcelError;
import com.wisdom.acm.szxm.common.officeUtils.ExcelUtil;
import com.wisdom.acm.szxm.common.redisUtils.RedisUtil;
import com.wisdom.acm.szxm.form.rygl.TsPlatAddForm;
import com.wisdom.acm.szxm.form.rygl.TsPlatUpdateForm;
import com.wisdom.acm.szxm.mapper.rygl.TsPlatMapper;
import com.wisdom.acm.szxm.po.rygl.TsPlatPo;
import com.wisdom.acm.szxm.service.rygl.TsPlatService;
import com.wisdom.acm.szxm.vo.rygl.TsPlatVo;
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
public class TsPlatServiceImpl extends BaseService<TsPlatMapper, TsPlatPo> implements TsPlatService
{
    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private CommUserService commUserService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public PageInfo<TsPlatVo> selectTsPlatList(Integer projInfoId, Integer pageSize, Integer currentPageNum)
    {
        Map<String,Object> mapWhere = Maps.newHashMap();
        mapWhere.put("projInfoId",StringHelper.formattString(String.valueOf(projInfoId)));
        PageHelper.startPage(currentPageNum, pageSize);
        List<TsPlatVo> tsPlatVoList =mapper.selectTsPlatList(mapWhere);
        PageInfo<TsPlatVo> pageInfo = new PageInfo<TsPlatVo>(tsPlatVoList);
        return pageInfo;
    }

    @Override public TsPlatVo addTsPlat(TsPlatAddForm tsPlatAddForm)
    {
        TsPlatPo tsPlatPo = dozerMapper.map(tsPlatAddForm, TsPlatPo.class);
        super.insert(tsPlatPo);
        TsPlatVo  tsPlatVo= dozerMapper.map(tsPlatPo, TsPlatVo.class);//po???????????????Vo??????
        UserVo userVo= commUserService.getUserVoByUserId(tsPlatPo.getCreator());
        tsPlatVo.setCreater(userVo.getName());
        return tsPlatVo;
    }

    @Override public TsPlatVo updateTsPlat(TsPlatUpdateForm tsPlatUpdateForm)
    {
        TsPlatPo tsPlatUpdatePo = dozerMapper.map(tsPlatUpdateForm, TsPlatPo.class);
        super.updateSelectiveById(tsPlatUpdatePo);//??????ID??????po?????????null??????????????????????????????null??????
        TsPlatPo tsPlatPo=this.selectById(tsPlatUpdatePo.getId());//?????????????????????
        TsPlatVo  tsPlatVo= dozerMapper.map(tsPlatPo, TsPlatVo.class);//po???????????????Vo??????
        UserVo userVo= commUserService.getUserVoByUserId(tsPlatPo.getCreator());
        tsPlatVo.setCreater(userVo.getName());
        return tsPlatVo;
    }

    @Override public void deleteTsPlat(List<Integer> ids)
    {
        this.deleteByIds(ids);
    }

    @Override
    public String uploadTsPlatFile(MultipartFile multipartFile, Map<String, Object> paramMap) {
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
            String address = "";// ??????????????????
            List<TsPlatPo> insertTsPlats = Lists.newArrayList();
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> datamap = dataList.get(i);
                excelError.addRow(Integer.valueOf(String.valueOf(datamap.get("rowIndex")))+1);

                name = String.valueOf(datamap.get("0"));
                if (StringHelper.isNullAndEmpty(name))
                    excelError.addError(0, "????????????", "????????????????????????");

                address = String.valueOf(datamap.get("1"));

                TsPlatPo tsPlatPo = new TsPlatPo();
                tsPlatPo.setProjInfoId(projInfoId);
                tsPlatPo.setProjectId(projectId);
                tsPlatPo.setSectionId(sectionId);
                tsPlatPo.setName(name);
                tsPlatPo.setAddress(address);
                insertTsPlats.add(tsPlatPo);
            }

            if (!excelError.isHasError())
            {
                this.insert(insertTsPlats);
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
