package com.wisdom.acm.base.vo.set;

import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

@Data
public class BaseSetProjectVo {
	// 工期类型
	private GeneralVo taskDrtnType;

	// 关键路径
	private GeneralVo cpmType;

	// 总浮时
	private Double cpmFloat;

	// 启用项目团队
	private Integer enableProjectTeam;

	// WBS/任务内部共享
	private Integer shareWbs;

	// 消息推送
	private Integer message;
}
