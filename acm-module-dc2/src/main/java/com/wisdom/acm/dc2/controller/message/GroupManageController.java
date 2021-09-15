package com.wisdom.acm.dc2.controller.message;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.service.message.GroupManageDetailsService;
import com.wisdom.acm.dc2.service.message.GroupManageService;
import com.wisdom.acm.dc2.vo.message.GroupManageDetailsVo;
import com.wisdom.acm.dc2.vo.message.GroupManageVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.ExportTemplateUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *短信群组管理
 */
@Controller
@RestController
@RequestMapping("message")
public class GroupManageController
{
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private GroupManageService groupManageService;
    @Autowired
    private GroupManageDetailsService groupManageDetailsService;
    
    /**
     * 查询短信群组分页
     * @param mapWhere 
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/list/{pageSize}/{currentPageNum}")
    public ApiResult getGroupManageListPage(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        PageInfo<GroupManageVo> queryTrainDailyList=groupManageService.selectGroupManageList(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryTrainDailyList);
    }
    
    /**
     * 查询短信群组
     * @param mapWhere 
     * @return
     */
    @GetMapping(value = "/list")
    public ApiResult getGroupManageList(@RequestParam Map<String, Object> mapWhere)
    {
        List<GroupManageVo> queryTrainDailyList=groupManageService.selectBygroupManageVoParams(mapWhere);
        return ApiResult.success(queryTrainDailyList);
    }
    
    /**
     * 查询群组人员详情分页
     * @param mapWhere 
     * @return
     */
    @GetMapping(value = "/getGrouppersonlistPage/{pageSize}/{currentPageNum}")
    public ApiResult getGrouppersonlistPage(@RequestParam Map<String, Object> mapWhere,
    		@PathVariable("pageSize")Integer pageSize,
    		@PathVariable("currentPageNum")Integer currentPageNum)
    {
    	Object id = mapWhere.get("groupId");
    	if (ObjectUtils.isEmpty(id)) {
            return ApiResult.result(1001, "群组id不能为空");
        }
    	PageInfo<GroupManageDetailsVo> queryTrainDailyList=groupManageDetailsService.selectGroupManageDetailsList(mapWhere, pageSize, currentPageNum);
        return new TableResultResponse(queryTrainDailyList);
    }
    
    /**
     * 查询群组人员详情
     * @param mapWhere 
     * @return
     */
    @GetMapping(value = "/getGrouppersonlist")
    public ApiResult getGrouppersonlist(@RequestParam Map<String, Object> mapWhere
    		)
    {
    	Object id = mapWhere.get("groupId");
    	if (ObjectUtils.isEmpty(id)) {
            return ApiResult.result(1001, "群组id不能为空");
        }
    	List<GroupManageDetailsVo> queryTrainDailyList=groupManageDetailsService.selectGroupManagePersonList(mapWhere);
        return  ApiResult.success(queryTrainDailyList);
    }
     
    /**
     * 导入群组人员Excel
     * @return
     */
    @PostMapping(value = {"/uploadFile/{groupName}"})
    public ApiResult uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("groupName") String groupName) {
        if(null == groupName)
            throw new BaseException("群组名称不能为空!");
        if(groupName.trim().length()>20)
            throw new BaseException("群组名称超出20个字!");
        if("".equals(file.getOriginalFilename()))
            throw new BaseException("上传文件不能为空!");
        if (file.getSize() != 0 && !"".equals(file.getName())) {
        	String errorId= groupManageService.dealUploadFile(file,groupName);
            if(StringUtils.isNotBlank(errorId)) {
                return ApiResult.result(1007,errorId);
            }
            return ApiResult.success(errorId);
        }
        return ApiResult.success();
    }
    
    /**
     * 修改群组人员Excel
     * @return
     */
    @PostMapping(value = {"/updateGroupFile/{id}/{groupName}"})
    public ApiResult updateUploadFile(@RequestBody MultipartFile file, 
    		@PathVariable("groupName") String groupName,
    		@PathVariable("id") Integer id) {
    	 if (ObjectUtils.isEmpty(id))
             return ApiResult.result(1001, "群组管理id不能为空");
    	if(null == groupName)
    		return ApiResult.result(1001, "群组管理名称不能为空");
        if(groupName.trim().length()>20)
        	return ApiResult.result(1001, "群组名称超出20个字!");
        	String errorId= groupManageService.updateFile(file, id, groupName);
            if(StringUtils.isNotBlank(errorId)) {
                return ApiResult.result(1007,errorId);
            }
            return ApiResult.success(errorId);
    }  
    
    /**
     * 删除群组
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteGroupManage")
    public ApiResult deleteGroupManage(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        groupManageService.deleteGroupManage(ids);
        return ApiResult.success();
    }

    /**
     * 导出群组模板
     * @param response
     */
    @PostMapping(value = {"/getGroupManage/export"})
    public void getGroupManage(HttpServletResponse response) {
        ExportTemplateUtil.export(request, response, "messageGroup.xlsx",
                "qunzu_muban.xlsx", "");
    }
    
    /**
     * 导出群组人员
     * @param response
     */
    @PostMapping(value = {"/getGroupManagePerson/export/{groupId}"})
    public void getGroupManagePerson(HttpServletResponse response,
    		@PathVariable("groupId") Integer groupId) {
    	Map<String, Object> mapWhere  = new HashMap<String, Object>();
    	mapWhere.put("groupId", groupId);
    	List<GroupManageDetailsVo>	listPersonList= groupManageDetailsService.selectGroupManagePersonList(mapWhere);
    	Map<String, Object> map  = new HashMap<String, Object>();
    	map.put("id", groupId);
    	List<GroupManageVo> voList = groupManageService.selectBygroupManageVoParams(mapWhere);
    	int number = 0;
    	if(voList!=null && voList.size()>0) {
    		number = voList.get(0).getGroupCode();
    	}
        List<Map<String, Object>> managePersonList = new ArrayList<>();
        if(!ObjectUtils.isEmpty(listPersonList)) {
        	managePersonList =this.getGroupManagePersonInfo(listPersonList);
        }
        ExportTemplateUtil.export(request, response, "messageGroupReports.xlsx",
                "群组编号"+number+"成员.xlsx", managePersonList);
    }
    
    private List<Map<String, Object>> getGroupManagePersonInfo(List<GroupManageDetailsVo> managePersonList) {
    	List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    	for(GroupManageDetailsVo vo: managePersonList) {
    		Map<String, Object> mapinfo = new HashMap<>();
    		mapinfo.put("personName", vo.getPersonName());
    		mapinfo.put("personMobile", vo.getPersonMobile());
    		mapinfo.put("personDepartment", vo.getPersonDepartment());
    		mapinfo.put("personPosition", vo.getPersonPosition());
    		mapList.add(mapinfo);
		}
    	return mapList;
	}
    
    /**
     * 修改群组状态
     * @param mapWhere
     * @return
     */
    @PutMapping("/update")
    public ApiResult add(@RequestParam Map<String, Object> mapWhere) {
    	Object id = mapWhere.get("id");
    	if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
    	Object status = mapWhere.get("status");
    	if (ObjectUtils.isEmpty(status))
        {
            return ApiResult.result(1001, "群组状态不能为空");
        }
    	groupManageService.updategroupManage(mapWhere);
        return ApiResult.success();
    }
}