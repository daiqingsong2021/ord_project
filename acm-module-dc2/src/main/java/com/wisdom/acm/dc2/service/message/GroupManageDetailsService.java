package com.wisdom.acm.dc2.service.message;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.message.GroupManageDetailsAddForm;
import com.wisdom.acm.dc2.po.message.GroupManageDetailsPo;
import com.wisdom.acm.dc2.vo.message.GroupManageDetailsVo;
import com.wisdom.base.common.service.CommService;


public interface GroupManageDetailsService extends CommService<GroupManageDetailsPo>
{
	/**
	 * 群组人员分页查询
	 * @param mapWhere
	 * @param pageSize
	 * @param currentPageNum
	 * @return
	 */
	 PageInfo<GroupManageDetailsVo> selectGroupManageDetailsList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);
 
	 /**
     * 删除群组人员
     * @param groupIds
     */
	 void deleteGroupManageDetails(List<Integer> groupIds);
	 
    /**
     * 根据参数查询群组人员
     * @param mapWhere
     * @return
     */
    List<GroupManageDetailsVo> selectGroupManagePersonList(Map<String, Object> mapWhere);
    
    /**
     * 新增群组管理
     * @param groupManageAddForm
     */
	void addGroupManageDetails(GroupManageDetailsAddForm groupManageDetailsAddForm);
}
