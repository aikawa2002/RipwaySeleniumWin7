package com.ricoh.serverconv;
/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate remote exec.
 *  $ CLASSPATH=.:../build javac Exec.java 
 *  $ CLASSPATH=.:../build java Exec
 * You will be asked username, hostname, displayname, passwd and command.
 * If everything works fine, given command will be invoked 
 * on the remote side and outputs will be printed out.
 *
 */
import java.util.ArrayList;
import java.util.List;

import com.ricoh.serverconv.manager.ConvertManagerImpl;
import com.ricoh.serverconv.manager.InformationManagerImpl;
import com.ricoh.serverconv.manager.ManagerInterface;
import com.ricoh.serverconv.server.ServerGetterImpl;
import com.ricoh.serverconv.server.ServerGetterInterface;
import com.ricoh.serverconv.util.ServerConvProperties;

public class Exec{
  public static void main(String[] arg){
      List<ServerGetterInterface> servs = new ArrayList<>();
      ServerConvProperties.setProperties("convertprop");
      
      for (String server_name:ServerConvProperties.servers) {
    	  servs.add(new ServerGetterImpl(server_name));
  	  }  
      try{
   	  
    	  ManagerInterface manager = new InformationManagerImpl();
    	  manager.manage(servs);
    	  
    	  ManagerInterface convmanager = new ConvertManagerImpl();
    	  convmanager.manage(servs);
    	   	  
    	  System.exit(0);

      } catch(Exception e){
    	  e.printStackTrace();
    	  System.out.println(e);
    	  System.exit(-1);
      }
  }
}
