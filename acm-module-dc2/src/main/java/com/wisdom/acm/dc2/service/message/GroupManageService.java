package com.wisdom.acm.dc2.service.message;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.form.message.GroupManageAddForm;
import com.wisdom.acm.dc2.form.message.GroupManageUpdateForm;
import com.wisdom.acm.dc2.po.message.GroupManagePo;
import com.wisdom.acm.dc2.vo.message.GroupManageVo;
import com.wisdom.base.common.service.CommService;


public interface GroupManageService extends CommService<GroupManagePo>
{
	/**
	 * 群组分页查询
	 * @param mapWhere
	 * @param pageSize
	 * @param currentPageNum
	 * @return
	 */
	 PageInfo<GroupManageVo> selectGroupManageList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);
 
	 
	 /**
      * 删除群组
      * @param ids
      */
	  void deleteGroupManage(List<Integer> ids);
	  
	  /**
	     * 处理上传的excel
	     * @param file
	     * @param groupName
	     * @return
	     */
	  String dealUploadFile(MultipartFile file, String groupName);
	  
	  
	  /**
	     * 修改群组管理
	     * @param file
	     * @param groupName
	     * @return
	     */
	  String updateFile(MultipartFile file,Integer id, String groupName);
	  
	  /**
       * 新增群组管理
       * @param groupManageAddForm
       */
	  String addGroupManage(GroupManageAddForm groupManageAddForm,String excelError);
	  
	  /**
	   * 群组查询
	   * @param mapWhere
	   * @return
	   */
	  List<GroupManageVo> selectBygroupManageVoParams(Map<String, Object> mapWhere);

	  /**
	     * 修改群组状态
	     * @param mapWhere
	   */
	  void updategroupManage(Map<String, Object> mapWhere);
	  
	  /**
       * 修改群组管理
       * @param groupManageUpdateForm
       */
	  void updateGroupManage(GroupManageUpdateForm groupManageUpdateForm);
}
