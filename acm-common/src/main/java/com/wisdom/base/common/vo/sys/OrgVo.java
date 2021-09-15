package com.wisdom.base.common.vo.sys;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "组织信息")
@Data
public class OrgVo {

	@ApiModelProperty(value = "ID")
	private Integer id;

	@ApiModelProperty(value = "代码")
	private String code;

	@ApiModelProperty(value = "名称")
	private String name;

	public OrgVo() {

	}

	public OrgVo(Integer id) {
		this.id = id;
	}

	public OrgVo(Integer id, String code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public OrgVo(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * 转字符串
	 * @return
	 */
	public String toString(){
		return this.name;
	}
}
