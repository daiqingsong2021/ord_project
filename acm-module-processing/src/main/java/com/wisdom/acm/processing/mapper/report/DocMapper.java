package com.wisdom.acm.processing.mapper.report;

import com.wisdom.acm.processing.po.report.DocPo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zll
 * 2020/8/20/020 11:04
 * Description:<描述>
 */
public interface DocMapper extends CommMapper<DocPo> {
    void updateFileStatus(@Param("fileId") Integer fileId);
}
