package com.wisdom.acm.szxm.controller.wtgl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.wisdom.acm.szxm.common.SzxmEnumsUtil;
import com.wisdom.acm.szxm.form.wtgl.QuestionAddForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionForwardForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionHandleForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionPublishForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionUpdateForm;
import com.wisdom.acm.szxm.form.wtgl.QuestionVerifyForm;
import com.wisdom.acm.szxm.service.wtgl.QuestionRecordService;
import com.wisdom.acm.szxm.service.wtgl.QuestionService;
import com.wisdom.acm.szxm.vo.wtgl.FltjQuestionVo;
import com.wisdom.acm.szxm.vo.wtgl.QuestionRecordVo;
import com.wisdom.acm.szxm.vo.wtgl.QuestionVo;
import com.wisdom.acm.szxm.vo.wtgl.TJQuestionVo;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 问题控制器
 */
@RestController
@RequestMapping("ques")
public class QuestionController  extends BaseController
{
    /**
     *
     */
    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRecordService questionRecordService;

    @Autowired
    private CommUserService commUserService;

    /**
     * 获取问题列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/queryQuestionList/{pageSize}/{currentPageNum}")
    public ApiResult queryQuestionList(@RequestParam Map<String, Object> mapWhere,@PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        PageInfo<QuestionVo> questionVoPageInfo=questionService.selectQuestions(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(questionVoPageInfo);
    }

    /**
     * 页签查询问题
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/queryYqQuestionList/{pageSize}/{currentPageNum}")
    public ApiResult queryYqQuestionList(@RequestParam Map<String, Object> mapWhere,@PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        mapWhere.put("isYq","1");
        PageInfo<QuestionVo> questionVoPageInfo=questionService.selectQuestions(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(questionVoPageInfo);
    }
    /**
     * 问题数量统计
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/secIssueQuantity")
    public ApiResult secIssueQuantity(@RequestParam Map<String, Object> mapWhere)
    {
        return ApiResult.success(questionService.secIssueQuantity(mapWhere));
    }

    /**
     * 问题数量统计表交互
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/secIssueList/{pageSize}/{currentPageNum}")
    public ApiResult secIssueList(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        return  new TableResultResponse(questionService.secIssueList(mapWhere, pageSize, currentPageNum));
    }

    /**
     * 问题分类统计
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/issueClassificationStatistic")
    public ApiResult issueClassificationStatistic(@RequestParam Map<String, Object> mapWhere)
    {
        return ApiResult.success(questionService.issueClassificationStatistic(mapWhere));
    }

    /**
     * 问题分类统计--交互
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/secIssueClassList/{pageSize}/{currentPageNum}")
    public ApiResult secIssueClassList(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        PageInfo<QuestionVo> questionVoPageInfo = questionService.secIssueClassList(mapWhere, pageSize, currentPageNum);
        return new TableResultResponse(questionVoPageInfo);
    }

    /**
     * 获取待我审核问题列表
     */
    @GetMapping(value = "/getReviewIngQuestionList/{pageSize}/{currentPageNum}")
    public ApiResult getReviewIngQuestionList(@RequestParam Map<String, Object> mapWhere,@PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        UserInfo userInfo=commUserService.getLoginUser();
        mapWhere.put("currentUserId",userInfo.getId());
        mapWhere.put("status", SzxmEnumsUtil.QuestionStatusEnum.DSH.getCode());
        PageInfo<QuestionVo> questionVoPageInfo=questionService.selectQuestions(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(questionVoPageInfo);
    }

    /**
     * 获取待我处理问题列表
     */
    @GetMapping(value = "/getProcessIngQuestionList/{pageSize}/{currentPageNum}")
    public ApiResult getProcessIngQuestionList(@RequestParam Map<String, Object> mapWhere,@PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum)
    {
        UserInfo userInfo=commUserService.getLoginUser();
        mapWhere.put("currentUserId",userInfo.getId());
        mapWhere.put("status", SzxmEnumsUtil.QuestionStatusEnum.DCL.getCode());
        PageInfo<QuestionVo> questionVoPageInfo=questionService.selectQuestions(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(questionVoPageInfo);
    }

    @GetMapping(value = "getQuestion/{id}")
    public ApiResult getQuestion(@PathVariable("id")Integer id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(questionService.selectQuestionsById(id));
    }

    /**
     * 根据问题ID，获取问题处理记录列表
     * @param questionId
     * @return
     */
    @GetMapping(value = "/{questionId}/queryQuestionRecordList")
    public ApiResult queryQuestionRecordList(@PathVariable("questionId")Integer questionId)
    {
        Map<String, Object> mapWhere= Maps.newHashMap();
        mapWhere.put("questionId",questionId);
        List<QuestionRecordVo> questionRecordVoList=questionRecordService.selectQuestionRecords(mapWhere);
        return ApiResult.success(questionRecordVoList);
    }

    /**
     * 增加问题
     * @param questionAddForm
     * @return
     */
    @PostMapping(value = "/addQuestion")
    public ApiResult addQuestion(@RequestBody @Valid QuestionAddForm questionAddForm)
    {
        questionService.addQuestion(questionAddForm);
        return ApiResult.success();
    }

    /**
     * 修改问题
     * @param questionUpdateForm
     * @return
     */
    @PutMapping(value = "/updateQuestion")
    public ApiResult updateQuestion(@RequestBody @Valid QuestionUpdateForm questionUpdateForm)
    {
        questionService.updateQuestion(questionUpdateForm);
        return ApiResult.success();
    }

    /**
     * 删除问题
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteQuestion")
    public ApiResult deleteQuestion(@RequestBody List<Integer> ids)
    {
        if (ObjectUtils.isEmpty(ids))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        questionService.deleteQuestion(ids);
        return ApiResult.success();
    }

    /**
     * 发布问题
     * @param questionPublishForm
     * @return
     */
    @PostMapping(value = "/publishQuestion")
    public ApiResult publishQuestion(@RequestBody @Valid QuestionPublishForm  questionPublishForm)
    {
        questionService.updatePublishQuestion(questionPublishForm);
        return ApiResult.success();
    }

    /**
     * 处理问题
     * @param questionHandleForm
     * @return
     */
    @PostMapping(value = "/handleQuestion")
    public ApiResult handleQuestion(@RequestBody @Valid QuestionHandleForm questionHandleForm)
    {
        questionService.addHandleQuestion(questionHandleForm);
        return ApiResult.success();
    }

    /**
     * 转发问题
     * @param questionForwardForm
     * @return
     */
    @PostMapping(value = "/forwardQuestion")
    public ApiResult forwardQuestion(@RequestBody @Valid QuestionForwardForm questionForwardForm)
    {
        questionService.addForwardQuestion(questionForwardForm);
        return ApiResult.success();
    }

    /**
     * 审核问题
     * @param questionVerifyForm
     * @return
     */
    @PostMapping(value = "/verifyQuestion")
    public ApiResult verifyQuestion(@RequestBody @Valid QuestionVerifyForm questionVerifyForm)
    {
        questionService.addVerifyQuestion(questionVerifyForm);
        return ApiResult.success();
    }

    /**
     * 挂起问题
     * @param questionId
     * @return
     */
    @PostMapping(value = "/hangUpQuestion/{questionId}")
    public ApiResult hangUpQuestion(@PathVariable("questionId") Integer questionId)
    {
        if (ObjectUtils.isEmpty(questionId))
        {
            throw new BaseException("问题ID不能为空");
        }
        questionService.addHandUpQuestion(questionId);
        return ApiResult.success();
    }

    /**
     * 取消挂起问题
     * @param questionId
     * @return
     */
    @PostMapping(value = "/cancelHangUpQuestion/{questionId}")
    public ApiResult cancelHangUpQuestion(@PathVariable("questionId") Integer questionId)
    {
        if (ObjectUtils.isEmpty(questionId))
        {
            throw new BaseException("问题ID不能为空");
        }
        questionService.addCancelHandUpQuestion(questionId);
        return ApiResult.success();
    }
}
