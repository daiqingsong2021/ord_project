package com.wisdom.acm.base.service.impl;


import com.wisdom.acm.base.form.custom.BaseCustomFieldSaveForm;
import com.wisdom.acm.base.form.custom.BaseCustomFieldUpdateForm;
import com.wisdom.acm.base.mapper.BaseCustomFieldMapper;
import com.wisdom.acm.base.po.BaseCustomFieldPo;
import com.wisdom.acm.base.service.BaseCustomFieldService;
import com.wisdom.acm.base.service.BaseDictService;
import com.wisdom.acm.base.vo.custom.BaseCustomFieldVo;
import com.wisdom.acm.base.vo.custom.BaseCustomValueVo;
import com.wisdom.base.common.po.BaseCustomPo;
import com.wisdom.base.common.service.BaseService;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.BaseCustomVo;
import com.wisdom.base.common.vo.CustomValueVo;
import com.wisdom.base.common.vo.DictionaryVo;
import com.wisdom.base.common.vo.DictionarysMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 增加自定义字段(所有自定义表都得增加）
 * 1.com.wisdom.base.common.vo.BaseCustomVo
 * 2.com.wisdom.acm.base.form.custom.BaseCustomFieldUpdateForm
 * 3.com.wisdom.acm.base.po.BaseCustomPo
 * 4.BaseCustomFieldMapper.xml
 */
@Service
public class BaseCustomFieldServiceImpl extends BaseService<BaseCustomFieldMapper, BaseCustomFieldPo> implements BaseCustomFieldService {

	@Autowired
	private BaseDictService dictService;

	/**
	 * 得到初始化的字段
	 *
	 * @param tableName 表名
	 * @return BaseCustomFieldVo
	 */
	private List<BaseCustomFieldVo> getInitListByTableName(String tableName) {
		List<BaseCustomFieldVo> voList = new ArrayList<>();
		Field[] fields = BaseCustomVo.class.getDeclaredFields();
		int i = 0;
		for (Field field : fields) {
			if (CustomValueVo.class == field.getType()) {
				BaseCustomFieldVo bcfv = new BaseCustomFieldVo();
				bcfv.setId(--i);
				bcfv.setTable(new DictionaryVo(tableName, ""));
				bcfv.setTitle("自定义(" + field.getName() + ")");
				bcfv.setFieldName(field.getName());
				bcfv.setDataType(new DictionaryVo("Text", ""));
				bcfv.setFormType(new DictionaryVo("Input", ""));
				bcfv.setRequired(0);
				bcfv.setColspan(1);
				bcfv.setRowspan(1);
				bcfv.setEnable(0);
				bcfv.setMaxLength(80);
				voList.add(bcfv);
			}
		}
		return voList;
	}

	/**
	 * 得到词典
	 *
	 * @return DictionarysMap
	 */
	private DictionarysMap getDictionary() {
		List<String> dictList = new ArrayList<>();
		dictList.add("base.custom.table"); //自定义表
		dictList.add("base.custom.data.type"); //数据类型
		dictList.add("base.custom.form.type"); //表单类型
		return this.dictService.getDictMapByTypeCode(dictList);
	}

	/**
	 * 设置词典
	 * @param vos 自定义字段
	 */
	private void setDictionary(List<BaseCustomFieldVo> vos) {
		if(!ObjectUtils.isEmpty(vos)){
			DictionarysMap dm = this.getDictionary();
			for(BaseCustomFieldVo vo : vos){
				this.setDictionary(dm, vo);
			}
		}
	}

	/**
	 * 设置词典
	 * @param vo 自定义字段
	 */
	private void setDictionary(BaseCustomFieldVo vo) {
		if(!ObjectUtils.isEmpty(vo)){
			DictionarysMap dm = this.getDictionary();
			this.setDictionary(dm, vo);
		}
	}

	/**
	 * 设置词典
	 * @param vo 自定义字段
	 */
	private void setDictionary(DictionarysMap dm, BaseCustomFieldVo vo) {
		if(!ObjectUtils.isEmpty(dm) && !ObjectUtils.isEmpty(vo)){
			if(!ObjectUtils.isEmpty(vo.getTable())){
				vo.setTable(dm.getDictionaryVo("base.custom.table", vo.getTable().getId()));
			}
			if(!ObjectUtils.isEmpty(vo.getDataType())){
				vo.setDataType(dm.getDictionaryVo("base.custom.data.type", vo.getDataType().getId()));
			}
			if(!ObjectUtils.isEmpty(vo.getFormType())){
				vo.setFormType(dm.getDictionaryVo("base.custom.form.type", vo.getFormType().getId()));
			}
		}
	}

	/**
	 * 获取自定义配置信息
	 *
	 * @param tableName 表名
	 * @return SysCustomFieldVo
	 */
	@Override
	public List<BaseCustomFieldVo> queryListByTableName(String tableName) {
		List<BaseCustomFieldVo> voList = new ArrayList<>();
		List<BaseCustomFieldVo> initVoList = this.getInitListByTableName(tableName); //初始化所有自定义字段
		Map<String, BaseCustomFieldVo> fieldVoMap = ListUtil.listToMap(this.mapper.queryListByTableName(tableName), "fieldName", String.class); //已配置的自定义字段
		for (BaseCustomFieldVo field : initVoList) {
			if(fieldVoMap.containsKey(field.getFieldName())){
				voList.add(fieldVoMap.get(field.getFieldName())); //增加已使用的自定义字段并按排序在前面
			}
		}
		for (BaseCustomFieldVo field : initVoList) {
			if(!fieldVoMap.containsKey(field.getFieldName())){
				voList.add(field); //增加未使用的自定义字段
			}
		}
		this.setDictionary(voList);
		return voList;
	}

	/**
	 * 获取自定义配置信息
	 *
	 * @param tableName 表名
	 * @return SysCustomFieldVo
	 */
	@Override
	public BaseCustomFieldVo queryByTableNameAndFieldName(String tableName, String fieldName) {
		BaseCustomFieldVo vo = this.mapper.queryByTableNameAndFieldName(tableName, fieldName);
		if (Tools.isEmpty(vo)) {
			List<BaseCustomFieldVo> voList = this.getInitListByTableName(tableName);
			for (BaseCustomFieldVo field : voList) {
				if (field.getFieldName().equals(fieldName)) {
					vo = field;
					break;
				}
			}
		}
		this.setDictionary(vo);
		return vo;
	}

	/**
	 * 保存自定义配置信息
	 *
	 * @return BaseCustomFieldVo
	 */
	@Override
	public BaseCustomFieldVo save(BaseCustomFieldSaveForm form) {
		BaseCustomFieldPo po = this.getByTableNameAndFieldName(form.getTableName(), form.getFieldName());
		if (ObjectUtils.isEmpty(po)) {
			po = this.dozerMapper.map(form, BaseCustomFieldPo.class);
			po.setSort(mapper.selectNextSort());
			this.insert(po);
		} else {
			this.dozerMapper.map(form, po);
			this.updateById(po);
		}
		BaseCustomFieldVo vo = this.mapper.queryByTableNameAndFieldName(form.getTableName(), form.getFieldName());
		this.setDictionary(vo);
		return vo;
	}

	/**
	 * 根据表名和字段名查询自定义
	 *
	 * @param tableName 表名
	 * @param fieldName 字段名
	 * @return SysCustomFieldPo
	 */
	private BaseCustomFieldPo getByTableNameAndFieldName(String tableName, String fieldName) {
		Example example = new Example(BaseCustomFieldPo.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("tableName", tableName);
		criteria.andEqualTo("fieldName", fieldName);
		return this.selectOneByExample(example);
	}

	/**
	 * 获取自定义配置信息
	 *
	 * @param tableName 表名
	 * @return SysCustomFieldVo
	 */
	@Override
	public List<BaseCustomValueVo> queryValueListByTableNameAndId(String tableName, Integer id) {
		List<BaseCustomValueVo> customs = new ArrayList<>();
		List<BaseCustomFieldVo> fields = this.mapper.queryEnableListByTableName(tableName); //查配自定义配置信息
		if (!ObjectUtils.isEmpty(fields)) {
			this.setDictionary(fields);
			DictionarysMap dicts = this.getDictionarys(fields); //查询所有自定义字段有词典信息
			BaseCustomVo value = this.mapper.queryValueListByTableNameAndId(tableName, id); //查询对象的值
			for (BaseCustomFieldVo field : fields) {
				BaseCustomValueVo custom = this.dozerMapper.map(field, BaseCustomValueVo.class);
				custom.setCustomValue(this.getValue(field, value, dicts));
				customs.add(custom);
			}
		}
		return customs;
	}

	/**
	 * 设置值
	 *
	 * @param field 自定义
	 * @param value 值
	 * @param dicts 词典
	 */
	private CustomValueVo getValue(BaseCustomFieldVo field, BaseCustomVo value, DictionarysMap dicts) {
		CustomValueVo cvv = null;
		if (!ObjectUtils.isEmpty(value)) {
			cvv = (CustomValueVo) Tools.getValue(value, field.getFieldName());
			if (!Tools.isEmpty(cvv) && !Tools.isEmpty(field.getDictType()) && !Tools.isEmpty(dicts)) {
				DictionaryVo dictVo = dicts.getDictionaryVo(field.getDictType().getId(), cvv.getValue());
				if (!Tools.isEmpty(dictVo)) {
					cvv.setDisplayName(dictVo.getName());
				}
			}
		}
		return cvv;
	}

	/**
	 * 得到词典
	 *
	 * @param fields 字段信息
	 * @return DictionarysMap
	 */
	private DictionarysMap getDictionarys(List<BaseCustomFieldVo> fields) {
		DictionarysMap dicts = null;
		if (!ObjectUtils.isEmpty(fields)) {
			List<String> dictList = new ArrayList<>();
			for (BaseCustomFieldVo field : fields) {
				if (!Tools.isEmpty(field.getDictType())) {
					dictList.add(field.getDictType().getId());
				}
			}
			if (!Tools.isEmpty(dictList)) {
				dicts = this.dictService.getDictMapByTypeCode(dictList);
			}
		}
		return dicts;
	}

	/**
	 * 保存自定义值
	 *
	 * @param form 表单
	 */
	@Override
	public void saveCustomValue(BaseCustomFieldUpdateForm form) {
		BaseCustomPo po = this.dozerMapper.map(form, BaseCustomPo.class);
		this.mapper.saveCustomValueById(form.getTableName(), po);
	}

}
