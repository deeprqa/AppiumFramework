package org.example;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import org.testng.Assert;
import org.testng.annotations.Test;
import pageObjects.FormPage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.touch.offset.ElementOption.element;

public class ECommerceTest extends Base {

    @Test(dataProvider="InputData", dataProviderClass = TestData.class)
    public void totalValidation(String input) throws IOException, InterruptedException {
        service = startServer();
        AndroidDriver<AndroidElement> driver = capabilites("GeneralStoreApp");
        FormPage form = new FormPage(driver);
        Utilities util = new Utilities(driver);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        form.nameTextBox.click();
        form.nameTextBox.sendKeys(input);
        driver.hideKeyboard();
        form.radioButtonMale.click();
        form.btnLetsShop.click();

        //driver.findElement(By.id("android:id/text1")).click();
        //driver.findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollIntoView(\"Germany\"))");

        String[] product = {"Converse All Star", "Air Jordan 9 Retro"};
        float productListamount = 0;
        //String product = "Converse All Star";
        for (int i = 0; i < product.length; i++) {
            util.scrollTOText(product[i]);
            String a = driver.findElementByXPath("//android.widget.TextView[@text='" + product[i] + "']/following-sibling::android.widget.LinearLayout/android.widget.TextView[@resource-id='com.androidsample.generalstore:id/productPrice']").getText();
            a = a.replace("$", "");
            productListamount = productListamount + Float.parseFloat(a);
            driver.findElementByXPath("//android.widget.TextView[@text='" + product[i] + "']/following-sibling::android.widget.LinearLayout/android.widget.TextView[@text='ADD TO CART']").click();
        }


        driver.findElementById("com.androidsample.generalstore:id/appbar_btn_cart").click();
        String actualAmount = driver.findElementById("com.androidsample.generalstore:id/totalAmountLbl").getText();
        //String cartProductText = driver.findElementById("com.androidsample.generalstore:id/productName").getText();
        System.out.println(actualAmount);

        Assert.assertTrue(actualAmount.contains(Float.toString(productListamount)));
        /*TouchAction t = new TouchAction(driver);
        WebElement checkBox = driver.findElementByClassName("android.widget.CheckBox");
        t.tap(tapOptions().withElement(element(checkBox))).perform();
        *//*WebElement terms = driver.findElementById("com.androidsample.generalstore:id/termsButton");
        t.longPress(longPressOptions().withElement(element(terms)).withDuration(ofSeconds(2))).release().perform();
        driver.findElementById("android:id/button1").click();
        *//*
        driver.findElementByXPath("//android.widget.Button[@text='Visit to the website to complete purchase']").click();
        Thread.sleep(7000);
        Set<String> contexts = driver.getContextHandles();

        for(String contextName : contexts){
            System.out.println(contextName);
        }

        driver.context("WEBVIEW_com.androidsample.generalstore");
        Thread.sleep(5000);
        driver.findElement(By.name("q")).sendKeys("hello");
        driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
        //driver.closeApp();
//        String toastMessage = driver.findElement(By.xpath("//android.widget.Toast[1]")).getAttribute("name");
//        System.out.println(toastMessage);*/
        //service.stop();

    }

}
