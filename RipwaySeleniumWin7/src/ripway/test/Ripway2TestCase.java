package ripway.test;

import static org.junit.Assert.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import ripway.common.RipwayDefine;

import com.thoughtworks.selenium.Selenium;

public class Ripway2TestCase {

	protected int[] cntval = new int[1000];
	protected Selenium selenium;
	protected String cgiUrl = null;
	protected String db_drv = null;
	protected String db_url = null;
	protected ArrayList<String> re = new ArrayList<String>();
	
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	private int mode = 1;

	private String testBrowser;
	private String testUrl;
	long start = 0;
	long end = 0;
	String timeout = "800000";

	ArrayList<String> list = new ArrayList<String>();
	String val = null;
	String val1 = null;
	Timestamp Time2 = null;
	Connection con = null;
	String site = null;
	int result = 0;
	protected  static StringBuffer str = new StringBuffer();
	static String select_sql = "select search from ripway_test where site = ? and result = 0 order by date desc limit 1";
	static String sql = "insert into ripway_test(site,text,search,timeval,result,date) values (?,?,?,?,?,?)";

	public String getDb_drv() {
		return db_drv;
	}

	@Before
	public void setUp() throws Exception {

		// プロパティファイル読み込み
		// テスト対象のサーバURL＆ブラウザ設定をプロパティから読み込む
		testBrowser = "*iexplore";
		timeout = "800000";
		// cgiUrl = prop.getProperty("SeleniumTest.CGI");
		db_drv = "com.mysql.jdbc.Driver";
		db_url = RipwayDefine.db_url;
		// SeleniumServerを起動する。
		File profileDir = new File("/root/.mozilla/firefox/edt2m12g.default");
		FirefoxProfile profile = new FirefoxProfile(profileDir);
		profile.setPreference("browser.safebrowsing.malware.enabled", false);
		driver = new FirefoxDriver(profile);
	
		baseUrl = RipwayDefine.baseUrl;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		Time2 = new Timestamp(System.currentTimeMillis());
		con = getConnection();
		System.out.println(db_url);
		System.out.println(baseUrl);
		site = "RIPWAY2";
	}

	@After
	public void tearDown() throws Exception {
		con.close();
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	protected Connection getConnection() throws Exception {
		Class.forName(getDb_drv());
		return DriverManager.getConnection(getDb_url());
	}

	private void login(String url) throws InterruptedException {
		Alert alert = null;
		
	    // open | /v3r3ricoh/LO.jsp | 
		driver.get(url);
		
		Thread.sleep(500L);
	}
	
	 
	 @SuppressWarnings("deprecation")
	@Test
	 public void test300() throws Exception {
		 this.re.add(Time2.toLocaleString()); int valnum = 0;
		 try{
			 login(baseUrl + "v3r3ricoh/admin/index.do");

			// type | name=authId | 9536
			driver.findElement(By.name("userID")).clear();
			driver.findElement(By.name("userID")).sendKeys("ripadm");
			// type | name=password | ripway
			driver.findElement(By.name("password")).clear();
			driver.findElement(By.name("password")).sendKeys("ripadm99");
			// click | css=button.main | 
			driver.findElement(By.xpath("//input[@value='ログイン']")).click();

			Thread.sleep(5000L);
			
			WebElement element = null;

			driver.findElement(By.linkText("ログインリスト")).click();

			start	 = System.currentTimeMillis(); 
			
			Thread.sleep(5000L);
			
			end =	 System.currentTimeMillis(); 
		    
			try {
		        element = driver.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[2]/font/b"));	
			    val =element.getText();
		        } catch(Exception e){}
			
			driver.findElement(By.xpath("//input[@value='ログアウト']")).click();
			Thread.sleep(5000L);

		 } catch(Exception e) { 
			 e.getStackTrace(); 
			 result = 1; 
	     } finally { 
		   valnum	 = Integer.parseInt(val.trim());
		   str.append("現在のログイン数:" + val + "\r\r");
		   setData("ripadmv3","現在のログイン数:" + val,valnum,(end - start)/1000,result);
		 }
	 }

	 @SuppressWarnings("deprecation")
	@Test
	 public void test001() throws Exception {
	 this.re.add(Time2.toLocaleString()); 
	 String msg = null; 
	 int valnum = 0;
	 int prevalnum =0; 
	 WebElement element = null;
	 
	 try{ 
		 prevalnum =getData("ripadm");
     login("http://192.168.187.101/ripway2/L1.jsp");
	// type | name=authId | 9536
	driver.findElement(By.name("userID")).clear();
	driver.findElement(By.name("userID")).sendKeys("ripadm");
	// type | name=password | ripway
	driver.findElement(By.name("password")).clear();
	driver.findElement(By.name("password")).sendKeys("ripadm99");
	// click | css=button.main | 
	driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr/td/button")).click();

	Thread.sleep(5000L);
    //二重ログインチェック
	try {
    element = driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[3]/tbody/tr/td[2]"));	
    System.out.println(element.getText());
      if (element.getText().startsWith("二重ログイン確認")) {
          driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr/td/button")).click();
      }
    } catch(Exception e){}

	 driver.findElement(By.linkText("再開")).click();
	 Thread.sleep(5000L);
	 
	 driver.findElement(By.xpath("//input[@value='件数確認']")).click();

	 start =	 System.currentTimeMillis(); 

	 try {
	(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
           public Boolean apply(WebDriver d) {
           //	WebElement element = d.findElement(By.xpath(" /html/body/form/table/tbody/tr/td/center/table[6]/tbody/tr[5]/td[2]/table/tbody/tr[3]/td/center/table/tbody/tr[2]/td[4]"));
           WebElement element = d.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[6]/tbody/tr[5]/td[2]/table/tbody/tr[3]/td/div/center/table/tbody/tr[2]/td[4]"));
           	boolean bl = false; 
           	if (element != null) bl = true;
               return bl;
           }
       });
	 end =	 System.currentTimeMillis(); 
    //element = driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[6]/tbody/tr[5]/td[2]/table/tbody/tr[3]/td/center/table/tbody/tr[2]/td[4]"));
    element = driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[6]/tbody/tr[5]/td[2]/table/tbody/tr[3]/td/div/center/table/tbody/tr[2]/td[4]"));
	 } catch(Exception e) { 
 	 	    start =	 System.currentTimeMillis(); 
			(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
		           public Boolean apply(WebDriver d) {
		           WebElement element = d.findElement(By.xpath(" /html/body/form/table/tbody/tr/td/center/table[6]/tbody/tr[5]/td[2]/table/tbody/tr[3]/td/center/table/tbody/tr[2]/td[4]"));
		           //WebElement element = d.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[6]/tbody/tr[5]/td[2]/table/tbody/tr[3]/td/div/center/table/tbody/tr[2]/td[4]"));
		           	boolean bl = false; 
		           	if (element != null) bl = true;
		               return bl;
		           }
		       });
			 end =	 System.currentTimeMillis(); 
		    element = driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[6]/tbody/tr[5]/td[2]/table/tbody/tr[3]/td/center/table/tbody/tr[2]/td[4]"));
		    //element = driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[6]/tbody/tr[5]/td[2]/table/tbody/tr[3]/td/div/center/table/tbody/tr[2]/td[4]"));
	 }

    val=element.getText();
    
	 driver.findElement(By.linkText("検索実行")).click();
	(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
           public Boolean apply(WebDriver d) {
           	WebElement element = d.findElement(By.name("F1"));
           	boolean bl = false; 
           	if (element != null) bl = true;
               return bl;
           }
     });

	 driver = driver.switchTo().frame("F1");
	 driver.findElement(By.linkText("検索条件")).click();

    (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
          public Boolean apply(WebDriver d) {
              return d.getTitle().toLowerCase().startsWith("ripway");
          }
      });
    
	 driver.findElement(By.linkText("新規調査")).click();
	Alert alert = null;

	 alert = driver.switchTo().alert();
System.out.println(alert.getText()); //ダイアログのメッセージ
     alert.accept();  //alertやconfirmのOKを押す。
	
	Thread.sleep(5000L);

	(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver d) {
            return d.getTitle().toLowerCase().startsWith("ripway");
        }
    });
	 //assertEquals("現在の検索条件・結果は失われます。よろしいですか？",
	 //selenium.getConfirmation()); 
	 } catch(Exception e) { 
		 e.getStackTrace();
	     result = 1; 
	 } finally { 
		 valnum = Integer.parseInt(val.replaceAll("件",	 "").trim());
		 //assertTrue(valnum >= prevalnum); 
		 if (valnum < prevalnum) {
	     msg = "件数が減少しています。";
	     result = 1; 
	     } else { 
		 msg = "JP件数確認:" +val; 
		 }
	 
	 str.append(msg + "  " +val + "　前回件数:" + String.valueOf(prevalnum) +"件\r"); 
	 setData("ripadm",msg,valnum,(end - start)/1000,result); 
	 }
	 val=null;
	 val1=null;
	 
	 try{ 
		 prevalnum =getData("ripadm_US");
		Thread.sleep(5000L);

	 driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[6]/tbody/tr[3]/td/table/tbody/tr/td/a")).click();
	 
	 driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr/td[2]/table/tbody/tr/td[2]/input")).click();
	 Thread.sleep(5000L);
	 
	 driver.findElement(By.xpath("//input[@value='件数確認']")).click();

	 start = System.currentTimeMillis(); 
	 
	(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
	           public Boolean apply(WebDriver d) {
	           	WebElement element = d.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[6]/tbody/tr[5]/td[2]/table/tbody/tr[3]/td/div/center/table/tbody/tr[2]/td[4]"));
	           	boolean bl = false; 
	           	if (element != null) bl = true;
	               return bl;
	           }
     });
	 end =	 System.currentTimeMillis(); 
     element = driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[6]/tbody/tr[5]/td[2]/table/tbody/tr[3]/td/div/center/table/tbody/tr[2]/td[4]"));
     val=element.getText();

	 driver.findElement(By.linkText("検索実行")).click();
	(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
           public Boolean apply(WebDriver d) {
           	WebElement element = d.findElement(By.name("F1"));
           	boolean bl = false; 
           	if (element != null) bl = true;
               return bl;
           }
     });

	 driver.findElement(By.linkText("検索条件")).click();

    (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
          public Boolean apply(WebDriver d) {
              return d.getTitle().toLowerCase().startsWith("ripway");
          }
      });

	 driver.findElement(By.linkText("ログアウト")).click();

	 (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
	       public Boolean apply(WebDriver d) {
	            return d.getTitle().toLowerCase().startsWith("ripway");
	        }
	 });

	 driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr/td/button")).click();
	 
	 } catch(Exception e) {
	 e.getStackTrace(); result = 1; 
	 } finally { 
		 valnum =	 Integer.parseInt(val.replaceAll("件", "").trim());
		 //assertTrue(valnum >=	 prevalnum);
		 if (valnum < prevalnum) {
			 msg = "件数が減少しています。";
			 result = 1; 
		} else { 
			msg = "US件数確認:" +val;
		}
		 str.append(msg + "  " +val + "　前回件数:" + String.valueOf(prevalnum) +"件\r\r");		 
		 setData("ripadm_US",msg,valnum,(end - start)/1000,result); 
		} 
	 }
	 
	 public void test002() throws Exception {
	 selenium.keyDownNative(String.valueOf(KeyEvent.VK_ENTER)); 
	 String msg = null; 
	 //CGI確認
	 try{selenium.openWindow("http://192.168.0.104/cgi-bin/D2.cgi?JPT=A&URI=99354442&FMT=null&service=NONE", "cgi"); 
	 long start = System.currentTimeMillis();
	 //selenium.waitForPageToLoad(timeout); 
	 selenium.waitForPopUp("cgi", timeout);

	 long end =	 System.currentTimeMillis();
	 
	 //selenium.selectFrame("frame_menu");
	 //selenium.click("link=書誌");
	 
	 //if (selenium.isTextPresent("特開平１１‐３５４４４２")){
	     str.append("CGIは正常です。\r");
	     msg = "CGIは正常です。";
	//	 selenium.click("link=この頁を閉じる");
	 //} else {
      //   str.append("CGIは不正です。\r");
	  //   msg = "CGIは不正です。";
	// }
     /* selenium.keyDownNative(String.valueOf(KeyEvent.VK_CONTROL)); // Stands
                                                                 // for
                                                     			 // CONTROL
	  pause(4000);

     selenium.keyPressNative("87"); // Stands for A "ascii code for W"

     pause(1000);
	 */
	     //setData("ripadm_CGI",msg,0,(end - start)/1000,0);
	 } catch(Exception e) { 
		 setData("ripadm_CGI","CGIが開きませんでした。",0,(end - start)/1000,1); 
		 str.append("CGIが開きませんでした。\r");
	 }
	 } 
	 
	// データベースへデータ追加
	protected void setData(String site, String text, int search, long timeval,
			int result) throws Exception {
		PreparedStatement prep = null;

		try {
			prep = con.prepareStatement(sql);
			prep.setString(1, site);
			prep.setString(2, text);
			prep.setInt(3, search);
			prep.setFloat(4, timeval);
			prep.setInt(5, result);
			prep.setTimestamp(6, Time2);
			prep.executeUpdate();

		} catch (SQLException se) {
			throw se;
		} finally {
			if (prep != null)
				prep.close();
		}
	}

	// データベースから値を取得
	protected int getData(String site) throws Exception {
		PreparedStatement prep = null;
		int ser = 0;
		try {
			prep = con.prepareStatement(select_sql);
			prep.setString(1, site);
			ResultSet rset = prep.executeQuery();
			while (rset.next()) {
				// 列番号による指定
				ser = rset.getInt(1);
			}
			rset.close();
		} catch (SQLException se) {
			throw se;
		} finally {
			if (prep != null)
				prep.close();
		}
		return ser;
	}

	public void setDb_drv(String db_drv) {
		this.db_drv = db_drv;
	}

	public String getDb_url() {
		return db_url;
	}

	public void setDb_url(String db_url) {
		this.db_url = db_url;
	}

	public static void main(String args[]) {
		 if ( args.length != 1 ) {
			 System.exit(-1);
		 }
		 RipwayDefine.setProperties(args[0]);
		 
		org.junit.runner.JUnitCore core = new org.junit.runner.JUnitCore();
		core.run(Ripway2TestCase.class);
		 System.exit(0);
	}
}
