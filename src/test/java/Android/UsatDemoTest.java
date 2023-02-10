package Android;

import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.appium.Target;
import com.applitools.eyes.config.Configuration;
import com.applitools.eyes.visualgrid.model.AndroidDeviceInfo;
import com.applitools.eyes.visualgrid.model.AndroidDeviceName;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidTouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class UsatDemoTest {
  public AppiumDriver driver;
  public AndroidTouchAction action;
  public WebDriverWait wait;
  public VisualGridRunner runner;
  public Eyes eyes;

  @BeforeTest
  public void setUp() throws MalformedURLException {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("platformName","Android");
    caps.setCapability("automationName", "UiAutomator2");
    caps.setCapability("platformVersion", "12");
    caps.setCapability("deviceName", "Pixel 4 API 32");
    caps.setCapability("app", System.getProperty("user.dir") + "/apps/gannettNews-usat-debug.apk");
    caps.setCapability("appPackage", "com.usatoday.android.news");
    caps.setCapability("appActivity", "com.gannett.android.news.ActivityLoading");

    driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), caps);
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    action = new AndroidTouchAction(driver);
    wait = new WebDriverWait(driver, 9);

    runner = new VisualGridRunner(new RunnerOptions().testConcurrency(3));
    eyes = new Eyes(runner);
    Configuration config = new Configuration();
    config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
    config.setServerUrl(System.getenv("APPLITOOLS_SERVER_URL"));
    config.setForceFullPageScreenshot(false);
    config.addMobileDevice(new AndroidDeviceInfo(AndroidDeviceName.Pixel_4_XL));
    config.addMobileDevice(new AndroidDeviceInfo(AndroidDeviceName.Galaxy_Note_10_Plus));
    eyes.setConfiguration(config);
    System.setProperty("APPLITOOLS_DONT_CLOSE_BATCHES", "true");
  }

  @Test
  public void onboardProcess() {
    eyes.open(driver, "USA Today", "Appium Android Demo");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logo")));
    AndroidElement onBoardingButton = (AndroidElement) wait.until(ExpectedConditions.elementToBeClickable(By.id("on_boarding_button")));
    onBoardingButton.click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(new MobileBy.ByAccessibilityId("Alerts Icon")));
    AndroidElement alertsOnBoardingButton = (AndroidElement) wait.until(ExpectedConditions.elementToBeClickable(By.id("on_boarding_button")));
    alertsOnBoardingButton.click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("follow_topics_onboard_icon")));
    AndroidElement topicsOnBoardingButton = (AndroidElement) wait.until(ExpectedConditions.elementToBeClickable(By.id("on_boarding_button")));
    // eyes.check("Topics list", Target.window());
    topicsOnBoardingButton.click();
    AndroidElement toolbarLogo = (AndroidElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toolbar_logo")));
    Assert.assertTrue(toolbarLogo.isDisplayed(), "Missing USA Today logo.");
    AndroidElement mainArticle = (AndroidElement) wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("article_info_layout")));
    Assert.assertTrue(mainArticle.isDisplayed(), "Missing main article in home page.");
  }

  @AfterTest
  public void tearDown() {
    driver.removeApp("com.usatoday.android.news");
    if (driver != null) {
      driver.quit();
    }
  }
}
