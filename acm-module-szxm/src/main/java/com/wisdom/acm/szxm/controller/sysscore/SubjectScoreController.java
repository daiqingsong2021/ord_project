package com.wisdom.acm.szxm.controller.sysscore;

/**
 * Author：wqd
 * Date：2020-01-02 16:15
 * Description：<描述>
 */

import com.wisdom.acm.szxm.form.sysscore.SubjectScoreAddForm;
import com.wisdom.acm.szxm.form.sysscore.SubjectScoreUpdForm;
import com.wisdom.acm.szxm.service.sysscore.SubjectScoreDetailService;
import com.wisdom.acm.szxm.vo.sysscore.SubjectScoreItemVo;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * 主观考核明细
 */
@Controller
@RestController
@RequestMapping("system/subjectScore")
public class SubjectScoreController {
    @Autowired
    private SubjectScoreDetailService subjectScoreDetailService;

    @PostMapping(value = "/addSubjectScore")
    public ApiResult addSubjectScore(@RequestBody @Valid SubjectScoreAddForm subjectScoreAddForm) {
        SubjectScoreItemVo subjectScoreItemVo = subjectScoreDetailService.addSubjectScore(subjectScoreAddForm);
        return ApiResult.success(subjectScoreItemVo);
    }

    @DeleteMapping(value = "/deleteSubjectScore")
    public ApiResult deleteSubjectScore(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        subjectScoreDetailService.deleteSubjectScore(ids);
        return ApiResult.success();
    }

    @DeleteMapping(value = "/deleteSubjectScoreByFileId")
    public ApiResult deleteSubjectScoreByFileId(@RequestBody List<Integer> fileIds) {
        if (ObjectUtils.isEmpty(fileIds)) {
            return ApiResult.result(1001, "文件id不能为空");
        }
        subjectScoreDetailService.deleteSubjectScoreByFileId(fileIds);
        return ApiResult.success();
    }

    @PutMapping(value = "/updateSubjectScore")
    public ApiResult updateSubjectScore(@RequestBody @Valid SubjectScoreUpdForm subjectScoreUpdForm) {
        SubjectScoreItemVo subjectScoreItemVo = subjectScoreDetailService.updateSubjectScore(subjectScoreUpdForm);
        return ApiResult.success(subjectScoreItemVo);
    }

    /**
     * 主观评分总分 + 列表
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/selectSubjectScore/{pageSize}/{currentPageNum}")
    public ApiResult selectSubjectScore(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize")Integer pageSize, @PathVariable("currentPageNum")Integer currentPageNum) {
        return ApiResult.success(subjectScoreDetailService.selectSubjectScore(mapWhere, pageSize, currentPageNum));
    }

    /**
     * 主观评分列表
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/selectSubjectItemScore")
    public ApiResult selectSubjectItemScore(@RequestParam Map<String, Object> mapWhere) {
        return ApiResult.success(subjectScoreDetailService.selectSubjectItemScore(mapWhere));
    }
}
