package com.ricoh.serverconv.analyze.info;

import java.util.ArrayList;
import java.util.List;

public class AliasesInfo {
	String username = null;
	String localfile = null;
	String localuser = null;
	String command = null;
	String includefile = null;
	List<String> emaillist = new ArrayList<>();
	String kubun = "";
	
	public void setValues(String valuse) {
		if (valuse.startsWith("/")) {
			setKubun("file");
			setLocalfile(valuse);
		} else if (valuse.startsWith("|")) {
			setKubun("command");
			setCommand(valuse);
		} else if (valuse.startsWith(":include")) {
			setKubun("include");
			setIncludefile(valuse);
		} else if (valuse.contains("@")) {
			setKubun("email");
			String[] strs = valuse.split(",");
			for (String str:strs) {
				emaillist.add(str);
			}
		} else {
			setKubun("else");
			setLocaluser(valuse);
		}
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLocalfile() {
		return localfile;
	}

	public void setLocalfile(String localfile) {
		this.localfile = localfile;
	}

	public String getLocaluser() {
		return localuser;
	}

	public void setLocaluser(String localuser) {
		this.localuser = localuser;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getIncludefile() {
		return includefile;
	}

	public void setIncludefile(String includefile) {
		this.includefile = includefile;
	}

	public List<String> getEmaillist() {
		return emaillist;
	}

	public void setEmaillist(List<String> emaillist) {
		this.emaillist = emaillist;
	}

	public String getKubun() {
		return kubun;
	}

	public void setKubun(String kubun) {
		this.kubun = kubun;
	}
	
}

