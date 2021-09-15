package com.wisdom.acm.szxm.controller.app;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.common.SzxmEnumsUtil;
import com.wisdom.acm.szxm.form.wtgl.*;
import com.wisdom.acm.szxm.service.app.AppQuesService;
import com.wisdom.acm.szxm.service.wtgl.QuestionService;
import com.wisdom.acm.szxm.vo.wtgl.QuestionVo;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("app/ques")
public class AppQuesController extends BaseController
{

    @Autowired
    private AppQuesService appQuesService;
    /**
     *
     */
    @Autowired
    private QuestionService questionService;

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
        PageInfo<QuestionVo> questionVoPageInfo=appQuesService.selectAppQuestions(mapWhere,pageSize,currentPageNum);
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
        PageInfo<QuestionVo> questionVoPageInfo=appQuesService.selectAppQuestions(mapWhere,pageSize,currentPageNum);
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
        PageInfo<QuestionVo> questionVoPageInfo=appQuesService.selectAppQuestions(mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(questionVoPageInfo);
    }

    @GetMapping(value = "getQuestion/{id}")
    public ApiResult getQuestion(@PathVariable("id")Integer id)
    {
        if (ObjectUtils.isEmpty(id))
        {
            return ApiResult.result(1001, "id不能为空");
        }
        return ApiResult.success(appQuesService.selectAppQuestionsById(id));
    }

    /**
     * 增加问题
     * @param questionAddForm
     * @return
     */
    @PostMapping(value = "/addQuestion")
    public ApiResult addQuestion(@RequestBody @Valid QuestionAddForm questionAddForm)
    {
        Integer quesId=questionService.addQuestion(questionAddForm);
        return ApiResult.success(quesId);
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

}
