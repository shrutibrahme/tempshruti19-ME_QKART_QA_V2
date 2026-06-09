package QKART_SANITY_LOGIN.Module1;

import java.util.List;
import javax.xml.xpath.XPath;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Checkout {
    RemoteWebDriver driver;
    String url = "https://qkart-qa-web.labs.crio.do/checkout";

    public Checkout(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToCheckout() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    /*
     * Return Boolean denoting the status of adding a new address
     */
    public Boolean addNewAddress(String addresString) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Click on the "Add new address" button, enter the addressString in the address
             * text box and click on the "ADD" button to save the address
             */
            driver.findElement(By.id("add-new-btn")).click();
            Thread.sleep(1000);
            //driver.findElement(By.xpath("//div[@class = 'MuiInputBase-root MuiOutlinedInput-root MuiInputBase-colorPrimary MuiInputBase-formControl MuiInputBase-multiline css-1j3d9ys-MuiInputBase-root-MuiOutlinedInput-root']")).sendKeys(addresString);
            driver.findElement(By.xpath("//textarea[@class = 'MuiInputBase-input MuiOutlinedInput-input MuiInputBase-inputMultiline css-1sqnrkk-MuiInputBase-input-MuiOutlinedInput-input']")).sendKeys(addresString);
            Thread.sleep(1000);

            driver.findElement(By.xpath("//button[text() = 'Add']")).click();
            Thread.sleep(1000);

            //return driver.findElement(By.xpath("//p[text() = ' Delete ']")).isDisplayed(); no need
            WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class = 'address-item not-selected MuiBox-root css-0']")));
            return true;
            
        } catch (Exception e) {
            System.out.println("Exception occurred while entering address: " + e.getMessage());
            return false;

        }
    }

    /*
     * Return Boolean denoting the status of selecting an available address
     */
    public Boolean selectAddress(String addressToSelect) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through all the address boxes to find the address box with matching
             * text, addressToSelect and click on it
             */
            Thread.sleep(3000);
            List<WebElement> listOfAddresses = driver.findElements(By.xpath("//div[@class = 'address-item not-selected MuiBox-root css-0']"));
            for (WebElement findAddress : listOfAddresses) {
                System.out.println(findAddress.getText());
                if (findAddress.getText().contains(addressToSelect)) {
                    System.out.println("Address found");
                    findAddress.click();
                    return findAddress.isSelected();
                }
            }

            System.out.println("Unable to find the given address");
            return false;
        } catch (Exception e) {
            System.out.println("Exception Occurred while selecting the given address: " + e.getMessage());
            return false;
        }

    }

    /*
     * Return Boolean denoting the status of place order action
     */
    public Boolean placeOrder() {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find the "PLACE ORDER" button and click on it

            WebElement placeOrderButton = driver.findElement(By.xpath("//button[text() ='PLACE ORDER']"));

            placeOrderButton.click();
            
            return true;

        } catch (Exception e) {
            System.out.println("Exception while clicking on PLACE ORDER: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the insufficient balance message is displayed
     */
    public Boolean verifyInsufficientBalanceMessage() {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 07: MILESTONE 6
            Thread.sleep(1000);
            String errorMessage = driver.findElement(By.id("notistack-snackbar")).getText();
            
            if(errorMessage.equals("You do not have enough balance in your wallet for this purchase")){
                return true;
            }
            return false;

        } catch (Exception e) {
            System.out.println("Exception while verifying insufficient balance message: " + e.getMessage());
            return false;
        }
    }
}
