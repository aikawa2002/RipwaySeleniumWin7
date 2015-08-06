package com.ricoh.serverconv.manager;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ricoh.serverconv.server.ServerGetterInterface;
import com.ricoh.serverconv.util.ServerConvProperties;

public class ConvertManagerImpl implements ManagerInterface {
	ServerGetterInterface inserver = null;
	ServerGetterInterface outserver = null;

	@Override
	public void manage(List<ServerGetterInterface> servs) {
		
		Set<String> key = ServerConvProperties.converMap.keySet();
		Iterator<String> ite = key.iterator();
		while (ite.hasNext()) {
			String k = ite.next();
			String c = (String)ServerConvProperties.converMap.get(k);
			// TODO 自動生成されたメソッド・スタブ
		  	  for (ServerGetterInterface server:servs) {
		  		  if (server.getName().equals(k)) {
		  			inserver = server;
		  		  } else if (server.getName().equals(c)) {
		  			outserver = server;
		  		  }
		  	  }
		  	  outserver.convert(inserver);
		}
	}  
	
}
