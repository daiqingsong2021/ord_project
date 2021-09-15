package com.wisdom.acm.activiti.mapper;

import com.wisdom.acm.activiti.po.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RoleMapper {

	/**
	 * 查询总数
	 * @return
	 */
	int getCount(@Param("findContent") String findContent);

	/**
	 * 查询角色集合
	 * @return
	 */
	List<Role> getList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("findContent") String findContent);

	/**
	 * 查询角色集合
	 * @return
	 */
	List<Role> getListById(@Param("roleIds") List<String> roleIds);

}
