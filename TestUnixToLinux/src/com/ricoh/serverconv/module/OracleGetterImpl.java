package com.ricoh.serverconv.module;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ricoh.serverconv.parser.ConfigNode;
import com.ricoh.serverconv.parser.OracleConfigParser;
import com.ricoh.serverconv.util.ServerConvProperties;


public class OracleGetterImpl extends HttpdGetterImpl {
	private ConfigNode oraconfig = null;
	
	@Override
	public void execCommand(Session session) {
		try {
			OracleConfigParser parser = new OracleConfigParser();
			oraconfig = getConfig(session, parser, "cat " + ServerConvProperties.TNSNAMES_DIR + ServerConvProperties.TNSNAMES);
		  if (oraconfig != null) getFile(session, ServerConvProperties.TNSNAMES_DIR, ServerConvProperties.TNSNAMES);
	      
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
		if (str == null || str.equals("oracle")) {
	        buf.append("---------------- oracle_config ----------------\n");
		    for (ConfigNode child : oraconfig.getChildren()) {
	              buf.append(child.getName() + " " + child.getContent() +"\n");
		    }
		}
		return buf.toString();
	}

	@Override
	public Object getNode(String str) {
		// TODO 自動生成されたメソッド・スタブ
		    return oraconfig;
	}
	
	@Override
	public void outPut(XSSFWorkbook wb) {
		// TODO 自動生成されたメソッド・スタブ
		XSSFSheet sheet = wb.createSheet("Oracle");
		XSSFCellStyle style = getHeaderStyle(wb);
		int r = 1;
		ConfigNode node = (ConfigNode)getNode(null);
		if (node != null) {
			String name = null;
			setHeader("oracle", sheet, r,style);
		    for (ConfigNode child : node.getChildren()) {
		    	if (child.getChildren().isEmpty()) continue;
			    for (ConfigNode gchild : child.getChildren()) {
				    r = r + 1;
				    XSSFRow row = sheet.createRow(r);
			    	if (name == null || !name.equals(child.getName())) {
				    	row.createCell(0).setCellValue(child.getName());
				    	name = child.getName();
			    	}
			    	row.createCell(1).setCellValue(gchild.getName());
			    }
		    }
		}
	}

	@Override
	public void compare(Session session, CommandGetterInterface ocom, String module) {
		// TODO 自動生成されたメソッド・スタブ
		ConfigNode node = (ConfigNode) ocom.getNode(module);
		if (node == null || node.getChildren().isEmpty()) return;

		try {
			setFile(session, "/export/home/oracle/", ServerConvProperties.ORACLEZIP, "D:\\conv\\");
			setFile(session, "/export/home/oracle/", ServerConvProperties.INSTALLRSP, "D:\\conv\\");
    		getConfig(session, null, "unzip -o  -d /export/home/oracle " + "/export/home/oracle/" + ServerConvProperties.ORACLEZIP);
    		getConfig(session, null, ServerConvProperties.INSTALLCMD);
    		getConfig(session, null, "sudo -u oracle mkdir -p " + ServerConvProperties.TNSNAMES_DIR.replaceAll("9.2.0", "11.2.0"));
			setFile(session, ServerConvProperties.TNSNAMES_DIR.replaceAll("9.2.0", "11.2.0"), ServerConvProperties.TNSNAMES, ocom.getConfigMap("localdir"));
    		//getConfig(session, null, "cpan DBD::Oracle");
    		getConfig(session, null, "/export/home/oracle/app/oracle/product/11.2.0/root.sh");
			return;
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
	}
}
