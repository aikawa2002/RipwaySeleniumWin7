package com.ricoh.serverconv.module;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ricoh.serverconv.analyze.ConfigAnalyzerInterface;
import com.ricoh.serverconv.analyze.UserConfigAnalyzerImpl;
import com.ricoh.serverconv.analyze.info.GroupInfo;
import com.ricoh.serverconv.analyze.info.PasswdInfo;
import com.ricoh.serverconv.parser.ConfigNode;
import com.ricoh.serverconv.parser.CronConfigParser;
import com.ricoh.serverconv.parser.ProfileConfigParser;
import com.ricoh.serverconv.parser.RcConfigParser;
import com.ricoh.serverconv.parser.UserAuthConfigParser;
import com.ricoh.serverconv.parser.UserConfigParser;
import com.ricoh.serverconv.util.ServerConvProperties;


public class UserGetterImpl extends HttpdGetterImpl {
	private ConfigNode passwdconfig = null;
	private ConfigNode groupconfig = null;
	private ConfigNode shadowconfig = null;
	private ConfigNode cronconfig = null;
	private ConfigNode authconfig = null;
	private ConfigNode profileconfig = null;
	private ConfigNode cshprofileconfig = null;
	private ConfigNode bashprofileconfig = null;
    private SortedMap<Integer, Integer> groupID = new TreeMap<>();
    private SortedMap<Integer, Integer> userID = new TreeMap<>();
	ConfigAnalyzerInterface useranalize = new UserConfigAnalyzerImpl();

	@Override
	public void execCommand(Session session) {
		try {
	      UserConfigParser parser = new  UserConfigParser();
	      passwdconfig = getConfig(session, parser, ServerConvProperties.PASSWD);
	      
	      groupconfig = getConfig(session, parser, ServerConvProperties.GROUP);
	      
	      shadowconfig = getConfig(session, parser, ServerConvProperties.SHADOW);
	    
	    CronConfigParser cparser = new CronConfigParser();
	    cronconfig = getConfig(session, cparser, ServerConvProperties.CRON);

	    UserAuthConfigParser uaparser = new UserAuthConfigParser();
	    authconfig = getConfig(session, uaparser, ServerConvProperties.SSH);
	    
	    ProfileConfigParser pparser = new ProfileConfigParser();
	    profileconfig = getConfig(session, pparser, ServerConvProperties.PROFILE);
	    
	    RcConfigParser rcparser = new RcConfigParser();
	    bashprofileconfig = getConfig(session, rcparser, ServerConvProperties.BSHPROFILE);
	    
	    cshprofileconfig = getConfig(session, rcparser, ServerConvProperties.CSHPROFILE);
	    
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
		StringBuffer buf = new StringBuffer();
		if (str == null || str.equals("passwd")) {
	        buf.append("---------------- passwd ----------------\n");
		    for (ConfigNode child : passwdconfig.getChildren()) {
	              buf.append(child.getName() + "=" + child.getContent() +"\n");
		    }
		}
		if (str == null || str.equals("group")) {
			buf.append("---------------- group ----------------\n");
		    for (ConfigNode child : groupconfig.getChildren()) {
	            buf.append(child.getName() + "=" + child.getContent() +"\n");
	        }
		}
		if (str == null || str.equals("shadow")) {
			buf.append("---------------- shadow ----------------\n");
		    for (ConfigNode child : shadowconfig.getChildren()) {
	            buf.append(child.getName() + "=" + child.getContent() +"\n");
	        }
		}
	    
		if (str == null || str.equals("cron")) {
	        buf.append("---------------- cron ----------------\n");
		    for (ConfigNode child : cronconfig.getChildren()) {
	            buf.append(child.getName() + "=" + child.getContent() +"\n");
	        }
		}
		
		return buf.toString();
	}

	@Override
	public Object getNode(String str) {
		// TODO 自動生成されたメソッド・スタブ
		if (str.equals("passwd")) {
		    return passwdconfig;
		}
		if (str.equals("group")) {
		    return groupconfig;
		}
		if (str.equals("shadow")) {
			return shadowconfig;
		}
		if (str.equals("cron")) {
			return cronconfig;
		}
		if (str.equals("auth")) {
			return authconfig;
		}
		if (str.equals("profile")) {
			return profileconfig;
		}
		if (str.equals("bash")) {
			return bashprofileconfig;
		}
		if (str.equals("csh")) {
			return cshprofileconfig;
		}
		return null;
	}
	
	@Override
	public void outPut(XSSFWorkbook wb) {
		// TODO 自動生成されたメソッド・スタブ
		XSSFSheet sheet = wb.createSheet("User");
		XSSFCellStyle style = getHeaderStyle(wb);
		
		//ユーザ情報取得
		ConfigNode node = (ConfigNode)getNode("passwd");
		int r = 1;
		if (node != null) {
			setHeader("passwd", sheet, r,style);
		    r = r + 1;
		    for (ConfigNode child : node.getChildren()) {
		    	PasswdInfo info = (PasswdInfo)useranalize.analyze("passwd",child.getContent());
		    	XSSFRow row = sheet.createRow(r);
		    	row.createCell(0).setCellValue(info.getUser_name());
		    	row.createCell(1).setCellValue(info.getReal_name());
		    	row.createCell(2).setCellValue(info.getUser_id());
		    	row.createCell(3).setCellValue(info.getGroup_id());
		    	row.createCell(4).setCellValue(info.getHome_dir());
		    	row.createCell(5).setCellValue(info.getLogin_shell());
			    r = r + 1;
		    }
		}

	    r = r + 1;
		node = (ConfigNode)getNode("group");
		if (node != null) {
			setHeader("group", sheet, r,style);
		    r = r + 1;
		    for (ConfigNode child : node.getChildren()) {
		    	GroupInfo info = (GroupInfo)useranalize.analyze("group",child.getContent());
		    	XSSFRow row = sheet.createRow(r);
		    	row.createCell(0).setCellValue(info.getGroup_name());
		    	row.createCell(1).setCellValue(info.getGroup_id());
		    	row.createCell(2).setCellValue(info.getGroup_users());
			    
			    r = r + 1;
		    }
		}
		
	    r = r + 1;
		node = (ConfigNode)getNode("cron");
		if (node != null) {
			String name = null;
			setHeader("cron", sheet, r,style);
		    for (ConfigNode child : node.getChildren()) {
		    	if (child.getChildren().isEmpty()) continue;
			    for (ConfigNode gchild : child.getChildren()) {
				    r = r + 1;
				    XSSFRow row = sheet.createRow(r);
			    	if (name == null || !name.equals(child.getName())) {
				    	row.createCell(0).setCellValue(child.getName());
				    	name = child.getName();
			    	}
			    	row.createCell(1).setCellValue(gchild.getContent());
			    }
		    }
		}

	    r = r + 1;
		node = (ConfigNode)getNode("auth");
		if (node != null) {
			String name = null;
		    r = r + 1;
			setHeader("auth", sheet, r,style);
		    for (ConfigNode child : node.getChildren()) {
		    	if (child.getChildren().isEmpty()) continue;
			    for (ConfigNode gchild : child.getChildren()) {
				    r = r + 1;
				    XSSFRow row = sheet.createRow(r);
			    	if (name == null || !name.equals(child.getName())) {
				    	row.createCell(0).setCellValue(child.getName());
				    	name = child.getName();
			    	}
			    	row.createCell(1).setCellValue(gchild.getContent());
			    }
		    }
	    }

	    r = r + 1;
		node = (ConfigNode)getNode("profile");
		if (node != null) {
			String name = null;
		    r = r + 1;
			setHeader("profile", sheet, r,style);
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
			    	row.createCell(2).setCellValue(gchild.getContent());
			    }
		    }
		}
		
	    r = r + 1;
		node = (ConfigNode)getNode("bash");
		if (node != null) {
			String name = null;
		    r = r + 1;
			setHeader("profile", sheet, r,style);
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
		
	    r = r + 1;
		node = (ConfigNode)getNode("csh");
		if (node != null) {
			String name = null;
		    r = r + 1;
			setHeader("profile", sheet, r,style);
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
		ConfigNode oconv = (ConfigNode)getNode(module);
		
		if (module.equals("passwd")) {
			getUniqUserId(oconv, node);
			if (groupID.size() == 0) {
				getUniqGroupId((ConfigNode)getNode("group"), (ConfigNode) ocom.getNode("group"));
			}
			execConvPasswd(session, oconv, node, ServerConvProperties.PA);
		} else if (module.equals("group")) {
			getUniqGroupId(oconv, node);
			execConvGroup(session, oconv, node, ServerConvProperties.GR);
			// gshadowファイル生成
			System.out.println(session.getHost() + ":grpconv");
		    try {
				getConfig(session, null, "grpconv");
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		} else if (module.equals("shadow")) {
			//execConv(session, oconv, node, SH);
			// gshadowファイル生成
			System.out.println(session.getHost() + ":pwconv");
		    try {
				getConfig(session, null, "pwconv");
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		} else if (module.equals("cron")) {
		    for (ConfigNode child : node.getChildren()) {
		    	boolean convflg = true;
		    	if (child.getChildren().isEmpty()) continue;
			    for (ConfigNode ocon : oconv.getChildren()) {
			    	if (child.getName().equals(ocon.getName())) {
			    		execConvCron(session, ocon, child, child.getName());
				    	convflg = false;
			    	}
			    }
			    if (convflg) {
		    		execConvCron(session, null, child, child.getName());
			    }
		    }
		} else if (module.equals("bash")) {
		    for (ConfigNode child : node.getChildren()) {
	    		String file = "/export/home/" + child.getName() + "/.bashrc";
	    		execConvRc(session, oconv, child, file);
		    }
		} else if (module.equals("csh")) {
		    for (ConfigNode child : node.getChildren()) {
	    		String file = "/export/home/" + child.getName() + "/.cshrc";
	    		execConvRc(session, oconv, child, file);
		    }
		} else if (module.equals("auth")) {
		    for (ConfigNode child : node.getChildren()) {
	    		String auth_file = "/export/home/" + child.getName() + "/" + ServerConvProperties.AU;
	    		execConvRc(session, oconv, child, auth_file);
	    		
		    }
	    }
	}
	
	private void execConvRc(Session session, ConfigNode oconv, ConfigNode child, String file) {
    	boolean convflg = true;
		String com = null;
		if (child.getChildren().isEmpty()) return;
	    for (ConfigNode ocon : oconv.getChildren()) {
	    	if (child.getName().equals(ocon.getName())) {
		    	convflg = false;
	    		break;
	    	}
	    }
    	if (convflg) {
    		StringBuffer buf = new StringBuffer();
		    for (ConfigNode gchild : child.getChildren()) {
	    		buf.append((gchild.getContent().replaceAll(Matcher.quoteReplacement("$")
	    													, Matcher.quoteReplacement("\\$")))
	    									   .replaceAll("9.2.0", "11.2.0") + "\n");
		    }
		    if (buf.length() == 0) return;
		    com = "cat <<EOF >> " +  file + "\n" + buf.toString() + "EOF";
			System.out.println(session.getHost() + ":" + com);
		    try {
				getConfig(session, null, com);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
    	}
	}
	
	private void execConvPasswd(Session session, ConfigNode oconv, ConfigNode node, String file) {
		StringBuffer buf = new StringBuffer();
	    for (ConfigNode child : node.getChildren()) {
	    	boolean convflg = true;
    		String com = null;
		    for (ConfigNode ocon : oconv.getChildren()) {
		    	if (child.getName().equals(ocon.getName())) {
			    	convflg = false;
		    		break;
		    	}
		    }
	    	if (convflg) {
		    	PasswdInfo info = (PasswdInfo)useranalize.analyze("passwd",child.getContent());
		    	Integer intg = groupID.get(Integer.valueOf(info.getGroup_id()));
		    	Integer intu = userID.get(Integer.valueOf(info.getUser_id()));
		    	String prepass ="x:"+ info.getUser_id() +":"+ info.getGroup_id() + ":";
		    	String afpass = "x:"+ intu +":"+ intg + ":";
		    	String content = child.getContent().replaceAll(prepass, afpass);
		    	if (!prepass.equals(afpass)) {
		    		System.out.println(session.getHost() + "ユーザID変換:" + info.getUser_name() + ":" + prepass + " → " + afpass);
		    	}
	    		buf.append(content + "\n");
	    	}
	    }
	    if (buf.length() == 0) return;
	    String com = "cat <<EOF >> " +  file + "\n" + buf.toString() + "EOF";
		System.out.println(session.getHost() + ":" + com);
	    try {
			getConfig(session, null, com);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	private void execConvGroup(Session session, ConfigNode oconv, ConfigNode node, String file) {
		StringBuffer buf = new StringBuffer();
	    for (ConfigNode child : node.getChildren()) {
	    	boolean convflg = true;
    		String com = null;
		    for (ConfigNode ocon : oconv.getChildren()) {
		    	if (child.getName().equals(ocon.getName())) {
			    	convflg = false;
		    		break;
		    	}
		    }
	    	if (convflg) {
		    	GroupInfo info = (GroupInfo)useranalize.analyze("group",child.getContent());
		    	Integer inte = groupID.get(Integer.valueOf(info.getGroup_id()));
		    	if (inte.toString().equals(info.getGroup_id())) {
		    		buf.append(child.getContent() + "\n");
		    	} else {
		    		System.out.println(session.getHost() + "グループID変換:" + info.getGroup_name() + ":" + info.getGroup_id() + " → " + inte);
		    		buf.append(child.getContent().replaceAll(":"+ info.getGroup_id() + ":", ":"+ inte + ":")  + "\n");
		    	}
	    	}
	    }
	    if (buf.length() == 0) return;
	    String com = "cat <<EOF >> " +  file + "\n" + buf.toString() + "EOF";
		System.out.println(session.getHost() + ":" + com);
	    try {
			getConfig(session, null, com);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	
	private void execConvCron(Session session, ConfigNode oconv, ConfigNode node, String user) {
		StringBuffer buf = new StringBuffer();
	    for (ConfigNode child : node.getChildren()) {
	    	boolean convflg = true;
	    	
	    	if (oconv != null) {
			    for (ConfigNode ocon : oconv.getChildren()) {
			    	if (ocon.getChildren().isEmpty()) continue;
			    	if (child.getContent().equals(ocon.getContent())) {
				    	convflg = false;
			    		break;
			    	}
			    }
	    	}
	    	if (convflg) {
	    		buf.append(child.getContent() + "\n");
	    	}
	    }
	    try {
		    String file = "/tmp/crontmp.txt";
		    String com = "cat <<EOF > " +  file + "\n" + buf.toString() + "EOF";
			System.out.println(session.getHost() + ":" + com);
			getConfig(session, null, com);
			com = "crontab -u " + user  + " " +  file;
			System.out.println(session.getHost() + ":" + com);
			getConfig(session, null, com);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	    
	}
	
	private void getUniqGroupId(ConfigNode oconv, ConfigNode node) {
	    for (ConfigNode child : node.getChildren()) {
	    	GroupInfo info = (GroupInfo)useranalize.analyze("group",child.getContent());
		    groupID.put(Integer.valueOf(info.getGroup_id()), Integer.valueOf(info.getGroup_id()));
	    }
	    for (ConfigNode ocon : oconv.getChildren()) {
	    	GroupInfo info = (GroupInfo)useranalize.analyze("group",ocon.getContent());
	    	Integer inte = groupID.get(Integer.valueOf(info.getGroup_id()));
	    	if (inte == null) {
	    		groupID.put(Integer.valueOf(info.getGroup_id()), Integer.valueOf(info.getGroup_id()));
	    	} else {
	    		Integer last = groupID.lastKey() + 1;
	    		groupID.put(Integer.valueOf(info.getGroup_id()), last);
	    		groupID.put(last, last);

	    	}
	    }
	}

	private void getUniqUserId(ConfigNode oconv, ConfigNode node) {
	    for (ConfigNode child : node.getChildren()) {
	    	PasswdInfo info = (PasswdInfo)useranalize.analyze("passwd",child.getContent());
	    	userID.put(Integer.valueOf(info.getUser_id()), Integer.valueOf(info.getUser_id()));
	    }
	    for (ConfigNode ocon : oconv.getChildren()) {
	    	PasswdInfo info = (PasswdInfo)useranalize.analyze("passwd",ocon.getContent());
	    	Integer inte = userID.get(Integer.valueOf(info.getUser_id()));
	    	if (inte == null) {
	    		userID.put(Integer.valueOf(info.getUser_id()), Integer.valueOf(info.getUser_id()));
	    	} else {
	    		Integer last = userID.lastKey() + 1;
	    		userID.put(Integer.valueOf(info.getUser_id()), last);
	    		userID.put(last, last);
	    	}
	    }
	}
}
