package com.ricoh.serverconv.module;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ricoh.serverconv.parser.ConfigNode;
import com.ricoh.serverconv.parser.ConfigParserInterface;
import com.ricoh.serverconv.parser.DirectoryConfigParser;
import com.ricoh.serverconv.util.ServerConvProperties;


public class DirectoryGetterImpl extends HttpdGetterImpl {
	private ConfigNode homeconfig = null;
	private ConfigNode config = null;
	private ConfigNode sshdirconfig = null;
	private ConfigNode authconfig = null;

	@Override
	public void execCommand(Session session) {
		try {
		  homeconfig = getConfig(session, new DirectoryConfigParser(), ServerConvProperties.HOME);
		      
	      config = getConfig(session, new DirectoryConfigParser(), ServerConvProperties.COMMAND);
	      
	      sshdirconfig = getConfig(session, new DirectoryConfigParser(), ServerConvProperties.SSHDIR);
	      
	      authconfig = getConfig(session, new DirectoryConfigParser(), ServerConvProperties.AUTHFILE);
	      
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
		StringBuffer buf = new StringBuffer();
        buf.append("---------------- directory ----------------\n");
	    for (ConfigNode child : config.getChildren()) {
            buf.append(child.getName() + "=" + child.getContent() + "\n");
        }	      
		return buf.toString();
	}

	@Override
	public Object getNode(String str) {
		// TODO 自動生成されたメソッド・スタブ
		if (str.equals("dir")) {
		    return config;
		} else if (str.equals("home")) {
		    return homeconfig;
		} else if (str.equals("ssh_dir")) {
		    return sshdirconfig;
		} else if (str.equals("auth")) {
		    return authconfig;
		}
		return null;
	}

	@Override
	public void outPut(XSSFWorkbook wb) {
		// TODO 自動生成されたメソッド・スタブ
		XSSFSheet sheet = wb.createSheet("Directory");
		int r = 0;
		String[] comms = {"home", "dir", "ssh_dir","auth"};
		for (String com:comms) {
			r = r + 1;
			ConfigNode node = (ConfigNode)getNode(com);
			if (node == null) return;
		    for (ConfigNode child : node.getChildren()) {
		    	if (child.getContent().equals("find:")) continue;
		    	XSSFRow row = sheet.createRow(r);
		    	row.createCell(0).setCellValue(child.getName());
		    	String[] strs = child.getContent().split(" ");
		    	int i = 0;
		    	int j = 0;
		    	for (String str:strs) {
		    		if (str.trim().length() > 0) {
			    		if (j == 0) {
							row.createCell(1).setCellValue(str);
			    		} else	if (j == 2) {
							row.createCell(2).setCellValue(str);
			    		} else	if (j == 3) {
							row.createCell(3).setCellValue(str);
			    		}
			    		j = j + 1;
		    		}
		    		i = i + 1;
		    	}
			    r = r + 1;
		    }
		}
	}	    

	@Override
	public void compare(Session session, CommandGetterInterface ocom, String module) {
		// TODO 自動生成されたメソッド・スタブ
		ConfigNode node = (ConfigNode) ocom.getNode(module);
		ConfigNode oconv = (ConfigNode) getNode(module);
		
		if (module.equals("auth")) {
		    for (ConfigNode child : node.getChildren()) {
		    	boolean convflg = true;
			    for (ConfigNode ocon : oconv.getChildren()) {
			    	if (child.getName().equals(ocon.getName())) {
				    	convflg = false;
			    		break;
			    	}
			    }
		    	if (convflg) {
				  	  String com = "touch " + child.getName();
				  	try {
						getConfig(session, null, com);
					} catch (Exception e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
					  //System.out.println(session.getHost() + ":" + com);
					  
				      try {
						execConv(session, child.getName(), child.getContent());
					} catch (Exception e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
		    	}
			}
		  	try {
				getConfig(session, null, ServerConvProperties.CPPROFILE);
				getConfig(session, null, ServerConvProperties.CHOWNPROFILE);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		} else if (module.equals("csh")) {
		  	try {
				getConfig(session, null, ServerConvProperties.CSHOWN);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		} else {
			if (node == null) {
			    StringBuffer buf = new StringBuffer();
				for (ConfigNode ocon : oconv.getChildren()) {
					buf.append("mkdir -p " + ocon.getName() + "\n");
				    try {
						buf.append(execConv(session, ocon.getName(), ocon.getContent()));
					} catch (Exception e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
			    }
			    if (buf.length() == 0) return;
			    buf.insert(0, "#!/bin/sh\n");
			    try {
				    String com = "cat <<EOF > /tmp/createDir.sh \n" + buf.toString() + "EOF";
					//System.out.println(session.getHost() + ":" + com);
					getConfig(session, null, com);
					com = "chmod 755 /tmp/createDir.sh";
					getConfig(session, null, com);
					com = "/tmp/createDir.sh";
					getConfig(session, null, com);
				} catch (Exception e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
			    return;
			}
		    StringBuffer buf = new StringBuffer();
		    for (ConfigNode child : node.getChildren()) {
		    	if (child.getName().equals("find:")) continue;
		    	boolean convflg = true;
			    for (ConfigNode ocon : oconv.getChildren()) {
			    	if (child.getName().equals(ocon.getName())) {
				    	convflg = false;
			    		break;
			    	}
			    }
		    	if (convflg) {
	    		  String com1 = "mkdir -p " + child.getName();
	    		  buf.append(com1 + "\n");
	    			  try {
						buf.append(execConv(session, child.getName(), child.getContent()));
					} catch (Exception e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
		    	}
		    }
		    if (buf.length() == 0) return;
		    buf.insert(0, "#!/bin/sh\n");
		    try {
			    String com = "cat <<EOF > /tmp/createDir.sh \n" + buf.toString() + "EOF";
				//System.out.println(session.getHost() + ":" + com);
				getConfig(session, null, com);
				com = "chmod 755 /tmp/createDir.sh";
				getConfig(session, null, com);
				com = "/tmp/createDir.sh";
				getConfig(session, null, com);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
	
	
	private String execConv(Session session, String name, String content) throws Exception {
  	  String[] strs = content.split(" ");
  	  int j = 0;
  	  String permit = null;
  	  String user = null;
  	  String group = null;
  	  for (String str:strs) {
  		  if (str.trim().length() > 0) {
  			  if (j == 0) {
  				  permit = str;
  			  } else if (j == 2) {
  				  user = str;
  			  } else if (j == 3) {
  				  group = str;
  			  }
  			  j = j + 1;
  		  }
  	  }
	  
	  String com = "chown " + user + ":" + group + " " +  name + "\n";
	  
  	  int permit_value = ServerConvProperties.calcPermission(permit);
	  com = com + "chmod " + permit_value + " " +  name + "\n";
	  
	  return com;
	}
	
}
