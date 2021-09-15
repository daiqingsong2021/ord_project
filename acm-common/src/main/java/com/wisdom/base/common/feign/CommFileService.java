package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.doc.DocFileInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 *
 */
@FeignClient(value = "acm-module-doc",configuration = FeignConfiguration.class)
public interface CommFileService {

    /**
     * 修改文件
     *
     * @param fileIds
     * @return
     */
    @RequestMapping(value = "/file/relations/{bizId}/{bizType}/update", method = RequestMethod.PUT)
    ApiResult updateFileRelation(@PathVariable("bizId") Integer bizId,
                                 @PathVariable("bizType") String bizType,
                                 @RequestBody List<Integer> fileIds);

    /**
     * 增加文件
     * @param fileIds
     * @return
     */
    @RequestMapping(value = "/file/relations/{bizId}/{bizType}/add", method = RequestMethod.POST)
    ApiResult addFileRelation(@PathVariable("bizId") Integer bizId,
                              @PathVariable("bizType") String bizType,
                              @RequestBody List<Integer> fileIds);

    /**
     * 删除关联文件
     * @param bizType
     * @param bizIds
     * @return
     */
    @RequestMapping(value = "/file/relations/{bizType}/delete", method = RequestMethod.DELETE)
    ApiResult deleteDocFileRelationByBiz(@RequestBody List<Integer> bizIds,@PathVariable("bizType") String bizType);

    /**
     * 根据文件路径获取fastdfs路径
     */
    @RequestMapping(value = "/file/{groupName}/{remoteFileName}/remoteurl", method = RequestMethod.GET)
    ApiResult getFileRemoteUrl(@PathVariable("groupName") String groupName,@PathVariable("remoteFileName") String remoteFileName);

    /**
     * 根据参数获取文件个数
     * @param bizIds
     * @param bizType
     * @return
     */
    @PostMapping("/file/relations/{bizType}/count/maps")
    ApiResult<Map<Integer,Integer>> countFileNumByBiz(@RequestBody List<Integer> bizIds, @PathVariable("bizType") String bizType);


    /**
     * 根据参数获取文件信息
     * @param bizId
     * @param bizType
     * @return
     */
    @RequestMapping(value = "/file/reations/{bizId}/{bizType}/list", method = RequestMethod.GET)
    ApiResult queryFileInfoByBiz(@PathVariable("bizId") Integer bizId,@PathVariable("bizType") String bizType);

    @RequestMapping(value = "/reations/{bizId}/{bizType}/list", method = RequestMethod.GET)
    ApiResult queryDocInfoByBiz(@PathVariable("bizId") Integer bizId, @PathVariable("bizType") String bizType);

    @RequestMapping(value = "/relations/{bizId}/{bizType}/add", method = RequestMethod.POST)
    public ApiResult addDocRelation(@RequestBody List<Integer> docIds, @PathVariable("bizId") Integer bizId, @PathVariable("bizType") String bizType);

    @RequestMapping(value = "/file/info/list", method = RequestMethod.POST)
    ApiResult<List<DocFileInfoVo>> queryFileInfoList(@RequestBody List<Integer> fileIds);
}
