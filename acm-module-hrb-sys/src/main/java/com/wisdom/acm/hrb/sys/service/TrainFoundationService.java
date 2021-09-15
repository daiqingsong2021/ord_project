package com.wisdom.acm.hrb.sys.service;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.hrb.sys.form.TrainFoundationAddForm;
import com.wisdom.acm.hrb.sys.form.TrainFoundationUpdateForm;
import com.wisdom.acm.hrb.sys.po.TrainFoundationPo;
import com.wisdom.acm.hrb.sys.vo.TrainFoundationVo;
import com.wisdom.base.common.service.CommService;
import java.util.List;
public interface TrainFoundationService extends CommService<TrainFoundationPo> {
    TrainFoundationVo addTrainFoundation(TrainFoundationAddForm trainFoundationAddForm);
    void delTrainFoundationList(List<Integer> ids);
    TrainFoundationVo updateTrainFoundation(TrainFoundationUpdateForm trainFoundationUpdateForm);
    PageInfo<TrainFoundationVo> queryTrainFoundationList(String line, String trainCode,Integer pageSize, Integer currentPageNum);
    List<TrainFoundationVo> getTrainFoundationVos(String line,String trainCode);
    String checkTrainFoundationIsHave(String line,String trainCode);
}
