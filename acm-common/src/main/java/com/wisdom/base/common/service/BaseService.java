package com.wisdom.base.common.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.form.BaseForm;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.mapper.CommMapper;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.po.BasePo;
import com.wisdom.base.common.util.*;
import com.wisdom.base.common.vo.log.LogContentsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.wisdom.base.common.util.LogUtil.getValue;

public abstract class BaseService<M extends CommMapper<T>, T extends BasePo> {
    private static Logger loggerOut = LoggerFactory.getLogger(BaseService.class);
    @Autowired
    protected M mapper;

    @Autowired
    protected org.dozer.Mapper dozerMapper;

    @Autowired
    private LeafService leafService;

    // 日志
    private ThreadLocal<AcmLogger> threadLocalLogger = new ThreadLocal<>();


    public void setMapper(M mapper) {
        this.mapper = mapper;
    }

    public T selectOne(T entity) {
        return mapper.selectOne(entity);
    }


    public T selectById(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    public List<T> selectByIds(List<Integer> ids){

        if(!ObjectUtils.isEmpty(ids)){
            Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            Example example = new Example(clazz);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id",ids);
            return this.selectByExample(example);
        }
        return null;
    }


    public List<T> selectList(T entity) {
        return mapper.select(entity);
    }


    public List<T> selectListAll() {
        return mapper.selectAll();
    }


    public Long selectCount(T entity) {
        return new Long(mapper.selectCount(entity));
    }


    public void insert(T entity) {

        if(entity.getId() == null){
            entity.setId(leafService.getId());
        }

        EntityUtils.setCreateInfo(entity);
        mapper.insert(entity);
        //System.out.println("==>"+entity);
    }

    /**
     * 批量增加
     *
     * @param list
     */
    public void insert(List<T> list) {

        if (!ObjectUtils.isEmpty(list)){
            for (T entity : list) {
                this.insert(entity);
            }
        }
    }


    public int insertSelective(T entity) {
        if(entity.getId() == null){
            entity.setId(leafService.getId());
        }
        EntityUtils.setCreateInfo(entity);
        int count = mapper.insertSelective(entity);
        return count > 0 ? count : null;
    }


    public void delete(T entity) {
        mapper.delete(entity);
    }

    public int deleteByExample(Object example){
        return this.mapper.deleteByExample(example);
    }

    public boolean deleteById(Object id) {
        int count = mapper.deleteByPrimaryKey(id);
        return count > 0 ? true : false;
    }

    public int deleteByIds(List<Integer> ids) {
        if(!ObjectUtils.isEmpty(ids)){
            Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            Example example = new Example(clazz);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id",ids);
            return this.deleteByExample(example);
        }
        return 0;
    }

    public boolean updateById(T entity) {
        EntityUtils.setUpdatedInfo(entity);
        int count = mapper.updateByPrimaryKey(entity);
        return count > 0 ? true : false;
    }


    public int updateSelectiveById(T entity) {
        EntityUtils.setUpdatedInfo(entity);
        int count = mapper.updateByPrimaryKeySelective(entity);
        return count;
    }

    public int updateSelectiveByIds(T entity,List<Integer> ids) {

        if(ObjectUtils.isEmpty(ids)){
            return 0;
        }
        EntityUtils.setUpdatedInfo(entity);
        Example example = this.getExample();
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",ids);
        int count = mapper.updateByExampleSelective(entity,example);
        return count;
    }

    public int updateByExampleSelective(T entity,Object example) {
        EntityUtils.setUpdatedInfo(entity);
        int count = mapper.updateByExampleSelective(entity,example);
        return count;
    }



    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

    public T selectOneByExample(Object example) {
        List<T> list = this.selectByExample(example);
        return !ObjectUtils.isEmpty(list) ? list.get(0) : null;
    }

    public int selectCountByExample(Object example) {
        return mapper.selectCountByExample(example);
    }

    private Example getExample(){
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Example example = new Example(clazz);
        return example;
    }

    public TableResultResponse<T> selectByQuery(Query query) {
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        Example example = new Example(clazz);
        if (query.entrySet().size() > 0) {
            Example.Criteria criteria = example.createCriteria();
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
            }
        }
        Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
        List<T> list = mapper.selectByExample(example);
        return new TableResultResponse<T>(result.getTotal(), list);
    }

    public TableResultResponse<T> selectByExample(Object example,int pageNum,int pageSize){

        Page<Object> result = PageHelper.startPage(pageNum, pageSize);
        List<T> list = this.mapper.selectByExample(example);
        return new TableResultResponse<T>(result.getTotal(), list);
    }

    public void deleteChildrenAndMe(Integer id){
        List<T> list = this.selectListAll();
        List<T> allList = this.queryChildrenPos(list,ListUtil.toArrayList(id));
        this.deleteByIds(ListUtil.toIdList(allList));
    }

    public void deleteChildrenAndMe(List<Integer> ids){
        List<T> list = this.selectListAll();
        List<T> allList = this.queryChildrenPos(list,ids);
        this.deleteByIds(ListUtil.toIdList(allList));
    }


    public List<T> queryChildrenAndMePos(Integer id){
        List<T> list = this.selectListAll();
        return this.queryChildrenPos(list,ListUtil.toArrayList(id));
    }

    public List<T> queryChildrenAndMePos(Integer id,Object example){
        List<T> list = this.selectByExample(example);
        return this.queryChildrenPos(list,ListUtil.toArrayList(id));
    }

    public List<T> queryChildrenAndMePos(List<Integer> ids){
        List<T> list = this.selectListAll();
        return this.queryChildrenPos(list,ids);
    }

    public List<T> queryChildrenAndMePos(List<Integer> ids,Object example){
        List<T> list = this.selectByExample(example);
        return this.queryChildrenPos(list,ids);
    }

    public List<T> queryChildrenPos(List<T> list,List<Integer> ids){
        List<T> retlist = new ArrayList<>();
        Map<Integer,List<T>> childrenMap = ListUtil.bulidTreeListMap(list,"parentId",Integer.class);
        Map<Integer,T> pomap = ListUtil.listToMap(list);

        for(Integer id : ids){
            T t = pomap.get(id);
            if(t != null){
                retlist.add(t);
            }
            retlist.addAll(this.queryChildrenPos(childrenMap,id));
        }

        return ListUtil.distinct(retlist,"id");
    }

    public List<T> queryChildrenAndMePosByTaskId(Integer id){
        List<T> list = this.selectListAll();
        return this.queryChildrenPosByTaskId(list,ListUtil.toArrayList(id));
    }

    private List<T> queryChildrenPosByTaskId(List<T> list,List<Integer> ids){
        List<T> retlist = new ArrayList<>();
        Map<Integer,List<T>> childrenMap = ListUtil.bulidTreeListMap(list,"parentId",Integer.class);
        Map<Integer,T> pomap = ListUtil.listToMap(list,"taskId",Integer.class);

        for(Integer id : ids){
            T t = pomap.get(id);
            if(t != null){
                retlist.add(t);
            }
            retlist.addAll(this.queryChildrenPos(childrenMap,id));
        }

        return ListUtil.distinct(retlist,"taskId");
    }

    private List<T> queryChildrenPos(Map<Integer,List<T>> childrenMap,Integer parentId){

        List<T> retlist = new ArrayList<>();
        List<T> list = childrenMap.get(parentId);

        if(!ObjectUtils.isEmpty(list)){

            for(T t : list){
                retlist.add(t);
                retlist.addAll(this.queryChildrenPos(childrenMap,t.getId()));
            }
        }

        return retlist;
    }


    public List<T> getMaxSort(){
        List<T> list = this.selectByExample("");
        return this.queryChildrenPos(list,ListUtil.toArrayList());
    }

    public Integer selectNextSort(){
        return this.mapper.selectNextSort();
    }

    public Integer selectNextSortByExample(Object example){
        return this.mapper.selectNextSortByExample(example);
    }

    /**
     * 根据id获取名称
     * @param ids
     * @return
     */
    public String queryNamesByIds(List<Integer> ids,String name){
        List<T> list = this.selectByIds(ids);
        String retName = ListUtil.listToNames(list,name);
        return retName;
    };

    public void setAcmLogger (AcmLogger logger ){
        this.threadLocalLogger.set(logger);
    }

    /**
     * 增加日志
     *
     * @param title
     * @param form
     * @param basePo
     */
    public  void addChangeLogger(String title, BaseForm form, BasePo basePo){
        List<LogContentsVo> logContentsVos = LogUtil.getEditChangeLogContent2(form,basePo);
        AcmLogger logger = new AcmLogger();
        logger.setTitle(title);
        logger.setLogContentsVos(logContentsVos);
        this.threadLocalLogger.set(logger);
    }

    /**
     * 增加日志
     *
     * @param form
     * @param basePo
     */
    public  void addChangeLogger(BaseForm form, BasePo basePo){
        List<LogContentsVo> logContentsVos = LogUtil.getEditChangeLogContent2(form,basePo);
        AcmLogger logger = new AcmLogger();
        logger.setLogContentsVos(logContentsVos);
        this.threadLocalLogger.set(logger);
    }

    /**
     *
     * @param form
     * @param basePo
     * @param status
     */
    public  void addChangeLogger(BaseForm form, BasePo basePo,String status)
    {
        loggerOut.info("开始addChangeLogger ！！！！！！！！！！！");
        List<LogContentsVo> logContentsVos = LogUtil.getEditChangeLogContent2(form,basePo);
        AcmLogger logger = new AcmLogger();
        logger.setLogContentsVos(logContentsVos);
        // 所有字段
        Class<?> p = basePo.getClass();
        // 获取PO数据
        Map<String, Method> poMap =LogUtil.getMethodMap(p.getMethods());
        // 所有字段
        Field[] fields = p.getDeclaredFields();
        loggerOut.info("recordId"+getValue(poMap.get("getid"), basePo));
        String recordTime=DateUtil.formatDateTime(new Date());
        String line="";
        String recordId=getValue(poMap.get("getid"), basePo);
        String recordStatus=status;
        for (Field f : fields)
        {
            Method pm = poMap.get("get" + f.getName().toLowerCase());
            String poValue = getValue(pm, basePo);
            if("recordtime".equals(f.getName().toLowerCase()))
            {
                recordTime= poValue;
            }
            if("line".equals(f.getName().toLowerCase()))
            {
                line= poValue;
            }
           /* if("id".equals(f.getName().toLowerCase()))
            {
                recordId= poValue;
            }*/

        }
        loggerOut.info("recordTime="+recordTime +"line="+line +"recordId="+recordId+"recordStatus="+recordStatus);
        logger.setRecordTime(recordTime);
        logger.setLine(line);
        logger.setRecordId(recordId);
        logger.setRecordStatus(recordStatus);
        this.threadLocalLogger.set(logger);
    }

    /**
     * 增加日志
     * @param recordTime
     * @param line
     * @param recordId
     * @param status
     */
    public  void addLogger(String recordTime,String line,String recordId,String status){
        AcmLogger logger = new AcmLogger(recordTime,line,recordId,status);
        this.threadLocalLogger.set(logger);
    }
    /**
     * 增加删除日志
     */
    public  void addDeleteLogger(AcmLogger  logger)
    {
        loggerOut.info("开始addDeleteLogger 删除日 ！！！！！！！！！！！"+logger.getRecordId());
        this.threadLocalLogger.set(logger);
    }

    /**
     * 增加日志
     *
     * @param form
     */
    public  void addNewLogger(BaseForm form){
        this.addNewLogger(null,form);
    }

    /**
     * 增加日志
     *
     */
    public  void addNewLogger(String title,BaseForm form){
        List<LogContentsVo> logContentsVos = LogUtil.getAddLogContent(form);
        AcmLogger logger = new AcmLogger();
        logger.setOptType("add");
        logger.setTitle(title);
        logger.setLogContentsVos(logContentsVos);
        this.threadLocalLogger.set(logger);
    }


    public AcmLogger getAcmLogger(){
        return this.threadLocalLogger.get();
    }

}
