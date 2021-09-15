package com.wisdom.acm.szxm.mapper.rygl;

import com.wisdom.acm.szxm.po.rygl.PeopleEntryDetailPo;
import com.wisdom.acm.szxm.vo.rygl.PeopleEntryDetailVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface PeopleEntryDetailMapper extends CommMapper<PeopleEntryDetailPo>
{
    List<PeopleEntryDetailVo> selectPeopleEntryDetail(Map<String, Object> mapWhere);

    /**
     * 根据身份证查询最新进退场记录
     * @param mapWhere
     * @return
     */
    List<PeopleEntryDetailVo> selectEntryDetail(Map<String, Object> mapWhere);
}
