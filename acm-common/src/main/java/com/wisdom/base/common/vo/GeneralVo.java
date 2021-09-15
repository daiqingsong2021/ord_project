package com.wisdom.base.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "通用VO")
@Data
public class GeneralVo {

	@ApiModelProperty(value = "ID")
	private Integer id;

	@ApiModelProperty(value = "代码")
	private String code;

	@ApiModelProperty(value = "名称")
	private String name;

	public GeneralVo() {

	}

	public GeneralVo(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public GeneralVo(Integer id, String code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

}

