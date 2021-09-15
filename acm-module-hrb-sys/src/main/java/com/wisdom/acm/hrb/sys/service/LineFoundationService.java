package com.wisdom.acm.hrb.sys.service;

import com.wisdom.acm.hrb.sys.form.LineFoundationAddForm;
import com.wisdom.acm.hrb.sys.po.LineFoundationPo;
import com.wisdom.acm.hrb.sys.vo.LineFoundationVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

/**
 * @author zll
 * 2020/10/20/020 10:54
 * Description:<线路管理信息>
 */
public interface LineFoundationService extends CommService<LineFoundationPo> {
    List<LineFoundationVo> addLineFoundationList(List<LineFoundationAddForm> lineFoundationAddForms);
    List<LineFoundationVo> queryLineFoundationList();
    void delLineFoundationList(List<LineFoundationVo>lineFoundationVos);
    String checkLineFoundationIsHave(String line);
}
