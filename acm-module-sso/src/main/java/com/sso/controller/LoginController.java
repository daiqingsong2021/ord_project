package com.sso.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import com.sso.config.JwtAuthenticationRequest;
import com.sso.config.ApiResult;
import com.alibaba.fastjson.JSON;

@RestController
public class LoginController {
	String baseUrl = "http://192.168.43.250:8765";
	String loginUrl = "http://192.168.43.250:3000";

	@GetMapping("/ssoLogin")
	public void helloWorld(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.addHeader("Content-Type", "application/json");
		response.setContentType("text/html;charset=UTF-8");
		Authentication authc = SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticatedPrincipal p = (OAuth2AuthenticatedPrincipal) authc.getPrincipal();
		String ub = (String) p.getAttributes().get("login_name");
		String name = (String) p.getAttributes().get("name");
		name = java.net.URLEncoder.encode(java.net.URLEncoder.encode(name));
		// String name = "";
		// name = java.net.URLDecoder.decode(name,"UTF-8");
		String uid = (String) p.getAttributes().get("uid");
		String phone_number = (String) p.getAttributes().get("phone_number");
		response.sendRedirect(loginUrl+"/login?uid=" + uid + "&name=" + name + "&userName=" + ub + "&phone_number=" + phone_number);
	}

	@GetMapping(value = "/queryTrafficMainList/{pageSize}/{currentPageNum}")
	public ApiResult<String> queryTrafficMainList(@RequestParam Map<String, Object> mapWhere, @PathVariable("pageSize") Integer pageSize, @PathVariable("currentPageNum") Integer currentPageNum) {
		Authentication authc = SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticatedPrincipal p = (OAuth2AuthenticatedPrincipal) authc.getPrincipal();
		String ub = (String) p.getAttributes().get("login_name");
		String name = (String) p.getAttributes().get("name");
		String uid = (String) p.getAttributes().get("uid");
		String phone_number = (String) p.getAttributes().get("phone_number");
		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(baseUrl);
		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.TEMPLATE_AND_VALUES);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setUriTemplateHandler(factory);
		// request body
		Map<String, String> foo = new HashMap<>();
		foo.put("userName", ub);
		foo.put("password", "123456");
		foo.put("name", name);
		foo.put("uid", uid);
		foo.put("phone_number", "");
		ResponseEntity<String> responseEntity = restTemplate.postForEntity("/api/auth/jwt/ssoToken", foo, String.class);
		// parse response
		Map mapTypes = JSON.parseObject(responseEntity.getBody());
		if ((Integer) mapTypes.get("status") == 200) {
			String data = (String) mapTypes.get("data");
			if (data != null && data != "") {
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Type", "application/json;charset=UTF-8");
				headers.add("Authorization", (String) mapTypes.get("data"));
				HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
				String urlTemplate = "/api/dc1/traffic/queryTrafficMainList/" + pageSize + "/" + currentPageNum+"?startTime="+mapWhere.get("startTime")+"&endTime="+mapWhere.get("endTime");
				String url = String.format(urlTemplate, 1, 2);
				ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
				Map resMap = JSON.parseObject(resEntity.getBody());
				return ApiResult.success1(resMap.get("data"), (Integer) resMap.get("total"));
			} else {
				return ApiResult.error("调用queryTrafficMainList接口失败");
			}
		} else {
			return ApiResult.error("调用token接口失败");
		}
	}
}
