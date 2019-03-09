import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

public class IFSCopier {
	public WebDriver driver = null;
	public JavascriptExecutor js = null;
	public WebDriverWait wait = null;
	String ifsURL = "https://www.indiafreestuff.in/";
	String xpath = "xpath";
	String css = "css";
	String id = "id";
	String name = "name";
	String className = "className";
	String lightningDeal = "LightningDeal";
	String specialDealKey = "specialDealKey";
	static ArrayList<String> postsList = new ArrayList<String>();
	String newPostUrl = "https://nonstopdeals.in/wp-admin/post-new.php";
	String admitadFlipkart = "https://ad.admitad.com/g/rb1qie435bc91b4d8bc4a80d05f527/?ulp=";
	// public String newPostUrl =
	// "http://localhost:70/wordpress/wp-admin/post-new.php";

	static {
		postsList.add("Ethics Men's Footwear 50% to 70% off from Rs. 119 - Amazon");
		postsList.add("[Extra Rs. 4000 off on Exchange] Vivo Y83 Pro 64gb Rs. 13990 - Flipkart");
		// postsList.add("Jack Jones Blazers Min 60% off from Rs. 1259 @ Flipkart");
	}

	@Test
	public void f() {

		try {
			driver.get(ifsURL);
			ArrayList<WebElement> allDeals = (ArrayList<WebElement>) js.executeScript(
					"return document.getElementsByClassName('product-list')[0].getElementsByClassName('product-item');");
			ArrayList<WebElement> amazonDeals = new ArrayList<WebElement>();
			ArrayList<WebElement> flipkartDeals = new ArrayList<WebElement>();
			for (int i = 0; i < allDeals.size(); i++) {
				if (allDeals.get(i).findElement(By.className("product-footer"))
						.findElement(By.className("logo-shop-now")).findElement(By.tagName("a")).getAttribute("href")
						.contains("amazon")) {
					amazonDeals.add(allDeals.get(i));
				} else if (allDeals.get(i).findElement(By.className("product-footer"))
						.findElement(By.className("logo-shop-now")).findElement(By.tagName("a")).getAttribute("href")
						.contains("flipkart")) {
					flipkartDeals.add(allDeals.get(i));
				}
			}

			if (!amazonDeals.isEmpty()) {
				try {
					for (int i = 0; i < amazonDeals.size(); i++) {

						if (!(postsList.contains(amazonDeals.get(i).findElements(By.tagName("a")).get(1).getText()))) {
							postsList.add(amazonDeals.get(i).findElements(By.tagName("a")).get(1).getText());
							String amazonLink = amazonDeals.get(i).findElement(By.className("product-footer"))
									.findElement(By.className("logo-shop-now")).findElement(By.className("btn-shopnow"))
									.getAttribute("href");
							String ifsTitle = amazonDeals.get(i).findElements(By.tagName("a")).get(1).getText();
							//String ifsOriginalPrice = amazonDeals.get(i).findElements(By.className("old-price")).get(1).getText();
							postIFSDeal(amazonLink, ifsTitle);
						} else {
							break;
						}
					}
				} catch (Exception e) {
					throw e;
				}
			}
			if (false/*!flipkartDeals.isEmpty()*/) {
				try {
					for (int i = 0; i < flipkartDeals.size(); i++) {

						if (!(postsList
								.contains(flipkartDeals.get(i).findElements(By.tagName("a")).get(1).getText()))) {
							postsList.add(flipkartDeals.get(i).findElements(By.tagName("a")).get(1).getText());
							String flipkartLink = flipkartDeals.get(i).findElement(By.className("product-footer"))
									.findElement(By.className("logo-shop-now")).findElement(By.className("btn-shopnow"))
									.getAttribute("href");
							String ifsTitle = flipkartDeals.get(i).findElements(By.tagName("a")).get(1).getText();
							postIFSFlipkartDeal(flipkartLink, ifsTitle);
						} else {
							break;
						}
					}
				} catch (Exception e) {
					throw e;
				}
			}
			try {
				Thread.sleep(30000);
			} catch (Exception e) {
				// TODO: handle exception
			}
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
		wait = new WebDriverWait(driver, 20);
		driver.manage().window().maximize();
		try {
			loginIntoAmazon("rohanmadaan.1997@gmail.com", "NONSTOPDEALS@1322");
			// loginIntoAmazon("allpdfnotes@gmail.com", "shambhu2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logIntoBlog("shubham", "shambhu2");
	}

	public void logIntoBlog(String username, String password) {
		driver.get(newPostUrl);
		driver.findElement(By.id("user_login")).sendKeys(username);
		driver.findElement(By.id("user_pass")).sendKeys(password);
		driver.findElement(By.id("wp-submit")).click();
	}

	public Map<String, String> getFieldsFromAmazon(String amazonURL, String ifsTitle) throws Exception {
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

		WebElement titleElement = getElementIfExist("productTitle", id);
		if (titleElement != null) {
			actualTitle = titleElement.getText();
		}

		try {
			Thread.sleep(1000);
		} catch (Exception e) {

		}

		int count = 1;
		String affilateLink = fetchAffilateLink();
		while ((affilateLink == null || affilateLink.length() < 5) && count < 3) {
			driver.get(amazonURL);
			affilateLink = fetchAffilateLink();
			count++;
		}
		if (count == 3 && (affilateLink == null || affilateLink.length() < 5)) {
			sendOnlyMessageOnTelegram(ifsTitle, driver.getCurrentUrl(), null, null);
			throw new Exception();
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

	public String fetchAffilateLink() throws Exception {
		String linkToReturn = null;

		WebElement getTextLinkCodeButton = getElementIfExist("#amzn-ss-text-link > span > span > strong > a", css);
		if (null != getTextLinkCodeButton) {
			clickElement(getTextLinkCodeButton);
		}
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		WebElement link = getElementIfExist("#amzn-ss-text-component > div > div", css);
		if (null != link) {
			linkToReturn = link.getText();
		} /*
			 * else { WebElement closeButton =
			 * getElementIfExist("#a-popover-1 > div > header > button", css);
			 * clickElement(closeButton); fetchAffilateLink(); }
			 */

		return linkToReturn;
	}

	public void loginIntoAmazon(String username, String password) throws Exception {
		driver.get("https://amazon.in");
		WebElement signInHover = getElementIfExist("//*[@id=\"nav-link-yourAccount\"]", xpath);
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

		/*
		 * if (oldPriceValue == 0 && newPriceValue == 0) {
		 * System.out.println("Cannot post deal - Price not found"); throw new
		 * Exception(); }
		 */

		// driver.get(blogUrl);

		driver.get(newPostUrl);

		// wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user_login")));
		// logIntoBlog("shubham", "shambhu2");

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

		return true;
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
			if (newPrice == null) {
				offerSalePriceTextField.sendKeys("&#8377; " + newPriceValue.intValue());
				if (oldPriceValue.intValue() == newPriceValue.intValue()) {
					oldPriceValue = (double) 0;
				}
			} else {
				offerSalePriceTextField.sendKeys("&#8377; " + newPrice);
			}
			if (oldPriceValue != 0) {
				offerOldPriceTextField.sendKeys("&#8377; " + oldPriceValue.intValue());
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

	public void addContentEggInPost(String actualTitle) throws Exception {
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

	public void postOnTelegram(Map<String, String> returnMap) throws Exception {

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

		if (oldPriceValue.intValue() != 0 && newPriceValue != 0
				&& oldPriceValue.intValue() != newPriceValue.intValue()) {
			newTitle = newTitle + " (Original Price Rs. " + oldPriceValue.intValue() + ")*";
		} else {
			newTitle = newTitle + "*";
		}

		if (null != returnMap.get("couponDiscount")) {
			newTitle = newTitle + "\n\n " + returnMap.get("couponDiscount");
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

	public void clickElement(WebElement element) throws Exception {
		// System.out.println("Clicking element: " + element.getText());
		try {
			if (element.isDisplayed()) {
				wait.until(ExpectedConditions.elementToBeClickable(element));
				element.click();
			}
		} catch (Exception e) {
			js.executeScript("document.getElementById('" + element.getAttribute("id") + "').click()");
			// throw e;
		}
	}

	public void postDealWrapper(String amazonURL, String ifsTitle) throws Exception {
		driver.get(amazonURL);
		Map<String, String> returnMap = getFieldsFromAmazon(amazonURL, ifsTitle);
		sendOnlyMessageOnTelegram(ifsTitle, returnMap.get("affilateLink"), returnMap.get("oldPrice"), returnMap.get("couponDiscount"));
		boolean dealPosted = postDeal(returnMap);
		//postOnTelegram(returnMap);
	}

	public void postIFSDeal(String ifsAmazonLink, String ifsTitle) throws Exception {
		js.executeScript("window.open('', '_blank');");

		ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
		String postTitle = null;
		String amazonLinkHref = null;
		try {
			driver.switchTo().window(tabs.get(1));
			postTitle = ifsTitle;
			amazonLinkHref = ifsAmazonLink;
			postDealWrapper(amazonLinkHref, ifsTitle);

			driver.close();
			driver.switchTo().window(tabs.get(0));
		} catch (Exception e) {
			try {
				String linkText = postTitle;
				String amazonLink = amazonLinkHref;
				driver.get(amazonLink);
				if (driver.findElements(By.cssSelector("#availability > span")).size() > 0
						&& driver.findElements(By.cssSelector("#availability > span")).get(0).getText()
								.contains("Currently unavailable")) {
					throw new Exception();
				}
				int count = 1;
				String affilatedLink = fetchAffilateLink();
				while ((affilatedLink == null || affilatedLink.length() < 5) && count < 3) {
					driver.get(amazonLink);
					affilatedLink = fetchAffilateLink();
					count++;
				}
				if (count == 3 && (affilatedLink == null || affilatedLink.length() < 5)) {
					sendOnlyMessageOnTelegram(linkText, driver.getCurrentUrl(), null, null);
					throw e;
				}
				sendOnlyMessageOnTelegram(linkText, affilatedLink, null, null);
			} catch (Exception e2) {
				System.out.println("Can't post - " + postTitle);
			}
			driver.close();
			driver.switchTo().window(tabs.get(0));
			// throw e;
		}
	}

	public void postDealOnFlipkartWrapper(String flipkartURL) throws Exception {
		driver.get(flipkartURL);
		Map<String, String> returnMap = getAttributesFromFlipkart();
		boolean dealPosted = postDeal(returnMap);
		postOnTelegram(returnMap);
	}

	public void postIFSFlipkartDeal(String ifsFlipkartLink, String ifsTitle) throws Exception {

		js.executeScript("window.open('', '_blank');");

		ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
		String postTitle = null;
		String flipkartLinkHref = null;
		try {
			driver.switchTo().window(tabs.get(1));
			postTitle = ifsTitle;
			flipkartLinkHref = ifsFlipkartLink;
			postDealOnFlipkartWrapper(flipkartLinkHref);

			driver.close();
			driver.switchTo().window(tabs.get(0));
		} catch (Exception e) {
			try {
				String linkText = postTitle;
				String flipkartLink = ifsFlipkartLink;
				driver.get(flipkartLink);
				/*js.executeScript(
						"document.onreadystatechange=function(){if(window.location.hostname.indexOf('www.flipkart.com')<0)return void alert('Please try on any Flipkart Page');var e=document.getElementsByClassName('fk_affiliate_id');e.length>0&&e[0].parentNode.removeChild(e[0]);var t=document.createElement('div');t.className='fk_affiliate_id',t.id='vishalkum20',document.body.appendChild(t);var e=document.getElementsByClassName('bkm_script');e.length>0&&e[0].parentNode.removeChild(e[0]);var a=document.createElement('script');a.src='https://affiliate-static.flixcart.net/affiliate/website/bookmarklet/fkBookmark.min.js?v'+parseInt(99999999*Math.random()),a['class']='bkm_script',document.body.appendChild(a)}();");
				try {
					Thread.sleep(2000);
				} catch (Exception e2) {
					// TODO: handle exception
				}*/
				try {
					/*
					 * String affilateLink = (String) js
					 * .executeScript("return document.getElementsByClassName('copyBox')[0].value");
					 */
					String currentURL = driver.getCurrentUrl();
					currentURL = URLEncoder.encode(currentURL, "UTF-8");
					String affilateLink = admitadFlipkart + currentURL;
					driver.get("https://tinyurl.com/");
					js.executeScript("document.getElementById('url').value = '" + affilateLink + "'");
					js.executeScript("document.getElementById('submit').click()");
					String tinyURLLink = (String) js
							.executeScript("return document.getElementsByTagName('b')[1].innerText");
					sendOnlyMessageOnTelegram(linkText, tinyURLLink, null, null);
				} catch (Exception e2) {
					throw new Exception();
				}

			} catch (Exception e2) {
				System.out.println("Can't post Flipkart Post - " + postTitle);
			}
			driver.close();
			driver.switchTo().window(tabs.get(0));
			// throw e;
		}

	}

	private Map<String, String> getAttributesFromFlipkart() throws Exception {
		Map<String, String> mapToReturn = new HashMap<String, String>();
		String oldPrice = null;
		String newPrice = null;
		String actualTitle = null;
		String imageURL = null;
		String affilateLink = null;
		String currentURL = null;
		StringBuffer features = new StringBuffer();
		
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}

		oldPrice = (String) js.executeScript("return document.getElementsByClassName('_3auQ3N _1POkHg')[0].innerText");
		newPrice = (String) js.executeScript("return document.getElementsByClassName('_1vC4OE _3qQ9m1')[0].innerText");
		actualTitle = (String) js.executeScript("return document.getElementsByClassName('_35KyD6')[0].innerText");
		imageURL = (String) js.executeScript(
				"return document.getElementsByClassName('_1Nyybr Yun65Y _30XEf0')[0].getAttribute('src')");

		// affilateLink = fetchFlipkartAffilateLink();
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
		currentURL = driver.getCurrentUrl();

		affilateLink = admitadFlipkart + currentURL;

		mapToReturn.put("oldPrice", oldPrice);
		mapToReturn.put("newPrice", newPrice);
		mapToReturn.put("postTitle", actualTitle);
		mapToReturn.put("features", features.toString());
		mapToReturn.put("imageURL", imageURL);
		mapToReturn.put("affilateLink", affilateLink);
		return mapToReturn;
	}

	private String fetchFlipkartAffilateLink() throws Exception {
		try {
			js.executeScript(
					"document.onreadystatechange=function(){if(window.location.hostname.indexOf('www.flipkart.com')<0)return void alert('Please try on any Flipkart Page');var e=document.getElementsByClassName('fk_affiliate_id');e.length>0&&e[0].parentNode.removeChild(e[0]);var t=document.createElement('div');t.className='fk_affiliate_id',t.id='vishalkum20',document.body.appendChild(t);var e=document.getElementsByClassName('bkm_script');e.length>0&&e[0].parentNode.removeChild(e[0]);var a=document.createElement('script');a.src='https://affiliate-static.flixcart.net/affiliate/website/bookmarklet/fkBookmark.min.js?v'+parseInt(99999999*Math.random()),a['class']='bkm_script',document.body.appendChild(a)}();");
			try {
				Thread.sleep(2000);
			} catch (Exception e2) {
				// TODO: handle exception
			}
			try {
				String affilateLink = (String) js
						.executeScript("return document.getElementsByClassName('copyBox')[0].value");
				return affilateLink;
			} catch (Exception e) {
				throw e;
			}
		} catch (Exception e) {
			throw e;
		}

	}

	public void sendOnlyMessageOnTelegram(String linkText, String affilatedLink, String originalPrice, String dealPercentage) {
		driver.get("https://nonstopdeals.in/wp-admin/admin.php?page=telegram_send");
		WebElement textArea = getElementIfExist("telegram_new_message", name);
		textArea.sendKeys("*" + linkText + "*\n\n");
		
		if(null != originalPrice) {
			textArea.sendKeys("MRP Rs. " + originalPrice.trim() + "\n\n");
		}
		
		if(null != dealPercentage) {
			textArea.sendKeys(dealPercentage +"\n\n");
		}
		
		textArea.sendKeys(affilatedLink);
		WebElement sendNowButton = getElementIfExist("submit", id);
		sendNowButton.click();
	}

	@AfterTest
	public void afterTest() {
		System.out.println(postsList.get(postsList.size() - 1));
	}

}
