package com.wisdom.base.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 实体类相关工具类
 * 解决问题： 1、快速对实体的常驻字段，如：crtUser、crtHost、updUser等值快速注入
 *
 * @version 1.0
 * @since 1.7
 */
@Slf4j
public class EntityUtils {



    /**
     * 快速将bean的crtUser、crtHost、crtTime附上相关值
     *
     * @param entity 实体bean
     * @author 王浩彬
     */
    public static <T> void setCreateInfo(T entity) {

        String hostIp = "";
        Integer userId = null;
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(!ObjectUtils.isEmpty(RequestContextHolder.getRequestAttributes()))
        {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            if (request != null) {
                hostIp = StringUtils.isNotBlank(request.getHeader("userHost")) ? request.getHeader("userHost") : ClientUtil.getClientIp(request);
                //hostIp = ClientUtil.getClientIp(request);
                if(!ObjectUtils.isEmpty(request.getHeader("userId"))) {
                    userId = Integer.valueOf(request.getHeader("userId"));
                }
            }
        }

        // 默认属性
        String[] fields = {"creator", "lastUpdIp", "creatTime","lastUpdTime"};
        Field field = ReflectionUtils.getAccessibleField(entity, "creatTime");
        // 默认值
        Object[] value = null;
        if (field != null && field.getType().equals(Date.class)) {
            value = new Object[]{userId, hostIp, new Date(),new Date(),};
        }
        // 填充默认属性值
        setDefaultValues(entity, fields, value);
    }

    /**
     * 快速将bean的updUser、updHost、updTime附上相关值
     *
     * @param entity 实体bean
     * @author 王浩彬
     */
    public static <T> void setUpdatedInfo(T entity) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        String hostIp = "";
        String name = "";
        Integer id = null;
        if(!ObjectUtils.isEmpty(servletRequestAttributes))
        {
            request = servletRequestAttributes.getRequest();
            if (request != null) {
                hostIp = StringUtils.isNotBlank(request.getHeader("userHost")) ? request.getHeader("userHost") : ClientUtil.getClientIp(request);
                //hostIp = ClientUtil.getClientIp(request);
                Object userName = request.getHeader("userName");
                name = (String) userName;
                if(!ObjectUtils.isEmpty(request.getHeader("userId"))) {
                    id = Integer.valueOf(request.getHeader("userId"));
                }
            }

        }
        // 默认属性
        String[] fields = {"lastUpdUser", "lastUpdIp", "lastUpdTime"};
        Field field = ReflectionUtils.getAccessibleField(entity, "lastUpdTime");
        Object[] value = null;
        if (field != null && field.getType().equals(Date.class)) {
            value = new Object[]{id, hostIp, new Date()};
        }
        // 填充默认属性值
        setDefaultValues(entity, fields, value);
    }

    /**
     * 依据对象的属性数组和值数组对对象的属性进行赋值
     *
     * @param entity 对象
     * @param fields 属性数组
     * @param value  值数组
     * @author 王浩彬
     */
    private static <T> void setDefaultValues(T entity, String[] fields, Object[] value) {
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            if (ReflectionUtils.hasField(entity, field)) {
                ReflectionUtils.invokeSetter(entity, field, value[i]);
            }
        }
    }

    /**
     * 根据主键属性，判断主键是否值为空
     *
     * @param entity
     * @param field
     * @return 主键为空，则返回false；主键有值，返回true
     */
    public static <T> boolean isPKNotNull(T entity, String field) {
        if (!ReflectionUtils.hasField(entity, field)) {
            return false;
        }
        Object value = ReflectionUtils.getFieldValue(entity, field);
        return value != null && !"".equals(value);
    }

    public static Map<String,Object> entity2DbDict(Object entity){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            Table table = entity.getClass().getAnnotation(Table.class);
            if(!ObjectUtils.isEmpty(table)){
                String tableName = table.name();
                if(!ObjectUtils.isEmpty(tableName)){
                    map.put("bo_code", tableName);//
                    for (Field field : entity.getClass().getDeclaredFields()) {
                        Column column = field.getAnnotation(Column.class);
                        if(!ObjectUtils.isEmpty(column)){
                            String columnName = column.name();
                            if(!ObjectUtils.isEmpty(columnName)){
                                field.setAccessible(true);
                                Object fieldValue = field.get(entity);
                                map.put(columnName,fieldValue);
                            }
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
        }
        return map;
    }
}
