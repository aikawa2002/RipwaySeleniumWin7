package com.ricoh.serverconv.manager;

import java.util.List;
import java.util.Map;

import com.ricoh.serverconv.analyze.ConfigAnalyzerInterface;
import com.ricoh.serverconv.analyze.SendMailConfigAnalyzerImpl;
import com.ricoh.serverconv.analyze.UserConfigAnalyzerImpl;
import com.ricoh.serverconv.analyze.info.AliasesInfo;
import com.ricoh.serverconv.analyze.info.GroupInfo;
import com.ricoh.serverconv.analyze.info.PasswdInfo;
import com.ricoh.serverconv.module.CommandGetterInterface;
import com.ricoh.serverconv.parser.ConfigNode;
import com.ricoh.serverconv.server.ServerGetterInterface;

public class AnalyzeManagerImpl implements ManagerInterface {

	@Override
	public void manage(List<ServerGetterInterface> servs) {
		// TODO 自動生成されたメソッド・スタブ
  	  for (ServerGetterInterface server:servs) {
			Map<String, CommandGetterInterface> ProcessMap  = server.getProcess();
			getUsersAnalyze(ProcessMap);
			getSendMailAnalyze(ProcessMap);
	    }
	}
  	  
  	  private void getUsersAnalyze(Map<String, CommandGetterInterface> ProcessMap) {
			//ユーザ情報取得
			ConfigAnalyzerInterface useranalize = new UserConfigAnalyzerImpl();
			CommandGetterInterface passwdconfig = (CommandGetterInterface) ProcessMap.get("user");
			ConfigNode node = (ConfigNode)passwdconfig.getNode("passwd");
		    for (ConfigNode child : node.getChildren()) {
		    	PasswdInfo info = (PasswdInfo)useranalize.analyze("passwd",child.getContent());
		    	StringBuffer buf = new StringBuffer();
		    	if(info == null) continue;
		    	buf.append("USER_NAME=");
		    	buf.append(info.getUser_name());
		    	buf.append(" REAL_NAME=");
		    	buf.append(info.getReal_name());
		    	buf.append(" USER_ID=");
		    	buf.append(info.getUser_id());
		    	buf.append(" GROUP_ID=");
		    	buf.append(info.getGroup_id());
		    	buf.append(" HOME_DIR=");
		    	buf.append(info.getHome_dir());
		    	buf.append(" SHELL=");
		    	buf.append(info.getLogin_shell());
		    	System.out.println(buf.toString());
		    }
			//グループ情報取得
			node = (ConfigNode)passwdconfig.getNode("group");
		    for (ConfigNode child : node.getChildren()) {
		    	GroupInfo info = (GroupInfo)useranalize.analyze("group",child.getContent());
		    	StringBuffer buf = new StringBuffer();
		    	if(info == null) continue;
		    	buf.append("GROUP_NAME=");
		    	buf.append(info.getGroup_name());
		    	buf.append(" GROUP_ID=");
		    	buf.append(info.getGroup_id());
		    	buf.append(" USERS=");
		    	buf.append(info.getGroup_users());
		    	System.out.println(buf.toString());
		    }
  	  }

  	  private void getSendMailAnalyze(Map<String, CommandGetterInterface> ProcessMap) {
		//sendmail aliases情報取得
	  	ConfigAnalyzerInterface useranalize = new SendMailConfigAnalyzerImpl();
	  	CommandGetterInterface passwdconfig = (CommandGetterInterface) ProcessMap.get("sendmail");
  		ConfigNode node = (ConfigNode)passwdconfig.getNode("aliases");
	    for (ConfigNode child : node.getChildren()) {
	    	StringBuffer buf = new StringBuffer();
	    	buf.append(child.getContent());
		    for (ConfigNode gchild : child.getChildren()) {
	            buf.append(gchild.getContent());
		    }
	    	AliasesInfo info = (AliasesInfo)useranalize.analyze("aliases",buf.toString());
	    	buf = new StringBuffer();
	    	if(info == null) continue;
	    	buf.append("ALIASE_NAME=");
	    	buf.append(info.getUsername());
	    	buf.append(" KUBUN=");
	    	String kubun = info.getKubun();
	    	buf.append(kubun);
	    	buf.append(" VALUE=");
	    	if (kubun.equals("include")) {
		    	buf.append(info.getIncludefile());
	    	} else if (kubun.equals("file")) {
		    	buf.append(info.getLocalfile());
	    	} else if (kubun.equals("command")) {
		    	buf.append(info.getCommand());
	    	} else if (kubun.equals("else")) {
		    	buf.append(info.getLocalfile());
	    	} else if (kubun.equals("email")) {
	    		List<String> list = info.getEmaillist();
	    		for (int i=0;i<list.size();i++) {
			    	buf.append(info.getEmaillist().get(i));
	    		}
	    	}
	    	System.out.println(buf.toString());
  	  	}
	}

}
