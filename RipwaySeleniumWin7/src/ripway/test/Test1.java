package ripway.test;

import java.io.File;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

public class Test1 {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	//File profileDir = new File("C:\\Users\\r3pc\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\wywyp1e9.default");
	FirefoxProfile profile = new FirefoxProfile();
	//FirefoxProfile profile = new FirefoxProfile(profileDir);
	profile.setPreference("browser.safebrowsing.malware.enabled", false);
    driver = new FirefoxDriver(profile);
    baseUrl = "http://r01.ripway.net/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

	private void login(String url) throws InterruptedException {
		Alert alert = null;
		
	    // open | /v3r3ricoh/LO.jsp | 
		//driver.get(url);
		driver.navigate().to(url);		
		Thread.sleep(1000);
		
		//SOCKS5
/*		alert = driver.switchTo().alert();
		System.out.println(alert.getText()); //ダイアログのメッセージ

	    alert.accept();  //alertやconfirmのOKを押す。
		
	    (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("Windows");
                //return d.getTitle().toLowerCase().startsWith("ripway");
            }
        });
*/	}

@Test
  public void test1() throws Exception {
	login(baseUrl + "v3r3ricoh/admin/index.do");
    //driver.get(baseUrl + "v3r3ricoh/admin/index.do");
    driver.findElement(By.name("userID")).clear();
    driver.findElement(By.name("userID")).sendKeys("ripadm");
    driver.findElement(By.name("password")).clear();
    driver.findElement(By.name("password")).sendKeys("ripadm99");
    driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
    driver.findElement(By.linkText("ログインリスト")).click();
    String aaa = driver.findElement(By.cssSelector("b")).getText();
    driver.findElement(By.cssSelector("input[type=\"button\"]")).click();
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

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
  
 public static void main(String args[]) {
		   org.junit.runner.JUnitCore core = new org.junit.runner.JUnitCore();
		   core.run(Test1.class);
 }

}
