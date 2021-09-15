package com.wisdom.acm.dc2.service.message.impl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc2.common.SzxmEnumsUtil;
import com.wisdom.acm.dc2.common.officeUtils.ExcelUtil;
import org.springframework.util.ObjectUtils;
import com.wisdom.acm.dc2.form.message.GroupManageAddForm;
import com.wisdom.acm.dc2.form.message.GroupManageDetailsAddForm;
import com.wisdom.acm.dc2.form.message.GroupManageUpdateForm;
import com.wisdom.acm.dc2.mapper.message.GroupManageMapper;
import com.wisdom.acm.dc2.po.message.GroupManagePo;
import com.wisdom.acm.dc2.service.message.GroupManageDetailsService;
import com.wisdom.acm.dc2.service.message.GroupManageService;
import com.wisdom.acm.dc2.vo.message.GroupManageVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.EntityUtils;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
public class GroupManageServiceImpl extends BaseService<GroupManageMapper, GroupManagePo> implements GroupManageService
{

    @Autowired
    private GroupManageDetailsService groupManageDetailsService;
    @Autowired
    private LeafService leafService;
  
	/**
	 * 群组分页查询
	 * @param mapWhere
	 * @param pageSize
	 * @param currentPageNum
	 * @return
	 */
	@Override
	public PageInfo<GroupManageVo> selectGroupManageList(Map<String, Object> mapWhere, Integer pageSize,
			Integer currentPageNum) {
		 	List<GroupManageVo>  groupManageVoList= mapper.selectByParams(mapWhere);
		 	groupManageVoList.stream().forEach(item->item.setStatusDesc(SzxmEnumsUtil.GroupManagePoActionEnum.getNameByCode(item.getStatus())));
		 	PageInfo<GroupManageVo> pageInfo = new PageInfo<GroupManageVo>(groupManageVoList);
		 	return pageInfo;
	}

	/**
     * 删除群组
     * @param ids
     */
	@Override
	public void deleteGroupManage(List<Integer> ids) {
		 this.deleteByIds(ids);
		 groupManageDetailsService.deleteGroupManageDetails(ids);
	}

	 /**
     * 处理上传的excel
     * @param file
     * @param groupName
     * @return
     */
	@Override
	public String dealUploadFile(MultipartFile file, String groupName) {
		 if (file.isEmpty()) {
	            throw new BaseException("文件不能为空!");
	        }
		 	GroupManageAddForm insertPo = new GroupManageAddForm();
		 	insertPo.setGroupName(groupName);
	        String fileName = file.getOriginalFilename();//文件名
	        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);//文件类型
	        if (!"xlsx".equals(ext)) {
	            throw new BaseException("文件格式不支持,必须是xlsx格式!");
	        }
	        Workbook wb = null;
	        String excelError = "";
	        try {
	            wb = ExcelUtil.getWorkbook(file);
	            Sheet sheet = ExcelUtil.getSheet(wb, 0);//获取第一页的工作簿
	            int rowNum=sheet.getPhysicalNumberOfRows();//获得总行数
	            if(rowNum>2001) {
	            		excelError=("超出2000条数据!");
	    	            throw new BaseException("超出2000条数据!");
	            }
	            // 取出指定工作簿的内容
	            List<Map<String, Object>> dataList = ExcelUtil.getSheetValue(sheet, 1);
	            String personName = "";// 姓名
	            String personMobile = "";// 手机号
	            String personDepartment = "";// 部门
	            String personPosition = "";// 单位
	            int rowIndex =0;//行数
	            
	            List<GroupManageDetailsAddForm> insertPos = Lists.newArrayList();
	            List<String> phone = Lists.newArrayList();
	            for (int i = 0; i < dataList.size(); i++) {
	            	GroupManageDetailsAddForm contactsPo = new GroupManageDetailsAddForm();
	                Map<String, Object> datamap = dataList.get(i);
	                rowIndex = Integer.parseInt(String.valueOf(datamap.get("rowIndex")))+1;
	                personName = String.valueOf(datamap.get("0"));
	                personMobile = String.valueOf(datamap.get("1"));
	                if (ObjectUtils.isEmpty(personName) || "null".equals(personName)) {
	                	excelError=("第"+rowIndex+"行姓名不能为空");
	                	throw new BaseException("第"+rowIndex+"行姓名不能为空");
	                }
	                personMobile = String.valueOf(datamap.get("1"));
	                if(!validateMobilePhone(personMobile)) {
	                	excelError=("第"+rowIndex+"行包含有无法识别的手机号");
	                	throw new BaseException("第"+rowIndex+"行包含有无法识别的手机号");
	                }
	                if(phone.contains(personMobile)) {	
	                	excelError=("第"+rowIndex+"行包含有重复的手机号");
	                	throw new BaseException("第"+rowIndex+"行包含有重复的手机号");
	                	
	                }
	                personDepartment = String.valueOf(datamap.get("2"));
	                personPosition = String.valueOf(datamap.get("3"));
	                contactsPo.setPersonName(personName);
	                contactsPo.setPersonMobile(personMobile);
	                if(personDepartment!=null && !"null".equals(personDepartment)) {
	                	contactsPo.setPersonDepartment(personDepartment);
	                }
	                if(personPosition!=null && !"null".equals(personPosition)) {
	                	contactsPo.setPersonPosition(personPosition);
	                }
	                phone.add(personMobile);
	                insertPos.add(contactsPo);
	               
	            }
	            insertPo.setAddForm(insertPos);
	            excelError = addGroupManage(insertPo,excelError);
	            if(ObjectUtils.isEmpty(excelError)) {
	            	return "";
	            }else {
	            	return excelError;
	            }
	        } catch (Exception e) {
	        	return excelError;
	        }
	}

	  /**
     * 修改群组管理
     * @param file
     * @param groupName
     * @return
     */
	@Override
	public String updateFile(MultipartFile file,Integer id,String groupName) {
			GroupManagePo groupManagePos = super.selectById(id);	
			 if (ObjectUtils.isEmpty(groupManagePos)) {
             	throw new BaseException("群组不存在或已经被删除！");
             }
			GroupManageUpdateForm insertPo = new GroupManageUpdateForm();
		 	insertPo.setGroupName(groupName);
		 	insertPo.setId(id);
		 	if (file != null && !file.isEmpty()) {
	        String fileName = file.getOriginalFilename();//文件名
	        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);//文件类型
	        if (!"xlsx".equals(ext)) {
	            throw new BaseException("文件格式不支持,必须是xlsx格式!");
	        }
	        Workbook wb = null;
	        String excelError = "";
	        try {
	            wb = ExcelUtil.getWorkbook(file);
	            Sheet sheet = ExcelUtil.getSheet(wb, 0);//获取第一页的工作簿
	            int rowNum=sheet.getPhysicalNumberOfRows();//获得总行数
	            if(rowNum>2001) {
	            		excelError=("超出2000条数据!");
	    	            throw new BaseException("超出2000条数据!");
	            }
	            // 取出指定工作簿的内容
	            List<Map<String, Object>> dataList = ExcelUtil.getSheetValue(sheet, 1);
	            String personName = "";// 姓名
	            String personMobile = "";// 手机号
	            String personDepartment = "";// 部门
	            String personPosition = "";// 单位
	            int rowIndex = 0;//行数
	            List<GroupManageDetailsAddForm> insertPos = Lists.newArrayList();
	            List<String> phone = Lists.newArrayList();
	            for (int i = 0; i < dataList.size(); i++) {
	            	GroupManageDetailsAddForm contactsPo = new GroupManageDetailsAddForm();
	                Map<String, Object> datamap = dataList.get(i);
	                rowIndex = Integer.parseInt(String.valueOf(datamap.get("rowIndex")));
	                personName = String.valueOf(datamap.get("0"));
	                if (ObjectUtils.isEmpty(personName)) {
	                	excelError=("第"+rowIndex+"行姓名不能为空");
	                	throw new BaseException("第"+rowIndex+"行姓名不能为空");
	                }
	                personMobile = String.valueOf(datamap.get("1"));
	                if(!validateMobilePhone(personMobile)) {
	                	excelError=("第"+rowIndex+"行包含有无法识别的手机号");
	                	throw new BaseException("第"+rowIndex+"行包含有无法识别的手机号");
	                }
	                if(phone.contains(personMobile)) {	
	                	excelError=("第"+rowIndex+"行包含有重复的手机号");
	                	throw new BaseException("第"+rowIndex+"行包含有重复的手机号");
	                	
	                }
	                personDepartment = String.valueOf(datamap.get("2"));
	                personPosition = String.valueOf(datamap.get("3"));
	                contactsPo.setPersonName(personName);
	                contactsPo.setPersonMobile(personMobile);
	                if(personDepartment!=null && !"null".equals(personDepartment)) {
	                	contactsPo.setPersonDepartment(personDepartment);
	                }
	                if(personPosition!=null && !"null".equals(personPosition)) {
	                	contactsPo.setPersonPosition(personPosition);
	                }
	                phone.add(personMobile);
	                insertPos.add(contactsPo);
	               
	            }
	            insertPo.setAddForm(insertPos);
	            updateGroupManage(insertPo);
	            return "";
	        } catch (Exception e) {
	        	return excelError;
	        }
		 }else {
			 updateGroupManage(insertPo);
			 return "";
		 }
	}
	
	
	/**
     * 新增群组管理
     * @param groupManageAddForm
     */
	@Override
	public String addGroupManage(GroupManageAddForm groupManageAddForm,String excelError) {
		GroupManagePo groupManagePo = dozerMapper.map(groupManageAddForm, GroupManagePo.class);
		groupManagePo.setStatus(SzxmEnumsUtil.GroupManagePoActionEnum.NORMAL.getCode());
        Map<String,Object> mapwhere = new HashMap<>();
        mapwhere.put("groupName",groupManageAddForm.getGroupName());
        List<GroupManageVo> groupManageVo = mapper.selectByParams(mapwhere);
        if (!ObjectUtils.isEmpty(groupManageVo)) {
        	excelError = "群组名称重复,不可添加";
        	return excelError;
        }
        int id=	leafService.getId();
        if(groupManagePo.getId() == null){
        	 groupManagePo.setId(id);
         }
        EntityUtils.setCreateInfo(groupManagePo);
        mapper.insert(groupManagePo);
        if(groupManageAddForm.getAddForm()!=null && groupManageAddForm.getAddForm().size()>0) {
        	for(GroupManageDetailsAddForm form:groupManageAddForm.getAddForm()) {
        		form.setGroupId(id);
        		groupManageDetailsService.addGroupManageDetails(form);
            }
        }
        return excelError;
	}
	
	/**
	   * 群组查询
	   * @param mapWhere
	   * @return
	   */
	@Override
	public List<GroupManageVo> selectBygroupManageVoParams(Map<String, Object> mapWhere) {
		List<GroupManageVo> volist = mapper.selectByParams(mapWhere);
		volist.stream().forEach(item->item.setStatusDesc(SzxmEnumsUtil.GroupManagePoActionEnum.getNameByCode(item.getStatus())));
		return volist;
	}

	 /**
     * 修改群组状态
     * @param mapWhere
   */
	@Override
	public void updategroupManage(Map<String, Object> mapWhere) {
		mapper.updategroupManage(mapWhere);
	}
	
    /**
     *  正则：手机号（简单）, 1字头＋10位数字即可.
     * @param in
     * @return
     */
    public static boolean validateMobilePhone(String in) {
        Pattern pattern = Pattern.compile("^[1]\\d{10}$");
        return pattern.matcher(in).matches();
    }

    /**
     * 修改群组管理
     * @param groupManageUpdateForm
     */
	@Override
	public void updateGroupManage(GroupManageUpdateForm groupManageUpdateForm) {
		GroupManagePo groupManagePo = dozerMapper.map(groupManageUpdateForm, GroupManagePo.class);
		GroupManagePo groupManagePos = super.selectById(groupManageUpdateForm.getId());
		//修改的GroupName没传说明不需要修改GroupName，或者GroupName保持不变
        if (StringUtils.isBlank(groupManageUpdateForm.getGroupName()) ||
            StringUtils.equalsIgnoreCase(groupManagePos.getGroupName(),groupManageUpdateForm.getGroupName())){
            super.updateSelectiveById(groupManagePo);
        }else{
        	Map<String, Object> mapWhere = new HashMap<String, Object>();
        	mapWhere.put("groupName", groupManageUpdateForm.getGroupName());
            //判断是否重复
        	List<GroupManageVo> groupManageVo = mapper.selectByParams(mapWhere);
        	if(groupManageVo!=null && groupManageVo.size()>0) {
        		throw new BaseException("群组名称重复,不允许修改");
        	}else{
            	super.updateSelectiveById(groupManagePo);
            }
        }
		List<GroupManageDetailsAddForm> addForm = groupManageUpdateForm.getAddForm();
		if(addForm!=null && addForm.size()>0) {
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(groupManagePo.getId());
			groupManageDetailsService.deleteGroupManageDetails(ids);
			for(GroupManageDetailsAddForm form:addForm) {
        		form.setGroupId(groupManagePo.getId());
        		groupManageDetailsService.addGroupManageDetails(form);
            }
		}
	}
}
