package ripway.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import ripway.common.RipwayDefine;
import ripway.common.SendMailer;

public class RipwayTest1 {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	private Connection con = null;
	protected String db_drv = null;
	protected String db_url = null;
	private int mode = 1;
    //private ripway.common.SshSocks5Connection ssh = null;
    static String sql = "insert into ripway_test(site,text,search,timeval,result,date) values (?,?,?,?,?,?)";
	Timestamp Time2 = null;
    String site = null; 
    int result = 0;
    String env = "[RICOH tomcat3]";
	protected int[] cntval = new int[1000];
	protected SendMailer mail = null;

	@Before
	public void setUp() throws Exception {

		File profileDir = new File("/root/.mozilla/firefox/edt2m12g.default");
		FirefoxProfile profile = new FirefoxProfile(profileDir);
		profile.setPreference("browser.safebrowsing.malware.enabled", false);
		driver = new FirefoxDriver(profile);
		baseUrl = RipwayDefine.base2Url;
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        site = "nec";
        Time2 = new Timestamp(System.currentTimeMillis());
        mail = new SendMailer();
	}

   @Test
	public void test1() throws Exception {
		Alert alert = null;
		
	    // open | /v3r3ricoh/LO.jsp | 
		driver.get(RipwayDefine.baseUrl + RipwayDefine.LO);

		Thread.sleep(500L);

		try {
		// assertTitle | RIPWAY | 
		assertEquals("RIPWAY", driver.getTitle());
		// type | name=authId | 9536
		driver.findElement(By.name("authId")).clear();
		driver.findElement(By.name("authId")).sendKeys("9536");
		// type | name=password | ripway
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("ripway");
		// click | css=button.main | 
		driver.findElement(By.cssSelector("button.main")).click();
		// assertTitle | RIPWAY | 
		assertEquals("RIPWAY", driver.getTitle());
		} catch (Exception e) {
 	   		String err = e.getMessage();
 	   		
 	   		isError(env + "ログインが出来ません。", "ログインが出来ません。\n "+err);
	    	return;
 	   	}

		WebElement element = null;
        //二重ログインチェック
		try {
        element = driver.findElement(By.xpath(RipwayDefine.nlogin));	
        System.out.println(element.getText());
          if (element.getText().startsWith("二重ログイン確認")) {
              driver.findElement(By.xpath(RipwayDefine.keizoku)).click();
          }
        } catch(Exception e){}
		
        // click | //a[3]/div | 
		try {
		driver.findElement(By.xpath(RipwayDefine.menu_shugo)).click();

		// assertTitle | RIPWAY | 
		assertEquals("RIPWAY", driver.getTitle());
//集合検索
		Thread.sleep(1000L);
		driver.findElement(By.xpath("(//input[@name='queryType'])[1]")).click();
		Thread.sleep(1000L);
		driver.findElement(By.id("qpart")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.id("qpart")).sendKeys("ビアラホス生産菌は、ビアラホスが自身のグルタミン合成酵素を阻害する事態に対処するためビアラホスを無毒化する酵素ホスフィノスリシン N-アセチル基転移酵素(phosphinothricin N-acetyltransferase: PAT, EC 2.3.1.183, 反応)の遺伝子barを持っている。そこでbarを植物内で発現できるように改変して導入することでビアラホス耐性作物を開発した(薬剤の分解・修飾による無毒化)。");
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("button[title=\"質問文章の検索条件を確認できます\"]")).click();

		Thread.sleep(10000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
            	WebElement element = d.findElement(By.xpath("/html/body/form/center/table[5]/tbody/tr[4]/td[2]/table/tbody/tr/td/table/tbody/tr[2]/td"));
            	boolean bl = false; 
            	if (element != null) bl = true;
                return bl;
            }
        });
		} catch (Exception e) {
 	   		String err = e.getMessage();
 	   		isError("集合 概念検索失敗  ", "集合 概念検索に失敗しました \n\n loginurl -->"+ baseUrl + RipwayDefine.LO  + "\n\n" + err);
	    	return;
		}

//穴埋め検索
		try {
		Thread.sleep(1000L);
		driver.findElement(By.xpath("(//input[@name='queryType'])[2]")).click();
		Thread.sleep(500L);
		driver.findElement(By.name("dataC")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.name("dataC")).sendKeys("リコー");
		driver.findElement(By.cssSelector("button[title=\"条件毎の件数を確認します\"]")).click();

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
        		try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
                return d.findElement(By.xpath("/html/body/form/center/table[5]/tbody/tr[2]/td[2]/table/tbody/tr[3]/td/div/table/tbody/tr/td[6]")).getText().trim().endsWith("件");
            }
        });
		new Select(driver.findElement(By.xpath("(//select[@name='kindC'])[2]"))).selectByVisibleText("公開系公報発行日 (PDA)");
		Thread.sleep(1000L);
		driver.findElement(By.xpath("(//input[@name='dataC'])[2]")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.xpath("(//input[@name='dataC'])[2]")).sendKeys("2011");
		Thread.sleep(1000L);
		driver.findElement(By.name("exprC")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.name("exprC")).sendKeys("1+2");
		Thread.sleep(10000L);
		driver.findElement(By.cssSelector("button[title=\"条件毎の件数を確認します\"]")).click();

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
        		try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
                return d.findElement(By.xpath("/html/body/form/center/table[5]/tbody/tr[2]/td[2]/table/tbody/tr[3]/td/div/table/tbody/tr[2]/td[6]")).getText().trim().endsWith("件");
            }
        });

        //element = driver.findElement(By.xpath("/html/body/form/center/table[5]/tbody/tr[2]/td[2]/table/tbody/tr[5]/td/table/tbody/tr/td[4]"));	
		//assertEquals("1333615", element.getText().replaceAll("件", "").trim());
		//assertEquals("1331289", element.getText().replaceAll("件", "").trim());

	
		driver.findElement(By.name("exprC")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.name("exprC")).sendKeys("1*2");
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("button[title=\"条件毎の件数を確認します\"]")).click();

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
        		try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
                return d.getTitle().toLowerCase().startsWith("ripway");
                //return d.findElement(By.xpath("/html/body/form/center/table[5]/tbody/tr[2]/td[2]/table/tbody/tr[5]/td/table/tbody/tr/td[3]")).getText().trim().endsWith("件");
            }
        });

		//element = driver.findElement(By.xpath("/html/body/form/center/table[5]/tbody/tr[2]/td[2]/table/tbody/tr[5]/td/table/tbody/tr/td[4]"));	
		//assertEquals("42513", element.getText().replaceAll("件", "").trim());
		} catch (Exception e) {
	   		String err = e.getMessage();
	   		isError("集合 穴埋め検索失敗  ", "穴埋め検索に失敗しました \n\n loginurl -->"+ baseUrl + RipwayDefine.LO   + "\n\n" + err);
    	return;
		}
				
//コマンド検索
		try {
		Thread.sleep(1000L);
		driver.findElement(By.xpath(RipwayDefine.shugo_command)).click();
		Thread.sleep(1000L);
		assertEquals("RIPWAY", driver.getTitle());
		driver.findElement(By.id("cpart")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.id("cpart")).sendKeys("FK=環境");
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("button[title=\"集合を作成し集合表に追加します\"]")).click();

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
        		try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
                return d.getTitle().toLowerCase().startsWith("ripway");
            }
        });
		assertEquals("RIPWAY", driver.getTitle());
		driver.findElement(By.cssSelector("button[title=\"集合中の公報を一覧表示します\"]")).click();
        
		Thread.sleep(5000L);
		
//確認アラート
		 alert = driver.switchTo().alert();
 System.out.println(alert.getText()); //ダイアログのメッセージ
	     alert.accept();  //alertやconfirmのOKを押す。
		
		Thread.sleep(5000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("ripway");
            }
        });

		assertEquals("RIPWAY", driver.getTitle());
		} catch (Exception e) {
			String err = e.getMessage();
			isError("集合 コマンド検索失敗  ", "コマンド検索に失敗しました \n\n loginurl -->"+ baseUrl + RipwayDefine.LO   + "\n\n" + err);
		    return;
		}
        String currentWindowId = driver.getWindowHandle();
		driver.switchTo().window(currentWindowId);

		WebElement iFrame = driver.findElement(By.name("F3"));
		driver = driver.switchTo().frame(iFrame);
		driver = driver.switchTo().frame("F3");
        
//公報明細表示
        // 増えたウィンドウIDを取得する。
        String newWindowId = null;
        WebDriver newWindowDriver = null;
        /*		try {
		Thread.sleep(500L);
        driver.findElement(By.xpath("/html/body/form/div/table/tbody/tr[2]/td[3]/a[2]")).click();
        // ウィンドウ表示までに時間がかかると、seleniumが先走ることがあるのでウィンドウが増えるまで待機。
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getWindowHandles().size() > 1;
            }
        });
        for (String id : driver.getWindowHandles()) {
        	if (!id.equals(currentWindowId)) {
        		newWindowId = id;
        	}
        }
		} catch (Exception e) {
			String err = e.getMessage();
 		    mail.send("CGI is failure  ", "CGI is failure \n\n loginurl -->"+ baseUrl + RipwayDefine.LO  + "\n\n" + err);
 		    //ssh.sendMail("CGI is failure  ", "CGI is failure \n\n loginurl -->"+ baseUrl + RipwayDefine.LO  + "\n\n" + err);
		    return;
		}
        // ウィンドウ切り替え
        WebDriver newWindowDriver = driver.switchTo().window(newWindowId);
        
		Thread.sleep(5000L);

		//スナップショット
        if (newWindowDriver instanceof TakesScreenshot) {
            File f = ((TakesScreenshot) newWindowDriver).getScreenshotAs(OutputType.FILE);
            //File t = new File("search_screenshots" + File.separator
            File t = new File("D:\\"
            		+ driver.getClass().getSimpleName().toLowerCase()
            		+ System.currentTimeMillis() + ".png");
            FileChannel src = new FileInputStream(f).getChannel();
            FileChannel target = new FileOutputStream(t).getChannel();     
            src.transferTo(0, src.size(), target);
        }
        
        newWindowDriver.close();
        
        driver.switchTo().window(currentWindowId);
		driver = driver.switchTo().frame(iFrame);
*/		//driver = driver.switchTo().frame("F3");

//PDF表示
/*        driver.findElement(By.xpath("/html/body/form/div/table/tbody/tr[2]/td[3]/a/img")).click();
		
        // ウィンドウ表示までに時間がかかると、seleniumが先走ることがあるのでウィンドウが増えるまで待機。
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getWindowHandles().size() > 1;
            }
        });
        // 増えたウィンドウIDを取得する。
        newWindowId = null;
        for (String id : driver.getWindowHandles()) {
        	if (!id.equals(currentWindowId)) {
        		newWindowId = id;
        	}
        }
                
        // ウィンドウ切り替え
        newWindowDriver = driver.switchTo().window(newWindowId);
        
		Thread.sleep(10000L);
                
        newWindowDriver.close();
        driver.switchTo().window(currentWindowId);
*/        
        
		try {
        driver = driver.switchTo().frame("F1");
		driver.findElement(By.linkText("検索条件")).click();
        
		Thread.sleep(10000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("ripway");
            }
        });
//番号検索
		Thread.sleep(1000L);
		driver.findElement(By.xpath("(//input[@name='queryType'])[4]")).click();
		Thread.sleep(5000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("ripway");
            }
        });
		driver.findElement(By.id("qpart")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.id("qpart")).sendKeys("特願平05-218544\n特願2010-506434\n特願2009-129275\n特願平09-101739\n特願2002-155825\n特願平08-519558\n特願2005-001759\n特願平02-057258\n特願2005-314719\n特願2006-328971\n特願平02-503987\n特願2002-364825\n特願平03-213869\n特願2006-074817\n特願2001-330673\n特願平07-048387\n特願平09-088157\n特願平07-050992\n特願平10-000388\n特願平07-048388\n特願2005-362955\n特願2005-130560\n特願平08-516360\n特願平05-141945\n特願昭62-503027\n特願2007-522642\n特願平04-051012\n特願2000-173997\n特願2001-399647\n特願平06-308439\n");
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("button[title=\"検索を実行し公報リストを表示します\"]")).click();

		Thread.sleep(10000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
            	WebElement element = d.findElement(By.name("F1"));
            	boolean bl = false; 
            	if (element != null) bl = true;
                return bl;
            }
        });
		} catch (Exception e) {
			String err = e.getMessage();
			isError("集合 番号検索失敗  ", "番号検索に失敗しました \n\n loginurl -->"+ baseUrl + RipwayDefine.LO   + "\n\n" + err);
			return;
		}
		try {
        driver = driver.switchTo().frame("F3");
		
		new Select(driver.findElement(By.name("showIndex"))).selectByIndex(1);

		Thread.sleep(2000L);
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("ripway");
            }
        });

		driver.switchTo().window(currentWindowId);
        driver = driver.switchTo().frame("F3");
		new Select(driver.findElement(By.name("showIndex"))).selectByIndex(2);

		Thread.sleep(2000L);
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("ripway");
            }
        });

		driver.switchTo().window(currentWindowId);
        driver = driver.switchTo().frame("F3");
		new Select(driver.findElement(By.name("showIndex"))).selectByIndex(3);
		
		Thread.sleep(2000L);
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("ripway");
            }
        });

		driver.switchTo().window(currentWindowId);
        driver = driver.switchTo().frame("F3");
		new Select(driver.findElement(By.name("showIndex"))).selectByIndex(4);

		Thread.sleep(2000L);
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("ripway");
            }
        });

//EXCELダウンロード
		driver.switchTo().window(currentWindowId);
        driver = driver.switchTo().frame("F3");
		new Select(driver.findElement(By.name("command"))).selectByVisibleText("Ｅｘｃｅｌダウンロード");
		driver.findElement(By.cssSelector("button.small")).click();
		
        // ウィンドウ表示までに時間がかかると、seleniumが先走ることがあるのでウィンドウが増えるまで待機。
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getWindowHandles().size() > 1;
            }
        });
        // 増えたウィンドウIDを取得する。
        newWindowId = null;
        for (String id : driver.getWindowHandles()) {
        	if (!id.equals(currentWindowId)) {
        		newWindowId = id;
        	}
        }
                
        // ウィンドウ切り替え
        newWindowDriver = driver.switchTo().window(newWindowId);
        
		Thread.sleep(10000L);
		driver.findElement(By.xpath("/html/body/form/center/b/button")).click();

		driver.switchTo().window(currentWindowId);
        driver = driver.switchTo().frame("F1");
		driver.findElement(By.linkText("ログアウト")).click();
		driver.findElement(By.cssSelector("button.main")).click();
		} catch (Exception e) {
			String err = e.getMessage();
			isError("その他エラー  ", "その他エラーに失敗しました \n\n loginurl -->"+ baseUrl + RipwayDefine.LO   + "\n\n" + err);
			return;
		}
	}

	@Test
	public void test2() throws Exception {
		Alert alert = null;

		// open | /v3r3ricoh/LO.jsp | 
		driver.get(RipwayDefine.baseUrl + RipwayDefine.LO);

	try {
	    // assertTitle | RIPWAY | 
		assertEquals("RIPWAY", driver.getTitle());
		driver.findElement(By.name("authId")).clear();
		driver.findElement(By.name("authId")).sendKeys("9536");
		//driver.findElement(By.name("authId")).sendKeys("ripadm1");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("ripway");
		//driver.findElement(By.name("password")).sendKeys("ripway.net");
		driver.findElement(By.cssSelector("button.main")).click();
		assertEquals("RIPWAY", driver.getTitle());
	} catch (Exception e) {
		String err = e.getMessage();
		isError("ログインエラー  ", "ログインエラー \n\n loginurl -->"+ baseUrl + RipwayDefine.LO   + "\n\n" + err);
		return;
	}

		WebElement element = null;
        //二重ログインチェック
		try {
        element = driver.findElement(By.xpath(RipwayDefine.nlogin));	
        System.out.println(element.getText());
          if (element.getText().startsWith("二重ログイン確認")) {
              driver.findElement(By.xpath(RipwayDefine.keizoku)).click();
          }
        } catch(Exception e){}
		try {
        // click | //a[3]/div | 
		driver.findElement(By.xpath(RipwayDefine.menu_naiyou)).click();
		Thread.sleep(2000L);
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("ripway");
            }
        });

//質問文章
		Thread.sleep(1000L);
		driver.findElement(By.name("queryType")).click();
		Thread.sleep(1000L);
		driver.findElement(By.name("dataQ")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.name("dataQ")).sendKeys("ビアラホス生産菌は、ビアラホスが自身のグルタミン合成酵素を阻害する事態に対処するためビアラホスを無毒化する酵素ホスフィノスリシン N-アセチル基転移酵素(phosphinothricin N-acetyltransferase: PAT, EC 2.3.1.183, 反応)の遺伝子barを持っている。そこでbarを植物内で発現できるように改変して導入することでビアラホス耐性作物を開発した(薬剤の分解・修飾による無毒化)。");
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("button[title=\"質問文章の検索条件を確認できます\"]")).click();
		Thread.sleep(10000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
            	WebElement element = d.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr[4]/td[2]/table/tbody/tr/td/table/tbody/tr[2]/td"));
            	boolean bl = false; 
            	if (element != null) bl = true;
                return bl;
            }
        });
		} catch (Exception e) {
 	   		String err = e.getMessage();
 	   		isError("内容 質問文章失敗  ", "質問文章に失敗しました \n\n loginurl -->"+ baseUrl + RipwayDefine.LO  + "\n\n" + err);
	    	return;
		}
		
//キーワード
		try {
		Thread.sleep(1000L);
		driver.findElement(By.xpath("(//input[@name='queryType'])[2]")).click();
		Thread.sleep(1000L);
		driver.findElement(By.name("dataK")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.name("dataK")).sendKeys("環境");
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("button[title=\"キーワードの検索条件を確認できます\"]")).click();
		Thread.sleep(5000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[3]")).getText().trim().endsWith("件");
            }
        });
		new Select(driver.findElement(By.xpath("(//select[@name='kindK'])[2]"))).selectByVisibleText("発明・考案の名称（更新）");		
		Thread.sleep(1000L);
		driver.findElement(By.xpath("(//input[@name='dataK'])[2]")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.xpath("(//input[@name='dataK'])[2]")).sendKeys("ソーラー");
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("button[title=\"キーワードの検索条件を確認できます\"]")).click();
		Thread.sleep(10000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr[2]/td[2]/table/tbody/tr[4]/td[3]")).getText().trim().endsWith("件");
            }
        });
		driver.findElement(By.name("exprK")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.name("exprK")).sendKeys("1*2");
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("button[title=\"キーワードの検索条件を確認できます\"]")).click();
		Thread.sleep(10000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr[2]/td[2]/table/tbody/tr[7]/td[3]")).getText().trim().endsWith("件");
            }
        });
		} catch (Exception e) {
 	   		String err = e.getMessage();
 	   		isError("内容 キーワード検索失敗  ", "キーワード検索に失敗しました \n\n loginurl -->"+ baseUrl + RipwayDefine.LO  + "\n\n" + err);
	    	return;
		}
		try {
		Thread.sleep(1000L);
		driver.findElement(By.xpath("(//input[@name='queryType'])[3]")).click();
		Thread.sleep(500L);
		new Select(driver.findElement(By.name("kindB"))).selectByVisibleText("更新出願人と権利者 (PARH)");
		driver.findElement(By.name("dataB")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.name("dataB")).sendKeys("リコー");
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("button[title=\"書誌項目の検索条件を確認できます\"]")).click();
		Thread.sleep(10000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr[2]/td[2]/table/tbody/tr[3]/td/div/table/tbody/tr/td[6]")).getText().trim().endsWith("件");
            }
        });
		
		new Select(driver.findElement(By.xpath("(//select[@name='kindB'])[2]"))).selectByIndex(4);
		driver.findElement(By.xpath("(//input[@name='dataB'])[2]")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.xpath("(//input[@name='dataB'])[2]")).sendKeys("2011");
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("button[title=\"書誌項目の検索条件を確認できます\"]")).click();
		Thread.sleep(10000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr[2]/td[2]/table/tbody/tr[3]/td/div/table/tbody/tr[2]/td[6]")).getText().trim().endsWith("件");
            }
        });
		driver.findElement(By.name("exprB")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.name("exprB")).sendKeys("1+2");
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("button[title=\"書誌項目の検索条件を確認できます\"]")).click();
		Thread.sleep(10000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.findElement(By.xpath("/html/body/form/table/tbody/tr/td/center/table[5]/tbody/tr[2]/td[2]/table/tbody/tr[5]/td/table/tbody/tr/td[3]")).getText().trim().endsWith("件");
            }
        });
		} catch (Exception e) {
 	   		String err = e.getMessage();
 	   		isError("内容 書誌項目検索失敗  ", "書誌項目検索に失敗しました \n\n loginurl -->"+ baseUrl + RipwayDefine.LO  + "\n\n" + err);
	    	return;
		}

		try {
		driver.findElement(By.xpath("(//input[@name='queryType'])[4]")).click();
		Thread.sleep(5000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("ripway");
            }
        });
		driver.findElement(By.name("PN")).clear();
		Thread.sleep(1000L);
		driver.findElement(By.name("PN")).sendKeys("特願平05-218544\n特願2010-506434\n特願2009-129275\n特願平09-101739\n特願2002-155825\n特願平08-519558\n特願2005-001759\n特願平02-057258\n特願2005-314719\n特願2006-328971\n特願平02-503987\n特願2002-364825\n特願平03-213869\n特願2006-074817\n特願2001-330673\n特願平07-048387\n特願平09-088157\n特願平07-050992\n特願平10-000388\n特願平07-048388\n特願2005-362955\n特願2005-130560\n特願平08-516360\n特願平05-141945\n特願昭62-503027\n特願2007-522642\n特願平04-051012\n特願2000-173997\n特願2001-399647\n特願平06-308439");
		Thread.sleep(1000L);
		driver.findElement(By.cssSelector("td.area_m > button")).click();

		Thread.sleep(10000L);

		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
            	WebElement element = d.findElement(By.name("F1"));
            	boolean bl = false; 
            	if (element != null) bl = true;
                return bl;
            }
        });

		assertEquals("RIPWAY", driver.getTitle());
        String currentWindowId = driver.getWindowHandle();
		driver.switchTo().window(currentWindowId);

		driver = driver.switchTo().frame("F1");
		driver.findElement(By.linkText("ログアウト")).click();
		driver.findElement(By.cssSelector("button.main")).click();
		} catch (Exception e) {
 	   		String err = e.getMessage();
 	   		isError("内容 番号検索失敗  ", "番号検索に失敗しました \n\n loginurl -->"+ baseUrl + RipwayDefine.LO  + "\n\n" + err);
	    	return;
		}

	}

	@SuppressWarnings("resource")
	private void isError(String subject,String mailtext) {
        File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String filename = "/tmp/" + driver.getClass().getSimpleName().toLowerCase() + System.currentTimeMillis() + ".png";
        File t = new File(filename);
        FileChannel src;
		try {
			src = new FileInputStream(f).getChannel();
        FileChannel target = new FileOutputStream(t).getChannel();     
        src.transferTo(0, src.size(), target);
    	mail.send(RipwayDefine.TITLE + subject, mailtext,filename);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		System.exit(-1);
	}
	
	
	//データベースへデータ追加
	 protected void setData(String site,String text,int search,long timeval,int result) throws Exception {
	     PreparedStatement prep = null;
		 
	     try{
			 prep = con.prepareStatement(sql);
			 prep.setString(1, site);
			 prep.setString(2, text);
			 prep.setInt(3, search);
			 prep.setFloat(4, timeval);
			 prep.setInt(5, result);
			 prep.setTimestamp(6, Time2);
			 prep.executeUpdate();
			 		
		 } catch(SQLException se)  {
	            throw se;
	     } finally {
           if(prep != null)
               prep.close();	    	 
	     }
	 }

	 @After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	 public static void main(String args[]) {
		 if ( args.length != 1 ) {
			 System.exit(-1);
		 }
		try {
		 RipwayDefine.setProperties(args[0]);
		 
		 org.junit.runner.JUnitCore core = new org.junit.runner.JUnitCore();
		 core.run(RipwayTest1.class);
		 System.exit(0);
		} catch(Exception e) {
			e.printStackTrace();
			 System.exit(-1);
		}
	 }
		
}