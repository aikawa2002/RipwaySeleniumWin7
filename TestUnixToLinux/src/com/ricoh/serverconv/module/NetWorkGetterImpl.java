package com.ricoh.serverconv.module;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ricoh.serverconv.parser.ApacheConfigParser;
import com.ricoh.serverconv.parser.ConfigNode;
import com.ricoh.serverconv.util.ServerConvProperties;


public class NetWorkGetterImpl extends HttpdGetterImpl {
	private ConfigNode sshconfig = null;
	
	@Override
	public void execCommand(Session session) {
		try {
			ApacheConfigParser parser = new ApacheConfigParser();
	      sshconfig = getConfig(session, parser, ServerConvProperties.CF_COMMAND);
		  if (sshconfig != null) getFile(session, ServerConvProperties.SSH_DIR, "sshd_config");
	      
		} catch (JSchException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }

	@Override
	public String getValue(String str) {
		// TODO 自動生成されたメソッド・スタブ
		StringBuffer buf = new StringBuffer();
		if (str == null || str.equals("ssh")) {
	        buf.append("---------------- sshd_config ----------------\n");
		    for (ConfigNode child : sshconfig.getChildren()) {
	              buf.append(child.getName() + "(" + ServerConvProperties.SshdMap.get(child.getName()) +")=" + child.getContent() +"\n");
		    }
		}
		return buf.toString();
	}

	@Override
	public Object getNode(String str) {
		// TODO 自動生成されたメソッド・スタブ
		if (str.equals("ssh")) {
		    return sshconfig;
		}
		return null;
	}
	
	@Override
	public void outPut(XSSFWorkbook wb) {
		// TODO 自動生成されたメソッド・スタブ
		XSSFSheet sheet = wb.createSheet("SSH");
		int r = 1;
		ConfigNode node = (ConfigNode)getNode("ssh");
		if (node == null) return;
	    for (ConfigNode child : node.getChildren()) {
	    	XSSFRow row = sheet.createRow(r);
	    	row.createCell(0).setCellValue(child.getName());
	    	row.createCell(1).setCellValue(ServerConvProperties.SshdMap.get(child.getName()));
			row.createCell(2).setCellValue(child.getContent());
		    r = r + 1;
	    }
	}

	@Override
	public void compare(Session session, CommandGetterInterface ocom, String module) {
		// TODO 自動生成されたメソッド・スタブ
/*    	try {
			setFile(session, "/etc/ssh/", "sshd_config", ocom.getConfigMap("localdir"));
			return;
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
*/		
	}
}
