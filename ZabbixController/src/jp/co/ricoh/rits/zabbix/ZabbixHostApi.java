package jp.co.ricoh.rits.zabbix;

import java.util.HashMap;

import jp.co.ricoh.rits.dto.ZabbixDto;

public class ZabbixHostApi extends ZabbixApi {
	 
	  public ZabbixHostApi(String url){
		  super(url);
	  }

	  public String getHost(String user, String password,long id) throws Exception{
		if (authId == null )auth(user,password);
	    ZabbixDto zabbixDto = new ZabbixDto();
	    HashMap<String,String> map = new HashMap<String,String>();

	    zabbixDto.setAuth(authId);
	    zabbixDto.setMethod("host.get");
	    zabbixDto.setId(id);
        map.put("output", "extend");
	    zabbixDto.setParams(map);

	    return response(zabbixDto);
	  }
	  
	  public String createTemplate(String user, String password) throws Exception{
		if (authId == null )auth(user,password);
	    ZabbixDto zabbixDto = new ZabbixDto();
	    HashMap<String,String> map = new HashMap<String,String>();

	    zabbixDto.setAuth(authId);
	    zabbixDto.setMethod("template.create");
        map.put("user", user);
	    map.put("password", password);
	    zabbixDto.setParams(map);
	    return response(zabbixDto);

	  }
	  
}
