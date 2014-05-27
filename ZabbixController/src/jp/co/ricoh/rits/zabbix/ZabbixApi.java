package jp.co.ricoh.rits.zabbix;

import java.util.HashMap;

import jp.co.ricoh.rits.dto.ZabbixDto;
import net.arnx.jsonic.JSON;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

@SuppressWarnings("deprecation")
public class ZabbixApi {
	 protected String url;
	 protected String authId;
	 protected String user;
	 protected String password;
	 
	 
	  public ZabbixApi(String url, String user, String password){
	    this.url = url;
	    this.user = user;
	    this.password = password;
	    try {
			auth(user, password);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	  }

	  public void auth(String user, String password) throws Exception{
	    
	    ZabbixDto zabbixDto = new ZabbixDto();
	    HashMap<String,String> map = new HashMap<String,String>();

        map.put("user", user);
	    map.put("password", password);
	    zabbixDto.setParams(map);
	    zabbixDto.setMethod("user.login");
	    zabbixDto.setId(1);
	    ZabbixDto response = callApi(zabbixDto);
	    this.authId = response.getResult();
	    System.out.println("AuthID= " + authId);
	  }


    protected String response(ZabbixDto params) throws Exception {
	    ZabbixDto response = callApi(params);
	    return response.getResult();
    }
    

    protected ZabbixDto callApi(ZabbixDto params) throws Exception {

	    // HTTP POST
	    HttpResponse httpResponse;
	    HttpPost httpPost = new HttpPost(this.url);
	    String responseBody;
	    try {
	      httpPost.setHeader("Content-Type", "application/json-rpc");
	      httpPost.setHeader("User-Agent",   "Zabbitan Widget");
	      System.out.println(JSON.encode(params));
	      httpPost.setEntity(new StringEntity(JSON.encode(params)));

	      DefaultHttpClient client = new DefaultHttpClient();
	      httpResponse = client.execute(httpPost);
	      responseBody = EntityUtils.toString(httpResponse.getEntity());
	    } catch (Exception e) {
	      throw new Exception("HTTP Request Error");
	    }

	    if( httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
	      throw new Exception("HTTP Error : " + responseBody);
	    }

	    ZabbixDto responseJson;
	    try {
	      responseJson = JSON.decode(responseBody,ZabbixDto.class);
	    } catch (Exception e) {
	      throw new Exception(e.getMessage());
	    }

	    if(responseJson.getError() != null && responseJson.getError().size() > 0) {
	      String message;
	      HashMap map = responseJson.getError();
	      message = "API Error :code=" + map.get("code") + ":message=" + map.get("message");
	      throw new Exception(message);
	    }
	    return responseJson;
	  }

}
