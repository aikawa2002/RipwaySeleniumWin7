package com.ricoh.serverconv.manager;

import java.util.List;

import com.ricoh.serverconv.server.ServerGetterInterface;

public interface ManagerInterface {
	
	public void manage(List<ServerGetterInterface> servs);

}
