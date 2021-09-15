package com.wisdom.acm.dc1.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc1.service.TrafficMainService;
import com.wisdom.acm.dc1.vo.TrafficMainVo;
import com.wisdom.acm.dc1.vo.UploadVo;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.dc.util.StringHelper;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zll
 * @date 2020/7/21/021 18:34
 * Description：<线网客运量>
 */
@Controller
@RestController
@RequestMapping("traffic")
public class TrafficLineMainController extends BaseController {
    @Autowired
    private TrafficMainService trafficMainService;

    @Autowired
    private HttpServletRequest request;
    /**
     * 查询主表数据
     * @param mapWhere
     * @param pageSize
     * @param currentPageNum
     * @return
     */
    @GetMapping(value = "/queryTrafficMainList/{pageSize}/{currentPageNum}")
    public ApiResult queryTrafficMainList(@RequestParam Map<String, Object> mapWhere,
                                          @PathVariable("pageSize")Integer pageSize,
                                          @PathVariable("currentPageNum")Integer currentPageNum)
    {
        PageInfo<TrafficMainVo> queryTrafficMainList= trafficMainService.queryTrafficMainList
                (mapWhere,pageSize,currentPageNum);
        return new TableResultResponse(queryTrafficMainList);
    }

    /**
     * 查询子表本日期的相关所有数据信息
     * @param recordTime
     * @return
     */
    @GetMapping(value = "/queryTrafficChildrenList")
    public ApiResult queryTrafficChildrenList(@RequestParam String recordTime)
    {
        List<Map<String, List<?>>> queryTrafficChildrenList= trafficMainService.queryTrafficChildrenList(recordTime);
        return ApiResult.success(queryTrafficChildrenList);
    }

    /**
     * 修改子表数据
     * @param trafficChildrenList
     * @return
     */
    @PutMapping(value = "/updateTrafficChildrenList")
    public ApiResult updateTrafficChildrenList(@RequestBody @Valid List<Map<String, List<?>>> trafficChildrenList,@RequestParam Integer id) {
        List<Map<String, List<?>>> trafficChildrens = trafficMainService.updateTrafficChildrenList(trafficChildrenList,id);
        return ApiResult.success(trafficChildrens);
    }

    /**
     * 删除当日主表数据以及关联相关字表数据
     * @param mapList
     * @return
     */
    @DeleteMapping(value = "/delTrafficMain")
    public ApiResult delTrafficMain(@RequestBody List<Map<String, Object>> mapList){
        if (ObjectUtils.isEmpty(mapList)) {
            return ApiResult.result(110,"id不能为空！");
        }
        trafficMainService.delTrafficMain(mapList);
        return ApiResult.success();
    }


    /**
     * 是否重复导入校验
     * @param file
     * @return
     */
    @PostMapping(value = "/queryIsHaveTrafficMain")
    public ApiResult queryIsHaveTrafficMain(@RequestParam("file") MultipartFile file)throws IOException
    {
        UploadVo isNotUpload= trafficMainService.queryIsHaveTrafficMain(file);
        if (!ObjectUtils.isEmpty(isNotUpload)) {
            return ApiResult.success(isNotUpload);
        }
        return ApiResult.result(1007, "导入失败");
    }

    /**
     * 导入客运日况信息Excel
     */
    @PostMapping(value = {"/uploadTrafficMainFile"})
    public ApiResult uploadTrafficMainFile(@RequestParam("file") MultipartFile file, HttpServletRequest request)
            throws IOException {
        MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
        if (StringHelper.isNullAndEmpty(String.valueOf(mRequest.getParameter("userId"))))
            throw new BaseException("用户id不能为空!");
        if (file.getSize() != 0 && !"".equals(file.getName())) {
            Integer userId=Integer.valueOf(mRequest.getParameter("userId"));
            String errorId = trafficMainService.uploadTrafficMainFile(file,userId);
            if (!StringHelper.isNotNullAndEmpty(errorId)) {
                return ApiResult.result(1007, errorId);
            }
            return ApiResult.success();
        }
        return ApiResult.success();
    }

    @GetMapping(value="/approvedTraffic")
    public ApiResult approvedTraffic(@RequestParam String ids){
        if (StringHelper.isNotNullAndEmpty(ids)) {//如果主键Ids 不为空
            String[] idsArray = ids.split(",");
            List<String> idsList = new ArrayList<String>(Arrays.asList(idsArray));
            List<Integer> idsIntegerList = idsList.stream().map(id -> Integer.valueOf(id)).collect(Collectors.toList());
            trafficMainService.approvedTraffic(idsIntegerList);
            return ApiResult.success();
        }else{
            return ApiResult.result(1001, "id不能为空");

        }
    }
}
