package com.ricoh.serverconv.server;

import java.util.Map;
import java.util.concurrent.Callable;

import com.ricoh.serverconv.module.CommandGetterInterface;



public interface ServerGetterInterface extends Callable<String> {
	
	public String getName();
	public Map<String, CommandGetterInterface> getProcess();
	public CommandGetterInterface getProcess(String pro);
	public void setProcess(CommandGetterInterface getimpl);
	public boolean make();
	public boolean outPut();
	public boolean convert(ServerGetterInterface server);

}
