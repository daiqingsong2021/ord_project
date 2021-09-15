package com.wisdom.acm.szxm.service.rygl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.rygl.HolidayAddForm;
import com.wisdom.acm.szxm.form.rygl.HolidayUpdateForm;
import com.wisdom.acm.szxm.po.rygl.HolidayPo;
import com.wisdom.acm.szxm.vo.rygl.HolidayVo;
import com.wisdom.acm.szxm.vo.rygl.LwryHolidayVo;
import com.wisdom.base.common.service.CommService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HolidayService extends CommService<HolidayPo>
{

    PageInfo<HolidayVo> selectHolidayList(Map<String, Object> mapWhere, List<String> sectionList, Integer pageSize,
            Integer currentPageNum);

    HolidayVo addHoliday(HolidayAddForm holidayAddForm);

    void deleteHoliday(List<Integer> ids);

    HolidayVo updateHoliday(HolidayUpdateForm kqConfigUpdateForm);

    HolidayVo selectByHolidayId(Integer id);

    int getDaysBySIdAndTime(Integer projectId,Integer sectionId, Date startTime, Date endTime);

    /**
     * 查询流程请假信息
     * @param mapWhere
     * @param sectionList
     * @return
     */
    List<HolidayVo> selectFlowHolidayList(Map<String, Object> mapWhere, List<String> sectionList);

    /**
     * 查询某一天的所有请假记录
     * @param holiQueryMap
     * @return
     */
    List<HolidayVo> selectInOneDate(Map<String, Object> holiQueryMap);

    /**
     * 请假审批完成后事件
     * @param ids
     */
    void approveHolidayFlow(String bizType, List<Integer> ids);

    /**
     * 获取请假审批单
     * @param id 请假id
     * @return
     */
    String getHolidayWord(Integer id);

    /**
     * 检查请假日期和创建日期超过1天数量
     * @param sectionId
     * @return
     */
    int queryHolidayCount(String sectionId);
    /**
     * 查询劳务人员请假记录
     * @param mapWhere
     * @return
     */
    List<HolidayVo>  queryLwryHolidayRecord(Map<String,Object> mapWhere);
    /**
     * 查询标段下的劳务人员请假记录
     * @param mapWhere
     * @return
     */
    PageInfo<LwryHolidayVo> selectSectionLwryHolidayList(Map<String, Object> mapWhere, Integer pageSize,
                                                         Integer currentPageNum);
}
