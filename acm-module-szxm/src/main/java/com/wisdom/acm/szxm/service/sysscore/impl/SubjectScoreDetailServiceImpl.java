package com.wisdom.acm.szxm.service.sysscore.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.StringHelper;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.form.sysscore.SubjectScoreAddForm;
import com.wisdom.acm.szxm.form.sysscore.SubjectScoreUpdForm;
import com.wisdom.acm.szxm.mapper.sysscore.SubjectScoreDetailMapper;
import com.wisdom.acm.szxm.po.sysscore.ObjectTemplatePo;
import com.wisdom.acm.szxm.po.sysscore.SubjectScoreDetailPo;
import com.wisdom.acm.szxm.service.sysscore.SubjectScoreDetailService;
import com.wisdom.acm.szxm.vo.sysscore.SubjectScoreItemVo;
import com.wisdom.acm.szxm.vo.sysscore.SubjectScoreVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.feign.plan.project.CommPlanProjectService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.vo.ProjectTeamVo;
import com.wisdom.base.common.vo.UserInfo;
import com.wisdom.base.common.vo.plan.project.PlanProjectVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-12-30 17:01
 * Description：<描述>
 */
@Service
@Slf4j
public class SubjectScoreDetailServiceImpl extends BaseService<SubjectScoreDetailMapper, SubjectScoreDetailPo> implements SubjectScoreDetailService {
    @Autowired
    private CommUserService commUserService;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private CommPlanProjectService commPlanProjectService;

    @Override
    public List<SubjectScoreItemVo> selectSubjectItemScore(Map<String, Object> mapWhere) {
        String projectId = StringHelper.formattString(mapWhere.get("projectId"));
        String sectionId = StringHelper.formattString(mapWhere.get("sectionId"));
        if (StringHelper.isNullAndEmpty(projectId) || StringHelper.isNullAndEmpty(sectionId)) {
            throw new BaseException("项目id或标段id不能为空");
        }

        List<SubjectScoreItemVo> subjectScoreItemVos = mapper.selectSubjectScore(mapWhere);
        return subjectScoreItemVos;
    }

    @Override
    public SubjectScoreVo selectSubjectScore(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum) {
        String projectId = StringHelper.formattString(mapWhere.get("projectId"));
        String sectionId = StringHelper.formattString(mapWhere.get("sectionId"));
        if (StringHelper.isNullAndEmpty(projectId) || StringHelper.isNullAndEmpty(sectionId)) {
            throw new BaseException("项目id或标段id不能为空");
        }
        String year = StringHelper.formattString(mapWhere.get("year"));
        String month = StringHelper.formattString(mapWhere.get("month"));
        if (StringHelper.isNullAndEmpty(year) || StringHelper.isNullAndEmpty(month)) {
            throw new BaseException("查询评分年、月不能为空");
        }
        //查询所有，计算所有文档平均星级
        List<SubjectScoreItemVo> subjectScoreItemVos = mapper.selectSubjectScore(mapWhere);
        if (ObjectUtils.isEmpty(subjectScoreItemVos)) {
            log.info("标段id为{}下暂无待考核文档", sectionId);
            return null;
        }
        BigDecimal total = new BigDecimal(0);
        for (SubjectScoreItemVo subjectScoreItemVo : subjectScoreItemVos) {
            if (ObjectUtils.isEmpty(subjectScoreItemVo.getScore())) {
                // 未评分 -- 默认4分
                total = total.add(new BigDecimal(4));
                continue;
            }
            total = total.add(subjectScoreItemVo.getScore());
        }
        BigDecimal average = total.divide(new BigDecimal(subjectScoreItemVos.size()), 2, BigDecimal.ROUND_HALF_UP);
        //文档评分最大是5，换算为百分制 ==》 乘20
        BigDecimal score = average.multiply(new BigDecimal(20));
        SubjectScoreVo subjectScoreVo = new SubjectScoreVo();
        subjectScoreVo.setTotalScore(new BigDecimal(100));
        subjectScoreVo.setActualScore(score);

        PageHelper.startPage(currentPageNum, pageSize);
        PageInfo<SubjectScoreItemVo> pageInfo = new PageInfo<>(subjectScoreItemVos);
        subjectScoreVo.setSubjectScoreItemVos(pageInfo);
        subjectScoreVo.setProjectId(Integer.parseInt(projectId));
        subjectScoreVo.setSectionId(Integer.parseInt(sectionId));


        //项目名称
        PlanProjectVo projectVo = commPlanProjectService.getProject(Integer.parseInt(projectId));
        subjectScoreVo.setProjectName(projectVo.getName());
        //标段缓存
        Map<Integer, ProjectTeamVo> sectionMap = szxmCommonUtil.getSectionMap(projectVo.getId());
        ProjectTeamVo sectionVo = sectionMap.get(sectionId);
        if (!ObjectUtils.isEmpty(sectionVo)) {
            subjectScoreVo.setSectionCode(sectionVo.getCode());
            subjectScoreVo.setSectionName(sectionVo.getName());
        }
        return subjectScoreVo;
    }

    @Override
    public SubjectScoreItemVo addSubjectScore(SubjectScoreAddForm subjectScoreAddForm) {
        SubjectScoreDetailPo subjectScoreDetailPo = dozerMapper.map(subjectScoreAddForm, SubjectScoreDetailPo.class);
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(new Date());
        int year = gregorianCalendar.get(Calendar.YEAR);
        //月份从0开始 范围是0-11
        int month = gregorianCalendar.get(Calendar.MONTH) + 1;
        int day = gregorianCalendar.get(Calendar.DATE);
        //判断评分所属评分月份，评分范围（上月25 -- 本月24）
        if (day > 24) {
            if (month + 1 > 12) {
                year = ++year;
                month = 1;
            } else {
                month = ++month;
            }
        }
        Example example = new Example(SubjectScoreDetailPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fileId", subjectScoreAddForm.getFileId());
        List<SubjectScoreDetailPo> subjectScoreDetailPos = super.selectByExample(example);
        if (!ObjectUtils.isEmpty(subjectScoreDetailPos)) {
            throw new BaseException("无法为一个文件创建多个评分");
        }
        subjectScoreDetailPo.setYear(year);
        subjectScoreDetailPo.setMonth(month);
        subjectScoreDetailPo.setUploadFileTime(new Date());
        super.insert(subjectScoreDetailPo);

        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("id", subjectScoreDetailPo.getId());
        List<SubjectScoreItemVo> subjectScoreItemVos = mapper.selectSubjectScore(mapWhere);
        if (ObjectUtils.isEmpty(subjectScoreItemVos)) {
            log.error("获取主观评分异常");
            return null;
        }
        return subjectScoreItemVos.get(0);
    }

    @Override
    public SubjectScoreItemVo updateSubjectScore(SubjectScoreUpdForm subjectScoreUpdForm) {
        SubjectScoreDetailPo subjectScoreDetailPo = dozerMapper.map(subjectScoreUpdForm, SubjectScoreDetailPo.class);
        //获取当前登录人信息
        UserInfo userInfo = commUserService.getLoginUser();
        subjectScoreDetailPo.setRaterId(userInfo.getId());
        subjectScoreDetailPo.setScoreTime(new Date());

        mapper.updateSubjectScoreByFileId(subjectScoreDetailPo);

        Map<String, Object> mapWhere = Maps.newHashMap();
        mapWhere.put("fileId", subjectScoreDetailPo.getFileId());
        List<SubjectScoreItemVo> subjectScoreItemVos = mapper.selectSubjectScore(mapWhere);
        if (ObjectUtils.isEmpty(subjectScoreItemVos)) {
            log.error("获取主观评分异常");
            return null;
        }
        return subjectScoreItemVos.get(0);
    }

    @Override
    public void deleteSubjectScore(List<Integer> ids) {
        this.deleteByIds(ids);
    }

    @Override
    public void deleteSubjectScoreByFileId(List<Integer> fileIds) {
        mapper.deleteSubjectScoreByFileId(fileIds);
    }
}
