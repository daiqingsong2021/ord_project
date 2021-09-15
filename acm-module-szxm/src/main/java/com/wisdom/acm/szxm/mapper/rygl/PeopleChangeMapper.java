package com.wisdom.acm.szxm.mapper.rygl;

import com.wisdom.acm.szxm.po.rygl.PeopleChangePo;
import com.wisdom.acm.szxm.vo.rygl.PeopleChangeVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PeopleChangeMapper extends CommMapper<PeopleChangePo>
{
    List<PeopleChangeVo> selectPeopleChangeList(Map<String, Object> mapWhere);

    /**
     * 检查变更业务日期与数据创建日期超过7天数量
     * @param sectionId
     * @return
     */
    Integer queryPeopleChangeCount(@Param("sectionId")String sectionId);
}
