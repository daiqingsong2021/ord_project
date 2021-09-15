package com.wisdom.base.common.util;

import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.leaf.common.PropertyFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ObjectUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 平台上下文环境变量
 *
 * @author
 */
public class ResourceUtil {

	/**
	 * 根据文件夹和文件夹内的参数获取资源集合（例如/fonts/*.txt,查询fonts文件夹下的所有txt文件）
	 *
	 * @param foler
	 * @return
	 */
	public static Resource[] findResouresByFoler(String foler) throws IOException {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources(foler);
		return resources;
	}

	public static InputStream findResoureFile(String resoureName) {
		try {
			Resource rs = findResoure(resoureName);
			InputStream is = rs.getInputStream();
			return is;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Resource findResoure(String resoureName) {
		Resource rs = new ClassPathResource(resoureName);
		return rs;
	}



	/**
	 * 得到配置文件
	 * @param in yml配置文件
	 * @return Map
	 */
	public static Map<String, Object> getYmlPropertie(InputStream in) {
		//DumperOptions options = new DumperOptions();
		//将默认读取的方式设置为块状读取
		//options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		Yaml yaml = new Yaml();
		//Map<String, Object> map = yaml.loadAs(in, Map.class);
		Iterator<Object> result = yaml.loadAll(in).iterator();
		Map<String, Object> allMap = new TreeMap<>();
		while (result.hasNext()) {
			Map<?, ?> map = (Map<?, ?>) result.next();
			iteratorYml(allMap, map, null);
		}
		return allMap;
	}

	/**
	 * 得到配置文件
	 * @param allMap 返回配置
	 * @param map 下级配置
	 * @param key key
	 */
	private static void iteratorYml(Map<String, Object> allMap, Map<?, ?> map, String key) {
		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String key2 = Tools.toString(entry.getKey());
			key2 = key != null ? key + "." + key2 : key2;
			Object value = entry.getValue();
			if (value instanceof Map) {
				iteratorYml(allMap, (Map) value, key2);
			} else {
				allMap.put(key2, value);
			}
		}
	}

	/**
	 * 把${param:value}表达式的value取出替换表达式
	 * @return
	 */
	public static String removeEl(String value){
		if(!Tools.isEmpty(value)){
			Pattern pattern = Pattern.compile("\\$\\{{1}([^\\}])*:{1}([^\\}])*\\}{1}"); //取这种参数${ORACLE_HOST:47.92.71.117}:${ORACLE_PORT:1521}
			Matcher matcher = pattern.matcher(value);
			while (matcher.find()) {
				String group = matcher.group();
				String val = group.split(":")[1].trim().replace("}", "");
				value = value.replace(group, val);
			}
		}
		return value;
	}
}
