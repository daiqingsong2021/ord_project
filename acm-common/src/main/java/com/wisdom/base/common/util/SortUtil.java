package com.wisdom.base.common.util;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.springframework.util.ObjectUtils;

import java.util.*;

public class SortUtil {

    /**
     * 对list进行排序
     * @param sortList 需要排序的list
     * @param param1   排序的参数名称
     * @param orderType 排序类型：正序-asc；倒序-desc  
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String,Object>> sort(List<Map<String,Object>> sortList, String param1, String orderType){
    	
    	if(ObjectUtils.isEmpty(sortList))
    	{
    		return sortList;
    	}
        Comparator mycmp1 = ComparableComparator.getInstance ();
        if("desc".equals(orderType)){
            mycmp1 = ComparatorUtils. reversedComparator(mycmp1); //逆序（默认为正序）
        }
         
        ArrayList<Object> sortFields = new ArrayList<Object>();
        sortFields.add( new BeanComparator(param1 , mycmp1)); //主排序（第一排序）
 
        ComparatorChain multiSort = new ComparatorChain(sortFields);
        Collections.sort (sortList , multiSort);
         
        return sortList;
    }

    public static<T> List<T> sortObj(List<T> sortList, String param1, String orderType){

        if(ObjectUtils.isEmpty(sortList))
        {
            return sortList;
        }
        Comparator mycmp1 = ComparableComparator.getInstance ();
        if("desc".equals(orderType)){
            mycmp1 = ComparatorUtils. reversedComparator(mycmp1); //逆序（默认为正序）
        }

        ArrayList<Object> sortFields = new ArrayList<Object>();
        sortFields.add( new BeanComparator(param1 , mycmp1)); //主排序（第一排序）

        ComparatorChain multiSort = new ComparatorChain(sortFields);
        Collections.sort (sortList , multiSort);

        return sortList;
    }


    /**
     * 对list进行排序
     * @param sortList 需要排序的list
     * @param param1   排序的参数名称:参数长度
     * @param param2   排序的参数名称:排序参数
     * @param orderType 排序类型：正序-asc；倒序-desc  
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String,Object>> sortParam2(List<Map<String,Object>> sortList, String param1,String param2, String orderType){
    	
    	if(ObjectUtils.isEmpty(sortList))
    	{
    		return sortList;
    	}
    	
        Comparator mycmp1 = ComparableComparator.getInstance ();
        Comparator mycmp2 = ComparableComparator.getInstance ();
        if("desc".equals(orderType)){
            mycmp1 = ComparatorUtils. reversedComparator(mycmp1); //逆序（默认为正序）
        }
         
        ArrayList<Object> sortFields = new ArrayList<Object>();
        sortFields.add( new BeanComparator(param1 , mycmp1)); //主排序（第一排序）
        sortFields.add( new BeanComparator(param2 , mycmp2)); //主排序（第一排序）
 
        ComparatorChain multiSort = new ComparatorChain(sortFields);
        Collections.sort (sortList , multiSort);
         
        return sortList;
    }
     
    /**
     * 实例
     * 
     * @return
     */
    public static List<Map<String,Object>> testMapSort(){
         
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name", "1");
        map.put("age", "1");
         
        Map<String,Object> map2 = new HashMap<String,Object>();
        map2.put("name", "2");
        map2.put("age", "13");
         
        Map<String,Object> map1 = new HashMap<String,Object>();
        map1.put("name", "2");
        map1.put("age", "12");
         
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        list.add(map);
        list.add(map1);
        list.add(map2);
         
        return sort(list, "age", "desc");
        //return sortParam2(list, "name", "age", "asc");
    }
     
}
