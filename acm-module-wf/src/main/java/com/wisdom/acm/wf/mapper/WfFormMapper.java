package com.wisdom.acm.wf.mapper;

import com.wisdom.acm.wf.po.WfFormPo;
import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.vo.wf.MyUnFinishTaskVo;
import com.wisdom.base.common.vo.wf.WfFormDataVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WfFormMapper extends CommMapper<WfFormPo> {

	/**
	 * 根据流程实例id获取流程实例对象集合
	 *
	 * @param procInstIds
	 * @return
	 */
	List<MyUnFinishTaskVo> selectTaskVoByProcInstIds(@Param("procInstIds") List<String> procInstIds);

}
