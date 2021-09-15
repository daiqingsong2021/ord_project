package com.wisdom.acm.szxm.service.wtgl;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.form.wtgl.QuestionAddForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionForwardForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionHandleForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionPublishForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionUpdateForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionVerifyForm;
import com.wisdom.acm.szxm.po.wtgl.QuestionPo;
import com.wisdom.acm.szxm.vo.wtgl.FltjQuestionVo;
import com.wisdom.acm.szxm.vo.wtgl.QuestionVo;
import com.wisdom.acm.szxm.vo.wtgl.TJQuestionVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface QuestionService extends CommService<QuestionPo>
{
    /**
     * 获取问题列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    PageInfo<QuestionVo> selectQuestions(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    /**
     * 获取问题列表 -- 安全检查调用 可查看与本人无关问题列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    PageInfo<QuestionVo> securityCheckQuestions(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    /**
     * 问题数量统计
     * @param mapWhere
     * @return
     */
    List<TJQuestionVo> secIssueQuantity(Map<String, Object> mapWhere);

    /**
     * 问题数量统计表交互
     * @param mapWhere
     * @return
     */
    PageInfo<QuestionVo> secIssueList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    /**
     * 问题分类统计
     * @param mapWhere
     * @return
     */
    FltjQuestionVo issueClassificationStatistic(Map<String, Object> mapWhere);

    /**
     * 问题分类统计表交互
     * @param mapWhere
     * @return
     */
    PageInfo<QuestionVo> secIssueClassList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    /**
     * 根据各个模块的主键id（biz_id）查询问题表数据
     * @param id
     * @return
     */
    List<QuestionVo>queryQuestionList(Integer id);

    /**
     * 查询单个问题
     * @param id
     * @return
     */
    QuestionVo  selectQuestionsById(Integer id);

    /**
     * 增加问题
     * @param questionAddForm
     */
    Integer addQuestion(QuestionAddForm questionAddForm);

    /**
     * 修改问题
     * @param questionUpdateForm
     */
    void updateQuestion(QuestionUpdateForm questionUpdateForm);

    /**
     * 删除问题
     * @param ids
     */
    void deleteQuestion(List<Integer> ids);

    /**
     * 发布问题
     * @param questionPublishForm
     */
    void updatePublishQuestion(QuestionPublishForm questionPublishForm);

    /**
     * 处理问题
     * @param questionHandleForm
     */
    void addHandleQuestion(QuestionHandleForm questionHandleForm);


    /**
     * 转发问题
     * @param questionForwardForm
     */
    void addForwardQuestion(QuestionForwardForm questionForwardForm);

    /**
     * 审核问题
     * @param questionVerifyForm
     */
    void addVerifyQuestion(QuestionVerifyForm questionVerifyForm);

    /**
     * 挂起问题
     * @param id
     */
    void addHandUpQuestion(Integer id);

    /**
     * 取消挂起问题
     * @param id
     */
    void addCancelHandUpQuestion(Integer id);

    /**
     * 检查要求处理日期与问题记录中责任单位处理信息的创建日期，没有记录或记录创建日期大于要求处理日期的数量
     * @param sectionId
     * @return
     */
    int queryQuestionCount(String sectionId);
}
