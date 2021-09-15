package com.wisdom.acm.szxm.controller.sysscore;

/**
 * Author：wqd
 * Date：2020-01-02 16:15
 * Description：<描述>
 */

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.szxm.common.SzxmCommonUtil;
import com.wisdom.acm.szxm.service.sysscore.SysScoreService;
import com.wisdom.acm.szxm.vo.sysscore.SysScoreVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 系统评分
 */
@Controller
@RestController
@RequestMapping("system/score")
public class SysScoreController {
    @Autowired

    private HttpServletRequest request;

    @Autowired
    private SzxmCommonUtil szxmCommonUtil;

    @Autowired
    private SysScoreService sysScoreService;

    @GetMapping(value = "/getSysScores/{pageSize}/{currentPageNum}")
    public ApiResult getSysScores(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
        //获取项目ID
        String projectId = request.getParameter("projectId");
        //获取标段ID集合
        String sectionIds = request.getParameter("sectionIds");

        //查询能看到的所有标段List
        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);
        PageInfo<SysScoreVo> sysScores = sysScoreService.getSysScores(mapWhere, sectionList, pageSize, currentPageNum);
        return new TableResultResponse(sysScores);
    }

    /**
     * 获取所有评分数据
     * @param mapWhere
     * @return
     */
    @GetMapping(value = "/getAllSysScores")
    public ApiResult getSysScores(@RequestParam Map<String, Object> mapWhere) {
        //获取项目ID
        String projectId = request.getParameter("projectId");
        //获取标段ID集合
        String sectionIds = request.getParameter("sectionIds");

        //查询能看到的所有标段List
        List<String> sectionList = szxmCommonUtil.getSectionList(projectId, sectionIds);
        return ApiResult.success(sysScoreService.getAllSysScores(mapWhere, sectionList));
    }

    @GetMapping(value = "/getFlowSysScores")
    public ApiResult getFlowSysScores(@RequestParam Map<String, Object> mapWhere) {
        String ids = request.getParameter("ids");
        if (ObjectUtils.isEmpty(ids)) {
            throw new BaseException("参数ids不能为空");
        }

        return ApiResult.success(sysScoreService.getFlowSysScores(mapWhere));
    }

    @GetMapping(value = "/getSysObjectScores")
    public ApiResult getSysObjectScores(@RequestParam Map<String, Object> mapWhere) {
        return ApiResult.success(sysScoreService.getSysObjectScores(mapWhere));
    }

    /**
     * 删除系统评分（包括其下客观评分细项）
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteSysObjectScore")
    public ApiResult deleteSysObjectScore(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "id不能为空");
        }
        sysScoreService.deleteSysObjectScore(ids);
        return ApiResult.success();
    }
}
