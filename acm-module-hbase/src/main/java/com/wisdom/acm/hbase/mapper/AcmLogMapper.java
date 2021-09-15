package com.wisdom.acm.hbase.mapper;

import com.wisdom.acm.hbase.form.AcmLogSearchForm;
import com.wisdom.acm.hbase.po.AcmLogPo;
import com.wisdom.acm.hbase.vo.*;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AcmLogMapper extends CommMapper<AcmLogPo> {

    /**
     * 查询用户列表
     * @param searchMap
     * @return
     */
    List<AcmLogVo> selectUsers(@Param("search") AcmLogSearchForm searchMap);

}

