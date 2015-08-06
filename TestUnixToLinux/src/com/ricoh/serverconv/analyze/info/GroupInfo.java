package com.ricoh.serverconv.analyze.info;

public class GroupInfo {
	String group_name = null;
	String group_id = null;
	String group_users = null;
	
	public void setValues(String[] valuse) {
		for (int i=0;i<valuse.length;i++) {
			String str =  valuse[i];
			if (i == 0) {
				group_name = str;
			} else if (i == 1) {
				group_id = str;
			} else if (i == 2) {
				group_id = str;
			} else if (i == 3) {
				group_users = str;
			}
		}
	}
	
	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroup_users() {
		return group_users;
	}

	public void setGroup_users(String group_users) {
		this.group_users = group_users;
	}

}

