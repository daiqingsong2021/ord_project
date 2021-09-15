package com.wisdom.acm.szxm.service.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.rygl.SpecialWorkerAddForm;
import com.wisdom.acm.szxm.form.rygl.SpecialWorkerUpdateForm;
import com.wisdom.acm.szxm.po.rygl.SpecialWorkerPo;
import com.wisdom.acm.szxm.vo.rygl.SpecialWorkerVo;
import com.wisdom.acm.szxm.vo.rygl.WarnVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface SpecialWorkerService extends CommService<SpecialWorkerPo>
{

    /**
     * 特殊工种查询列表
     * @param mapWhere 业务查询条件
     * @param sectionList 查询标段List
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    PageInfo<SpecialWorkerVo> selectSpecialWorkerList(Map<String,Object> mapWhere, List<String> sectionList,Integer pageSize, Integer currentPageNum);

    /**
     * 获取证书预警信息
     * @param mapWhere
     * @return
     */
    WarnVo getWarningInformation(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    /**
     * 查询特殊工种 流程用
     * @param mapWhere
     * @param sectionList
     * @return
     */
    List<SpecialWorkerVo> getFlowSpecialWorkerList(Map<String, Object> mapWhere, List<String> sectionList);

    /**
     * 增加特殊工种
     * @param specialWorkerAddForm
     * @return
     */
    SpecialWorkerVo addSpecialWorker(SpecialWorkerAddForm specialWorkerAddForm);

    /**
     * 删除特殊工种
     * @param ids
     */
    void deleteSpecialWorker(List<Integer> ids);

    /**
     * 更新特殊工种
     * @param specialWorkerUpdateForm
     * @return
     */
    SpecialWorkerVo updateSpecialWorker(SpecialWorkerUpdateForm specialWorkerUpdateForm);

    SpecialWorkerVo selectBySpecialWorkerId(Integer id);

    /**
     * 检查特殊工种进场审批通过日期与证书数据创建日期超过7天数量
     * @param sectionId
     * @return
     */
    int querySpecialWorkerCount(String sectionId);
}
