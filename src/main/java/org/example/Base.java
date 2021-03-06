package org.example;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Base {
    public static AppiumDriverLocalService service;
    public static AndroidDriver<AndroidElement> driver;

    @BeforeTest
    public AppiumDriverLocalService startServer() {
        boolean flag = checkIfSeverIsRunning(4723);
        if (!flag) {
            service = AppiumDriverLocalService.buildDefaultService();
            service.start();
        }
        return service;
    }

    @AfterTest
    public void stopServer() {
        service.stop();
    }

    public static boolean checkIfSeverIsRunning(int port) {
        boolean isServerRunning = false;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();
        } catch (IOException e) {
            isServerRunning = true;
        } finally {
            serverSocket = null;
        }
        return isServerRunning;
    }

    public static AndroidDriver<AndroidElement> capabilites(String appName) throws IOException, InterruptedException {

        FileInputStream f = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\org\\example\\global.propertise");
        Properties prop = new Properties();
        prop.load(f);

        File appDir = new File("src");

        //File app = new File(appDir, "General-Store.apk");
        File app = new File(appDir, (String) prop.get(appName));

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        //String device = (String) prop.get("device");
        String device = System.getProperty("deviceName");
        if (device.contains("RD")) {
            startEmulator();
        }
        cap.setCapability(MobileCapabilityType.DEVICE_NAME, device);
        //cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Device");
        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "100");
        cap.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), cap);
        cap.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
        cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 14);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
    }

    public static void startEmulator() throws IOException, InterruptedException {
        Runtime.getRuntime().exec(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\startEmulator.bat");
        Thread.sleep(5000);
    }

    public static void getScreenShot(String fileName) throws IOException {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir") + "\\Screenshot\\"+fileName+".png"));
    }
}
