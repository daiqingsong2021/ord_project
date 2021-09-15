package com.wisdom.acm.dc2.mapper.message;

import com.wisdom.acm.dc2.po.message.GroupManageDetailsPo;
import com.wisdom.acm.dc2.vo.message.GroupManageDetailsVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface GroupManageDetailsMapper extends CommMapper<GroupManageDetailsPo>
{
    List<GroupManageDetailsVo> selectByParams(Map<String, Object> mapWhere);
    
    /**
     * 删除群组人员
     * @param groupIds
     */
    public void deleteGroupManageDetails(List<Integer> groupIds);
}
