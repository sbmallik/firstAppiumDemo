package iOS;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.ios.IOSTouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class FirstiOSTest {
  public AppiumDriver driver;
  public IOSTouchAction action;

  public void scrollDown() {
    Dimension dimension = driver.manage().window().getSize();
    int scrollStart = (int) (dimension.getHeight() * 0.8);
    int scrollEnd = (int) (dimension.getHeight() * 0.2);
    action = new IOSTouchAction(driver)
        .press(PointOption.point(0, scrollStart))
        .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
        .moveTo(PointOption.point(0, scrollEnd))
        .release()
        .perform();
  }

  @BeforeTest
  public void setUp() throws MalformedURLException {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability("platformName","iOS");
    caps.setCapability("automationName", "XCUITest");
    caps.setCapability("platformVersion", "15.5");
    caps.setCapability("deviceName", "iPhone 12 mini");
    caps.setCapability("app", System.getProperty("user.dir") + "/apps/UIKitCatalog.zip");

    driver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), caps);
    action = new IOSTouchAction(driver);
  }

  @Test
  public void clickAppButton() {
    IOSElement buttons = (IOSElement) driver.findElementByAccessibilityId("Buttons");
    action.tap(ElementOption.element(buttons)).perform();
    IOSElement buttonTitle = (IOSElement) driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Buttons']"));
    Assert.assertTrue(buttonTitle.isDisplayed());
    driver.navigate().back();
  }

  @Test
  public void scrollTest() {
    scrollDown();
    IOSElement toolbars = (IOSElement) driver.findElementByAccessibilityId("Toolbars");
    action.tap(ElementOption.element(toolbars)).perform();
    IOSElement customToolbar = (IOSElement) driver.findElementByAccessibilityId("Custom");
    Assert.assertTrue(customToolbar.isDisplayed());
    driver.navigate().back();
  }

  @Test
  public void testPicker() {
    IOSElement pickerView = (IOSElement) driver.findElementByAccessibilityId("Picker View");
    action.tap(ElementOption.element(pickerView)).perform();
    IOSElement redColorComponent = (IOSElement) driver.findElementByAccessibilityId("Red color component value");
    IOSElement greenColorComponent = (IOSElement) driver.findElementByAccessibilityId("Green color component value");
    IOSElement blueColorComponent = (IOSElement) driver.findElementByAccessibilityId("Blue color component value");
    Assert.assertTrue(redColorComponent.isDisplayed());
    Assert.assertTrue(greenColorComponent.isDisplayed());
    Assert.assertTrue(blueColorComponent.isDisplayed());
    redColorComponent.sendKeys("30");
    greenColorComponent.sendKeys("200");
    blueColorComponent.sendKeys("100");
    Assert.assertEquals(redColorComponent.getAttribute("value"), "30");
    Assert.assertEquals(greenColorComponent.getAttribute("value"), "200");
    Assert.assertEquals(blueColorComponent.getAttribute("value"), "100");
    driver.navigate().back();
  }

  @AfterTest
  public void tearDown() {
    if (driver != null) {
      driver.quit();
    }
  }
}
