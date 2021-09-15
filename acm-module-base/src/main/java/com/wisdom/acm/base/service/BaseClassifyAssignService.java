package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.classify.BaseClassifyAssignForm;
import com.wisdom.acm.base.form.classify.BaseUpdateClassifyAssignForm;
import com.wisdom.acm.base.po.BaseClassifyAssignPo;
import com.wisdom.acm.base.vo.classify.BaseClassifyAssignVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface BaseClassifyAssignService extends CommService<BaseClassifyAssignPo> {

    /**
     * 根据业务模块代码和业务数据id查找分类码页签数据
     * @param boCode
     * @param boId
     * @return
     */
    public List<BaseClassifyAssignVo> queryClassifyAssignListByBoCodeAndBoId(String boCode, Integer boId);

    /**
     * 分配分类码
     * @param classifyAssignForm
     * @return
     */
    public BaseClassifyAssignVo assignClassify(BaseClassifyAssignForm classifyAssignForm);

    BaseClassifyAssignVo updateAssignClassify(BaseUpdateClassifyAssignForm updateClassifyAssignForm);

    /**
     * 删除分类码分配
     * @param ids
     */
    void deleteClassifyAssign(List<Integer> ids);

    String queryClassifyNamesByIds(List<Integer> ids);

    List<BaseClassifyAssignPo> queryClassifyAssignByClassifyId(List<Integer> classifyIds);
}
