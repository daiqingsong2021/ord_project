package com.sso.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@EnableWebSecurity
public class OAuth2Configuration extends WebSecurityConfigurerAdapter {
	protected @Autowired ClientRegistrationRepository crr;
	String outUrl = "http://192.168.43.250:12000/ssoLogin";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests(authorize -> authorize.anyRequest().authenticated()).oauth2Login();
		http.oauth2Client();
		// �������ã�֧�ֵ���ע��
		http.logout(c -> {
			String authorizeUri = crr.findByRegistrationId("bingoiam").getProviderDetails().getAuthorizationUri();
			int idx = authorizeUri.lastIndexOf("/oauth2/authorize");
			String oauth2LogoutUri;
			try {
				// iamsso����ע����ַ��iamsso��/oauth2/logout·�����������ͨ��tokenUri����iamsso�ĸ���ַ���Ͳ���Ҫ����������
				// ����ע����ַҪ��post_logout_redirect_uri��������ʾ����ע����ɺ�ص��ĸ���ַ
				oauth2LogoutUri = authorizeUri.substring(0, idx) + "/oauth2/logout?post_logout_redirect_uri=" + URLEncoder.encode(outUrl, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			// ���ñ���ע����ɺ���е���ע��
			c.logoutSuccessUrl(oauth2LogoutUri);
		});
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOriginPattern("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.setAllowCredentials(true);
		source.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(source);
	}
}