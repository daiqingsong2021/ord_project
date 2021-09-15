package com.wisdom.acm.szxm.mapper.sysscore;

import com.wisdom.acm.szxm.po.sysscore.SubjectScoreDetailPo;
import com.wisdom.acm.szxm.vo.sysscore.SubjectScoreItemVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-12-30 16:52
 * Description：<描述>
 */
public interface SubjectScoreDetailMapper extends CommMapper<SubjectScoreDetailPo> {
    List<SubjectScoreItemVo> selectSubjectScore(Map<String, Object> mapWhere);

    /**
     * 根据fileid更新评分
     * @param subjectScoreDetailPo
     * @return
     */
    int updateSubjectScoreByFileId(SubjectScoreDetailPo subjectScoreDetailPo);

    /**
     * 根据文档id删除主观评分
     * @param fileIds
     */
    void deleteSubjectScoreByFileId(@Param("fileIds") List<Integer> fileIds);
}
