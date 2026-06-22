package QKART_TESTNG;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;
import static org.testng.Assert.assertTrue;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class QKART_Tests {

    public static RemoteWebDriver driver;
    public static String lastGeneratedUserName;

     @BeforeSuite(alwaysRun = true)
    public static void createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);
        System.out.println("createDriver()");
    }

    /*
     * Testcase01: Verify a new user can successfully register
     */
    //TC1_Username - testUser
    //TC1_Password - abc@123
    @Test(description = "Verify registration happens correctly", priority = 1, groups = "Sanity_test")
    @Parameters({"TC1_Username", "TC1_Password"})
    public void TestCase01(@Optional("abc@12345") String TC1_Username, @Optional("abc@12345") String TC1_Password) throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");
         //takeScreenshot(driver, "StartTestCase", "TestCase1");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
         status = registration.registerUser(TC1_Username, TC1_Password, true);
        assertTrue(status, "Failed to register new user");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
         status = login.PerformLogin(lastGeneratedUserName, TC1_Password);
         logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        assertTrue(status, "Failed to login with registered user");

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();

         logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status
         ? "PASS" : "FAIL");
         //takeScreenshot(driver, "EndTestCase", "TestCase1");
    }

    /*
     * Verify that an existing user is not allowed to re-register on QKart
     */
    @Test(description = "Verify re-registering an already registered user fails", priority = 2, groups = "Sanity_test")
    @Parameters({"TC1_Username", "TC1_Password"})
    public void TestCase02(@Optional("abc@12345") String TC1_Username, @Optional("abc@12345") String TC1_Password) throws InterruptedException {
        Boolean status;
        logStatus("Start Testcase", "Test Case 2: Verify User Registration with an existing username ", "DONE");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser(TC1_Username, TC1_Password, true);
        logStatus("Test Step", "User Registration : ", status ? "PASS" : "FAIL");
        
        //assertTrue
        //assertFalse

        Assert.assertTrue(status, "registration failed");

        /*
        if (!status) {
            logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "PASS" : "FAIL");
            //return false;

        }
        */

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, TC1_Password, false);

        Assert.assertFalse(status, "Re-registration is successful");

        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
        logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "FAIL" : "PASS");
        //return !status;
    }

     /*
     * Verify the functinality of the search text box
     */
    //TC5_ProductNameToSearchFor - YONEX
    //TC5_ProductNameToSearchFor - Gesundheit
    @Test(description = "Verify the functionality of search text box", priority = 3, groups = "Sanity_test")
    @Parameters({"TC3_ProductNameToSearchFor", "TC3_ProductNameToSearchFor2"})
    public void TestCase03(String TC3_ProductNameToSearchFor, String TC3_ProductNameToSearchFor2) throws InterruptedException {
        logStatus("TestCase 3", "Start test case : Verify functionality of search box ", "DONE");
        boolean status;

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for the "yonex" product
        status = homePage.searchForProduct(TC3_ProductNameToSearchFor);

        //assertTrue
        //assertFalse
        Assert.assertTrue(status, "Search for product failed");

        /*
        if (!status) {
            logStatus("TestCase 3", "Test Case Failure. Unable to search for given product", "FAIL");
            //return false;
        }
        */

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        // Verify the search results are available

        Assert.assertFalse(searchResults.isEmpty(), "Test Case Failure. There were no results for the given search string");
        /*
        if (searchResults.size() == 0) {
            logStatus("TestCase 3", "Test Case Failure. There were no results for the given search string", "FAIL");
            //return false;
        }
        */
        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            Assert.assertTrue(elementText.toUpperCase().contains("YONEX"), "Test Case Failure. Test Results contains un-expected values:" + elementText);
            
            /*
            if (!elementText.toUpperCase().contains("YONEX")) {
                logStatus("TestCase 3", "Test Case Failure. Test Results contains un-expected values: " + elementText,
                        "FAIL");
                //return false;
            }
            */
        }

        logStatus("Step Success", "Successfully validated the search results ", "PASS");

        // Search for product
        status = homePage.searchForProduct(TC3_ProductNameToSearchFor2);

        Assert.assertTrue(status, "Search for product 'Gesundheit' failed");
        /*
        if (!status) {
            logStatus("TestCase 3", "Test Case Failure. Invalid keyword returned results", "FAIL");
            //return false;
        }
        */

        // Verify no search results are found
        searchResults = homePage.getSearchResults();

        Assert.assertEquals(searchResults.size(), 0, "Test Case Fail. Expected: no results , actual: Results were available");

        Assert.assertTrue(homePage.isNoResultFound(), "Test case Failed, no products found message is not displayed");

        logStatus("TestCase 3", "Test Case PASS. Verified that no search results were found for the given text",
        "PASS");

        /*
        if (searchResults.size() == 0) {
            if (homePage.isNoResultFound()) {
                logStatus("Step Success", "Successfully validated that no products found message is displayed", "PASS");
            }
            logStatus("TestCase 3", "Test Case PASS. Verified that no search results were found for the given text",
                    "PASS");
        } else {
            logStatus("TestCase 3", "Test Case Fail. Expected: no results , actual: Results were available", "FAIL");
            //return false;
        }
        */

        //return true;
    }

    /*
     * Verify the presence of size chart and check if the size chart content is as
     * expected
     */
    //TC4_ProductNameToSearchFor - Running Shoes
    @Test(description = "Verify the existence of size chart for certain items and validate contents of size chart", priority = 4, groups = "Regression_Test")
    @Parameters({"TC4_ProductNameToSearchFor"})
    public void TestCase04(String TC4_ProductNameToSearchFor) throws InterruptedException {
        logStatus("TestCase 4", "Start test case : Verify the presence of size Chart", "DONE");
        boolean status = false;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for product and get card content element of search results
        status = homePage.searchForProduct(TC4_ProductNameToSearchFor);
        List<WebElement> searchResults = homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);

            Assert.assertTrue(result.verifySizeChartExists(), "Test Case Fail. Size Chart Link does not exist");

            Assert.assertTrue( result.verifyExistenceofSizeDropdown(driver), "Failed to validate the presence of dropdown");

            Assert.assertTrue(result.openSizechart(), "Test Case Fail. Failure to open Size Chart" );

            Assert.assertTrue(result.validateSizeChartContents(expectedTableHeaders, expectedTableBody, driver), "Failure while validating contents of Size Chart Link");

            Assert.assertTrue(result.closeSizeChart(driver) , "Failed to close the size Chart modal");

            // Verify if the size chart exists for the search result
           /* if (result.verifySizeChartExists()) {
                logStatus("Step Success", "Successfully validated presence of Size Chart Link", "PASS");

                // Verify if size dropdown exists
                status = result.verifyExistenceofSizeDropdown(driver);
                logStatus("Step Success", "Validated presence of drop down", status ? "PASS" : "FAIL");

                // Open the size chart
                if (result.openSizechart()) {
                    // Verify if the size chart contents matches the expected values
                    if (result.validateSizeChartContents(expectedTableHeaders, expectedTableBody, driver)) {
                        logStatus("Step Success", "Successfully validated contents of Size Chart Link", "PASS");
                    } else {
                        logStatus("Step Failure", "Failure while validating contents of Size Chart Link", "FAIL");
                        status = false;
                    }

                    // Close the size chart modal
                    status = result.closeSizeChart(driver);

                } else {
                    logStatus("TestCase 4", "Test Case Fail. Failure to open Size Chart", "FAIL");
                    //return false;
                }

            } else {
                logStatus("TestCase 4", "Test Case Fail. Size Chart Link does not exist", "FAIL");
                //return false;
            }*/
        }
        
        logStatus("TestCase 4", "End Test Case: Validated Size Chart Details", status ? "PASS" : "FAIL");
        //return status;
    }

    //TC5_ProductNameToSearchFor - YONEX Smash Badminton Racquet
    //TC5_ProductNameToSearchFor2 - Tan Leatherette Weekender Duffle
    //TC5_AddressDetails - Addr line 1 addr Line 2 addr line 3

    @Test(description = "Verify that a new user can add multiple products in to the cart and Checkout", priority = 5, groups = "Sanity_test")
    @Parameters({"TC5_ProductNameToSearchFor", "TC5_ProductNameToSearchFor2", "TC5_AddressDetails"})
    public void TestCase05(String TC5_ProductNameToSearchFor, String TC5_ProductNameToSearchFor2, String TC5_AddressDetails) throws InterruptedException {
        //takeScreenshot(driver, "StartTestCase", "TestCase05");

        Boolean status;
        logStatus("Start TestCase", "Test Case 5: Verify Happy Flow of buying products", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue(status, "Registration failed : Test Case Failure. Happy Flow Test Failed");
        /*
        if (!status) {
            logStatus("TestCase 5", "Test Case Failure. Happy Flow Test Failed", "FAIL");
        }
        */
        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        assertTrue(status, "Failed to login with registered user");

        /*
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase", "Test Case 5: Happy Flow Test Failed : ", status ? "PASS" : "FAIL");
        }
        */

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct(TC5_ProductNameToSearchFor);
        Assert.assertTrue(status, "Failed to search product: Yonex");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        status = homePage.searchForProduct(TC5_ProductNameToSearchFor2);
        Assert.assertTrue(status, "Failed to search product: Tan");
        homePage.addProductToCart("Tan Leatherette Weekender Duffle");

        // Click on the checkout button
        homePage.clickCheckout();

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress(TC5_AddressDetails);
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        // Place the order
        checkoutPage.placeOrder();
        // SLEEP_STMT_04: Wait for place order to succeed and navigate to Thanks page
        //Thread.sleep(3000);

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        webDriverWait.until(ExpectedConditions.urlContains("/thanks"));
        

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");
        Assert.assertTrue(status, "Failed to navigate to thanks page");

        // Go to the home page
        homePage.navigateToHome();
        Thread.sleep(3000);

        // Log out the user
        homePage.PerformLogout();

        logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ", status ? "PASS" : "FAIL");
        //takeScreenshot(driver, "EndTestCase", "TestCase05");

        //return status;
    }

     /*
     * Verify the quantity of items in cart can be updated
     */

     //prod1 - Xtend Smart Watch
     //prod2 - Yarine Floor Lamp

    @Test(description = "Verify that the contents of the cart can be edited", priority = 6, groups = "Regression_Test")
    @Parameters({"prod1", "prod2"})
    public void TestCase06(String prod1, String prod2) throws InterruptedException {
        //takeScreenshot(driver, "StartTestCase", "TestCase06");
        Boolean status;
        logStatus("Start TestCase", "Test Case 6: Verify that cart can be edited", "DONE");
        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

        // TODO: Register a new user

        registration.navigateToRegisterPage();
        
        status = registration.registerUser("shrutiUser", "Password", true);
        Assert.assertTrue(status, "Registration failed : Test Case Failure. Happy Flow Test Failed");

        /*
        if (!status) {
            logStatus("TestCase 6 ", "Test Case Failure. Happy Flow Test Failed", "FAIL");
        }
        */

        lastGeneratedUserName = registration.lastGeneratedUsername;

        // TODO: Login using the newly registed user
        login.navigateToLoginPage();
        
        status = login.PerformLogin(lastGeneratedUserName, "Password");

        assertTrue(status, "Failed to login with registered user");
        /*
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase", "Test Case 6: Happy Flow Test Failed : ", status ? "PASS" : "FAIL");
        }
        */

        homePage.navigateToHome();

        // TODO: Add "Xtend Smart Watch" to cart
        status = homePage.searchForProduct(prod1);
        Assert.assertTrue(status, "Failed to search product: Xtend Smart Watch");

        homePage.addProductToCart(prod1);
        

        // TODO: Add "Yarine Floor Lamp" to cart
        status = homePage.searchForProduct(prod2);
        Assert.assertTrue(status, "Failed to search product: Yarine Floor Lamp");

        homePage.addProductToCart(prod2);


        // update watch quantity to 2
        homePage.changeProductQuantityinCart(prod1, 2);

        // update table lamp quantity to 0
        homePage.changeProductQuantityinCart(prod2, 0);

        // update watch quantity again to 1
        homePage.changeProductQuantityinCart(prod1, 1);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = driver.getCurrentUrl().endsWith("/thanks");
        Assert.assertTrue(status, "Failed to navigate to thanks page");

        
        homePage.navigateToHome();
        Thread.sleep(3000);
        homePage.PerformLogout();

        logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ", status ? "PASS" : "FAIL");
        //takeScreenshot(driver, "EndTestCase", "TestCase06");
        //return status;
    }

    //prod3 - Stylecon 9 Seater RHS Sofa Set
    //qty - 10
    @Test(description = "Verify that insufficient balance error is thrown when the wallet balance is not enough", priority = 7, groups = "Sanity_test")
    @Parameters({"prod3", "qty"})
    public void TestCase07(String prod3, int qty) throws InterruptedException {
        //takeScreenshot(driver, "StartTestCase", "TestCase07");
        Boolean status;
        logStatus("Start TestCase",
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough",
                "DONE");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue(status, "Registration failed : Test Case Failure. Happy Flow Test Failed");
        /*
        if (!status) {
            logStatus("Step Failure", "User Perform Registration Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase",
                    "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                    status ? "PASS" : "FAIL");
                    takeScreenshot(driver, "Failed", "TestCase07");
            return false;
        }
        */
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        assertTrue(status, "Failed to login with registered user");
        /*
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase",
                    "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                    status ? "PASS" : "FAIL");
                    takeScreenshot(driver, "Failed", "TestCase07");
            return false;
        }
        */

        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("Stylecon");
        Assert.assertTrue(status, "Failed to search product: Stylecon");

        homePage.addProductToCart(prod3);
        Thread.sleep(3000);

        homePage.changeProductQuantityinCart(prod3, qty);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = checkoutPage.verifyInsufficientBalanceMessage();

        Assert.assertTrue(status, "Failed to throw insufficient balance error when the wallet balance is not enough");
        
        logStatus("End TestCase",
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                status ? "PASS" : "FAIL");
        //takeScreenshot(driver, "EndTestCase", "TestCase07");


        //return status;
    }

    @Test(description = "Verify that a product added to a cart is available when a new tab is added", priority = 8, groups = "Regression_Test")
    public void TestCase08() throws InterruptedException {
        Boolean status = false;

        logStatus("Start TestCase",
                "Test Case 8: Verify that product added to cart is available when a new tab is opened",
                "DONE");
        //takeScreenshot(driver, "StartTestCase", "TestCase09");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue(status, "Registration failed : Test Case Failure. Happy Flow Test Failed");
        /*
        if (!status) {
            logStatus("TestCase 8",
                    "Test Case Failure. Verify that product added to cart is available when a new tab is opened",
                    "FAIL");
            takeScreenshot(driver, "Failure", "TestCase09");
        }*/

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        Assert.assertTrue(status, "Failed to login with registered user");
        /*
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failure", "TestCase9");
            logStatus("End TestCase",
                    "Test Case 8:   Verify that product added to cart is available when a new tab is opened",
                    status ? "PASS" : "FAIL");
        }
        */

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX");
        Assert.assertTrue(status, "Failed to search product: Yonex");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");

        String currentURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);

        driver.get(currentURL);
        Thread.sleep(2000);

        List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
        status = homePage.verifyCartContents(expectedResult);
        Assert.assertTrue(status, "Verification of cart contents failed");

        driver.close();

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

        logStatus("End TestCase",
        "Test Case 8: Verify that product added to cart is available when a new tab is opened",
        status ? "PASS" : "FAIL");
        //takeScreenshot(driver, "EndTestCase", "TestCase08");

        //return status;
    }

    @Test(description = "Verify that privacy policy and about us links are working fine", priority = 9, groups = "Regression_Test")
    public void TestCase09() throws InterruptedException {
        Boolean status = false;

        logStatus("Start TestCase",
                "Test Case 09: Verify that the Privacy Policy, About Us are displayed correctly ",
                "DONE");
        //takeScreenshot(driver, "StartTestCase", "TestCase09");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue(status, "Registration failed : Test Case Failure. Happy Flow Test Failed");
        /*
        if (!status) {
            logStatus("TestCase 09",
                    "Test Case Failure.  Verify that the Privacy Policy, About Us are displayed correctly ",
                    "FAIL");
            takeScreenshot(driver, "Failure", "TestCase09");
        }*/
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        Assert.assertTrue(status, "Failed to login with registered user");
        /*
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failure", "TestCase09");
            logStatus("End TestCase",
                    "Test Case 9:    Verify that the Privacy Policy, About Us are displayed correctly ",
                    status ? "PASS" : "FAIL");
        }*/

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        String basePageURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        status = driver.getCurrentUrl().equals(basePageURL);

        Assert.assertTrue(status, "Verifying parent page url didn't change on privacy policy link click failed");
        /*
        if (!status) {
            logStatus("Step Failure", "Verifying parent page url didn't change on privacy policy link click failed", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failure", "TestCase09");
            logStatus("End TestCase",
                    "Test Case 9: Verify that the Privacy Policy, About Us are displayed correctly ",
                    status ? "PASS" : "FAIL");
        }*/

        SoftAssert softAssert = new SoftAssert();

        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
        WebElement PrivacyPolicyHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = PrivacyPolicyHeading.getText().equals("Privacy Policy");
        softAssert.assertTrue(status, "Verifying new tab opened has Privacy Policy page heading failed");
        /*
        if (!status) {
            logStatus("Step Failure", "Verifying new tab opened has Privacy Policy page heading failed", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failure", "TestCase9");
            logStatus("End TestCase",
                    "Test Case 9: Verify that the Privacy Policy, About Us are displayed correctly ",
                    status ? "PASS" : "FAIL");
        }*/

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
        driver.findElement(By.linkText("Terms of Service")).click();
        handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
        WebElement TOSHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = TOSHeading.getText().equals("Terms of Service");
        softAssert.assertTrue(status, "Verifying new tab opened has Terms Of Service page heading failed");
        /*
        if (!status) {
            logStatus("Step Failure", "Verifying new tab opened has Terms Of Service page heading failed", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failure", "TestCase9");
            logStatus("End TestCase",
                    "Test Case 9: Verify that the Privacy Policy, About Us are displayed correctly ",
                    status ? "PASS" : "FAIL");
        }*/

        driver.close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

        logStatus("End TestCase",
        "Test Case 9: Verify that the Privacy Policy, About Us are displayed correctly ",
        "PASS");
        //takeScreenshot(driver, "EndTestCase", "TestCase9");

        //return status;
    }

    //TC10_Name - crio user
    //TC10_email - criouser@gmail.com
    //TC10_message - Testing the contact us page
    @Test(description = "Verify that the contact us dialog works fine", priority = 10, groups = "Regression_Test")
    @Parameters({"TC10_Name", "TC10_email", "TC10_message"})
    public void TestCase10(String TC10_Name, String TC10_email, String TC10_message) throws InterruptedException {
        logStatus("Start TestCase",
                "Test Case 10: Verify that contact us option is working correctly ",
                "DONE");
        //takeScreenshot(driver, "StartTestCase", "TestCase10");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        driver.findElement(By.xpath("//*[text()='Contact us']")).click();

        WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        name.sendKeys(TC10_Name);
        WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        email.sendKeys(TC10_email);
        WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        message.sendKeys(TC10_message);

        WebElement contactUs = driver.findElement(
                By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));

        contactUs.click();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.invisibilityOf(contactUs));

        logStatus("End TestCase",
                "Test Case 10: Verify that contact us option is working correctly ",
                "PASS");

        //takeScreenshot(driver, "EndTestCase", "TestCase10");

        //return true;
        Assert.assertTrue(true);
    }

    //TC5_ProductNameToSearchFor - YONEX Smash Badminton Racquet
    //TC5_AddressDetails - Addr line 1  addr Line 2  addr line 3
    @Test(description = "Ensure that the Advertisement Links on the QKART page are clickable", priority = 11, groups = "Sanity_test")
    @Parameters({"TC5_ProductNameToSearchFor", "TC5_AddressDetails"})
    public void TestCase11(String TC5_ProductNameToSearchFor, String TC5_AddressDetails) throws InterruptedException {
        Boolean status = false;
        logStatus("Start TestCase",
                "Test Case 11: Ensure that the links on the QKART advertisement are clickable",
                "DONE");
        //takeScreenshot(driver, "StartTestCase", "TestCase11");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        Assert.assertTrue(status, "Registration failed : Test Case Failure. Happy Flow Test Failed");
        /*
        if (!status) {
            logStatus("TestCase 11",
                    "Test Case Failure. Ensure that the links on the QKART advertisement are clickable",
                    "FAIL");
            takeScreenshot(driver, "Failure", "TestCase11");
        }*/
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        Assert.assertTrue(status, "Login failed");
        /*
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "Failure", "TestCase 11");
            logStatus("End TestCase",
                    "Test Case 11:  Ensure that the links on the QKART advertisement are clickable",
                    status ? "PASS" : "FAIL");
        }*/

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct(TC5_ProductNameToSearchFor);
        Assert.assertTrue(status, "Failed to search product: YONEX Smash Badminton Racquet");
        homePage.addProductToCart(TC5_ProductNameToSearchFor);
        homePage.changeProductQuantityinCart(TC5_ProductNameToSearchFor, 1);
        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress(TC5_AddressDetails);
        checkoutPage.selectAddress(TC5_AddressDetails);
        checkoutPage.placeOrder();
        Thread.sleep(3000);

        String currentURL = driver.getCurrentUrl();

        List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));

        status = Advertisements.size() == 3;
        Assert.assertTrue(status, "Failed to verify that 3 Advertisements are available");
       // logStatus("Step ", "Verify that 3 Advertisements are available", status ? "PASS" : "FAIL");

        WebElement Advertisement1 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[1]"));
        driver.switchTo().frame(Advertisement1);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(status, "Failed to verify that ad 1 is clickable");
       // logStatus("Step ", "Verify that Advertisement 1 is clickable ", status ? "PASS" : "FAIL");

        driver.get(currentURL);
        Thread.sleep(3000);

        WebElement Advertisement2 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[2]"));
        driver.switchTo().frame(Advertisement2);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        softAssert.assertTrue(status, "Failed to verify that ad 2 is clickable");
        //logStatus("Step ", "Verify that Advertisement 2 is clickable ", status ? "PASS" : "FAIL");

        logStatus("End TestCase",
                "Test Case 11:  Ensure that the links on the QKART advertisement are clickable",
                status ? "PASS" : "FAIL");
        //return status;
    }


    @AfterSuite
    public static void quitDriver() {
        System.out.println("quit()");
        driver.quit();
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s", String.valueOf(java.time.LocalDateTime.now()), type,
                message, status));
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        try {
            File theDir = new File("/screenshots");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            String timestamp = String.valueOf(java.time.LocalDateTime.now());
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType, description);
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("screenshots/" + fileName);
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

