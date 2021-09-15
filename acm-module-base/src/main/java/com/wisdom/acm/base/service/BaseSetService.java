package com.wisdom.acm.base.service;

import com.wisdom.acm.base.form.set.BaseSetDocUpdateForm;
import com.wisdom.acm.base.form.set.BaseSetProjectUpdateForm;
import com.wisdom.acm.base.form.set.BaseSetTimeUdateForm;
import com.wisdom.acm.base.po.BaseSetPo;
import com.wisdom.acm.base.vo.BaseBoVo;
import com.wisdom.acm.base.vo.set.BaseSetBoVo;
import com.wisdom.acm.base.vo.set.BaseSetDocVo;
import com.wisdom.acm.base.vo.set.BaseSetProjectVo;
import com.wisdom.acm.base.vo.set.BaseSetTimeVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface BaseSetService extends CommService<BaseSetPo> {

    List<BaseSetBoVo> queryBoList();

    /**
     * 修改项目全局设置
     * @param baseSetForm
     */
    void updateProject(BaseSetProjectUpdateForm baseSetForm);

    /**
     * 修改文档全局设置
     * @param baseSetForm
     */
    void updateDoc(BaseSetDocUpdateForm baseSetForm);

    /**
     * 修改时间设置
     * @param baseSetForm
     */
    void updateTime(BaseSetTimeUdateForm baseSetForm);

    /**
     * 修改客车运营时间
     * @param dates
     */
    void updateTrainTime(Map<String,String> dates);

    /**
     * 项目全局设置信息
     * @return
     */
    BaseSetProjectVo getProjectInfo();

    /**
     * 文档全局设置信息
     * @return
     */
    BaseSetDocVo getDocInfo();

    /**
     * 时间全局设置信息
     * @return
     */
    BaseSetTimeVo getTimeInfo();

    BaseSetPo getBaseSetPoByBoCodeAndKey(String boCode, String key);

    List<BaseSetPo> queryBaseSetPoByCodeAndKey(String boCode, String bsKey);

    List<BaseBoVo> getTrainTime();
}
