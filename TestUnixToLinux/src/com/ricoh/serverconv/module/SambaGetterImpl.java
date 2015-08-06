package com.ricoh.serverconv.module;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ricoh.serverconv.parser.ConfigNode;
import com.ricoh.serverconv.parser.SambaConfigParser;
import com.ricoh.serverconv.util.ServerConvProperties;


public class SambaGetterImpl extends HttpdGetterImpl {
	private ConfigNode config = null;

	@Override
	public void execCommand(Session session) {
		try {
	      config = getConfig(session, new SambaConfigParser(), ServerConvProperties.SAMBACOMMAND);
	      if (config != null) getFile(session, ServerConvProperties.SAMBA_DIR, "smb.conf");
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
        buf.append("---------------- smb.conf ----------------\n");
	    for (ConfigNode child : config.getChildren()) {
            buf.append(child.getName() + "=" + child.getContent() + "\n");
		    for (ConfigNode gchild : child.getChildren()) {
	            buf.append(gchild.getContent() + "\n");
		    }
        }	      
		return buf.toString();
	}

	@Override
	public Object getNode(String str) {
		// TODO 自動生成されたメソッド・スタブ
		return config;
	}

	@Override
	public void outPut(XSSFWorkbook wb) {
		// TODO 自動生成されたメソッド・スタブ
		XSSFSheet sheet = wb.createSheet("Samba");
		int r = 1;
		ConfigNode node = (ConfigNode)getNode(null);
		if (node == null) return;
		String name = null;
		XSSFCellStyle style = getHeaderStyle(wb);
		setHeader("samba", sheet, r,style);
	    for (ConfigNode child : node.getChildren()) {
		    for (ConfigNode gchild : child.getChildren()) {
			    r = r + 1;
			    XSSFRow row = sheet.createRow(r);
		    	if (name == null || !name.equals(child.getName())) {
			    	row.createCell(0).setCellValue(child.getName());
			    	name = child.getName();
		    	}
				row.createCell(1).setCellValue(gchild.getName());
				row.createCell(2).setCellValue(gchild.getContent());
		    	row.createCell(3).setCellValue(ServerConvProperties.SmbMap.get(gchild.getName()));
		    }
	    }
	}
	
	@Override
	public void compare(Session session, CommandGetterInterface ocom, String module) {
		// TODO 自動生成されたメソッド・スタブ
		ConfigNode node = (ConfigNode) ocom.getNode(null);
		ConfigNode oconv = (ConfigNode) getNode(null);
    	try {
			setFile(session, "/etc/samba/", "smb.conf", ocom.getConfigMap("localdir"));
			return;
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
	}
}
