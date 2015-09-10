package com.ricoh.serverconv.module;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ricoh.serverconv.parser.ConfigNode;
import com.ricoh.serverconv.parser.ConfigParserInterface;
import com.ricoh.serverconv.util.ServerConvProperties;


public class EnvPrePareImpl extends HttpdGetterImpl {
	

	@Override
	public void execCommand(Session session) {
		try {
		  getConfig(session, null, ServerConvProperties.EPEL);

		  getConfig(session, null, ServerConvProperties.HOME_CHANGE_DIR);
		  
		  getConfig(session, null, ServerConvProperties.CHAR_CHANGE);
		  
		  String installs = configMap.get("prepare");
		  String[] inst = installs.split(",");
		  for (String install:inst) {
			  getConfig(session, null, ServerConvProperties.YUM_INSTALL.replaceAll("@@@", install));
		  }
		  String services = configMap.get("service");
		  String[] serv = services.split(",");
		  for (String service:serv) {
			  getConfig(session, null, ServerConvProperties.SERVICE_ON.replaceAll("@@@", service));
		  }
		   
		} catch (JSchException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }

	@Override
	protected synchronized ConfigNode getConfigViaSu(Session session, ConfigParserInterface parser, String command) throws Exception {
		 super.wait_time = 60000;
		 return super.getConfigViaSu(session, parser, command);
	}
	
	@Override
	protected synchronized ConfigNode getConfigViaServer(Session session, ConfigParserInterface parser, String command) throws Exception {
		 super.wait_time = 10000;
		 return super.getConfigViaServer(session, parser, command);
	}
	
	@Override
	public String getValue(String str) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public Object getNode(String str) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void outPut(XSSFWorkbook wb) {
		return;
	}	    

	@Override
	public void compare(Session session, CommandGetterInterface ocom, String module) {
		return;
	}
	
}
