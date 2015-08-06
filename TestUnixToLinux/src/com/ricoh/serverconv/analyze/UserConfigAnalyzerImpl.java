package com.ricoh.serverconv.analyze;

import com.ricoh.serverconv.analyze.info.GroupInfo;
import com.ricoh.serverconv.analyze.info.PasswdInfo;

public class UserConfigAnalyzerImpl implements ConfigAnalyzerInterface {
	
	@Override
	public Object analyze(String config, String content) {
		// TODO 自動生成されたメソッド・スタブ
		String[] values = content.split(":");
		if (config.equals("passwd")) {
			PasswdInfo userinf = new PasswdInfo();
			userinf.setValues(values);
			return userinf;
		} else if (config.equals("group")) {
				GroupInfo groupinf = new GroupInfo();
				groupinf.setValues(values);
				return groupinf;
		}

		return null;
	}
	

}
