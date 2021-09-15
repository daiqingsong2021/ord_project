package com.wisdom.base.common.util;

import com.wisdom.base.common.vo.TreeVo;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ace on 2017/6/12.
 */
public class TreeUtil {

    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public static <T extends TreeVo> List<T> bulid(List<T> treeNodes, Integer root) {

        Map<Integer, List<T>> childrenMap = toChildrenMap(treeNodes);
        List<T> list = bulidChildren(childrenMap, root);

        return list;
    }



    /**
     * 递归计算子节点
     *
     * @param childrenMap
     * @param parentId
     * @param <T>
     * @return
     */
    private static <T extends TreeVo> List<T> bulidChildren(Map<Integer, List<T>> childrenMap, Integer parentId) {
        List<T> list = childrenMap.get(parentId);
        if (!ObjectUtils.isEmpty(list)) {
            for (T t : list) {
                // 递归查询子节点
                List<T> children = bulidChildren(childrenMap, t.getId());
                t.setChildren(null);
                if (!ObjectUtils.isEmpty(children)) {
                    t.setChildren(children);
                }
            }
        }

        return list;
    }
    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public static <T extends TreeVo> List<T> bulidNotRoot(List<T> treeNodes) {

        List<Integer> ids = new ArrayList<>();
        List<T> roots = new ArrayList<>();
        if(!ObjectUtils.isEmpty(treeNodes)){
            for(T t : treeNodes){
                ids.add(t.getId());
            }
            for(T t : treeNodes){
                if(t.getParentId() == null || t.getParentId().equals(0) || !ids.contains(t.getParentId())){
                    roots.add(t);
                }
            }
        }
        Map<Integer, List<T>> childrenMap = toChildrenMap(treeNodes);
        List<T> list = bulidChildren(childrenMap, null, roots);

        return list;
    }
    /**
     * 递归计算子节点
     *
     * @param childrenMap
     * @param parentId
     * @param <T>
     * @return
     */
    private static <T extends TreeVo> List<T> bulidChildren(Map<Integer, List<T>> childrenMap, Integer parentId,List<T> roots) {
        List<T> list = null;
        if(parentId == null){
            list = roots;
        }else{
            list = childrenMap.get(parentId);
        }
        if (!ObjectUtils.isEmpty(list)) {
            for (T t : list) {
                // 递归查询子节点
                List<T> children = bulidChildren(childrenMap, t.getId());
                t.setChildren(null);
                if (!ObjectUtils.isEmpty(children)) {
                    t.setChildren(children);
                }
            }
        }

        return list;
    }


    /**
     * 将集合转化为父节点ID对应子节点集合的Map集合
     *
     * @param treeNodes
     * @param <T>
     * @return
     */
    public static <T extends TreeVo> Map<Integer, List<T>> toChildrenMap(List<T> treeNodes) {
        Map<Integer, List<T>> childrenMap = new HashMap<>();
        if (!ObjectUtils.isEmpty(treeNodes)) {
            for (T t : treeNodes) {

                if (childrenMap.get(t.getParentId()) == null) {
                    List<T> l = new ArrayList<>();
                    l.add(t);
                    childrenMap.put(t.getParentId(), l);
                } else {
                    childrenMap.get(t.getParentId()).add(t);
                }
            }
        }
        return childrenMap;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static <T extends TreeVo> List<T> findChildren(List<T> treeNodes, Integer id) {
        Map<Integer, List<T>> childrenMap = toChildrenMap(treeNodes);
        return childrenMap.get(id);
    }

}
