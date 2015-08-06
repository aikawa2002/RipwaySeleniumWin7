package com.ricoh.serverconv.analyze.info;

public class PasswdInfo {
	String user_name = null;
	String password = null;
	String user_id = null;
	String group_id = null;
	String real_name = null;
	String home_dir = null;
	String login_shell = null;
	
	public void setValues(String[] valuse) {
		for (int i=0;i<valuse.length;i++) {
			String str =  valuse[i];
			if (i == 0) {
				user_name = str;
			} else if (i == 1) {
				password = str;
			} else if (i == 2) {
				user_id = str;
			} else if (i == 3) {
				group_id = str;
			} else if (i == 4) {
				real_name = str;
			} else if (i == 5) {
				home_dir = str;
			} else if (i == 6) {
				login_shell = str;
			}
		}
	}
	
	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getHome_dir() {
		return home_dir;
	}

	public void setHome_dir(String home_dir) {
		this.home_dir = home_dir;
	}

	public String getLogin_shell() {
		return login_shell;
	}

	public void setLogin_shell(String login_shell) {
		this.login_shell = login_shell;
	}

}

