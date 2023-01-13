package Android;

import io.appium.java_client.AppiumDriver;
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
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class FirstAndroidTest {
  public AppiumDriver driver;
  public AndroidTouchAction action;

  public void tapOnElement(AndroidElement element) {
    action.tap(ElementOption.element(element)).perform();
  }

  public void scrollDown() {
    Dimension dimension = driver.manage().window().getSize();
    int scrollStart = (int) (dimension.getHeight() * 0.8);
    int scrollEnd = (int) (dimension.getHeight() * 0.1);
    action = new AndroidTouchAction(driver)
        .press(PointOption.point(0, scrollStart))
        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
        .moveTo(PointOption.point(0, scrollEnd))
        .release()
        .perform();
  }

  public void dragAndDrop(AndroidElement dragElement, AndroidElement dropElement) {
    action.longPress(ElementOption.element(dragElement))
        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
        .moveTo(ElementOption.element(dropElement))
        .release()
        .perform();
  }

  @BeforeTest
  public void setUp() throws MalformedURLException {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("platformName","Android");
    caps.setCapability("automationName", "UiAutomator2");
    caps.setCapability("platformVersion", "10.0");
    caps.setCapability("deviceName", "Pixel 2 API 29");
    caps.setCapability("app", System.getProperty("user.dir") + "/apps/ApiDemos-debug.apk");

    driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), caps);
    action = new AndroidTouchAction(driver);
  }

  @Test
  public void clickAppButton() {
    WebDriverWait wait = new WebDriverWait(driver, 3);
    AndroidElement app = (AndroidElement) driver.findElementByAccessibilityId("App");
    wait.until(ExpectedConditions.elementToBeClickable(app));
    app.click();
    driver.navigate().back();
  }

  @Test
  public void scroll_test() {
    tapOnElement((AndroidElement) driver.findElementByAccessibilityId("Views"));
    scrollDown();
    tapOnElement((AndroidElement) driver.findElementByAccessibilityId("Lists"));
    driver.navigate().back();
    driver.navigate().back();
  }

  @Test
  public void drag_drop() {
    tapOnElement((AndroidElement) driver.findElementByAccessibilityId("Views"));
    tapOnElement((AndroidElement) driver.findElementByAccessibilityId("Drag and Drop"));
    dragAndDrop(
        (AndroidElement) driver.findElement(By.id("drag_dot_1")),
        (AndroidElement) driver.findElement(By.id("drag_dot_2"))
    );
    driver.navigate().back();
    driver.navigate().back();
  }

  @AfterTest
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }
}
