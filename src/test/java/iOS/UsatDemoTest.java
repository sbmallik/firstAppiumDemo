package iOS;

import com.applitools.eyes.appium.Eyes;
import com.applitools.eyes.appium.Target;
import com.applitools.eyes.config.Configuration;
import com.applitools.eyes.visualgrid.model.IosDeviceInfo;
import com.applitools.eyes.visualgrid.model.IosDeviceName;
import com.applitools.eyes.visualgrid.services.RunnerOptions;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.ios.IOSTouchAction;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class UsatDemoTest {
  public AppiumDriver driver;
  public IOSTouchAction action;
  public WebDriverWait wait;
  public VisualGridRunner runner;
  public Eyes eyes;

  @BeforeTest
  public void setUp() throws MalformedURLException {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("platformName","iOS");
    caps.setCapability("automationName", "XCUITest");
    caps.setCapability("platformVersion", "15.5");
    caps.setCapability("deviceName", "iPhone 12 Pro Max");
    caps.setCapability("app", System.getProperty("user.dir") + "/apps/App.zip");

    driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), caps);
    driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    action = new IOSTouchAction(driver);
    wait = new WebDriverWait(driver, 9);

    runner = new VisualGridRunner(new RunnerOptions().testConcurrency(3));
    eyes = new Eyes(runner);
    Configuration config = new Configuration();
    config.setApiKey(System.getenv("APPLITOOLS_API_KEY"));
    config.setServerUrl(System.getenv("APPLITOOLS_SERVER_URL"));
    config.addMobileDevice(new IosDeviceInfo(IosDeviceName.iPhone_12_Pro_Max));
    config.addMobileDevice(new IosDeviceInfo(IosDeviceName.iPhone_12_mini));
    config.addMobileDevice(new IosDeviceInfo(IosDeviceName.iPad_Air_2));
    eyes.setConfiguration(config);
    System.setProperty("APPLITOOLS_DONT_CLOSE_BATCHES", "true");
  }

  @Test
  public void onboardProcess() {
    eyes.open(driver, "USA Today", "Appium iOS Demo");
    wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("user ed view")));
    IOSElement onBoardingButton = (IOSElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//XCUIElementTypeButton[@name='Next']")));
    onBoardingButton.click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("Want news notifications?")));
    IOSElement alertsOnBoardingButton = (IOSElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//XCUIElementTypeButton[@name='Not Now']")));
    alertsOnBoardingButton.click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("NotificationsBell")));
    IOSElement onBoardingButton2 = (IOSElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//XCUIElementTypeButton[@name='Next']")));
    onBoardingButton2.click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("FollowTopicsTag_Dark")));
    eyes.check("Topics list", Target.window());
    IOSElement onBoardingButton3 = (IOSElement) wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//XCUIElementTypeButton[@name='Skip']")));
    onBoardingButton3.click();
    IOSElement searchIcon = (IOSElement) wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.AccessibilityId("frontSearchBarButton")));
    Assert.assertTrue(searchIcon.isDisplayed(), "Missing Search icon.");
  }

  @AfterTest
  public void tearDown() {
    driver.removeApp("com.usatoday.android.news");
    eyes.closeAsync();
    runner.getAllTestResults();
    if (driver != null) {
      driver.quit();
    }
    eyes.abortIfNotClosed();
  }
}
