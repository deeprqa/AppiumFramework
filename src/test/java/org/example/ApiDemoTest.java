package org.example;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.testng.annotations.Test;
import pageObjects.HomePage;

import java.io.IOException;

public class ApiDemoTest extends Base{

    @Test
    public void apiDemoAppTest() throws IOException, InterruptedException {
        service = startServer();
        AndroidDriver<AndroidElement> driver = capabilites("ApiDemoApp");
        HomePage h = new HomePage(driver);

        h.Preferences.click();
        //service.stop();
    }
}
