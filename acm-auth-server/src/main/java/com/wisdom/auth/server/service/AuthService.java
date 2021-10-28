package com.wisdom.auth.server.service;


import com.wisdom.auth.server.util.user.JwtAuthenticationRequest;

public interface AuthService {
	String login(JwtAuthenticationRequest authenticationRequest) throws Exception;

	String snLogin(JwtAuthenticationRequest authenticationRequest) throws Exception;
	String ssoLogin(JwtAuthenticationRequest authenticationRequest) throws Exception;

	String refresh(String oldToken) throws Exception;

	void validate(String token) throws Exception;
}
