package com.wisdom.acm.base.mapper;

import com.wisdom.base.common.vo.GeneralVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DBMetadataMapper {

    List<GeneralVo> listTable();

    List<GeneralVo> listTableColumn(@Param("tableName") String tableName);

}
