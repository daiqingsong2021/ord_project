package com.wisdom.acm.sys.service.impl;

import com.wisdom.acm.sys.vo.ADDepartmentVo;
import com.wisdom.base.common.vo.TreeVo;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2019-10-16 18:57
 * Description：<描述>
 */
@Component
public class UUVServiceUtils {

    /**
     * 递归计算子节点  内部
     *
     * @param childrenMap
     * @param parentCode
     * @return
     */
    public List<ADDepartmentVo> bulidInnerChildren(Map<String, List<ADDepartmentVo>> childrenMap, String parentCode) {
        List<ADDepartmentVo> list = childrenMap.get(parentCode);
        if (!ObjectUtils.isEmpty(list)) {
            childrenMap.remove(parentCode);
            for (ADDepartmentVo t : list) {
                // 递归查询子节点
                List<ADDepartmentVo> children = bulidInnerChildren(childrenMap, t.getDeptCode());
                t.setChildren(null);
                if (!ObjectUtils.isEmpty(children)) {
                    t.setChildren(children);
                }
            }
        }

        return list;
    }

    public  <T extends TreeVo>  List<T> getChildren(T orgVo, List<T> orgVoList) {
        orgVoList.add(orgVo);
        if (!ObjectUtils.isEmpty(orgVo.getChildren())) {
            List<T> childrens = orgVo.getChildren();
            for (T sysOrgVo : childrens) {
                getChildren(sysOrgVo, orgVoList);
            }
        }
        return orgVoList;
    }
}
