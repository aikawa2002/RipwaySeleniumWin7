package ripway.common;

import java.util.ResourceBundle;


public class RipwayDefine {
	public static final String TITLE = "【RIPWAYサービス監視】";
	public static final String LO = "v3r3ricoh/LO.jsp";
	
	public static final String nlogin="/html/body/form/table/tbody/tr/td/center/table[3]/tbody/tr/td[2]";
	public static final String keizoku="/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr/td/button";

	public static final String menu_naiyou="/html/body/form/table/tbody/tr/td/center/a[2]/div";
	public static final String menu_shugo="//a[3]/div";

	public static final String shugo_command="(//input[@name='queryType'])[3]";

	public static final String select_sql = "select search from ripway_test where site = ? and result = 0 order by date desc limit 1";
	public static final String sql = "insert into ripway_test(site,text,search,timeval,result,date) values (?,?,?,?,?,?)";
	public static final String db_drv = "com.mysql.jdbc.Driver";

	public static String PROXY = null;
	public static String baseUrl = null;
	public static String base2Url = null;
	public static String db_url = null;
	protected static String smtp= null;
	protected static String from= null;
	protected static String sender= null;
	
	public static void setProperties(String propName) {
		 ResourceBundle rb = ResourceBundle.getBundle(propName);
		 RipwayDefine.PROXY = rb.getString("PROXY");
		 RipwayDefine.baseUrl = rb.getString("baseUrl");
		 RipwayDefine.base2Url = rb.getString("base2Url");
		 RipwayDefine.smtp = rb.getString("smtp");
		 RipwayDefine.from = rb.getString("from");
		 RipwayDefine.sender = rb.getString("sender");
		 RipwayDefine.db_url = rb.getString("db_url");
	}

}
