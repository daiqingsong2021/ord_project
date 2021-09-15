package com.wisdom.acm.szxm.service.rygl;

import com.wisdom.acm.szxm.po.rygl.GlryKqRecordPo;
import com.wisdom.acm.szxm.po.rygl.LwryKqRecordPo;

import java.util.List;

/**
 * Author：wqd
 * Date：2019-12-19 11:30
 * Description：<描述>
 */
public interface KqRecordService {
    /**
     * 保存考勤记录
     * @param glryKqRecordPos 管理人员考勤
     * @param lwryKqRecordPos 劳务人员考勤
     */
    void saveRykqRecord(List<GlryKqRecordPo> glryKqRecordPos, List<LwryKqRecordPo> lwryKqRecordPos);

    void hzkqRecord(String startDate,String endDate, String type);
}
