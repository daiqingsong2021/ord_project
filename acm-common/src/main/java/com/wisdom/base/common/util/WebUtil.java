package com.wisdom.base.common.util;

import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.calc.calendar.Tools;
import org.dozer.DozerBeanMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class WebUtil {

	/**
	 * 得到HttpHeaders
	 *
	 * @return HttpHeaders
	 */
	public static HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		HttpServletRequest servletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Enumeration<String> headerNames = servletRequest.getHeaderNames();
		if (!ObjectUtils.isEmpty(headerNames)) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				String value = servletRequest.getHeader(name);
                if(name.equalsIgnoreCase("accept-encoding") || "content-type".equalsIgnoreCase(name) && value.indexOf("multipart/form-data") > -1){
                    continue;
                }
                headers.add(name, value);
			}
		}
		headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
		headers.add("transfer-encoding", "chunked");
		return headers;
	}

	/**
	 * POST 请求
	 * @param url
	 * @param param
	 * @param _class
	 * @param <T>
	 * @return
	 */
	public static <T> T post(String url, Object param, Class<T> _class){
		DozerBeanMapper mapper = new DozerBeanMapper();
		HttpHeaders headers = WebUtil.getHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(param, headers);
		RestTemplate restTemplate = new RestTemplate();
		ApiResult<T> apiResult = restTemplate.exchange(url, HttpMethod.POST, entity,
			new ParameterizedTypeReference<ApiResult<T>>() {}).getBody();
		if (!apiResult.isSuccess()) {
			throw new BaseException(apiResult.getMessage());
		} else if(!Tools.isEmpty(apiResult.getData())){
			return mapper.map(apiResult.getData(), _class);
		}
		return null;
	}

	/**
	 * POST 请求
	 * @param url
	 * @param param
	 * @return
	 */
	public static ApiResult post(String url, Object param){
		HttpHeaders headers = WebUtil.getHttpHeaders();
		HttpEntity<?> entity = new HttpEntity<>(param, headers);
		RestTemplate restTemplate = new RestTemplate();
		if(!url.contains("http")){
			url = "http:"+url;
		}
		ApiResult result = restTemplate.exchange(url, HttpMethod.POST, entity, new ParameterizedTypeReference<ApiResult>() {}).getBody();
		return result;
	}
}
