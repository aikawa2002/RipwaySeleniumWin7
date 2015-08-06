package com.ricoh.serverconv.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ricoh.serverconv.server.ServerGetterInterface;

public class InformationManagerImpl implements ManagerInterface {

	@Override
	public void manage(List<ServerGetterInterface> servs) {
		// TODO 自動生成されたメソッド・スタブ
	  List<Future<String>> list = new ArrayList<>();
	  ExecutorService service = Executors.newFixedThreadPool(servs.size());
  	  for (ServerGetterInterface server:servs) {
  		  list.add(service.submit(server));
//  		  server.make();
/*    	  if (server.make()) {
    			Map<String, CommandGetterInterface> ProcessMap  = server.getProcess();
    		    System.out.println("############################" + server.getName() + "############################");
    		    System.out.println(ProcessMap.get("system").getValue(null));
    		    System.out.println(ProcessMap.get("network").getValue(null));
    		    System.out.println(ProcessMap.get("httpd").getValue(null));
    		    System.out.println(ProcessMap.get("user").getValue(null));
    		    System.out.println(ProcessMap.get("sendmail").getValue(null));
    		    System.out.println(ProcessMap.get("samba").getValue(null));
    	  }
	*/  }
  	 for (Future<String> fu:list) {
  		 try {
			fu.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
  	 }
  	 service.shutdown();
  	  
	}  
}
