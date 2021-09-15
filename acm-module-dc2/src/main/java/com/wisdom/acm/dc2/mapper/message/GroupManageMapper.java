package com.wisdom.acm.dc2.mapper.message;

import com.wisdom.acm.dc2.po.message.GroupManagePo;
import com.wisdom.acm.dc2.vo.message.GroupManageVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface GroupManageMapper extends CommMapper<GroupManagePo>
{
    List<GroupManageVo> selectByParams(Map<String, Object> mapWhere);
    
    void updategroupManage(Map<String, Object> mapWhere);
}
