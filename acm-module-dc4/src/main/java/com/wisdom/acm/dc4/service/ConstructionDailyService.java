package com.wisdom.acm.dc4.service;


import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc4.form.ConstructionDailyAddForm;
import com.wisdom.acm.dc4.form.ConstructionDailyUpdateForm;
import com.wisdom.acm.dc4.po.ConstructionDailyPo;
import com.wisdom.acm.dc4.vo.ConstructionDailyVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

/**
 * ConstructionDailyService interface
 * @author  chenhai
 * @data 2020/08/13
 * 施工日况
 */

public interface ConstructionDailyService   extends CommService<ConstructionDailyPo> {


    /**
     *
     * @param constructionDailyAddForm
     * @return
     */
    ConstructionDailyVo addConstructionDaily(ConstructionDailyAddForm constructionDailyAddForm);

    /**
     * 根据线路和时间查询施工日况
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    PageInfo<ConstructionDailyVo> selectConstructionDailyList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    void deleteConstructionDaily(Integer id);

    ConstructionDailyVo updateConstructionDaily(ConstructionDailyUpdateForm constructionDailyUpdateForm);

    ConstructionDailyVo selectById(Integer id);

    List<ConstructionDailyVo> selectByParams(Map<String, Object> mapWhere);

    /**
     * 审批结束流程回调
     * @param ids 业务IDs
     */
    void approveConstructionDailyWorkFlow(String bizType, List<Integer> ids);

    void approvedConstructionDaily(List<Integer> ids);

    /**
     * 通过rc方式添加数据
     */
    void addRcConstructionDaily();

}
