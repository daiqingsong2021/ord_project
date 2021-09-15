package com.wisdom.acm.szxm.service.rygl;

import com.wisdom.acm.szxm.po.rygl.ProjInfoPo;
import com.wisdom.acm.szxm.vo.rygl.PeopleVo;
import com.wisdom.acm.szxm.vo.rygl.ProjInfoVo;
import com.wisdom.acm.szxm.vo.rygl.KqRecordVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

/**
 * 中控考勤Servie
 */
public interface ZkService
{

    /**
     * 批量增加或者更新考勤系统组织机构
     * @param projInfoVoList
     * @return 1 成功 0 失败
     */
    public int addOrUpdateZkOrg(List<ProjInfoVo> projInfoVoList);


    public int addOrUpdateZkOrg(ProjInfoVo projInfoVo);

    /**
     * 删除考勤系统组织机构
     * @param projInfoVo
     * @return 1 成功 0 失败
     */
    public int deleteZkOrg(ProjInfoVo projInfoVo);

    /**
     * 批量增加或者更新考勤系统人员
     * @param peopleVoList
     * @return 1 成功 0 失败
     */
    public int addOrUpdateZkPeole(List<PeopleVo> peopleVoList);

    public int addOrUpdateZkPeole(PeopleVo peopleVo);

    /**
     * 删除考勤系统人员
     * @param peopleId
     * @return 1 成功 0 失败
     */
    public int deleteZkPeole(Integer peopleId);

    /**
     * 根据条件查询考勤系统考勤记录
     * @param queryMap
     * @return
     */
    public List<KqRecordVo> getZkRecords(Map<String,String> queryMap);

}
