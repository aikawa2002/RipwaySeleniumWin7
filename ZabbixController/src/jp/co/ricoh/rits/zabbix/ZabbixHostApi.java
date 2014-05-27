package jp.co.ricoh.rits.zabbix;

import java.util.HashMap;

import jp.co.ricoh.rits.dto.ZabbixDto;

public class ZabbixHostApi extends ZabbixApi {
	 
	  public ZabbixHostApi(String url, String user, String password) {
		super(url, user, password);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public String get(long id) throws Exception{
		ZabbixDto zabbixDto = new ZabbixDto();
	    HashMap<String,String> map = new HashMap<String,String>();

	    zabbixDto.setAuth(authId);
	    zabbixDto.setMethod("host.get");
	    zabbixDto.setId(id);
        map.put("output", "extend");
	    zabbixDto.setParams(map);

	    return response(zabbixDto);
	  }
	  
	  public String create(long id) throws Exception{
	    ZabbixDto zabbixDto = new ZabbixDto();
	    HashMap<String,String> map = new HashMap<String,String>();

	    zabbixDto.setAuth(authId);
	    zabbixDto.setMethod("host.create");
        map.put("host", user);
	    map.put("interfaces", password);
	    map.put("groups", null);
	    map.put("templates", null);
	    map.put("inventory", null);
	    zabbixDto.setId(id);
	    zabbixDto.setParams(map);
	    return response(zabbixDto);
	    
	  }
	  

	  public String exists(long id) throws Exception{
	    ZabbixDto zabbixDto = new ZabbixDto();
	    HashMap<String,String> map = new HashMap<String,String>();

	    zabbixDto.setAuth(authId);
	    zabbixDto.setMethod("host.exists");
        map.put("hostid", null);
	    map.put("host", null);
	    map.put("name", null);
	    map.put("node", null);
	    map.put("nodeids", null);
	    zabbixDto.setId(id);
	    zabbixDto.setParams(map);
	    return response(zabbixDto);
	  }
	  	  
}
