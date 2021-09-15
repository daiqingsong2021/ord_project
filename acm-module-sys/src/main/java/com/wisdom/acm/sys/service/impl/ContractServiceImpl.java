package com.wisdom.acm.sys.service.impl;

import com.google.common.collect.Lists;
import com.wisdom.acm.sys.form.SectionAddForm;
import com.wisdom.acm.sys.mapper.OrgMapper;
import com.wisdom.acm.sys.po.SysOrgPo;
import com.wisdom.acm.sys.service.GCContractService;
import com.wisdom.acm.sys.vo.ProjectTeamVo;
import com.wisdom.acm.sys.vo.SectionVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.webservice.DateUtil;
import com.wisdom.webservice.contract.ContractServiceUtils;
import com.wisdom.webservice.contract.entity.EntityRequestOfSectionEntity;
import com.wisdom.webservice.contract.entity.SectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：wqd
 * Date：2019-09-25 14:32
 * Description：<描述>
 */
@Service
public class ContractServiceImpl extends BaseService<OrgMapper, SysOrgPo> implements GCContractService {
    private static Logger logger = LoggerFactory.getLogger(ContractServiceImpl.class);

    @Override
    public List<SectionVo> queryGCSections() {
        List<SectionEntity> allSection = ContractServiceUtils.getInstance().getAllSection(getEntityRequestOfSectionEntity());
        ArrayList<SectionVo> sectionVos = Lists.newArrayList();
        if (!ObjectUtils.isEmpty(allSection)) {
            for (SectionEntity sectionEntity : allSection) {
                SectionVo sectionVo = new SectionVo();
                sectionVo.setCode(sectionEntity.getSectionCode());
                sectionVo.setName(sectionEntity.getSectionName());
                sectionVo.setPgSectionId(sectionEntity.getSectionUid());
                sectionVos.add(sectionVo);
            }
        } else {
            throw new BaseException("查询品高标段信息错误");
        }
        logger.info("查询品高所有标段成功，数量为:{}", sectionVos.size());
        return sectionVos;
    }

    /**
     * 调用获取标段信息 请求封装
     *
     * @return
     */
    private EntityRequestOfSectionEntity getEntityRequestOfSectionEntity() {
        EntityRequestOfSectionEntity request = new EntityRequestOfSectionEntity();
        request.setIsPaging(true);
        request.setPageIndex(1);
        request.setPageCount(0);
        request.setSortingField("LastUpdateDate Desc");
        request.setLastUpdateDate(DateUtil
                .convertToXMLGregorianCalendar("1970-01-01", DateUtil.DATE_DEFAULT_FORMAT));
        return request;
    }

    @Override
    public void tbSection(List<SectionAddForm> sectionAddForms) {
        if (ObjectUtils.isEmpty(sectionAddForms)) {
            throw new BaseException("导入品高标段信息不能为空");
        }

        List<SysOrgPo> sysOrgPos = Lists.newArrayList();
        List<ProjectTeamVo> projectTeamVos = mapper.selectSections();
        List<String> bidCodes = ListUtil.toValueList(projectTeamVos, "teamCode", String.class);
        int sortNum = projectTeamVos.size();
        for (SectionAddForm sectionAddForm : sectionAddForms) {
            String teamCode = sectionAddForm.getTeamCode();
            teamCode = teamCode.replaceAll("标", "").trim();//将标去掉，并去掉左右空字符
            if (bidCodes.contains(teamCode)) {
                logger.info("该标段已存在库中， 标段编号是 {}", teamCode);
                continue;
            }
            //品高标段信息 类型为标段
            sectionAddForm.setTeamCode(teamCode);
            sortNum++;
            SysOrgPo sysOrgPo = getSysOrgPo(sectionAddForm, sortNum);
            sysOrgPos.add(sysOrgPo);
        }
        if (ObjectUtils.isEmpty(sysOrgPos)) {
            throw new BaseException("可以导入的标段为空");
        }
        this.insert(sysOrgPos);
        logger.info("同步标段成功，成功导入标段数：{}, 请求导入标段数:{}", sysOrgPos.size(), sectionAddForms.size());
    }

    /**
     * 获取入库对象
     *
     * @param sectionAddForm 同步标段对象
     * @param sortNum        排序编号 累加
     * @return org
     */
    private SysOrgPo getSysOrgPo(SectionAddForm sectionAddForm, int sortNum) {
        SysOrgPo org = new SysOrgPo();
        org.setOrgCode(sectionAddForm.getTeamCode());
        org.setOrgName(sectionAddForm.getTeamName());
        org.setParentId(sectionAddForm.getParentId());
        org.setBizType(sectionAddForm.getBizType());
        org.setBizId(sectionAddForm.getBizId());
        org.setExtendedColumn1("section");
        if ("0".equals(sectionAddForm.getIsSupervise())) {
            org.setExtendedColumn2("construction");
        }else if ("1".equals(sectionAddForm.getIsSupervise())){
            org.setExtendedColumn2("supervisor");
        }
        org.setExtendedColumn3(sectionAddForm.getExtendedColumn3());
        org.setExtendedColumn4(sectionAddForm.getExtendedColumn4());
        org.setExtendedColumn5(sectionAddForm.getExtendedColumn5());
        org.setPgSectionId(sectionAddForm.getPgSectionId());
        //默认有效
        org.setStatus(1);
        org.setSort(sortNum);
        return org;
    }

    public static void main(String args[]) {
        String a = "SRT-2-122 标 ".replaceAll("标", "");
        System.out.println(a.trim());
    }
}
