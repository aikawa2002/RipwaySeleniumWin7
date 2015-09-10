package com.ricoh.serverconv.module;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ricoh.serverconv.analyze.SendMailConfigAnalyzerImpl;
import com.ricoh.serverconv.analyze.info.AliasesInfo;
import com.ricoh.serverconv.parser.ConfigNode;
import com.ricoh.serverconv.parser.SendMailConfigParser;
import com.ricoh.serverconv.parser.SystemInfoParser;
import com.ricoh.serverconv.util.ServerConvProperties;


public class SendMailGetterImpl extends HttpdGetterImpl {
	private ConfigNode mcconfig = null;
	private ConfigNode acconfig = null;
	private ConfigNode localconfig = null;
	private ConfigNode aliasconfig = null;
	private ConfigNode mailerconfig = null;
	private ConfigNode virtuseconfig = null;

	@Override
	public void execCommand(Session session) {
		try {
		  SendMailConfigParser parser = new SendMailConfigParser();
		  SystemInfoParser sparser = new SystemInfoParser();
	      String command = ServerConvProperties.MC_COMMAND;
	      if (configMap.get("sendmailmc") != null) {
	    	  command = "cat " + configMap.get("sendmailmc");
		  }
	      mcconfig = getConfig(session, parser, command);
	      
	      acconfig = getConfig(session, parser, ServerConvProperties.AC_COMMAND);
		  if (acconfig != null) getFile(session, ServerConvProperties.SENDMAIL_DIR, "access");
	      
	      localconfig = getConfig(session, parser, ServerConvProperties.LC_COMMAND);
	      if (localconfig != null) getFile(session, ServerConvProperties.SENDMAIL_DIR, "local-host-names");
	      
	      aliasconfig = getConfig(session, parser, ServerConvProperties.ALIAS_COMMAND);
	      if (aliasconfig != null) getFile(session, ServerConvProperties.SENDMAIL_DIR, "aliases");
	      
	      mailerconfig = getConfig(session, sparser, ServerConvProperties.MAILER_COMMAND);
	      if (mailerconfig != null) getFile(session, ServerConvProperties.SENDMAIL_DIR, "mailertable");
	      
	      virtuseconfig = getConfig(session, parser, ServerConvProperties.VIRTUSE_COMMAND);
	      if (virtuseconfig != null) getFile(session, ServerConvProperties.SENDMAIL_DIR, "virtusertable");
		  
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
		if (str == null || str.equals("sendmail")) {
	        buf.append("---------------- sendmail.mc ----------------\n");
		    for (ConfigNode child : mcconfig.getChildren()) {
	              buf.append(child.getName() + "=" + child.getContent() +"\n");
		    }
		}
		if (str == null || str.equals("access")) {
			buf.append("---------------- access ----------------\n");
		    for (ConfigNode child : acconfig.getChildren()) {
	            buf.append(child.getName() + "=" + child.getContent() +"\n");
	        }
		}
		if (str == null || str.equals("local")) {
			buf.append("---------------- local-host-names ----------------\n");
		    for (ConfigNode child : localconfig.getChildren()) {
	            buf.append(child.getName() + "=" + child.getContent() +"\n");
	        }
		}
	    
		if (str == null || str.equals("aliases")) {
			buf.append("---------------- aliases ----------------\n");
		    for (ConfigNode child : aliasconfig.getChildren()) {
	            buf.append(child.getName() + "=" + child.getContent());
			    for (ConfigNode gchild : child.getChildren()) {
		            buf.append(gchild.getContent());
			    }
	            buf.append("\n");
	        }
		}

		if (str == null || str.equals("mailer")) {
			buf.append("---------------- mailertable ----------------\n");
		    for (ConfigNode child : mailerconfig.getChildren()) {
	            buf.append(child.getName() + "=" + child.getContent());
			    for (ConfigNode gchild : child.getChildren()) {
		            buf.append(gchild.getContent());
			    }
	            buf.append("\n");
	        }
		}

		if (str == null || str.equals("virtuse")) {
			buf.append("---------------- virtusertable ----------------\n");
		    for (ConfigNode child : virtuseconfig.getChildren()) {
	            buf.append(child.getName() + "=" + child.getContent());
			    for (ConfigNode gchild : child.getChildren()) {
		            buf.append(gchild.getContent());
			    }
	            buf.append("\n");
	        }
		}
		return buf.toString();
	}

	@Override
	public Object getNode(String str) {
		// TODO 自動生成されたメソッド・スタブ
		if (str.equals("sendmail")) {
		    return mcconfig;
		}
		if (str.equals("access")) {
		    return acconfig;
		}
		if (str.equals("local")) {
		    return localconfig;
		}
	    
		if (str.equals("aliases")) {
		    return aliasconfig;
		}

		if (str.equals("mailer")) {
		    return mailerconfig;
		}

		if (str == null || str.equals("virtuse")) {
		    return virtuseconfig;
		}
		return null;
	}
	
	@Override
	public void outPut(XSSFWorkbook wb) {
		// TODO 自動生成されたメソッド・スタブ
		XSSFSheet sheet = wb.createSheet("SendMail");
		
		String[] mlist = {"access", "local", "mailer", "virtuse"};
		//sendmail情報取得
		SendMailConfigAnalyzerImpl mailanalize = new SendMailConfigAnalyzerImpl();
		int r = 1;
		ConfigNode node = (ConfigNode)getNode("sendmail");
		if (node != null) {
	    for (ConfigNode child : node.getChildren()) {
	    	XSSFRow row = sheet.createRow(r);
	    	row.createCell(0).setCellValue(child.getName());
	    	if (child.getContent().indexOf(",") > -1) {
	    		String[] strs = child.getContent().split(",");
	    		String value = strs[0].replaceAll("'", "");
	    		row.createCell(1).setCellValue(value);
	    		row.createCell(2).setCellValue(strs[1].replaceAll("`", ""));
	    		row.createCell(3).setCellValue(ServerConvProperties.MailMcMap.get(child.getName() + "_"+ value ));
	    	} else {
	    		row.createCell(1).setCellValue(child.getContent());
	    		if (child.getName().equals("FEATURE") || child.getName().equals("define")) {
		    		row.createCell(3).setCellValue(ServerConvProperties.MailMcMap.get(child.getName() + "_"+ child.getContent() ));
	    		} else {
		    		row.createCell(3).setCellValue(ServerConvProperties.MailMcMap.get(child.getName()));
	    		}
	    	}
		    r = r + 1;
	    }
		
		for (String command:mlist) {
		    r = r + 1;
			node = (ConfigNode)getNode(command);
		    for (ConfigNode child : node.getChildren()) {
		    	if (child.getName().equals("cat")) continue;
		    	XSSFRow row = sheet.createRow(r);
		    	row.createCell(0).setCellValue(child.getName());
			    r = r + 1;
		    }
		}
		}
		
	    r = r + 1;
	    node = (ConfigNode)getNode("aliases");
		if (node != null) {
	    for (ConfigNode child : node.getChildren()) {
	    	if (child.getContent() == null) continue;
	    	AliasesInfo info = (AliasesInfo)mailanalize.analyze("aliases",child.getContent());
	    	if (info == null) continue;
	    	XSSFRow row = sheet.createRow(r);
	    	row.createCell(0).setCellValue(info.getUsername());
	    	if (info.getKubun().equals("file")) {
		    	row.createCell(1).setCellValue(info.getLocalfile());
	    	} else if (info.getKubun().equals("command")) {
		    	row.createCell(1).setCellValue(info.getCommand());
	    	} else if (info.getKubun().equals("include")) {
		    	row.createCell(1).setCellValue(info.getIncludefile());
	    	} else if (info.getKubun().equals("email")) {
	    		int j = 1;
				for (String mail:info.getEmaillist()) {
			    	row.createCell(j).setCellValue(mail);
			    	j = j + 1;
				}
	    	} else {
		    	row.createCell(1).setCellValue(info.getLocaluser());
	    	}
    		int k = 2;
		    for (ConfigNode gchild : child.getChildren()) {
		    	row.createCell(k).setCellValue(gchild.getContent().trim());
		    	k = k + 1;
		    }
		    //cell.setCellStyle(cellstyle);   // これでcellに対してスタイルを設定
		    r = r + 1;
	    }
		}
	}

	@Override
	public void compare(Session session, CommandGetterInterface ocom, String module) {
		// TODO 自動生成されたメソッド・スタブ
		
		String[] modules = {"local","aliases","mailer","virtuse","access"};
		String[] modules_value = {"local-host-names","aliases","mailertable","virtusertable","access"};

		for (int i= 0;i < modules.length;i++) {
			ConfigNode node = (ConfigNode) ocom.getNode(modules[i]);
			ConfigNode oconv = (ConfigNode)getNode(modules[i]);
			if (node == null) continue;
			boolean flg = true;
			if (modules[i].equals("aliases")) {
				flg = super.execConv(session, oconv, node, "/etc/" + modules_value[i]);
			} else {
				flg = execConv(session, oconv, node, "/etc/mail/" + modules_value[i]);
			}
			if (!flg) {
		    	try {
					if (modules[i].equals("aliases")) {
						setFile(session, "/etc/", modules_value[i], ocom.getConfigMap("localdir"));
					} else {
						setFile(session, "/etc/mail/", modules_value[i], ocom.getConfigMap("localdir"));
					}
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			}
		}
	}	
	
	protected boolean execConv(Session session, ConfigNode oconv, ConfigNode node, String file) {
		StringBuffer buf = new StringBuffer();
	    for (ConfigNode child : node.getChildren()) {
	    	boolean convflg = true;
		    for (ConfigNode ocon : oconv.getChildren()) {
		    	if (child.getName().equals(ocon.getName())) {
			    	convflg = false;
		    		break;
		    	}
		    }
	    	if (convflg) {
	    		buf.append(child.getName() + "\n");
	    	}
	    }
	    if (buf.length() == 0) {
	    	return false;
	    }
	    String com = "cat <<EOF >> " +  file + "\n" + buf.toString() + "EOF";
		System.out.println(session.getHost() + ":" + com);
	    try {
			getConfig(session, null, com);
			if (file.indexOf("aliases") > -1)  getConfig(session, null, "newaliases");
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	    return true;
	}

}
