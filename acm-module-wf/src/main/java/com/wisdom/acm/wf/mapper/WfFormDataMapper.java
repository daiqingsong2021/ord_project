package com.wisdom.acm.wf.mapper;

import com.wisdom.acm.wf.po.WfFormDataPo;
import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.vo.wf.WfFormDataVo;
import com.wisdom.base.common.vo.wf.WfFormProcVo;
import com.wisdom.base.common.vo.wf.WfProcessBizVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WfFormDataMapper extends CommMapper<WfFormDataPo> {

    /**
     * 通过流程实例查询bizId集合
     * @param procInstId
     * @return
     */
    List<WfProcessBizVo> selectWfDataBizIds(@Param("procInstId") Integer procInstId);

    /**
     * 根据业务查询流程信息
     * @param bizIds 业务Id
     * @param bizType 业务类型
     * @return
     */
    List<WfFormProcVo> selectProcInstIdByBizs(@Param("bizIds") List<Integer> bizIds, @Param("bizType") String bizType);

    /**
     * 根据业务查询流程信息
     * @param bizId 业务Id
     * @param bizType 业务类型
     * @return
     */
    WfFormProcVo selectProcInstIdByBiz(@Param("bizId") Integer bizId, @Param("bizType") String bizType);

    /**
     * 根据流程实例id获取流程业务数据集合
     *
     * @param procInstId
     * @return
     */
    List<WfFormDataVo> selectFormDataVosByProcInst(@Param("procInstId") String procInstId);

    /**
     * 根据流程表单id获取流程业务数据集合
     *
     * @param formId
     * @return
     */
    List<WfFormDataVo> selectFormDataVosByFormId(@Param("formId") String formId);
}
