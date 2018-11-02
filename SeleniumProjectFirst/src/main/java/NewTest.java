import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class NewTest {
	public String baseUrl = "https://amzn.to/2RZDlE6";
	String driverPath = "C:\\geckodriver.exe";
	public String blogUrl = "http://localhost:70/wordpress/wp-admin";
	public String newPostUrl = "http://localhost:70/wordpress/wp-admin/post-new.php";
	public WebDriver driver;

	@Test
	public void verifyHomepageTitle() {

		System.out.println("launching firefox browser");
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Shubham Arora\\Downloads\\chromedriver.exe");
		driver = new ChromeDriver();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		driver.manage().window().maximize();
		driver.get(baseUrl);
		
		String expectedTitle = "Welcome: Mercury Tours";
		WebElement signInHover = driver.findElement(By.id("nav-link-yourAccount"));
		signInHover.click();
		WebElement emailTextField = driver.findElement(By.id("ap_email"));
		emailTextField.sendKeys("allpdfnotes@gmail.com");
		driver.findElement(By.id("continue")).click();
		driver.findElement(By.id("ap_password")).sendKeys("shambhu2");
		driver.findElement(By.id("signInSubmit")).click();
		
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user_login")));
		
		//String linkText = "http://abc.com";
		
		//Getting old and new price
		//wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#price > table > tbody > tr:nth-child(1) > td.a-span12.a-color-secondary.a-size-base > span.a-text-strike")));
		String oldPrice = null;
		if(driver.findElements(By.cssSelector("#price > table > tbody > tr:nth-child(1) > td.a-span12.a-color-secondary.a-size-base > span.a-text-strike")).size() > 0
				&& driver.findElement(By.cssSelector("#price > table > tbody > tr:nth-child(1) > td.a-span12.a-color-secondary.a-size-base > span.a-text-strike")).isDisplayed()) {
			oldPrice = driver.findElement(By.cssSelector("#price > table > tbody > tr:nth-child(1) > td.a-span12.a-color-secondary.a-size-base > span.a-text-strike")).getText();
		}
		else if(driver.findElements(By.cssSelector("#priceblock_ourprice")).size() > 0
				&& driver.findElement(By.cssSelector("#priceblock_ourprice")).isDisplayed())
		{
			oldPrice = driver.findElement(By.cssSelector("#priceblock_ourprice")).getText();
		}
		 
		
		String newPrice = null;
		
		if(driver.findElements(By.cssSelector("#priceblock_dealprice")).size() > 0
				&& driver.findElement(By.cssSelector("#priceblock_dealprice")).isDisplayed()) {
			newPrice = driver.findElement(By.cssSelector("#priceblock_dealprice")).getText();
		} 
		else if( driver.findElements(By.cssSelector("#priceblock_ourprice")).size() > 0
				&& driver.findElement(By.cssSelector("#priceblock_ourprice")).isDisplayed()) {
			newPrice = driver.findElement(By.cssSelector("#priceblock_ourprice")).getText();
		}
		oldPrice = oldPrice.replaceAll(",", "");
		newPrice = newPrice.replaceAll(",", "");
		Double oldPriceValue = Double.parseDouble(oldPrice);
		Double newPriceValue = Double.parseDouble(newPrice);
		newPrice = newPrice.replaceAll("\\s+","");
		
		// Fetching affilate link
		driver.findElement(By.cssSelector("#amzn-ss-text-link > span > span > strong > a")).click();
		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//*[@id=\"amzn-ss-full-link-radio-button\"]/label/i")));
		driver.findElement(By.xpath("//*[@id=\"amzn-ss-full-link-radio-button\"]/label/i")).click();
		WebElement link = driver.findElement(By.id("amzn-ss-text-fulllink-textarea"));
		String linkText = link.getText();
		
		StringBuffer s = new StringBuffer();
		
		// Getting actual title and adding price at end
		String actualTitle = driver.findElement(By.id("productTitle")).getText();
		String newTitle = null;
		newTitle = "Buy " + actualTitle + " at Rs. " + newPriceValue.intValue() + " at Amazon";
		
		// Assert.assertEquals(actualTitle, expectedTitle);
		System.out.println("Link = " + linkText);

		if (driver.findElements(By.id("featurebullets_feature_div")).size() != 0) {
			s.append(driver.findElement(By.id("featurebullets_feature_div")).getText());
		}
		
		// Making changes to the description now
		
		s.insert(0, "<ul><li>");
		s.append("</li></ul>");
		while(s.indexOf("\n") != -1) {
			int index = s.indexOf("\n");
			s.replace(index, index+1, "</li><li>");
		}

		/**
		 * Posting the deal now.
		 */
		driver.get(blogUrl);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user_login")));
		driver.findElement(By.id("user_login")).sendKeys("shubham");
		driver.findElement(By.id("user_pass")).sendKeys("shambhu2");
		driver.findElement(By.id("wp-submit")).click();

		driver.get(newPostUrl);
		
		// Adding post title and body
		driver.findElement(By.name("post_title")).sendKeys(newTitle);
		driver.findElement(By.id("content-html")).click();
		driver.findElement(By.className("wp-editor-area")).sendKeys(s);
		
		//Adding offer url
		driver.findElement(By.id("rehub_offer_product_url")).sendKeys(linkText);
		
		// Adding Content Egg 
		WebElement keywordTextField = driver.findElement(By.cssSelector("#content-egg > div > div:nth-child(5) > div.ng-isolate-scope > div > div.tab-pane.ng-scope.active > div.search_panel.ng-scope > div > div.col-md-11.col-lg-5 > div > input"));
		keywordTextField.sendKeys(actualTitle.split("-")[0]);
		if(driver.findElement(By.cssSelector("#content-egg > div > div:nth-child(5) > div.ng-isolate-scope > div > div.tab-pane.ng-scope.active > div.search_panel.ng-scope > div > div.col-md-11.col-lg-5 > div > div > button:nth-child(1)")).isDisplayed()) {
			// Search Button
			driver.findElement(By.cssSelector("#content-egg > div > div:nth-child(5) > div.ng-isolate-scope > div > div.tab-pane.ng-scope.active > div.search_panel.ng-scope > div > div.col-md-11.col-lg-5 > div > div > button:nth-child(1)")).click();
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(driver.findElement(By.cssSelector("#content-egg > div > div:nth-child(5) > div.ng-isolate-scope > div > div.tab-pane.ng-scope.active > div.search_panel.ng-scope > div > div.col-sm-1.text-right > a")).isDisplayed()) {
			// Add all button
			driver.findElement(By.cssSelector("#content-egg > div > div:nth-child(5) > div.ng-isolate-scope > div > div.tab-pane.ng-scope.active > div.search_panel.ng-scope > div > div.col-sm-1.text-right > a")).click();
		}
		
		// Adding Old and New Price
		
		WebElement offerOldPriceTextField = driver.findElement(By.xpath("//*[@id=\"rehub_offer_product_price_old\"]"));
		WebElement offerSalePriceTextField = driver.findElement(By.xpath("//*[@id=\"rehub_offer_product_price\"]"));
		offerOldPriceTextField.sendKeys("&#8377; "+oldPriceValue);
		offerSalePriceTextField.sendKeys("&#8377; "+newPriceValue);
		
		// Clicking on publish
		//WebElement publishButton = driver.findElement(By.cssSelector("#publish"));
		js.executeScript("document.getElementById('publish').click()");
		
		//driver.findElement(By.cssSelector("#publish")).click();
		String linkFromBlog = driver.findElement(By.id("sample-permalink")).getText();
		System.out.println(s);
		System.out.println("Link from blog: " + linkFromBlog);

		// featurebullets_feature_div

		//driver.close();
	}
}