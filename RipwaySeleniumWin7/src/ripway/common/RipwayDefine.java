package ripway.common;


public class RipwayDefine {
	public static final String PROXY = "proxy.ricoh.co.jp:8080"; //10.250.80.20:8080
	public static final String baseUrl = "http://192.168.187.142/";
	//public static final String baseUrl = "http://10.247.3.136/";
	public static final String LO = "v3r3ricoh/LO.jsp";
	//public static final String LO = "ripway_gsdi/LO.jsp";
	
	public static final String nlogin="/html/body/form/table/tbody/tr/td/center/table[3]/tbody/tr/td[2]";
	public static final String keizoku="/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr/td/button";

	public static final String menu_naiyou="/html/body/form/table/tbody/tr/td/center/a[2]/div";
	public static final String menu_shugo="//a[3]/div";

	public static final String shugo_command="(//input[@name='queryType'])[3]";

	public static final String select_sql = "select search from ripway_test where site = ? and result = 0 order by date desc limit 1";
	public static final String sql = "insert into ripway_test(site,text,search,timeval,result,date) values (?,?,?,?,?,?)";
	public static final String db_drv = "com.mysql.jdbc.Driver";
	//public static final String db_url = "jdbc:mysql://10.247.3.135:3306/test?user=root&password=password&useUnicode=true&characterEncoding=UTF-8";
	public static final String db_url = "jdbc:mysql://10.247.6.101:3306/test?user=root&password=ripway.net&useUnicode=true&characterEncoding=UTF-8";
//	public static final String db_url = "jdbc:mysql://10.247.6.209:3306/test?user=root&password=ripway.net&useUnicode=true&characterEncoding=UTF-8";

}
