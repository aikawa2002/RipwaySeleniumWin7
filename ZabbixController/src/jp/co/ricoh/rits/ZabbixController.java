package jp.co.ricoh.rits;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import jp.co.ricoh.rits.zabbix.ZabbixHostApi;

public class ZabbixController {
	  public static void main(String[] args) {
			 if ( args.length != 1 ) {
				 System.exit(-1);
			 }
			
		  	final Properties rb = new Properties();
	        InputStream inStream = null;
	        try {
				inStream = new BufferedInputStream(new FileInputStream(args[0]));
		        rb.load(inStream);
			} catch (FileNotFoundException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
	        
			String zabbixAddress = rb.getProperty("ZABBIX_ADDRESS");
			String zabbixUser = rb.getProperty("ZABBIX_USER");
			String zabbixPassword = rb.getProperty("ZABBIX_PASSWORD");
			System.out.println(zabbixAddress + ":" + zabbixUser + ":" + zabbixPassword);
			ZabbixHostApi zabbixHostApi = new ZabbixHostApi("http://" + zabbixAddress + "/zabbix/api_jsonrpc.php", zabbixUser, zabbixPassword);

		    try {
		      //ホスト情報取得
		      String result = zabbixHostApi.get(1);
		      System.out.println(result);

		      //テンプレート作成
		      //result = zabbixApi.createTemplate(zabbixUser, zabbixPassword);
		      //System.out.println(result);
		      
		    } catch (Exception e) {
		      System.err.println(e.getMessage());
		      e.printStackTrace();
		    }
		  }
}
