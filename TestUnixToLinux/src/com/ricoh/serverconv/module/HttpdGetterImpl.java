package com.ricoh.serverconv.module;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.ricoh.serverconv.parser.ApacheConfigParser;
import com.ricoh.serverconv.parser.ConfigNode;
import com.ricoh.serverconv.parser.ConfigParserInterface;
import com.ricoh.serverconv.util.ServerConvProperties;


public class HttpdGetterImpl implements CommandGetterInterface {
	protected Map<String, String> configMap = null;
	private ConfigNode config = null;
	protected int wait_time = 3000;
	protected String command = "/etc/httpd/conf/httpd.conf";
	
	@Override
	public void init(Map<String, String> configMap) {
		this.configMap = configMap;
	}

	@Override
	public void execCommand(Session session) {
		command = "/etc/httpd/conf/httpd.conf";
		// TODO 自動生成されたメソッド・スタブ
		if (configMap.get("httpd") != null) {
		  command = configMap.get("httpd");
		}
		try {
	      config = getConfig(session, new ApacheConfigParser(), "cat " + command);
		} catch (JSchException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
    }

	protected synchronized boolean setFile(Session session, String dir ,String filename, String local_dir) throws Exception {

	  if (configMap.get("telnet_server").trim().length() > 0) return false;
      if (configMap.get("sudo_pass").trim().length() > 0) return false;
      
	  if (local_dir == null || local_dir.length() == 0) return true;
      
      FileInputStream fis=null;
      
      String command="scp -t "+ dir + filename;
      Channel channel=session.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);

      // get I/O streams for remote scp
      OutputStream out=channel.getOutputStream();
      InputStream in=channel.getInputStream();

      channel.connect();

      if(checkAck(in)!=0){
    	  return false;
      }
      String lfile = local_dir + filename;
      File _lfile = new File(lfile);

      long filesize=_lfile.length();
      command="C0644 "+filesize+" ";
      if(lfile.lastIndexOf('/')>0){
        command+=lfile.substring(lfile.lastIndexOf('/')+1);
      }
      else{
        command+=lfile;
      }
      command+="\n";
      out.write(command.getBytes()); out.flush();
      if(checkAck(in)!=0){
    	  return false;
      }

      fis=new FileInputStream(lfile);
      byte[] buf=new byte[1024];
      while(true){
        int len=fis.read(buf, 0, buf.length);
		if(len<=0) break;
	    out.write(buf, 0, len);
	  }
      fis.close();
      fis=null;
      // send '\0'
      buf[0]=0; out.write(buf, 0, 1); out.flush();
      if(checkAck(in)!=0){
    	  return true;
      }
      out.close();

      channel.disconnect();
      
	  return true;
	}
	
	protected synchronized boolean getFile(Session session, String dir ,String filename) throws Exception {

	  String local_dir = configMap.get("localdir");
	  if (local_dir == null || local_dir.length() == 0) return true;
	  if (configMap.get("telnet_server").trim().length() > 0) return true;
	      
      // exec 'scp -f rfile' remotely
      Channel channel=session.openChannel("exec");
	  return getFromFile(channel, dir, filename);
	}

	private boolean getFromFile(Channel channel,String dir ,String filename) {
		  FileOutputStream fos= null;
		  try {
		  String local_dir = configMap.get("localdir");
		  if (local_dir == null || local_dir.length() == 0) return true;
		  
	      // exec 'scp -f rfile' remotely
	      String command="scp -f "+ dir + filename;
	      ((ChannelExec)channel).setCommand(command);

	      // get I/O streams for remote scp
	      OutputStream out=channel.getOutputStream();
	      InputStream in=channel.getInputStream();

	      channel.connect();

	      byte[] buf=new byte[1024];

	      // send '\0'
	      buf[0]=0; out.write(buf, 0, 1); out.flush();

	      while(true){
			int c=checkAck(in);
		     if(c!='C'){
			 break;
		  }

	    // read '0644 '
	    in.read(buf, 0, 5);

	    long filesize=0L;
	    while(true){
	      if(in.read(buf, 0, 1)<0){
	        // error
	        break; 
	      }
	      if(buf[0]==' ')break;
	      filesize=filesize*10L+(long)(buf[0]-'0');
	    }

	    String file=null;
	    for(int i=0;;i++){
	      in.read(buf, i, 1);
	      if(buf[i]==(byte)0x0a){
	        file=new String(buf, 0, i);
	        break;
	      }
	    }
			
		//System.out.println("filesize="+filesize+", file="+file);
		
	    // send '\0'
		    buf[0]=0; out.write(buf, 0, 1); out.flush();
		
		    // read a content of lfile
		    fos=new FileOutputStream(local_dir +  filename);
		    int foo;
		    while(true){
		      if(buf.length<filesize) foo=buf.length;
		      else foo=(int)filesize;
		      foo=in.read(buf, 0, foo);
		      if(foo<0){
		        // error 
		        break;
		      }
		      fos.write(buf, 0, foo);
		      filesize-=foo;
		      if(filesize==0L) break;
		    }
		    fos.close();
		    fos=null;
		
			if(checkAck(in)!=0){
			      return false;
			}
		
		    // send '\0'
		    buf[0]=0; out.write(buf, 0, 1); out.flush();
		  }
	      
	      channel.disconnect();

	      return true;
		}  catch(Exception e){
		  System.out.println(e);
		  try{if(fos!=null)fos.close();}catch(Exception ee){}
		  return false;
		}
	}
	
	private int checkAck(InputStream in) throws IOException{
	    int b=in.read();
	    // b may be 0 for success,
	    //          1 for error,
	    //          2 for fatal error,
	    //          -1
	    if(b==0) return b;
	    if(b==-1) return b;
	
	    if(b==1 || b==2){
	      StringBuffer sb=new StringBuffer();
	      int c;
	      do {
		c=in.read();
		sb.append((char)c);
	      }
	      while(c!='\n');
	      if(b==1){ // error
		System.out.print(sb.toString());
	      }
	      if(b==2){ // fatal error
		System.out.print(sb.toString());
	      }
	    }
	    return b;
	  }
	      
	protected synchronized ConfigNode getConfig(Session session, ConfigParserInterface parser, String command) throws Exception {
	      if (parser != null && configMap.get("telnet_server").trim().length() > 0) return getConfigViaServer(session, parser, command);
	      if (parser != null && configMap.get("sudo_pass").trim().length() > 0) return getConfigViaSu(session, parser, command);
	      
	      Channel channel = null;
		  channel = session.openChannel("exec");
	      ((ChannelExec)channel).setCommand(command);
	      channel.setInputStream(null);

	      ((ChannelExec)channel).setErrStream(System.err);

	      InputStream in=channel.getInputStream();
	      
	      if (parser == null) {
	          channel.connect();
	          
	          byte[] tmp=new byte[1024];
	          while(true){
	            while(in.available()>0){
	              int i=in.read(tmp, 0, 1024);
	              if(i<0)break;
	              System.out.print(new String(tmp, 0, i));
	            }
	            if(channel.isClosed()){
	              if(in.available()>0) continue;
	              System.out.println("exit-status: "+channel.getExitStatus());
	              break;
	            }
	            try{Thread.sleep(1000);}catch(Exception ee){}
	          }
	          
	          channel.disconnect();
	    	  return null;
	      }
	      
	      channel.connect();
	      ConfigNode config = parser.parse(in);
	      channel.disconnect();
	      return config;
	      
	}
	
	protected synchronized ConfigNode getConfigViaSu(Session session, ConfigParserInterface parser, String command) throws Exception {
		 
	      Channel channel = null;
		  channel = session.openChannel("shell");
		  String tmpfile = session.getHost() + "_tmp.txt";
		  		  
		  channel.setOutputStream( new BufferedOutputStream(Files.newOutputStream(Paths.get(tmpfile)))); 
		  //channel.setOutputStream( System.out); 

	      List<String> commands = new ArrayList<String>();
	      commands.add("su -");
	      commands.add(configMap.get("sudo_pass"));
	      
	      //((ChannelExec)channel).setErrStream(System.err);
	      
	      PrintStream shellStream = new PrintStream(channel.getOutputStream());  // printStream for convenience 
	      channel.connect(); 
	      for(String command1: commands) {
		      Thread.sleep(1000);
	          shellStream.println(command1); 
	          shellStream.flush();
	      }
	      
	      channel.setOutputStream( new BufferedOutputStream(Files.newOutputStream(Paths.get(tmpfile)))); 
	      ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
	      
	      shellStream = new PrintStream(channel.getOutputStream());  // printStream for convenience 
	      Thread.sleep(1000);
          shellStream.println(command); 
          shellStream.flush();
	      Thread.sleep(wait_time);
	      
	      InputStream in = getConfigInputStream(tmpfile,6);
		  ConfigNode config = parser.parse(in);
	      channel.disconnect();
	      return config;
	      
	}
	
	protected synchronized ConfigNode getConfigViaServer(Session session, ConfigParserInterface parser, String command) throws Exception {
		 
	      Channel channel = null;
		  channel = session.openChannel("shell");
		  String tmpfile = session.getHost() + "_via_tmp.txt";
		  channel.setOutputStream( new BufferedOutputStream(Files.newOutputStream(Paths.get(tmpfile)))); 
		  //channel.setOutputStream( System.out); 

	      List<String> commands = new ArrayList<String>();
	      commands.add("telnet " + configMap.get("telnet_server"));
	      commands.add(configMap.get("telnet_user"));
	      commands.add(configMap.get("telnet_pass"));
	      commands.add("su -");
	      commands.add(configMap.get("sudo_pass"));
	      
	      //((ChannelExec)channel).setErrStream(System.err);
	      
	      PrintStream shellStream = new PrintStream(channel.getOutputStream());  // printStream for convenience 
	      channel.connect(); 
	      for(String command1: commands) {
		      Thread.sleep(1000);
	          shellStream.println(command1); 
	          shellStream.flush();
	      }
		  
		  channel.setOutputStream( new BufferedOutputStream(Files.newOutputStream(Paths.get(tmpfile)))); 
	      ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
	      shellStream = new PrintStream(channel.getOutputStream());  // printStream for convenience 
	      Thread.sleep(1000);
	      shellStream.println(command); 
	      shellStream.flush();
	      Thread.sleep(wait_time);
	      
	      InputStream in = getConfigInputStream(tmpfile,5);
		  ConfigNode config = parser.parse(in);
	      channel.disconnect();
	      
	      return config;
	      
	}
	
	protected synchronized InputStream getConfigInputStream(String filename, int excuteline) {
        //Construct the LineNumberReader object
	      StringBuffer buf = new StringBuffer();
	      

	       Path src = Paths.get(filename);
		   try  {
		     // ファイル内容の読み込み
			   List<String> lines = Files.readAllLines(src, Charset.forName("EUC_JP"));
			   int l = 0;
			   for(String line : lines){
		        	l = l +1;
		        	if (l < excuteline) continue;
		        	//System.out.println(line);
		        	buf.append(line + "\n");
			     }
		   } catch (Exception ex) {
			         ex.printStackTrace();
		   }
	      return new BufferedInputStream(new ByteArrayInputStream(buf.toString().getBytes()));
	}
	
	@Override
	public String getValue(String str) {
		// TODO 自動生成されたメソッド・スタブ
		StringBuffer buf = new StringBuffer();
        buf.append("---------------- httpd.conf ----------------\n");
	    for (ConfigNode child : config.getChildren()) {
              buf.append(child.getName() + "=" + child.getContent() +"\n");
        }	      
		return buf.toString();
	}

	@Override
	public Object getNode(String str) {
		// TODO 自動生成されたメソッド・スタブ
		return config;
	}

	@Override
	public void outPut(XSSFWorkbook wb) {
		// TODO 自動生成されたメソッド・スタブ
		XSSFSheet sheet = wb.createSheet("Httpd");
		
		int r = 1;
		ConfigNode node = (ConfigNode)getNode(null);
		if (node == null) return;
		XSSFCellStyle style = getHeaderStyle(wb);
		setHeader("httpd", sheet, r,style);
	    r = r + 1;
	    for (ConfigNode child : node.getChildren()) {
	    	if (child.getName().equals("cat:")) continue;
	    	XSSFRow row = sheet.createRow(r);
	    	row.createCell(0).setCellValue(child.getName());
			row.createCell(1).setCellValue(child.getContent());
			row.createCell(4).setCellValue(ServerConvProperties.HttpdMap.get(child.getName()));
			List<String> deflist = ServerConvProperties.HttpdDefaultMap.get(child.getName());
			if (deflist != null) {
				String conte = child.getContent();
				if (child.getName().contains("LoadModule")) {
					String[] cs = child.getContent().split("/");
					conte = cs[cs.length-1];
				}
				if (deflist.contains(conte)) {
					row.createCell(3).setCellValue("Default");
				}
			} else {
				row.createCell(3).setCellValue("-");
			}
		    r = r + 1;
		    for (ConfigNode gchild : child.getChildren()) {
		    	row = sheet.createRow(r);
		    	row.createCell(1).setCellValue(gchild.getName());
				row.createCell(2).setCellValue(gchild.getContent());
				row.createCell(4).setCellValue(ServerConvProperties.HttpdMap.get(gchild.getName()));
				
				deflist = ServerConvProperties.HttpdDefaultMap.get(gchild.getName());
				if (deflist != null && deflist.contains(gchild.getContent())) {
					row.createCell(3).setCellValue("Default");
				} else {
					row.createCell(3).setCellValue("-");
				}
			    r = r + 1;
		    }
	    }
	}
	
	protected void setHeader(String name, XSSFSheet sheet, int Index, XSSFCellStyle style) {
	    XSSFRow row = sheet.createRow(Index);
	    List<String> list = ServerConvProperties.OutPutHeadMap.get(name);
	    int size = list.size();
	    for (int i=0;i<size;i++) {
	    	XSSFCell cell =	row.createCell(i);
	    	cell.setCellValue(list.get(i));
	    	cell.setCellStyle(style);
	    }
	}

	protected XSSFCellStyle getHeaderStyle(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		return style;
	}

	@Override
	public void compare(Session session, CommandGetterInterface ocom, String module) {
		// TODO 自動生成されたメソッド・スタブ
		ConfigNode node = (ConfigNode) ocom.getNode(null);
		ConfigNode oconv = (ConfigNode) getNode(null);
	    for (ConfigNode child : node.getChildren()) {
	    	if (child.getName().equals("cat:")) continue;
	    	int convflg = 0;
    		String com = null;
    		String oconContent = null;
		    for (ConfigNode ocon : oconv.getChildren()) {
		    	if (child.getName().equals(ocon.getName())) {
			    	if (child.getContent().equals(ocon.getContent())) {
				    	convflg = 2;
			    	} else {
				    	convflg = 1;
				    	oconContent = ocon.getContent();
			    	}
		    		break;
		    	}
		    }
	    	if (convflg == 0) {
	    		com = "echo '" + child.getName() + "\t" + child.getContent() + "' >> " + command;
		    	System.out.println(com);
	    	} else if (convflg == 1) {
	    		com = "sed s/'" + child.getName() + "\t" +child.getContent() + "'/'" + child.getName() + "\t" + oconContent + "'/g " + command;
		    	System.out.println(com);
	    	}
/*	    		try {
	    		      getConfig(session, null, com);
	    		      
	    			} catch (JSchException e) {
	    				// TODO 自動生成された catch ブロック
	    				e.printStackTrace();
	    			} catch (Exception e) {
	    				// TODO 自動生成された catch ブロック
	    				e.printStackTrace();
	    			}
	    	}
*/	    }
		
	}

	protected boolean execConv(Session session, ConfigNode oconv, ConfigNode node, String file) {
		StringBuffer buf = new StringBuffer();
	    for (ConfigNode child : node.getChildren()) {
	    	boolean convflg = true;
		    for (ConfigNode ocon : oconv.getChildren()) {
		    	if (child.getName().equals(ocon.getName())) {
			    	convflg = false;
		    		break;
		    	}
		    }
	    	if (!oconv.getChildren().isEmpty() && convflg) {
	    		buf.append(child.getName() + "\t" +child.getContent() + "\n");
	    	}
	    }
	    if (buf.length() == 0) {
	    	return false;
	    }
	    String com = "cat <<EOF >> " +  file + "\n" + buf.toString() + "EOF";
		System.out.println(session.getHost() + ":" + com);
	    try {
			getConfig(session, null, com);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	    return true;
	}
	
	@Override
	public String getConfigMap(String str) {
		// TODO 自動生成されたメソッド・スタブ
		return configMap.get(str);
	}

}
