package jp.co.ricoh.rits.dto;

import java.util.HashMap;

public class ZabbixDto {
	String auth = null;
	long id;
    String jsonrpc="2.0";
	String method = null;
	HashMap<String,String> params = new HashMap<String,String>();
	String result = null;
	HashMap<String, String> error = null;

	
	public String getJsonrpc() {
		return jsonrpc;
	}
	public void setJsonrpc(String jsonrpc) {
		this.jsonrpc = jsonrpc;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public HashMap<String, String> getParams() {
		return params;
	}
	public void setParams(HashMap<String, String> params) {
		this.params = params;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public HashMap<String, String> getError() {
		return error;
	}
	public void setError(HashMap<String, String> error) {
		this.error = error;
	}

}
