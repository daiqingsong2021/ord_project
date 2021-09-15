package com.wisdom.base.common.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.wisdom.base.common.po.BasePo;
import com.google.common.collect.Lists;
import org.springframework.util.ObjectUtils;

/**
 * Author : Mr.He Date : 2013-9-27 Verion : 3.0
 */
public class ListUtil {

	private ListUtil() {

	}

	/**
	 * 将Po对象按照ID转化为id与对象一一对应的Map集合。
	 * 
	 * @param list
	 * @return
	 */
	public static <T extends BasePo> Map<Integer, T> listToMap(List<T> list) {

		Map<Integer, T> retMap = new HashMap<>();

		if (!ObjectUtils.isEmpty(list)) {
			for (T t : list) {

				if (t != null) {
					retMap.put(t.getId(), t);
				}
			}
		}
		return retMap;
	}

    /**
     * 将对象按照指定属性转化为属性值与对象一一对应的Map集合。
     *
     * @param list
     * @param field
     * @param fieldClass
     * @param <T> 值
     * @param <U> 键
     * @return
     */
    public static <T extends Object,U> Map<U, T> listToMap(List<T> list,String field,Class<U> fieldClass) {

        Map<U, T> retMap = new HashMap<>();

        if (!ObjectUtils.isEmpty(list)) {

            Method getMethod = null;
            try {
                getMethod = getMenthod(field,list.get(0));
                for (T t : list) {
                    if (t != null) {
                        U u = (U)getMethod.invoke(t);
                        retMap.put(u, t);
                    }
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }


        }
        return retMap;
    }

    /**
     * 将对象按照指定属性转化为属性值与对象一一对应的Map集合。
     *
     * @param list
     * @param field
     * @param <T> 值
     * @return
     */
    public static <T> String listToNames(List<T> list,String field) {
		String retName = "";
		StringBuffer names = new StringBuffer();
        if (!ObjectUtils.isEmpty(list)) {
            Method getMethod = null;

            try {
                getMethod = getMenthod(field,list.get(0));
                for (T t : list) {
                    if (t != null) {
                        String u = (String) getMethod.invoke(t);
						names.append(u + ",");
                    }
					String temp = names.toString().substring(0,names.toString().length()-1);
					retName = temp;
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }


        }
        return retName;
    }

	/**
	 * 将对象按照指定属性且第一条数据转化为属性值与对象一一对应的map集合
	 * @param list
	 * @param field
	 * @param filedClass
	 * @param isTop
	 * @param <T>
	 * @param <U>
	 * @return
	 */
    public static <T extends  Object,U> Map<U,T> listToMap(List<T> list,String field,Class<U> filedClass, boolean isTop){

    	Map<U, T> retMap = new HashMap<>();

		if (!ObjectUtils.isEmpty(list)) {

			Method getMethod = null;
			try {
				getMethod = getMenthod(field,list.get(0));
				for (T t : list) {
					if (t != null) {
						U u = (U)getMethod.invoke(t);

						if(isTop){
							if(!retMap.containsKey(u)){
								retMap.put(u, t);
							}
						}else{
							retMap.put(u, t);
						}

					}
				}
			} catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}


		}
		return retMap;
	}



    /**
     * 按照指定属性转化为属性值与对象一一对应的Map集合。
     * @param list
     * @param field
     * @return
     */
    public static Map<String, Map<String, Object>> listMapToMap(List<Map<String, Object>> list, String field) {
        Map<String, Map<String, Object>> retMap = new HashMap<>();
        if (!ObjectUtils.isEmpty(list)) {
            for (Map<String, Object> map : list) {
                String key = FormatUtil.toString(map.get(field));
                if (!ObjectUtils.isEmpty(key)) {
                    retMap.put(key, map);
                }
            }
        }
        return retMap;
    }

    public static <T> Method getMenthod(String field,T t) throws IntrospectionException {

        PropertyDescriptor pd = new PropertyDescriptor(field, t.getClass());
        Method getMethod = pd.getReadMethod();// 获得get方法

        return getMethod;
    }

    /**
     * 根据指定属性，转化为一对多的Map集合
     *
     * @param list
     * @param field
     * @param fieldClass
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T extends Object,U extends Object> Map<U, List<T>> listToMapList(List<T> list, String field,Class<U> fieldClass) {

        Map<U, List<T>> retMap = new HashMap<>();

        if (!ObjectUtils.isEmpty(list)) {
            try {
                Method getMethod = getMenthod(field,list.get(0));
                List<T> temp = null;
                for (T t : list) {
                    if (t != null) {
                        Integer value = null;
                        U o = (U)getMethod.invoke(t);
                        temp = retMap.get(o);
                        if (null == temp) {
                            temp = new ArrayList<>();
                        }
                        temp.add(t);
                        retMap.put(o, temp);
                    }
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return retMap;
    }

	/**
	 * 将Map集合内的元素转化为list集合
     *
	 * @param map
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> mapToList(Map<String,T> map){
		List<T> retList = new ArrayList<>();
		if(map != null){
			for(Entry entry : map.entrySet()){
				retList.add((T)entry.getValue());
			}
		}
		return retList;
	}

    /**
     * 按照ID提取出新的ID集合
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends Object> List<Integer> toIdList(List<T> list){

        return toValueList(list,"id",Integer.class);
    }

    /**
     * 按照指定属性提取出新的集合
     *
     * @param list
     * @param field
     * @param fieldClass
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T extends Object,U> List<U> toValueList(List<T> list,String field,Class<U> fieldClass) {

        return toValueList(list,field,fieldClass,false);
    }

    /**
     * 按照指定属性提取出新的集合,可去重复
     *
     * @param list
     * @param field
     * @param fileClass
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T extends Object,U> List<U> toValueList(List<T> list,String field,Class<U> fileClass,boolean distinct) {

        List<U> idList = new ArrayList<>();
        boolean first = true;
        List<T> temp = null;
        if (!ObjectUtils.isEmpty(list)) {
            Method getMethod = null;
            try {
                getMethod = getMenthod(field,list.get(0));
                for (T t : list) {
                    if (t != null) {
                        Integer value = null;
                        U o = (U)getMethod.invoke(t);
                        if(distinct && idList.contains(o)){
                            continue;
                        }
                        idList.add(o);
                    }
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return idList;
    }


    /**
     * 指定Map内属性提取出新的集合
     *
     * @param list
     * @param field
     * @param uClass
     * @param <U>
     * @return
     */
    public static <U> List<U> toValueListByListMap(List<Map<String, Object>> list, String field,Class<U> uClass) {

        return toValueListByListMap(list,field,uClass,false);
    }


    /**
     * 指定Map内属性提取出新的集合，可去重
     *
     * @param list
     * @param feild
     * @param uClass
     * @param distinct
     * @param <U>
     * @return
     */
    public static <U> List<U> toValueListByListMap(List<Map<String, Object>> list, String feild,Class<U> uClass,boolean distinct) {

        List<U> retList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(list) && !ObjectUtils.isEmpty(feild)) {
            U u;
            for (Map<String, Object> map : list) {
                u = (U)map.get(feild);
                if(distinct && retList.contains(u))
                    continue;
                retList.add(u);
            }
        }
        return retList;
    }

    /**
     * 去重复
     * @param strs
     * @param <T>
     * @return
     */
	public static <T> List<T> distinct(List<T> strs) {
	    //
		List<T> distinctStrs = new ArrayList<>();
		for (T t : strs) {
			if (!distinctStrs.contains(t))
				distinctStrs.add(t);
		}
		return distinctStrs;
	}

    /**
     * 按照某一属性去重复
     *
     * @param list
     * @param field
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T,U> List<T> distinct(List<T> list,String field) {
        //
        List<T> retlist = new ArrayList<>();

        if(!ObjectUtils.isEmpty(list)){
            List<String> distinct = new ArrayList<>();
            try {
                Method getMethod = getMenthod(field,list.get(0));
                for (T t : list) {
                    String u = String.valueOf(getMethod.invoke(t));
                    if (!distinct.contains(u)){
                        distinct.add(u);
                        retlist.add(t);
                    }
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return retlist;
    }

	/**
	 * 将数组转化为list
	 * 
	 * @param arr
	 * @return
	 */
	public static <T> List<T> toArrayList(T... arr) {
        if(!ObjectUtils.isEmpty(arr)){
            return Lists.newArrayList(arr);
        }
        return new ArrayList<>();
	}



	public static List<String> spliceArrayListByStr(String strs, String mark) {
		if (!ObjectUtils.isEmpty(strs) && !ObjectUtils.isEmpty(mark)) {
            return toArrayList(strs.split(mark));
		}
		return null;
	}

	/**
	 * 解析为父子关系的列表（树形需要的列表）
	 * 
	 * @author lcs
	 * @param list
     * @param field
	 * @return
	 */
	public static Map<String, List<Map<String, Object>>> bulidTreeListMap(List<Map<String, Object>> list, String field) {

		Map<String, List<Map<String, Object>>> childMap = new HashMap<>();

		if (!ObjectUtils.isEmpty(list) && !ObjectUtils.isEmpty(field)) {
			for (Map<String, Object> map : list) {
				String parentId = FormatUtil.toString(map.get(field));
				if (childMap.get(parentId) == null) {
					List<Map<String, Object>> lst = new ArrayList<>();
					lst.add(map);
					childMap.put(parentId, lst);
				} else {
					childMap.get(parentId).add(map);
				}
			}
		}

		return childMap;
	}

    /**
     * 解析为父子关系的列表（树形需要的列表）
     *
     * @author lcs
     * @param list
     * @param field
     * @return
     */
	public static <T,U> Map<U, List<T>> bulidTreeListMap(List<T> list, String field, Class<U> fieldClass) {

		Map<U, List<T>> childPoMap = new HashMap<>();

		if (!ObjectUtils.isEmpty(list)) {
			try {
                Method getMethod = getMenthod(field,list.get(0));
				for (T t : list) {
					U key = (U)getMethod.invoke(t);
					if (childPoMap.get(key) == null) {
						childPoMap.put(key, toArrayList(t));
					} else {
						childPoMap.get(key).add(t);
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IntrospectionException e) {
				e.printStackTrace();
			}
		}
		return childPoMap;
	}

	/**
	 * 字符串数组转化为 逗号隔开的字符串
	 *
	 * @param arr
	 * @return
	 */
	public static String toStr(String[] arr) {
		StringBuffer str = new StringBuffer();
		if (!ObjectUtils.isEmpty(arr)) {
			for (String s : arr) {
			    if(str.length() > 0){
                    str.append(",");
                }
				str.append(s);
			}
		}
		return str.toString();
	}

	/**
	 * 字符串数组转化为 逗号隔开的字符串
	 *
	 * @param arr
	 * @return
	 */
	public static String toStr(List<String> arr) {
        StringBuffer str = new StringBuffer();
        if (!ObjectUtils.isEmpty(arr)) {
            for (String s : arr) {
                if(str.length() > 0){
                    str.append(",");
                }
                str.append(s);
            }
        }
        return str.toString();
	}

	public static <T> List<T> union(List<T> a, List<T> b) {
		List<T> results = new ArrayList<T>(a);
		results.addAll(b);
		return results;
	}
	/**
	 * 取交集
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> List<T> intersection(List<T> a, List<T> b) {
		List<T> results = new ArrayList<T>(a);
		results.retainAll(b);
		return results;
	}
	/**
	 * 
	 * @param superset
	 * @param subset
	 * @return
	 */
	public static <T> List<T> difference(List<T> superset, List<T> subset) {
		List<T> results = new ArrayList<T>(superset);
		results.removeAll(subset);
		return results;
	}


	public static <T> List<T> complement(List<T> a, List<T> b) {
		return difference(union(a, b), intersection(a, b));
	}
	
	public static <T> T getFirstItem(List<T> list)
	{
		return list == null || list.isEmpty() ? null : list.get(0);
	}

    public static  <T> List<T> filter(List<T> list, String field, Object value) {

        List<T> retlist = new ArrayList<>();
        if (!ObjectUtils.isEmpty(list)) {
            try {
                Method getMethod = getMenthod(field, list.get(0));
                for (T t : list) {
                    Object key = getMethod.invoke(t);
                    if (FormatUtil.toString(key).equals(FormatUtil.toString(value))) {
                        retlist.add(t);
                    }
                }
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return retlist;
    }
}
