package com.wisdom.acm.sys.service;

import com.wisdom.acm.sys.form.SysViewAddForm;
import com.wisdom.acm.sys.form.SysViewUpdateForm;
import com.wisdom.acm.sys.po.SysViewPo;
import com.wisdom.acm.sys.vo.SysViewTreeVo;
import com.wisdom.acm.sys.vo.SysViewVo;
import com.wisdom.base.common.service.CommService;
import com.wisdom.base.common.vo.UserInfo;

import java.util.List;

public interface SysViewService extends CommService<SysViewPo> {


    List<SysViewVo> findViewByUser(UserInfo user, String bizType);

    List<SysViewTreeVo> querySysViewTreeByUser(UserInfo user, String bizType);

    void setDefaultView(Integer userId, Integer viewId, String bizObj);

    void setUserView2GlobalView(Integer viewId);

    SysViewPo saveView(SysViewAddForm sysViewAddForm);

    SysViewPo updateView(SysViewUpdateForm sysViewUpdateForm);

    SysViewPo updateViewName(Integer viewId, String viewName);

    void deleteViews(List<Integer> ids);

    SysViewVo getSysViewVoById(Integer viewId);
}
