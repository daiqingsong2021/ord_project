package com.wisdom.acm.dc2.mapper;

import com.wisdom.acm.dc2.po.DailyChangeVersionPo;
import com.wisdom.acm.dc2.vo.DailyChangeVersionVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface DailyChangeVersionMapper extends CommMapper<DailyChangeVersionPo>
{
    List<DailyChangeVersionVo> selectByParams(Map<String, Object> mapWhere);

}
