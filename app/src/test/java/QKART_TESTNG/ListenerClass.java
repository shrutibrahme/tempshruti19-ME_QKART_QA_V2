package QKART_TESTNG;

import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerClass implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "onTestStart", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
       QKART_Tests.takeScreenshot(QKART_Tests.driver, "onTestFailure", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        QKART_Tests.takeScreenshot(QKART_Tests.driver, "onTestSuccess", result.getName());
    }
    
}
