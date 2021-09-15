package com.wisdom.acm.szxm.service.rygl.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.form.rygl.CertGlAddForm;
import com.wisdom.acm.szxm.form.rygl.CertGlUpdateForm;
import com.wisdom.acm.szxm.mapper.rygl.CertGlMapper;
import com.wisdom.acm.szxm.po.rygl.CertGlPo;
import com.wisdom.acm.szxm.po.rygl.SpecialWorkCertPo;
import com.wisdom.acm.szxm.service.rygl.CertGlService;
import com.wisdom.acm.szxm.service.rygl.SpecialWorkCertService;
import com.wisdom.acm.szxm.vo.rygl.CertGlVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CertGlServiceImpl extends BaseService<CertGlMapper, CertGlPo> implements
        CertGlService
{

    @Autowired private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private CommPlanProjectService commPlanProjectService;

    @Autowired
    private SpecialWorkCertService specialWorkCertService;

    @Override public List<CertGlVo> getCertGlList(Map<String, Object> mapWhere)
    {
        List<CertGlVo> retList= Lists.newArrayList();
        Example example = new Example(CertGlPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId",String.valueOf(mapWhere.get("projectId")));
        example.setOrderByClause("CREAT_TIME desc");
        List<CertGlPo> certGlPos =this.selectByExample(example);
       for(CertGlPo certGlPo:certGlPos)
       {
           CertGlVo certGlVo=dozerMapper.map(certGlPo,CertGlVo.class);
           retList.add(certGlVo);
       }
        return retList;
    }

    @Override public CertGlVo addCertGl(CertGlAddForm certGlAddForm)
    {
        CertGlPo certGlPo = dozerMapper.map(certGlAddForm, CertGlPo.class);
        this.insert(certGlPo);

        CertGlVo certGlVo = dozerMapper.map(certGlPo, CertGlVo.class);//po对象转换为Vo对象
        certGlVo.setProjectName(commPlanProjectService.getProject(certGlPo.getProjectId()).getName());
        return certGlVo;
    }

    @Override
    public PageInfo<CertGlVo> selectCertGlList(Map<String, Object> mapWhere, Integer pageSize,
            Integer currentPageNum)
    {
        Example example = new Example(CertGlPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId",String.valueOf(mapWhere.get("projectId")));
        if(StringHelper.isNotNullAndEmpty(String.valueOf(mapWhere.get("certName"))))
            criteria.andLike("certName","%"+String.valueOf(mapWhere.get("certName"))+"%");
        example.setOrderByClause("CREAT_TIME desc");
        PageHelper.startPage(currentPageNum, pageSize);
        List<CertGlPo> certGlPoList =this.selectByExample(example);
        PageInfo<CertGlPo> pageInfo_ = new PageInfo<CertGlPo>(certGlPoList);
        List<CertGlVo> retList =Lists.newArrayList();
        PlanProjectVo projectVo=commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));
        for (CertGlPo certGlPo : certGlPoList)
        {
            CertGlVo certGlVo=dozerMapper.map(certGlPo,CertGlVo.class);
            certGlVo.setProjectName(projectVo.getName());
            retList.add(certGlVo);
        }
        PageInfo<CertGlVo> pageInfo = new PageInfo<CertGlVo>(retList);
        szxmCommonUtil.cooyPageInfo(pageInfo_,pageInfo);
        return pageInfo;
    }

    @Override public void deleteCertGl(List<Integer> ids)
    {
        Example example = new Example(SpecialWorkCertPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("certGlId",ids);
        List<SpecialWorkCertPo> specialWorkCertPoList=specialWorkCertService.selectByExample(example);
        if(!ObjectUtils.isEmpty(specialWorkCertPoList))
        {
            throw new BaseException("存在关联证书信息数据，无法删除!");
        }
        this.deleteByIds(ids);
    }

    @Override
    public CertGlVo updateCertGl(CertGlUpdateForm certGlUpdateForm)
    {
        CertGlPo updateTypePo = dozerMapper.map(certGlUpdateForm, CertGlPo.class);
        super.updateSelectiveById(updateTypePo);//根据ID更新po，值为null的不更新，只更新不为null的值
        CertGlPo certGlPo = this.selectById(updateTypePo.getId());//将数据查询出来
        CertGlVo certGlVo = dozerMapper.map(certGlPo, CertGlVo.class);//po对象转换为Vo对象
        certGlVo.setProjectName(commPlanProjectService.getProject(certGlPo.getProjectId()).getName());
        return certGlVo;
    }

    @Override public CertGlVo selectByCertGlById(Integer id)
    {
        CertGlPo certGlPo=this.selectById(id);

        CertGlVo certGlVo = dozerMapper.map(certGlPo, CertGlVo.class);//po对象转换为Vo对象
        certGlVo.setProjectName(commPlanProjectService.getProject(certGlPo.getProjectId()).getName());
        return certGlVo;
    }
}
