package com.ecommerce.backend.response;

public class AuthResponse {

	private String token;
	private String msg;
	
	
	public AuthResponse() {
		super();
	}
	public AuthResponse(String token, String msg) {
		super();
		this.token = token;
		this.msg = msg;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	

}
