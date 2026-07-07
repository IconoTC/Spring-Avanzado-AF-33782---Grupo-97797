package com.example.aop;

public interface AuthenticationService {

	void login();

	void logout();

	boolean isAuthenticated();

}