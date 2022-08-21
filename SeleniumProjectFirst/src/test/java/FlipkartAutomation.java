import org.testng.annotations.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;

public class FlipkartAutomation {
	String driverPath = "C:\\geckodriver.exe";
	public String blogUrl = "http://nonstopdeals.in/wp-admin";
	public String newPostUrl = "http://nonstopdeals.in/wp-admin/post-new.php";
	public WebDriver driver = null;
	public JavascriptExecutor js = null;
	public WebDriverWait wait = null;
	String xpath = "xpath";
	String css = "css";
	String id = "id";
	String name = "name";
	String className = "className";

	public WebElement getElementIfExist(String property, String propertyType) throws NoSuchElementException {
		// System.out.println("Getting WebElement: " + property);
		if (propertyType.equalsIgnoreCase("id")) {
			if (driver.findElements(By.id(property)).size() > 0) {
				WebElement element = driver.findElement(By.id(property));
				return element;
			}
		} else if (propertyType.equalsIgnoreCase("css")) {
			if (driver.findElements(By.cssSelector(property)).size() > 0) {
				WebElement element = driver.findElement(By.cssSelector(property));
				return element;
			}
		} else if (propertyType.equalsIgnoreCase("xpath")) {
			if (driver.findElements(By.xpath(property)).size() > 0) {
				WebElement element = driver.findElement(By.xpath(property));
				return element;
			}
		} else if (propertyType.equalsIgnoreCase("name")) {
			if (driver.findElements(By.name(property)).size() > 0) {
				WebElement element = driver.findElement(By.name(property));
				return element;
			}
		} else if (propertyType.equalsIgnoreCase("className")) {
			if (driver.findElements(By.className(property)).size() > 0) {
				WebElement element = driver.findElement(By.className(property));
				return element;
			}
		}
		return null;
	}

	public void clickElement(WebElement element) {
		// System.out.println("Clicking element: " + element.getText());
		try {
			if (element.isDisplayed()) {
				wait.until(ExpectedConditions.elementToBeClickable(element));
				element.click();
			}
		} catch (Exception e) {
			js.executeScript("document.getElementById('" + element.getAttribute("id") + "').click()");
		}
	}

	public void addContentEggInPost(String actualTitle) {
		WebElement keywordTextField = getElementIfExist(
				"#content-egg > div > div:nth-child(5) > div.ng-isolate-scope > div > div.tab-pane.ng-scope.active > div.search_panel.ng-scope > div > div.col-md-11.col-lg-5 > div > input",
				css);
		if (null != keywordTextField) {

			String[] splittedTitle = actualTitle.split(" ");

			StringBuffer titleToSearch = new StringBuffer();
			int wordCount = 0;
			for (int i = 0; i < splittedTitle.length; i++) {
				if ((splittedTitle[i].contains("-") || splittedTitle[i].contains("(") || splittedTitle[i].contains(","))
						&& wordCount > 5) {
					break;
				} else {
					if (wordCount > 5) {
						break;
					}
					wordCount++;
					titleToSearch.append(splittedTitle[i] + " ");
				}
			}

			keywordTextField.sendKeys(titleToSearch);
		}

		WebElement searchButton = getElementIfExist(
				"#content-egg > div > div:nth-child(5) > div.ng-isolate-scope > div > div.tab-pane.ng-scope.active > div.search_panel.ng-scope > div > div.col-md-11.col-lg-5 > div > div > button:nth-child(1)",
				css);
		clickElement(searchButton);
		// wait.until(ExpectedConditions.);
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebElement addAllButton = getElementIfExist(
				"#content-egg > div > div:nth-child(5) > div.ng-isolate-scope > div > div.tab-pane.ng-scope.active > div.search_panel.ng-scope > div > div.col-sm-1.text-right > a",
				css);
		clickElement(addAllButton);

	}

	public void logIntoBlog(String username, String password) {
		driver.findElement(By.id("user_login")).sendKeys(username);
		driver.findElement(By.id("user_pass")).sendKeys(password);
		driver.findElement(By.id("wp-submit")).click();
	}

	private Map<String, String> getAttributesFromFlipkart() {
		Map<String, String> mapToReturn = new HashMap<String, String>();
		String oldPrice = null;
		String newPrice = null;
		String actualTitle = null;
		String imageURL = null;
		StringBuffer features = new StringBuffer();

		oldPrice = (String) js.executeScript("return document.getElementsByClassName('_3auQ3N _1POkHg')[0].innerText");
		newPrice = (String) js.executeScript("return document.getElementsByClassName('_1vC4OE _3qQ9m1')[0].innerText");
		actualTitle = (String) js.executeScript("return document.getElementsByClassName('_35KyD6')[0].innerText");
		imageURL = (String) js.executeScript(
				"return document.getElementsByClassName('_1Nyybr Yun65Y _30XEf0')[0].getAttribute('src')");
		features.append((String) js.executeScript("return document.getElementsByClassName('_3WHvuP')[0].innerText"));
		features.insert(0, "<ul><li>");
		features.append("</li></ul>");
		while (features.indexOf("\n") != -1) {
			int index = features.indexOf("\n");
			features.replace(index, index + 1, "</li><li>");
		}

		oldPrice = oldPrice.replaceAll(",", "");
		newPrice = newPrice.replaceAll(",", "");
		oldPrice = oldPrice.substring(1, oldPrice.length());
		newPrice = newPrice.substring(1, newPrice.length());

		mapToReturn.put("oldPrice", oldPrice);
		mapToReturn.put("newPrice", newPrice);
		mapToReturn.put("postTitle", actualTitle);
		mapToReturn.put("features", features.toString());
		mapToReturn.put("imageURL", imageURL);
		return mapToReturn;
	}

	public void postDeal(Map<String, String> returnMap, String zongoyURL) {
		Double oldPriceValue = (double) 0;
		Double newPriceValue = (double) 0;
		String newPrice = null;
		if (returnMap.get("oldPrice") != null) {
			oldPriceValue = Double.parseDouble(returnMap.get("oldPrice"));
		}

		if (returnMap.get("newPrice").contains("-")) {
			newPrice = returnMap.get("newPrice");
		} else {
			String newPriceForParsing = returnMap.get("newPrice");
			newPriceForParsing = newPriceForParsing.replaceAll(",", "");
			newPriceValue = Double.parseDouble(newPriceForParsing);
		}

		String actualTitle = returnMap.get("postTitle");
		String features = returnMap.get("features");
		String newTitle = null;
		newTitle = /* "Buy " + */ actualTitle + " at Rs. " + newPriceValue.intValue() + " @ Flipkart";

		driver.get(newPostUrl);

		WebElement postTitle = getElementIfExist("post_title", name);
		if (null != postTitle) {
			postTitle.sendKeys(newTitle);
		}

		WebElement contentHTML = getElementIfExist("content-html", id);
		if (null != contentHTML) {
			clickElement(contentHTML);
		}
		js.executeScript("document.getElementsByClassName('wp-editor-area')[0].value = \"" + features + "\"");

		//addContentEggInPost(actualTitle);

		WebElement offerOldPriceTextField = getElementIfExist("//*[@id=\"rehub_offer_product_price_old\"]", xpath);
		WebElement offerSalePriceTextField = getElementIfExist("//*[@id=\"rehub_offer_product_price\"]", xpath);
		if (null != offerOldPriceTextField && null != offerSalePriceTextField) {
			if (oldPriceValue != 0) {
				offerOldPriceTextField.sendKeys("&#8377; " + oldPriceValue);
			}
			if (newPrice == null) {
				offerSalePriceTextField.sendKeys("&#8377; " + newPriceValue);
			} else {
				offerSalePriceTextField.sendKeys("&#8377; " + newPrice);
			}
		}

		WebElement productURL = getElementIfExist("rehub_offer_product_url", id);
		if (null != productURL) {
			productURL.sendKeys(zongoyURL);
		}

		WebElement imageURLElement = getElementIfExist("knawatfibu_url", id);
		if (null != imageURLElement && null != returnMap.get("imageURL")) {
			js.executeScript("document.getElementById('knawatfibu_url').value = '" + returnMap.get("imageURL") + "'");
			// imageURLElement.sendKeys(returnMap.get("imageURL"));
		}

		WebElement publishButton = getElementIfExist("publish", id);
		clickElement(publishButton);

	}

	public void postOnTelegram(Map<String, String> returnMap) {
		Double oldPriceValue = (double) 0;
		Double newPriceValue = (double) 0;
		String newPrice = null;
		if (returnMap.get("oldPrice") != null) {
			oldPriceValue = Double.parseDouble(returnMap.get("oldPrice"));
		}

		if (returnMap.get("newPrice").contains("-")) {
			newPrice = returnMap.get("newPrice");
		} else {
			String newPriceForParsing = returnMap.get("newPrice");
			newPriceForParsing = newPriceForParsing.replaceAll(",", "");
			newPriceValue = Double.parseDouble(newPriceForParsing);
		}
		String actualTitle = returnMap.get("postTitle");

		String newTitle = /* "Buy " + */actualTitle + " at Rs. " + newPriceValue.intValue() + " @ Flipkart";

		newTitle = newTitle + "(Original Price Rs. " + oldPriceValue.intValue() + ")";

		WebElement postOnTelCheckBox = getElementIfExist("telegram_m_send", id);
		clickElement(postOnTelCheckBox);

		WebElement shortLinkElement = getElementIfExist("shortlink", id);
		String shortLink = null;
		if (shortLinkElement != null) {
			shortLink = shortLinkElement.getAttribute("value");
		}
		WebElement telMessageBody = getElementIfExist("telegram_m_send_content", id);
		telMessageBody.clear();
		telMessageBody.sendKeys(newTitle);
		telMessageBody.sendKeys("\n \n");
		telMessageBody.sendKeys(shortLink);
		WebElement publishButton = getElementIfExist("publish", id);
		clickElement(publishButton);
	}

	@Test
	public void f() {
		try {
			System.out.println("Enter flipkart deal URL: ");
			Scanner sc = new Scanner(System.in);
			String flipkartDealUrl = sc.nextLine();
			/*System.out.println("Enter zingoy url: ");
			String zingoyURL = sc.nextLine();*/
			driver.get(flipkartDealUrl);
			Map<String, String> returnMap = getAttributesFromFlipkart();
			postDeal(returnMap, flipkartDealUrl);
			postOnTelegram(returnMap);
			f();
		} catch (Exception e) {
			f();
		}
	}

	@BeforeTest
	public void beforeTest() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Shubham Arora\\Downloads\\chromedriver.exe");
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
		wait = new WebDriverWait(driver,  java.time.Duration.ofSeconds(20));

		driver.manage().window().maximize();

		driver.get(newPostUrl);
		logIntoBlog("scriptUser", "8UXh6ki#NvGdmI4p*P8)9vFE");

	}

	private void loginIntoZingoy(String username, String password) {
		// driver.get(arg0);
		driver.get("https://www.zingoy.com/");
		WebElement loginElement = getElementIfExist("#top-bar > div.bgdprimary > div > ul > li:nth-child(6) > a", css);
		clickElement(loginElement);

		js.executeScript("document.getElementById('login_form_id').value = '" + username + "';");
		js.executeScript("document.getElementById('password_form_id').value = '" + password + "';");

		System.out.println("Please press click button after verifying capcha");
	}

}
