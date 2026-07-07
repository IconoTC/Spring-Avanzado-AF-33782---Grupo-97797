package com.example.aop;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationServiceMock implements AuthenticationService {
	private boolean authenticated = false;
	
	@Override
	public void login() {
		authenticated = true;
	}
	
	@Override
	public void logout() {
		authenticated = false;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}
}
