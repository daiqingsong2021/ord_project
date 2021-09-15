package com.wisdom.acm.dc2.service.message.impl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.message.GroupManageDetailsAddForm;
import com.wisdom.acm.dc2.mapper.message.GroupManageDetailsMapper;
import com.wisdom.acm.dc2.po.message.GroupManageDetailsPo;
import com.wisdom.acm.dc2.service.message.GroupManageDetailsService;
import com.wisdom.acm.dc2.vo.message.GroupManageDetailsVo;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class GroupManageDetailsServiceImpl extends BaseService<GroupManageDetailsMapper, GroupManageDetailsPo> implements GroupManageDetailsService
{

	/**
	 * 群组分页查询
	 * @param mapWhere
	 * @param pageSize
	 * @param currentPageNum
	 * @return
	 */
	@Override
	public PageInfo<GroupManageDetailsVo> selectGroupManageDetailsList(Map<String, Object> mapWhere, Integer pageSize,
			Integer currentPageNum) {
		 	List<GroupManageDetailsVo>  GroupManageDetailsVoList= mapper.selectByParams(mapWhere);
		    PageInfo<GroupManageDetailsVo> pageInfo = new PageInfo<GroupManageDetailsVo>(GroupManageDetailsVoList);
		 	return pageInfo;
	}

	 /**
     * 删除群组人员
     * @param groupIds
     */
	@Override
	public void deleteGroupManageDetails(List<Integer> groupIds) {
		mapper.deleteGroupManageDetails(groupIds);
	}

	 /**
     * 根据参数查询群组人员
     * @param mapWhere
     * @return
     */
	@Override
	public List<GroupManageDetailsVo> selectGroupManagePersonList(Map<String, Object> mapWhere) {
		List<GroupManageDetailsVo>  GroupManageDetailsVoList= mapper.selectByParams(mapWhere);
		return GroupManageDetailsVoList;
	}

	 /**
     * 新增群组管理
     * @param groupManageAddForm
     */
	@Override
	public void addGroupManageDetails(GroupManageDetailsAddForm groupManageDetailsAddForm) {
		GroupManageDetailsPo groupManageDetailPo = dozerMapper.map(groupManageDetailsAddForm, GroupManageDetailsPo.class);
        super.insert(groupManageDetailPo);
	}
}
