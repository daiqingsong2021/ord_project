package com.wisdom.acm.szxm.mapper.wtgl;

import com.wisdom.acm.szxm.po.wtgl.QuestionPo;
import com.wisdom.acm.szxm.vo.wtgl.FileBizType;
import com.wisdom.acm.szxm.vo.wtgl.QuestionClassVo;
import com.wisdom.acm.szxm.vo.wtgl.QuestionMonthVo;
import com.wisdom.acm.szxm.vo.wtgl.QuestionVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface QuestionMapper extends CommMapper<QuestionPo> {

    /**
     * 获取问题列表 只查询与自己有关系的问题，包括问题创建者，问题所有处理记录创建者（非挂起），问题下一步处理人（非挂起）
     * @param
     * @return
     */
    List<QuestionVo> selectQuestionList(Map<String, Object> mapWhere);

    /**
     * 获取问题列表
     * @param mapWhere
     * @return
     */
    List<QuestionVo> selectYqQuestionList(Map<String, Object> mapWhere);

    /**
     * 获取问题列表 某个日期项目下所有问题
     * @param
     * @return
     */
    List<QuestionVo> selectQuestionQuantity(Map<String, Object> mapWhere);

    /**
     * 获取问题列表 某个日期项目下所有问题
     * @param
     * @return
     */
    List<QuestionVo> secIssueList(Map<String, Object> mapWhere);

    /**
     * 按问题类型获取问题个数 某个日期项目下所有问题
     * @param
     * @return
     */
    List<QuestionClassVo> selectQuestionType(Map<String, Object> mapWhere);

    /**
     * 按问题创建月份获取问题个数
     * @param
     * @return
     */
    List<QuestionMonthVo> selectQuestionMonth(Map<String, Object> mapWhere);

    /**
     * 根据相关条件查询单个问题
     * @param id
     * @return
     */
    QuestionVo selectQuestion(@Param("id") Integer id);

    /**
     *  查询问题的文件
     * @param bizIds
     * @return
     */
    List<FileBizType> selectQuestionFile(@Param("bizIds") List<Integer> bizIds);

    /**
     * 查询
     * @param projectId
     * @param userId
     * @return
     */
    List<Integer> selectProjectTeamOrg(@Param("projectId")Integer projectId,@Param("userId")Integer userId);
    /**
     * 根据各个模块的主键id（biz_id）查询问题表数据
     * @param id
     * @return
     */
    List<QuestionVo> queryQuestionList(@Param("id") Integer id);

    /**
     * 检查要求处理日期与问题记录中责任单位处理信息的创建日期，没有记录或记录创建日期大于要求处理日期的数量
     * @param sectionId
     * @return
     */
    Integer queryQuestionCount(@Param("sectionId")String sectionId);
}
