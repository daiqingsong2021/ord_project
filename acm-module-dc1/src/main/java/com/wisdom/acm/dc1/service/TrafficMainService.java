package com.wisdom.acm.dc1.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc1.po.TrafficMainPo;
import com.wisdom.acm.dc1.vo.TrafficMainVo;
import com.wisdom.acm.dc1.vo.UploadVo;
import com.wisdom.base.common.service.CommService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * @date 2020/7/21/021 18:43
 * Description：<描述>
 */
public interface TrafficMainService  extends CommService<TrafficMainPo> {
    PageInfo<TrafficMainVo> queryTrafficMainList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    String uploadTrafficMainFile(MultipartFile file,Integer userId );

    void delTrafficMain(List<Map<String, Object>> mapList);

    List<Map<String, List<?>>> queryTrafficChildrenList(String recordTime);

    List<Map<String, List<?>>> updateTrafficChildrenList(List<Map<String, List<?>>> trafficChildrenList,Integer id);

    void updateTrafficMainPo(TrafficMainPo trafficMainPo, Date recordTime);

    UploadVo queryIsHaveTrafficMain(MultipartFile file);

    void approvedTraffic(List<Integer> idsIntegerList);
}
