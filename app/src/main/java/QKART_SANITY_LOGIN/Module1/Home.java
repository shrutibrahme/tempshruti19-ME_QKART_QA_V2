package QKART_SANITY_LOGIN.Module1;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    RemoteWebDriver driver;
    String url = "https://qkart-qa-web.labs.crio.do";

    public Home(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToHome() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    public Boolean PerformLogout() throws InterruptedException {
        try {
            // Find and click on the Logout Button
            WebElement logout_button = driver.findElement(By.className("MuiButton-text"));
            logout_button.click();

            // SLEEP_STMT_10: Wait for Logout to complete
            // Wait for Logout to Complete
            Thread.sleep(3000);

            return true;
        } catch (Exception e) {
            // Error while logout
            return false;
        }
    }

    /*
     * Returns Boolean if searching for the given product name occurs without any
     * errors
     */
    public Boolean searchForProduct(String product) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Clear the contents of the search box and Enter the product name in the search
            // box
            WebElement searchBox= driver.findElement(By.name("search"));
            searchBox.clear();
            searchBox.sendKeys(product);
            Thread.sleep(5000);
            return true;
        } catch (Exception e) {
            System.out.println("Error while searching for a product: " + e.getMessage());
            return false;
        }
    }

    /*
     * Returns Array of Web Elements that are search results and return the same
     */
    public List<WebElement> getSearchResults() {
        List<WebElement> searchResults = new ArrayList<WebElement>() {
        };
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Find all webelements corresponding to the card content section of each of
            // search results
            searchResults = driver.findElements(By.xpath("//*[@id='root']/div/div/div[3]/div[1]/div[2]/div/div"));
            //searchResults = driver.findElements(By.xpath("//*[@id='root']/div/div/div[3]/div[1]/div[2]/div/div/div[1]/p[1]"));
            return searchResults;
        } catch (Exception e) {
            System.out.println("There were no search results: " + e.getMessage());
            return searchResults;

        }
    }

    /*
     * Returns Boolean based on if the "No products found" text is displayed
     */
    public Boolean isNoResultFound() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Check the presence of "No products found" text in the web page. Assign status
            // = true if the element is *displayed* else set status = false
            WebElement noProd = driver.findElement(By.xpath("//h4[text() = ' No products found ']"));
            if (noProd.getText().contains("No products found")) {
                status = true;      
            } else {
                status = false;
            }

            ///use isdisplayed method no need of contains

            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if add product to cart is successful
     */
    public Boolean addProductToCart(String productName) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through each product on the page to find the WebElement corresponding
             * to the matching productName
             * 
             * Click on the "ADD TO CART" button for that element
             * 
             * Return true if these operations succeeds
             */
<<<<<<< ours
            for (WebElement eachSearchResult : getSearchResults()) {
                if(eachSearchResult.getText().contains(productName)){
                    System.out.println(eachSearchResult.getText());
                    eachSearchResult.findElement(By.xpath("//button[text() = 'Add to cart']")).click();
                    System.out.println("Found the "+ productName +" and clicked on add to cart");
                    return true;
                }
            }
            System.out.println("Unable to find the given product");
            return false;

=======
            System.out.println("Unable to find the given product");
            return false;
>>>>>>> theirs
        } catch (Exception e) {
            System.out.println("Exception while performing add to cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting the status of clicking on the checkout button
     */
    public Boolean clickCheckout() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find and click on the the Checkout button
            driver.findElement(By.xpath("//button[text() = 'Checkout']")).click();
            status = true;
            return status;
        } catch (Exception e) {
            System.out.println("Exception while clicking on Checkout: " + e.getMessage());
            return status;
        }
    }



   /* public Boolean changeProductQuantityinCart(String productName, int quantity) {
        try {
            // 1. Find all rows in the cart
            List<WebElement> cartRows = driver.findElements(By.className("css-1gjj37g"));
    
            for (WebElement row : cartRows) {
                // 2. Find the row matching the product name
                if (row.getText().contains(productName)) {
                    
                    // 3. Get current quantity (usually inside a div between the - and + buttons)
                    // XPath finds the div containing the number
                    WebElement currentQtyElement = row.findElement(By.xpath(".//div[@data-testid='item-qty']"));
                    int currentQty = Integer.parseInt(currentQtyElement.getText());
    
                    // 4. Click buttons until target quantity is reached
                    while (currentQty != quantity) {
                        if (currentQty < quantity) {
                            // Click Plus
                            row.findElement(By.xpath(".//button[contains(@aria-label, 'Add')]")).click();
                        } else {
                            // Click Minus
                            row.findElement(By.xpath(".//button[contains(@aria-label, 'Remove')]")).click();
                        }
    
                        // 5. CRITICAL: Wait for the UI to update the number
                        Thread.sleep(1000); 
                        
                        // Re-read the quantity. If item was removed (qty 0), it might throw an exception
                        try {
                            currentQty = Integer.parseInt(currentQtyElement.getText());
                        } catch (Exception e) {
                            // If element is gone, we assume it's removed (0)
                            currentQty = 0;
                        }
                    }
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }*/

    /*
     * Return Boolean denoting the status of change quantity of product in cart
     * operation
     */
<<<<<<< ours
    public Boolean   changeProductQuantityinCart(String productName, int quantity) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

            // Find the item on the cart with the matching productName
            // Increment or decrement the quantity of the matching product until the current
            // quantity is reached (Note: Keep a look out when then input quantity is 0,
            // here we need to remove the item completely from the cart)

            List<WebElement> cartListElements =driver.findElements(By.xpath("//div[@class = 'MuiBox-root css-1gjj37g']"));
            
            for (WebElement eachCartElement : cartListElements) {

                System.out.println(eachCartElement.getText());

                WebElement titleElement = eachCartElement.findElement(By.xpath("./div[1]"));
                String title = titleElement.getText();


                if(title.equals(productName)){

                    while(true){
                   WebElement currentQtyElement = eachCartElement.findElement(By.xpath(".//div[@data-testid = 'item-qty']"));
                   String elementQty = currentQtyElement.getText();
                   int currentIntQuantity =Integer.parseInt(elementQty);

                    if(currentIntQuantity < quantity){
                        eachCartElement.findElement(By.xpath(".//*[@data-testid = 'AddOutlinedIcon']")).click();
                        Thread.sleep(5000);
                    }
                    else if (currentIntQuantity > quantity) {
                        eachCartElement.findElement(By.xpath(".//*[@data-testid = 'RemoveOutlinedIcon']")).click();
                        Thread.sleep(5000);
                    }
                    else if (currentIntQuantity == quantity) {
                        break;
                    }      
                   }
                }
            }
=======
    public Boolean changeProductQuantityinCart(String productName, int quantity) {
        try {



>>>>>>> theirs

            return false;
        } catch (Exception e) {
            if (quantity == 0)
                return true;
            System.out.println("exception occurred when updating cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the cart contains items as expected
     */
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            WebElement cartParent = driver.findElement(By.className("cart"));
            List<WebElement> cartContents = cartParent.findElements(By.className("css-zgtx0t"));

            ArrayList<String> actualCartContents = new ArrayList<String>() {
            };
            for (WebElement cartItem : cartContents) {
                actualCartContents.add(cartItem.findElement(By.className("css-1gjj37g")).getText().split("\n")[0]);
            }

            for (String expected : expectedCartContents) {
                if (!actualCartContents.contains(expected)) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            System.out.println("Exception while verifying cart contents: " + e.getMessage());
            return false;
        }
    }
}
