package com.ricoh.serverconv.server;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import com.ricoh.serverconv.module.CommandGetterInterface;
import com.ricoh.serverconv.module.DirectoryGetterImpl;
import com.ricoh.serverconv.module.EnvPrePareImpl;
import com.ricoh.serverconv.module.HttpdGetterImpl;
import com.ricoh.serverconv.module.NetWorkGetterImpl;
import com.ricoh.serverconv.module.OracleGetterImpl;
import com.ricoh.serverconv.module.SambaGetterImpl;
import com.ricoh.serverconv.module.SendMailGetterImpl;
import com.ricoh.serverconv.module.SystemInfoGetterImpl;
import com.ricoh.serverconv.module.UserGetterImpl;
import com.ricoh.serverconv.util.ServerConvProperties;


public class ServerGetterImpl implements ServerGetterInterface {
	private String name = "";
	private Session session = null;
	private Map<String, String> configMap = null;
	private Map<String, CommandGetterInterface> ProcessMap  = null;
	private final Map<String, CommandGetterInterface> moduleMap =new HashMap<String, CommandGetterInterface>(){{ 
		put("system", new SystemInfoGetterImpl());
		put("user", new UserGetterImpl());
		put("network", new NetWorkGetterImpl());
		put("sendmail", new SendMailGetterImpl());
		put("httpd", new HttpdGetterImpl());
		put("samba", new SambaGetterImpl());
		put("dir", new DirectoryGetterImpl());
		put("oracle", new OracleGetterImpl());
	}};
	
	public ServerGetterImpl(String name) {
		this.name = name;
		init();
	}
	
	private void init() {
		String user = null;
		String host = null;
		String password = null;
		String iddsa = null;
	    String idflg = "false";
	    
	    try {
	        user = ServerConvProperties.getProperties(name + "_user");
	        host = ServerConvProperties.getProperties(name + "_host");
	        password = ServerConvProperties.getProperties(name + "_password");
	        iddsa = ServerConvProperties.getProperties(name + "_iddsa");
	        idflg = ServerConvProperties.getProperties(name + "_idflg");
	    	ProcessMap  = new HashMap<>();
	    	configMap = new HashMap<>();
	        configMap.put("shadow", ServerConvProperties.getProperties(name + "_shadow"));
	        configMap.put("httpd", ServerConvProperties.getProperties(name + "_httpd"));
	        configMap.put("sendmailmc", ServerConvProperties.getProperties(name + "_sendmailmc"));
	        configMap.put("sudo_pass", ServerConvProperties.getProperties(name + "_sudo_pass"));
	        configMap.put("telnet_server", ServerConvProperties.getProperties(name + "_telnet_server"));
	        configMap.put("telnet_user", ServerConvProperties.getProperties(name + "_telnet_user"));
	        configMap.put("telnet_pass", ServerConvProperties.getProperties(name + "_telnet_pass"));
	        configMap.put("prepare", ServerConvProperties.getProperties(name + "_prepare"));
	        configMap.put("localdir", ServerConvProperties.getProperties(name + "_localdir"));
	        
	    } catch (MissingResourceException e) {
	        e.printStackTrace();
	        return;
	    }
	    
		JSch jsch=new JSch();
		try {
			if (idflg.equals("true")) {
				System.out.println("You chose "+ iddsa);
				jsch.addIdentity(iddsa);
			}
			session=jsch.getSession(user, host, 22);
		    MyUserInfo ui=new MyUserInfo();
		    ui.setPassword(password);
		    session.setUserInfo((UserInfo)ui);
		} catch (JSchException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
        if (configMap.get("prepare") != null && configMap.get("prepare").length() > 0) {
        	moduleMap.put("prepare", new EnvPrePareImpl());
        }
		
	}

	@Override
	public String getName() {
		// TODO 自動生成されたメソッド・スタブ
		return name;
	}

	@Override
	public String call() throws Exception {
		// TODO 自動生成されたメソッド・スタブ
		make();
		outPut();
		return "success";
	}
	
	@Override
	public boolean make() {
	    try {
			session.connect();
			
			CommandGetterInterface premodule = moduleMap.get("prepare");
			if (premodule != null) {
				System.out.println(getName() + " 事前準備処理開始");
				getServerConfig("prepare", premodule);
				ProcessMap.remove("prepare");
				moduleMap.remove("prepare");
			}
			
			for (String module:ServerConvProperties.moduleList) {
				System.out.println(getName() + " 処理開始:" + module);
				getServerConfig(module, moduleMap.get(module));
			};
			
		    ////session.disconnect();
		} catch (JSchException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean outPut() {
		System.out.println(getName() + " 出力開始");
		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook();
		
		Map<String, CommandGetterInterface> ProcessMap  = getProcess();
		
		for (String module:ServerConvProperties.moduleList) {
			CommandGetterInterface passwdconfig = (CommandGetterInterface) ProcessMap.get(module);
			if (passwdconfig == null) continue;
			passwdconfig.outPut(wb);
		}	
		
	    FileOutputStream os;
		try {
			os = new FileOutputStream("c:\\" + getName() + "_ParameterSheet.xlsx");
		    wb.write(os);    // ファイルに書き出し	    }
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public Map<String, CommandGetterInterface> getProcess() {
		// TODO 自動生成されたメソッド・スタブ
		return ProcessMap;
	}

	@Override
	public CommandGetterInterface getProcess(String pro) {
		// TODO 自動生成されたメソッド・スタブ
		return ProcessMap.get(pro);
	}

	@Override
	public void setProcess(CommandGetterInterface getimpl) {
		// TODO 自動生成されたメソッド・スタブ

	}
	
	private void getServerConfig(String configName, CommandGetterInterface command) {
		command.init(configMap);
		command.execCommand(session);
	    ProcessMap.put(configName, command);
	}
	
	@Override
	public boolean convert(ServerGetterInterface server) {
		// TODO 自動生成されたメソッド・スタブ
		for (String[] module:ServerConvProperties.convSenarioList) {
			if (!ServerConvProperties.moduleList.contains(module[0])) continue;  
			
			System.out.println(getName() + " コンバート開始:" + module[0] + " " + module[1]);
			moduleMap.get(module[0]).compare(session, server.getProcess(module[0]),module[1]);
		}
	    session.disconnect();
		return true;
	}

	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive{
		    public String getPassword(){ return passwd; }
		    public void setPassword(String password){ this.passwd = password; }
		    public boolean promptYesNo(String str){
		       return true;
		    }
		  
		    String passwd;
		    JTextField passwordField=(JTextField)new JPasswordField(20);

		    public String getPassphrase(){ return null; }
		    public boolean promptPassphrase(String message){ return true; }
		    public boolean promptPassword(String message){
		      if ( passwd != null) {
		    	  return true;
		      }
		      Object[] ob={passwordField}; 
		      int result=
		        JOptionPane.showConfirmDialog(null, ob, message,
		                                      JOptionPane.OK_CANCEL_OPTION);
		      if(result==JOptionPane.OK_OPTION){
		        passwd=passwordField.getText();
		        return true;
		      }
		      else{ 
		        return false; 
		      }
		    }
		    public void showMessage(String message){
		      JOptionPane.showMessageDialog(null, message);
		    }
		    final GridBagConstraints gbc = 
		      new GridBagConstraints(0,0,1,1,1,1,
		                             GridBagConstraints.NORTHWEST,
		                             GridBagConstraints.NONE,
		                             new Insets(0,0,0,0),0,0);
		    private Container panel;
		    public String[] promptKeyboardInteractive(String destination,
		                                              String name,
		                                              String instruction,
		                                              String[] prompt,
		                                              boolean[] echo){
		      panel = new JPanel();
		      panel.setLayout(new GridBagLayout());

		      gbc.weightx = 1.0;
		      gbc.gridwidth = GridBagConstraints.REMAINDER;
		      gbc.gridx = 0;
		      panel.add(new JLabel(instruction), gbc);
		      gbc.gridy++;

		      gbc.gridwidth = GridBagConstraints.RELATIVE;

		      JTextField[] texts=new JTextField[prompt.length];
		      for(int i=0; i<prompt.length; i++){
		        gbc.fill = GridBagConstraints.NONE;
		        gbc.gridx = 0;
		        gbc.weightx = 1;
		        panel.add(new JLabel(prompt[i]),gbc);

		        gbc.gridx = 1;
		        gbc.fill = GridBagConstraints.HORIZONTAL;
		        gbc.weighty = 1;
		        if(echo[i]){
		          texts[i]=new JTextField(20);
		        }
		        else{
		          texts[i]=new JPasswordField(20);
		        }
		        panel.add(texts[i], gbc);
		        gbc.gridy++;
		      }

		        String[] response=new String[prompt.length];
		        for(int i=0; i<prompt.length; i++){
		          response[i]=texts[i].getText();
		        }
			return response;
		    }
		  }

}
