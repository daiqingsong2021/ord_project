package com.wisdom.acm.wf.mapper;

import com.wisdom.acm.wf.po.WfDelegatePo;
import com.wisdom.acm.wf.vo.WfDelegateVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WfDelegateMapper extends CommMapper<WfDelegatePo> {

    /**
     * 查询基本信息
     * @param id
     * @return
     */
    WfDelegateVo selectWfDelegateInfo(@Param("id") Integer id);

    /**
     * 查询流程代理信息
     * @param ids
     * @return
     */
    List<WfDelegateVo> selectWfDelegateVosByIds(@Param("ids") List<Integer> ids);


    /**
     * 查询流程代理信息
     * @param bizTypeCode 业务代码
     * @param assignees 委托人
     * @param dateTime 开始时间
     * @return
     */
    List<WfDelegateVo> selectWfDelegateVosByBizTypeCode(@Param("bizTypeCode") String bizTypeCode, @Param("assignees") List<Integer> assignees,
                                                        @Param("dateTime") Date dateTime);

}
