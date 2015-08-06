package com.ricoh.serverconv.analyze;

import com.ricoh.serverconv.analyze.info.AliasesInfo;

public class SendMailConfigAnalyzerImpl implements ConfigAnalyzerInterface {
	
	@Override
	public Object analyze(String config, String content) {
		// TODO 自動生成されたメソッド・スタブ
		if (config.equals("aliases")) {
			String[] values = content.split(":[\\s]");
			if (values.length < 2) return null;
			AliasesInfo inf = new AliasesInfo();
			inf.setUsername(values[0]);
			inf.setValues(values[1].trim());
			return inf;
		}

		return null;
	}
	

}
