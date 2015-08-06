package com.ricoh.serverconv.module;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ricoh.serverconv.parser.ConfigNode;
import com.ricoh.serverconv.parser.SystemInfoParser;
import com.ricoh.serverconv.util.ServerConvProperties;


public class SystemInfoGetterImpl extends HttpdGetterImpl {
	private ConfigNode config = null;
	private ConfigNode ifconfig = null;
	private ConfigNode envconfig = null;
	private ConfigNode allowconfig = null;
	private ConfigNode resolvconfig = null;
	private ConfigNode nsswconfig = null;
	private ConfigNode syslogconfig = null;
	private ConfigNode ntpconfig = null;

	@Override
	public void execCommand(Session session) {
		try {
		     SystemInfoParser parser = new SystemInfoParser();
		     envconfig = getConfig(session, parser, ServerConvProperties.ENV);

		     config = getConfig(session, parser, ServerConvProperties.HOSTS);
		     if (config != null) getFile(session, "/etc/", "hosts");

		     allowconfig = getConfig(session, parser, ServerConvProperties.HOSTS_ALLOW);

		     resolvconfig = getConfig(session, parser, ServerConvProperties.RESOLV_CONF);

		     nsswconfig = getConfig(session, parser, ServerConvProperties.NSSW_CONF);
    
		     syslogconfig = getConfig(session, parser, ServerConvProperties.SYSLOG);
		     
		     ntpconfig = getConfig(session, parser, ServerConvProperties.NTP_CONF);
		     
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
        buf.append("---------------- env ----------------\n");
	    for (ConfigNode child : envconfig.getChildren()) {
	    	buf.append(child.getName() + "===" + child.getContent() + "\n");
        }	      
        buf.append("---------------- hosts ----------------\n");
	    for (ConfigNode child : config.getChildren()) {
	    	buf.append(child.getName() + "===" + child.getContent() + "\n");
        }	      
        buf.append("---------------- hosts_allow ----------------\n");
	    for (ConfigNode child : allowconfig.getChildren()) {
	    	buf.append(child.getName() + "===" + child.getContent() + "\n");
        }	      
        buf.append("---------------- resolv.conf ----------------\n");
	    for (ConfigNode child : resolvconfig.getChildren()) {
	    	buf.append(child.getName() + "===" + child.getContent() + "\n");
        }	      
        buf.append("---------------- nsswitch.conf ----------------\n");
	    for (ConfigNode child : nsswconfig.getChildren()) {
	    	buf.append(child.getName() + "===" + child.getContent() + "\n");
        }	      
        buf.append("---------------- ntp.conf ----------------\n");
	    for (ConfigNode child : ntpconfig.getChildren()) {
	    	buf.append(child.getName() + "===" + child.getContent() + "\n");
        }	      
        buf.append("---------------- syslog.conf ----------------\n");
	    for (ConfigNode child : syslogconfig.getChildren()) {
	    	buf.append(child.getName() + "===" + child.getContent() + "\n");
        }	      
	    /*	    for (ConfigNode child : ifconfig.getChildren()) {
	    	buf.append(child.getName() +" " + child.getContent() + "\n");
		    for (ConfigNode gchild : child.getChildren()) {
	            buf.append(gchild.getName() + gchild.getContent());
		    }
        }	      */
		return buf.toString();
	}

	@Override
	public Object getNode(String str) {
		// TODO 自動生成されたメソッド・スタブ
		if (str.equals("hosts")) {
		    return config;
		}
		if (str.equals("env")) {
		    return envconfig;
		}
		if (str.equals("hosts_allow")) {
			return allowconfig;
		}
		if (str.equals("resolv")) {
			return resolvconfig;
		}
		if (str.equals("nsswitch")) {
			return nsswconfig;
		}
		if (str.equals("ntp")) {
			return ntpconfig;
		}
		if (str.equals("syslog")) {
			return syslogconfig;
		}

		return null;
	}
	
	@Override
	public void outPut(XSSFWorkbook wb) {
		
		XSSFSheet sheet = wb.createSheet("System");
		
		// TODO 自動生成されたメソッド・スタブ
		String[] strs = {"hosts", "env", "hosts_allow", "resolv", "nsswitch", "ntp", "syslog"};
		int r = 0;
		for (String str:strs) {
		    r = r + 1;
			ConfigNode node = (ConfigNode)getNode(str);
			if (node == null) continue;
		    for (ConfigNode child : node.getChildren()) {
		    	XSSFRow row = sheet.createRow(r);
		    	row.createCell(0).setCellValue(child.getName());
				row.createCell(1).setCellValue(child.getContent());
			    r = r + 1;
		    }
		}
	}
	
	@Override
	public void compare(Session session, CommandGetterInterface ocom, String module) {
		// TODO 自動生成されたメソッド・スタブ

		ConfigNode node = (ConfigNode) ocom.getNode(module);
		ConfigNode oconv = (ConfigNode)getNode(module);
		
		if (module.equals("hosts")) {
			if (!execConv(session, oconv, node, "/etc/hosts")) {
		    	try {
					setFile(session, "/etc/", "hosts", ocom.getConfigMap("localdir"));
					return;
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}
		
	}	


}
