package com.wisdom.base.common.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.EntityUtils;
import com.wisdom.base.common.util.Query;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CommService<T> {

    public T selectOne(T entity);

    public T selectById(Object id);

    public List<T> selectList(T entity);

    public List<T> selectListAll();

    public Long selectCount(T entity);

    public void insert(T entity);

    /**
     * 批量增加
     *
     * @param list
     */
    void insert(List<T> list);

    public int insertSelective(T entity);

    public void delete(T entity);

    public boolean deleteById(Object id);

    public int deleteByIds(List<Integer> ids);

    public boolean updateById(T entity);

    int updateSelectiveById(T entity);

    /**
     * 根据ID集合修改
     *
     * @param entity
     * @param ids
     * @return
     */
    int updateSelectiveByIds(T entity,List<Integer> ids);
    /**
     * 根据example条件修改Po,为空不修改。
     *
     * @param entity
     * @param example
     * @return
     */
    public int updateByExampleSelective(T entity,Object example);

    public List<T> selectByExample(Object example);

    public T selectOneByExample(Object example);

    /**
     * 根据ID集合查询PO集合
     *
     * @param ids
     * @return
     */
    public List<T> selectByIds(List<Integer> ids);
    /**
     * 删除子节点，包括子节
     *
     * @param id
     */
    public void deleteChildrenAndMe(Integer id);

    /**
     * 删除子节点，包括子节
     *
     * @param ids
     */
    public void deleteChildrenAndMe(List<Integer> ids);

    /**
     * 查询子节点集合（包括当前节点）
     *
     * @param id
     * @return
     */
    public List<T> queryChildrenAndMePos(Integer id);

    /**
     * 查询子节点集合（包括当前节点）
     * @param id
     * @param example
     * @return
     */
    public List<T> queryChildrenAndMePos(Integer id,Object example);

    /**
     * 查询子节点集合（包括当前节点）
     *
     * @param ids
     * @return
     */
    public List<T> queryChildrenAndMePos(List<Integer> ids);

    /**
     * 查询子节点集合（包括当前节点）
     * @param ids
     * @param example
     * @return
     */
    public List<T> queryChildrenAndMePos(List<Integer> ids,Object example);

    public int selectCountByExample(Object example);

    public TableResultResponse<T> selectByExample(Object example,int pageNum,int pageSize);

    /**
     * 查询最大排序号
     * @return
     */
    public Integer selectNextSort();

    /**
     * 根据Example查询最大排序号
     *
     * @param example
     * @return
     */
    public Integer selectNextSortByExample(Object example);

    /**
     * 根据Example删除
     *
     * @param example
     * @return
     */
    public int deleteByExample(Object example);

    /**
     * 根据id获取名称
     * @param ids
     * @return
     */
    public String queryNamesByIds(List<Integer> ids,String name);
}
