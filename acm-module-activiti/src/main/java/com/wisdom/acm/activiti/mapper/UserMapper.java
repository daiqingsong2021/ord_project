package com.wisdom.acm.activiti.mapper;

import com.wisdom.acm.activiti.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {

	/**
	 * 查询总数
	 * @return
	 */
	int getCount(@Param("findContent") String findContent);

	/**
	 * 查询用户集合
	 * @return
	 */
	List<User> getList(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("findContent") String findContent);

	/**
	 * 查询用户集合
	 * @return
	 */
	List<User> getListById(@Param("userIds") List<String> userIds);

}
