package com.ricoh.serverconv.module;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.Session;


public interface CommandGetterInterface {
	
	public void init(Map<String, String> configMap);
	public void execCommand(Session session);
	public String getValue(String str);
	public Object getNode(String str);
	public String getConfigMap(String str);
	public void outPut(XSSFWorkbook wb);
	public void compare(Session session, CommandGetterInterface command,String module);

}
