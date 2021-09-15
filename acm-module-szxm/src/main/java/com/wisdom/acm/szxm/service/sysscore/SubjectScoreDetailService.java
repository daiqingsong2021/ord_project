package com.wisdom.acm.szxm.service.sysscore;

import com.wisdom.acm.szxm.form.sysscore.SubjectScoreAddForm;
import com.wisdom.acm.szxm.form.sysscore.SubjectScoreUpdForm;
import com.wisdom.acm.szxm.po.sysscore.SubjectScoreDetailPo;
import com.wisdom.acm.szxm.vo.sysscore.SubjectScoreItemVo;
import com.wisdom.acm.szxm.vo.sysscore.SubjectScoreVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-12-30 16:54
 * Description：<描述>
 */
public interface SubjectScoreDetailService extends CommService<SubjectScoreDetailPo> {
    /**
     * 获取文档评分列表
     * @param mapWhere
     * @return
     */
    List<SubjectScoreItemVo> selectSubjectItemScore(Map<String, Object> mapWhere);

    /**
     * 获取主观评分
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    SubjectScoreVo selectSubjectScore(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    SubjectScoreItemVo addSubjectScore(SubjectScoreAddForm subjectScoreAddForm);

    SubjectScoreItemVo updateSubjectScore(SubjectScoreUpdForm subjectScoreUpdForm);

    void deleteSubjectScore(List<Integer> ids);

    /**
     * 根据文件id删除主观评分
     * @param fileIds
     */
    void deleteSubjectScoreByFileId(List<Integer> fileIds);
}
