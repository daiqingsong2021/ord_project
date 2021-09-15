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
        TsPlatVo  tsPlatVo= dozerMapper.map(tsPlatPo, TsPlatVo.class);//po对象转换为Vo对象
        UserVo userVo= commUserService.getUserVoByUserId(tsPlatPo.getCreator());
        tsPlatVo.setCreater(userVo.getName());
        return tsPlatVo;
    }

    @Override public TsPlatVo updateTsPlat(TsPlatUpdateForm tsPlatUpdateForm)
    {
        TsPlatPo tsPlatUpdatePo = dozerMapper.map(tsPlatUpdateForm, TsPlatPo.class);
        super.updateSelectiveById(tsPlatUpdatePo);//根据ID更新po，值为null的不更新，只更新不为null的值
        TsPlatPo tsPlatPo=this.selectById(tsPlatUpdatePo.getId());//将数据查询出来
        TsPlatVo  tsPlatVo= dozerMapper.map(tsPlatPo, TsPlatVo.class);//po对象转换为Vo对象
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
            throw new BaseException("文件不能为空!");
        }
        String fileName = multipartFile.getOriginalFilename();//文件名
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);//文件类型
        if (!"xlsx".equals(ext))
        {
            throw new BaseException("文件格式不支持!");
        }
        Workbook wb = null;
        try
        {
            wb = ExcelUtil.getWorkbook(multipartFile);
            Sheet sheet = ExcelUtil.getSheet(wb, 0);//获取第一页的工作簿
            // 取出指定工作簿的内容
            List<Map<String, Object>> dataList = ExcelUtil.getSheetValue(sheet, 1);
            List<Map<String, Object>> columnList = ExcelUtil.getSheetValue(sheet, 0, 1);

            //定义错误日志
            ExcelError excelError = new ExcelError();
            Integer projInfoId = Integer.valueOf(String.valueOf(paramMap.get("projInfoId")));// 进退场主键ID*
            Integer projectId = ObjectUtils.isEmpty(paramMap.get("projectId")) ? null :
                    Integer.valueOf(String.valueOf(paramMap.get("projectId")));// 项目ID
            Integer sectionId = ObjectUtils.isEmpty(paramMap.get("sectionId")) ? null :
                    Integer.valueOf(String.valueOf(paramMap.get("sectionId")));// 标段ID
            String name = "";// 名称*
            String address = "";// 调试平台地址
            List<TsPlatPo> insertTsPlats = Lists.newArrayList();
            for (int i = 0; i < dataList.size(); i++)
            {
                Map<String, Object> datamap = dataList.get(i);
                excelError.addRow(Integer.valueOf(String.valueOf(datamap.get("rowIndex")))+1);

                name = String.valueOf(datamap.get("0"));
                if (StringHelper.isNullAndEmpty(name))
                    excelError.addError(0, "平台名称", "平台名称不能为空");

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
            {//导出错误Excel
                String errorId= UUIDHexGenerator.generator();
                redisUtil.setxObjectValue(errorId,excelError,120);//120秒后消亡
                return  errorId;
            }
        }
        catch (Exception e)
        {
            throw new BaseException("导入错误!");
        }
    }
}
