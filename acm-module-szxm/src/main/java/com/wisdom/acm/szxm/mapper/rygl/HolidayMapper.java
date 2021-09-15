package com.wisdom.acm.szxm.mapper.rygl;

import com.wisdom.acm.szxm.po.rygl.HolidayPo;
import com.wisdom.acm.szxm.vo.rygl.HolidayVo;
import com.wisdom.acm.szxm.vo.rygl.LwryHolidayVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HolidayMapper extends CommMapper<HolidayPo>
{
    List<HolidayVo> queryHisTory(Map<String, Object> mapWhere);

    /**
     * 查询请假列表
     * @param mapWhere
     * @return
     */
    List<HolidayVo> selectHolidayList(Map<String, Object> mapWhere);

    /**
     * 查询某一天的请假记录
     * @param holiQueryMap
     * @return
     */
    List<HolidayVo> selectInOneDate(Map<String, Object> holiQueryMap);

    /**
     * 检查请假日期和创建日期超过1天数量
     * @param sectionId
     * @return
     */
    Integer queryHolidayCount(@Param("sectionId")String sectionId);
    List<HolidayVo> queryLwryHolidayRecord(Map<String, Object> mapWhere);
    List<LwryHolidayVo> selectSectionLwryHolidayList(Map<String, Object> mapWhere);
}
