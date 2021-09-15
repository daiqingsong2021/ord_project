package com.wisdom.acm.szxm.service.rygl.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.form.rygl.AllKqConfigForm;
import com.wisdom.acm.szxm.form.rygl.KqConfigAddForm;
import com.wisdom.acm.szxm.form.rygl.KqConfigUpdateForm;
import com.wisdom.acm.szxm.mapper.rygl.KqConfigMapper;
import com.wisdom.acm.szxm.po.rygl.KqConfigPo;
import com.wisdom.acm.szxm.service.rygl.KqConfigService;
import com.wisdom.acm.szxm.vo.rygl.KqConfigVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommProjectTeamService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.calc.calendar.PmCalendar;
import com.wisdom.base.common.vo.ProjectTeamVo;
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
public class KqConfigServiceImpl extends BaseService<KqConfigMapper, KqConfigPo> implements KqConfigService
{

    @Autowired
    private CommProjectTeamService commProjectTeamService;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private CommPlanProjectService commPlanProjectService;

    @Override
    public PageInfo<KqConfigVo> selectKqConfigList(Map<String, Object> mapWhere, List<String> sectionList,
            Integer pageSize, Integer currentPageNum)
    {
        Example example = new Example(KqConfigPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId",String.valueOf(mapWhere.get("projectId")));
        criteria.andEqualTo("type","1");//只查询标段的考勤
        criteria.andIn("sectionId", sectionList);//标段IN
        example.setOrderByClause("CREAT_TIME desc");
        PageHelper.startPage(currentPageNum, pageSize);
        List<KqConfigPo> kqConfigPoList =this.selectByExample(example);
        PageInfo<KqConfigPo> pageInfo_ = new PageInfo<KqConfigPo>(kqConfigPoList);
        List<KqConfigVo> kqConfigVoList= Lists.newArrayList();
        PlanProjectVo projectVo=commPlanProjectService.getProject(Integer.valueOf(String.valueOf(mapWhere.get("projectId"))));

        Map<Integer,ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
        for(KqConfigPo kqConfigPo:kqConfigPoList)
        {
            KqConfigVo kqConfigVo=new KqConfigVo();
            dozerMapper.map(kqConfigPo, kqConfigVo);//po对象转换为Vo对象
            kqConfigVo.getMangerKqVo().setCode(kqConfigPo.getMangerkq());
            if("0".equals(kqConfigPo.getMangerkq()))
                kqConfigVo.getMangerKqVo().setName("否");
            else
                kqConfigVo.getMangerKqVo().setName("是");

            kqConfigVo.getWorkerKqVo().setCode(kqConfigPo.getWorkerkq());
            if("0".equals(kqConfigPo.getWorkerkq()))
                kqConfigVo.getWorkerKqVo().setName("否");
            else
                kqConfigVo.getWorkerKqVo().setName("是");

            kqConfigVo.getCalenderVo().setCode(String.valueOf(kqConfigPo.getCalenderId()));
            PmCalendar calendar= szxmCommonUtil.getPmCalendar(kqConfigPo.getCalenderId());
            kqConfigVo.getCalenderVo().setName(calendar.getClndrName());//根据日历ID查询日历名称

            ProjectTeamVo sectionVo = sectionMap.get(kqConfigPo.getSectionId());
            kqConfigVo.setSectionCode(sectionVo.getCode());
            kqConfigVo.setSectionName(sectionVo.getName());
            kqConfigVo.setProjectName(projectVo.getName());
            kqConfigVoList.add(kqConfigVo);

        }

        PageInfo<KqConfigVo> pageInfo = new PageInfo<KqConfigVo>(kqConfigVoList);
        szxmCommonUtil.cooyPageInfo(pageInfo_,pageInfo);
        return pageInfo;
    }

    @Override public KqConfigVo addKqConfig(KqConfigAddForm kqConfigAddForm)
    {
        //标段下的考勤配置只能有一条，否则报错，给与提示
        Example example = new Example(KqConfigPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId",kqConfigAddForm.getProjectId());
        criteria.andEqualTo("sectionId",kqConfigAddForm.getSectionId());
        KqConfigPo existPo= this.selectOneByExample(example);
        if(!ObjectUtils.isEmpty(existPo))
        {
            throw new BaseException("该标段已存在相关的考勤配置");
        }
        KqConfigPo kqConfigPo = dozerMapper.map(kqConfigAddForm, KqConfigPo.class);
        super.insert(kqConfigPo);

        KqConfigVo kqConfigVo = dozerMapper.map(kqConfigPo, KqConfigVo.class);//po对象转换为Vo对象
        ProjectTeamVo sectionVo = commProjectTeamService.getProjectTeamById(kqConfigPo.getSectionId());
        kqConfigVo.setSectionCode(sectionVo.getCode());
        kqConfigVo.setSectionName(sectionVo.getName());
        PlanProjectVo projectVo=commPlanProjectService.getProject(kqConfigPo.getProjectId());
        kqConfigVo.setProjectName(projectVo.getName());

        kqConfigVo.getMangerKqVo().setCode(kqConfigPo.getMangerkq());
        if("0".equals(kqConfigPo.getMangerkq()))
            kqConfigVo.getMangerKqVo().setName("否");
        else
            kqConfigVo.getMangerKqVo().setName("是");

        kqConfigVo.getWorkerKqVo().setCode(kqConfigPo.getWorkerkq());
        if("0".equals(kqConfigPo.getWorkerkq()))
            kqConfigVo.getWorkerKqVo().setName("否");
        else
            kqConfigVo.getWorkerKqVo().setName("是");

        PmCalendar calendar= szxmCommonUtil.getPmCalendar(kqConfigPo.getCalenderId());
        kqConfigVo.getCalenderVo().setName(calendar.getClndrName());//根据日历ID查询日历名称

        return kqConfigVo;
    }

    @Override public void deleteKqConfig(List<Integer> ids)
    {
        this.deleteByIds(ids);
    }

    @Override public KqConfigVo updateKqConfig(KqConfigUpdateForm kqConfigUpdateForm)
    {
        KqConfigPo updateConfigPo = dozerMapper.map(kqConfigUpdateForm, KqConfigPo.class);
        super.updateSelectiveById(updateConfigPo);//根据ID更新po，值为null的不更新，只更新不为null的值

        KqConfigPo kqConfigPo = this.selectById(updateConfigPo.getId());//将数据查询出来
        KqConfigVo kqConfigVo = dozerMapper.map(kqConfigPo, KqConfigVo.class);//po对象转换为Vo对象
        ProjectTeamVo sectionVo = commProjectTeamService.getProjectTeamById(kqConfigPo.getSectionId());
        kqConfigVo.setSectionCode(sectionVo.getCode());
        kqConfigVo.setSectionName(sectionVo.getName());
        PlanProjectVo projectVo=commPlanProjectService.getProject(kqConfigPo.getProjectId());
        kqConfigVo.setProjectName(projectVo.getName());

        kqConfigVo.getMangerKqVo().setCode(kqConfigPo.getMangerkq());
        if("0".equals(kqConfigPo.getMangerkq()))
            kqConfigVo.getMangerKqVo().setName("否");
        else
            kqConfigVo.getMangerKqVo().setName("是");

        kqConfigVo.getWorkerKqVo().setCode(kqConfigPo.getWorkerkq());
        if("0".equals(kqConfigPo.getWorkerkq()))
            kqConfigVo.getWorkerKqVo().setName("否");
        else
            kqConfigVo.getWorkerKqVo().setName("是");

        PmCalendar calendar= szxmCommonUtil.getPmCalendar(kqConfigPo.getCalenderId());
        kqConfigVo.getCalenderVo().setName(calendar.getClndrName());//根据日历ID查询日历名称

        return kqConfigVo;
    }

    @Override public KqConfigVo selectByKqConfigId(Integer id)
    {
        KqConfigPo kqConfigPo=this.selectById(id);
        KqConfigVo kqConfigVo = dozerMapper.map(kqConfigPo, KqConfigVo.class);//po对象转换为Vo对象
        ProjectTeamVo sectionVo = commProjectTeamService.getProjectTeamById(kqConfigPo.getSectionId());
        kqConfigVo.setSectionCode(sectionVo.getCode());
        kqConfigVo.setSectionName(sectionVo.getName());
        PlanProjectVo projectVo=commPlanProjectService.getProject(kqConfigPo.getProjectId());
        kqConfigVo.setProjectName(projectVo.getName());

        kqConfigVo.getMangerKqVo().setCode(kqConfigPo.getMangerkq());
        if("0".equals(kqConfigPo.getMangerkq()))
            kqConfigVo.getMangerKqVo().setName("否");
        else
            kqConfigVo.getMangerKqVo().setName("是");

        kqConfigVo.getWorkerKqVo().setCode(kqConfigPo.getWorkerkq());
        if("0".equals(kqConfigPo.getWorkerkq()))
            kqConfigVo.getWorkerKqVo().setName("否");
        else
            kqConfigVo.getWorkerKqVo().setName("是");

        PmCalendar calendar= szxmCommonUtil.getPmCalendar(kqConfigPo.getCalenderId());
        kqConfigVo.getCalenderVo().setName(calendar.getClndrName());//根据日历ID查询日历名称

        return kqConfigVo;
    }

    @Override public KqConfigVo saveAllKqConfig(AllKqConfigForm allKqConfigForm)
    {
        KqConfigPo kqConfigPo =new KqConfigPo();
        if(ObjectUtils.isEmpty(allKqConfigForm.getId()))
        {
            //增加
            kqConfigPo = dozerMapper.map(allKqConfigForm, KqConfigPo.class);
            super.insert(kqConfigPo);

        }
        else
        {
            //修改
            KqConfigPo updateConfigPo = dozerMapper.map(allKqConfigForm, KqConfigPo.class);
            super.updateSelectiveById(updateConfigPo);//根据ID更新po，值为null的不更新，只更新不为null的值

             kqConfigPo = this.selectById(updateConfigPo.getId());//将数据查询出来
        }

        KqConfigVo kqConfigVo = dozerMapper.map(kqConfigPo, KqConfigVo.class);//po对象转换为Vo对象
        kqConfigVo.getMangerKqVo().setCode(kqConfigPo.getMangerkq());
        if("0".equals(kqConfigPo.getMangerkq()))
            kqConfigVo.getMangerKqVo().setName("否");
        else
            kqConfigVo.getMangerKqVo().setName("是");

        kqConfigVo.getWorkerKqVo().setCode(kqConfigPo.getWorkerkq());
        if("0".equals(kqConfigPo.getWorkerkq()))
            kqConfigVo.getWorkerKqVo().setName("否");
        else
            kqConfigVo.getWorkerKqVo().setName("是");

        PmCalendar calendar= szxmCommonUtil.getPmCalendar(kqConfigPo.getCalenderId());
        kqConfigVo.getCalenderVo().setName(calendar.getClndrName());//根据日历ID查询日历名称

        return kqConfigVo;
    }

    @Override public KqConfigVo getAllKqConfig()
    {
        Example example = new Example(KqConfigPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type","0");//查询全局的考勤配置
        KqConfigPo kqConfigPo=this.selectOneByExample(example);

        KqConfigVo kqConfigVo=new KqConfigVo();
        if(ObjectUtils.isEmpty(kqConfigPo))
        {
            //未查到说明是初始化
            kqConfigVo.setType("0");
            kqConfigVo.getWorkerKqVo().setCode("1");
            kqConfigVo.getWorkerKqVo().setName("是");
            kqConfigVo.getMangerKqVo().setCode("1");
            kqConfigVo.getMangerKqVo().setName("是");
            //查询默认日历ID以及名称
            PmCalendar calendar= szxmCommonUtil.getDefaultPmCalendar();
            kqConfigVo.getCalenderVo().setCode(calendar.getId());
            kqConfigVo.getCalenderVo().setName(calendar.getClndrName());
            kqConfigVo.setCalenderId(Integer.valueOf(calendar.getId()));

        }
        else
        {
            //存在，直接转换返回
            kqConfigVo = dozerMapper.map(kqConfigPo, KqConfigVo.class);//po对象转换为Vo对象
            kqConfigVo.getMangerKqVo().setCode(kqConfigPo.getMangerkq());
            if("0".equals(kqConfigPo.getMangerkq()))
                kqConfigVo.getMangerKqVo().setName("否");
            else
                kqConfigVo.getMangerKqVo().setName("是");

            kqConfigVo.getWorkerKqVo().setCode(kqConfigPo.getWorkerkq());
            if("0".equals(kqConfigPo.getWorkerkq()))
                kqConfigVo.getWorkerKqVo().setName("否");
            else
                kqConfigVo.getWorkerKqVo().setName("是");

            PmCalendar calendar= szxmCommonUtil.getPmCalendar(kqConfigPo.getCalenderId());
            kqConfigVo.getCalenderVo().setName(calendar.getClndrName());//根据日历ID查询日历名称
        }
        return kqConfigVo;
    }
}
