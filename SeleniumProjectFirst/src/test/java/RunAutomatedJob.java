import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class RunAutomatedJob {
	public String baseUrl = "https://goo.gl/mD6fv2";
	String driverPath = "C:\\geckodriver.exe";
	// public String newPostUrl = "http://nonstopdeals.in/wp-admin/post-new.php";
	public String newPostUrl = "http://localhost:70/wordpress/wp-admin/post-new.php";
	public WebDriver driver = null;
	public JavascriptExecutor js = null;
	public WebDriverWait wait = null;
	String xpath = "xpath";
	String css = "css";
	String id = "id";
	String name = "name";
	String className = "className";
	String lightningDeal = "LightningDeal";
	String specialDealKey = "specialDealKey";

	static {
	}

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

	/*
	 * public boolean checkIfElementExists(String property, String propertyType) {
	 * 
	 * }
	 */

	public Map<String, String> getFieldsFromAmazon() throws Exception {
		Map<String, String> mapToReturn = new HashMap<String, String>();
		String oldPrice = null;
		String newPrice = null;
		String actualTitle = null;
		String specialDealType = null;
		String imageURL = null;
		String dealPercentage = null;
		StringBuffer features = new StringBuffer();
		String couponDiscount = null;
		WebElement oldPriceElement = getElementIfExist(
				"#price > table > tbody > tr:nth-child(1) > td.a-span12.a-color-secondary.a-size-base > span.a-text-strike",
				css);
		if (oldPriceElement != null) {
			oldPrice = oldPriceElement.getText();
		} else {
			oldPriceElement = getElementIfExist("#priceblock_ourprice", css);
			if (oldPriceElement != null) {
				oldPrice = oldPriceElement.getText();
			}
		}

		WebElement newPriceElement = getElementIfExist("#priceblock_dealprice", css);
		if (newPriceElement != null) {
			newPrice = newPriceElement.getText();
		} else if (getElementIfExist("#priceblock_saleprice", css) != null) {
			newPriceElement = getElementIfExist("#priceblock_saleprice", css);
			newPrice = newPriceElement.getText();
		} else {
			newPriceElement = getElementIfExist("#priceblock_ourprice", css);
			if (newPriceElement != null) {
				newPrice = newPriceElement.getText();
			}
		}

		WebElement applyCouponDom = getElementIfExist("//*[@id=\"vpcButton\"]/div/label/span", xpath);

		if (applyCouponDom != null) {
			couponDiscount = applyCouponDom.getText();
		}

		if (getElementIfExist("goldboxBuyBox", id) != null) {
			specialDealType = lightningDeal;

			long dealPercentageDomSize = 0;
			dealPercentageDomSize = (Long) js.executeScript(
					"return document.getElementsByClassName('a-size-small a-color-base a-text-bold').length");

			if (dealPercentageDomSize > 1 && couponDiscount != null) {
				dealPercentage = (String) js.executeScript(
						"return document.getElementsByClassName('a-size-small a-color-base a-text-bold')[1].innerText;");
			} else if (dealPercentageDomSize > 0) {
				dealPercentage = (String) js.executeScript(
						"return document.getElementsByClassName('a-size-small a-color-base a-text-bold')[0].innerText;");
			}
		}

		String affilateLink = baseUrl;// fetchAffilateLink();

		WebElement titleElement = getElementIfExist("productTitle", id);
		if (titleElement != null) {
			actualTitle = titleElement.getText();
		}

		try {
			Thread.sleep(1000);
		} catch (Exception e) {

		}
		WebElement imageURLElement = getElementIfExist("//*[@id=\"landingImage\"]", xpath);

		if (imageURLElement != null) {
			imageURL = imageURLElement.getAttribute("src");
		}

		if (imageURLElement != null) {
			imageURL = imageURLElement.getAttribute("src");
		}

		WebElement featuresElement = getElementIfExist("featurebullets_feature_div", id);
		if (null != featuresElement) {
			features.append(featuresElement.getText());
			features.insert(0, "<ul><li>");
			features.append("</li></ul>");
			while (features.indexOf("\n") != -1) {
				int index = features.indexOf("\n");
				features.replace(index, index + 1, "</li><li>");
			}
		}

		if (null != oldPrice) {
			oldPrice = oldPrice.replaceAll(",", "");
		}

		if (null != newPrice) {
			newPrice = newPrice.replaceAll(",", "");
		}

		/**
		 * Start Minus the discount coupon
		 */
		if (!newPrice.contains("-")) {
			if (couponDiscount != null) {
				String couponValue = couponDiscount.split(" ")[1];
				if (couponValue.charAt(couponValue.length() - 1) == '%') {
					couponValue = couponValue.split("%")[0];
					Double newPriceValue = Double.parseDouble(newPrice);
					newPriceValue = newPriceValue - (newPriceValue * Double.parseDouble(couponValue) / 100);
					newPrice = newPriceValue.toString();
				} else {
					Double newPriceValue = Double.parseDouble(newPrice);
					newPriceValue = newPriceValue - Double.parseDouble(couponValue);
					newPrice = newPriceValue.toString();
				}
			}

		}
		/**
		 * End Minus the discount coupon
		 */
		mapToReturn.put("oldPrice", oldPrice);
		mapToReturn.put("newPrice", newPrice);
		mapToReturn.put("postTitle", actualTitle);
		mapToReturn.put("features", features.toString());
		mapToReturn.put("affilateLink", affilateLink);
		mapToReturn.put(specialDealKey, specialDealType);
		mapToReturn.put("imageURL", imageURL);
		mapToReturn.put("dealPercentage", dealPercentage);
		mapToReturn.put("couponDiscount", couponDiscount);
		return mapToReturn;
	}

	public String fetchAffilateLink() {
		String linkToReturn = null;

		WebElement getTextLinkCodeButton = getElementIfExist("#amzn-ss-text-link > span > span > strong > a", css);
		if (null != getTextLinkCodeButton) {
			clickElement(getTextLinkCodeButton);
			;
		}
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		/*
		 * WebElement radioButton =
		 * getElementIfExist("//*[@id=\"amzn-ss-full-link-radio-button\"]/label/i",
		 * xpath); if (null != radioButton) {
		 * wait.until(ExpectedConditions.elementToBeClickable(radioButton));
		 * clickElement(radioButton); }
		 */

		WebElement link = getElementIfExist("#amzn-ss-text-component > div > div", css);
		if (null != link) {
			linkToReturn = link.getText();
		}

		return linkToReturn;
	}

	public void loginIntoAmazon(String username, String password) {
		WebElement signInHover = getElementIfExist("nav-link-yourAccount", id);
		if (signInHover != null) {
			clickElement(signInHover);
		}

		WebElement emailTextField = getElementIfExist("ap_email", id);
		if (emailTextField != null) {
			emailTextField.sendKeys(username);
		}
		WebElement continueButton = getElementIfExist("continue", id);
		if (continueButton != null) {
			clickElement(continueButton);
		}

		WebElement passwordField = getElementIfExist("ap_password", id);
		if (passwordField != null) {
			passwordField.sendKeys(password);
		}

		WebElement submitButton = getElementIfExist("signInSubmit", id);
		if (submitButton != null) {
			clickElement(submitButton);
		}
	}

	public void logIntoBlog(String username, String password) {
		driver.findElement(By.id("user_login")).sendKeys(username);
		driver.findElement(By.id("user_pass")).sendKeys(password);
		driver.findElement(By.id("wp-submit")).click();
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
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebElement addAllButton = getElementIfExist(
				"#content-egg > div > div:nth-child(5) > div.ng-isolate-scope > div > div.tab-pane.ng-scope.active > div.search_panel.ng-scope > div > div.col-sm-1.text-right > a",
				css);
		clickElement(addAllButton);

	}

	public void addPostAttributes(Map<String, String> returnMap) {
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
		String affilateLink = returnMap.get("affilateLink");

		WebElement productURL = getElementIfExist("rehub_offer_product_url", id);
		if (null != productURL) {
			productURL.sendKeys(affilateLink);
		}

		WebElement offerOldPriceTextField = getElementIfExist("//*[@id=\"rehub_offer_product_price_old\"]", xpath);
		WebElement offerSalePriceTextField = getElementIfExist("//*[@id=\"rehub_offer_product_price\"]", xpath);
		if (null != offerOldPriceTextField && null != offerSalePriceTextField) {
			if (oldPriceValue != 0 && oldPriceValue != newPriceValue) {
				offerOldPriceTextField.sendKeys("&#8377; " + oldPriceValue);
			}
			if (newPrice == null) {
				offerSalePriceTextField.sendKeys("&#8377; " + newPriceValue);
			} else {
				offerSalePriceTextField.sendKeys("&#8377; " + newPrice);
			}
		}

		WebElement imageURLElement = getElementIfExist("knawatfibu_url", id);
		if (null != imageURLElement && null != returnMap.get("imageURL")) {
			js.executeScript("document.getElementById('knawatfibu_url').value = '" + returnMap.get("imageURL") + "'");
		}

		if (returnMap.get(specialDealKey) != null && returnMap.get(specialDealKey).equalsIgnoreCase(lightningDeal)) {
			js.executeScript("document.getElementsByName('rehub_post_side[is_editor_choice]')[3].click();");
		}

		if (returnMap.get("couponDiscount") != null) {
			js.executeScript("document.getElementById('rehub_offer_product_coupon').value = '"
					+ returnMap.get("couponDiscount") + "'");
		}
	}

	public void signOutFromAmazon() {
		driver.get("https://www.amazon.in/gp/flex/sign-out.html/ref=nav_youraccount_signout?ie=UTF8&action=sign-out");
	}

	public void postOnTelegram(Map<String, String> returnMap) {

		try {
			// Select Yes on Auto Publish
			js.executeScript("document.getElementsByName('afap_auto_post')[0].value='yes'");
		} catch (Exception e) {
			// TODO: handle exception
		}

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
		String newTitle = null;
		actualTitle = actualTitle.replaceAll("'", "");
		if (newPrice == null) {
			newTitle = /* "Buy " + */"*" + actualTitle + " at Rs. " + newPriceValue.intValue() + " @ Amazon";
		} else {
			newTitle = /* "Buy " + */"*" + actualTitle + " at Rs. " + newPrice + " @ Amazon";
		}

		if (returnMap.get(specialDealKey) != null && returnMap.get(specialDealKey).equalsIgnoreCase(lightningDeal)) {
			if (returnMap.get("dealPercentage") != null) {
				newTitle = "*(Lightning Deal! " + returnMap.get("dealPercentage") + " Claimed Grab Fast!!)* \n\n"
						+ newTitle;
			} else {
				newTitle = "*(Lightning Deal!) Grab Fast!!* \n\n" + newTitle;
			}
		}

		if (oldPriceValue.intValue() != 0) {
			newTitle = newTitle + " (Original Price Rs. " + oldPriceValue.intValue() + ")*";
		} else {
			newTitle = newTitle + "*";
		}

		if (null != returnMap.get("couponDiscount")) {
			newTitle = newTitle + "\n\n + " + returnMap.get("couponDiscount");
		}

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
		telMessageBody.sendKeys("\n");
		WebElement publishButton = getElementIfExist("publish", id);
		clickElement(publishButton);
	}

	public boolean postDeal(Map<String, String> returnMap) throws Exception {
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
		features = features.replaceAll("'", "");
		actualTitle = actualTitle.replaceAll("'", "");
		String affilateLink = returnMap.get("affilateLink");
		String newTitle = null;
		if (newPrice == null) {
			newTitle = /* "Buy " + */ actualTitle + " at Rs. " + newPriceValue.intValue() + " @ Amazon";
		} else {
			newTitle = /* "Buy " + */actualTitle + " at Rs. " + newPrice + " @ Amazon";
		}

		driver.get(newPostUrl);

		WebElement postTitle = getElementIfExist("post_title", name);
		if (null != postTitle) {
			try {
				js.executeScript("document.getElementsByName('post_title')[0].value = '" + newTitle + "'");
			} catch (Exception e) {
				postTitle.sendKeys(newTitle);
			}

		}

		// js.executeScript("document.getElementsByName('post_title')[0].value = '" +
		// newTitle +"'");

		WebElement contentHTML = getElementIfExist("content-html", id);
		if (null != contentHTML) {
			clickElement(contentHTML);
		}

		/*
		 * WebElement bodyElement = getElementIfExist("wp-editor-area", className); if
		 * (null != bodyElement) { bodyElement.sendKeys(features); }
		 */

		try {
			js.executeScript("document.getElementsByClassName('wp-editor-area')[0].value = '" + features + "'");
		} catch (Exception e) {
			WebElement bodyElement = getElementIfExist("wp-editor-area", className);
			if (null != bodyElement) {
				bodyElement.sendKeys(features);
			}
		}

		// Adding Content Egg
		// addContentEggInPost(actualTitle);
		addPostAttributes(returnMap);

		WebElement publishButton = getElementIfExist("publish", id);
		clickElement(publishButton);

		String shortLink = null;
		WebElement shortLinkElement = getElementIfExist("shortlink", id);
		if (shortLinkElement != null) {
			shortLink = shortLinkElement.getAttribute("value");
		}
		// System.out.println("Short Link: " + shortLink);

		String outputMessage = newTitle + "(Original Price Rs. " + oldPriceValue.intValue() + ")";
		outputMessage = outputMessage.replaceAll(" ", "%20");
		outputMessage = outputMessage.replaceAll("'", "%27");
		// outputMessage = outputMessage.replaceAll("(", "%28");
		// outputMessage = outputMessage.replaceAll(")", "%28");
		outputMessage = "https://wa.me/?text=" + outputMessage;
		outputMessage = outputMessage.concat("%0A%0A");
		outputMessage = outputMessage.concat(shortLink);
		// System.out.println(outputMessage);

		// js.executeScript("document.getElementById('publish').click()");

		return true;
	}

	@Test
	public void postAutomatedJob() throws Exception {

		// driver.manage().window().maximize();
		// driver.get(baseUrl);

		// loginIntoAmazon("allpdfnotes@gmail.com", "shambhu2");
		Map<String, String> returnMap = null;
		Scanner sc = new Scanner(System.in);
		try {
			while (true) {
				System.out.println("Enter amazon deal URL: ");
				baseUrl = sc.nextLine();
				driver.get(baseUrl);
				returnMap = getFieldsFromAmazon();
				boolean dealPosted = postDeal(returnMap);
				postOnTelegram(returnMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Can't post this deal.... ");
			postAutomatedJob();
		}

		// signOutFromAmazon();

		// boolean dealPosted = postDeal(returnMap);
		// postOnTelegram(returnMap);

		// driver.close();
	}

	@BeforeTest
	public void getBaseURL() {
		// System.out.println("Enter amazon deal URL: ");
		// Scanner sc = new Scanner(System.in);
		// baseUrl = sc.nextLine();
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Shubham Arora\\Downloads\\chromedriver.exe");
		driver = new ChromeDriver();
		js = (JavascriptExecutor) driver;
		wait = new WebDriverWait(driver, 20);

		driver.manage().window().maximize();
		// driver.get(baseUrl);
		// loginIntoAmazon("rohanmadaan.1997@gmail.com", "123456123#abc");

		driver.get(newPostUrl);
		logIntoBlog("shubham", "shambhu2");

	}
}
