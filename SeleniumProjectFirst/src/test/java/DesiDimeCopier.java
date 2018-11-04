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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

public class DesiDimeCopier {
	public WebDriver driver = null;
	public JavascriptExecutor js = null;
	public WebDriverWait wait = null;
	String desiDimeURL = "https://www.desidime.com/new";
	String xpath = "xpath";
	String css = "css";
	String id = "id";
	String name = "name";
	String className = "className";
	String lightningDeal = "LightningDeal";
	String specialDealKey = "specialDealKey";
	static ArrayList<String> postsList = new ArrayList<String>();
	String previousFirstDealTitle = "[58% off] Syska Ionic Function HD 3600I Hair Dryer and Ionic HS 2021I Hair St...";
	String newPostUrl = "http://nonstopdeals.in/wp-admin/post-new.php";
	String firstAmazonDealTitle = "";

	static {
		postsList.add("Altavista Crafted Top Solid Wood Coffee Table (Brown) for 999");
	}

	@Test
	public void f() {
		
		if(!postsList.isEmpty()) {
			System.out.println(postsList.get(postsList.size() - 1));
		}

		driver.get(desiDimeURL);
		ArrayList<WebElement> allDeals = (ArrayList<WebElement>) js
				.executeScript("return document.getElementsByClassName('deal-box shadow');");
		ArrayList<WebElement> amazonDeals = new ArrayList<WebElement>();
		for (int i = 0; i < allDeals.size(); i++) {
			if (allDeals.get(i).getText().contains("Amazon")) {
				amazonDeals.add(allDeals.get(i));
			}
		}

		if (!amazonDeals.isEmpty()) {
			//firstAmazonDealTitle = (amazonDeals.get(0).findElements(By.tagName("a"))).get(3).getText();
			try {
				for (int i = 0; i < amazonDeals.size(); i++) {

					if(!(postsList.contains(amazonDeals.get(i).findElements(By.tagName("a")).get(3).getText()))) {
						postsList.add(amazonDeals.get(i).findElements(By.tagName("a")).get(3).getText());
						WebElement link = (amazonDeals.get(i).findElements(By.tagName("a"))).get(3);
						postDesiDimeDeal(link);
					} else {
						break;
					}
					
					/*if (!(amazonDeals.get(i).findElements(By.tagName("a"))).get(3).getText()
							.equalsIgnoreCase(previousFirstDealTitle)) {
						WebElement link = (amazonDeals.get(i).findElements(By.tagName("a"))).get(3);
						postDesiDimeDeal(link);

					} else {
						// previousFirstDealTitle = firstAmazonDealTitle;
						break;
					}*/
				}

				try {
					Thread.sleep(30000);
				} catch (Exception e) {
					// TODO: handle exception
				}
				f();
			} catch (Exception e) {
				previousFirstDealTitle = firstAmazonDealTitle;
				f();
			}
		} else {
			try {
				Thread.sleep(30000);
			} catch (Exception e) {
				// TODO: handle exception
			}
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
			loginIntoAmazon("rohanmadaan.1997@gmail.com", "123456123#abc");
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

	public Map<String, String> getFieldsFromAmazon() throws Exception {
		Map<String, String> mapToReturn = new HashMap<String, String>();
		String oldPrice = null;
		String newPrice = null;
		Double oldPriceValue = null;
		Double newPriceValue = null;
		String actualTitle = null;
		String specialDealType = null;
		String imageURL = null;
		String dealPercentage = null;
		StringBuffer features = new StringBuffer();
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

		if (getElementIfExist("goldboxBuyBox", id) != null) {
			specialDealType = lightningDeal;

			long dealPercentageDomSize = 0;
			dealPercentageDomSize = (Long) js.executeScript(
					"return document.getElementsByClassName('a-size-small a-color-base a-text-bold').length");

			if (dealPercentageDomSize > 0) {
				dealPercentage = (String) js.executeScript(
						"return document.getElementsByClassName('a-size-small a-color-base a-text-bold')[0].innerText;");
			}
		}

		String affilateLink = fetchAffilateLink();

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
		mapToReturn.put("oldPrice", oldPrice);
		mapToReturn.put("newPrice", newPrice);
		mapToReturn.put("postTitle", actualTitle);
		mapToReturn.put("features", features.toString());
		mapToReturn.put("affilateLink", affilateLink);
		mapToReturn.put(specialDealKey, specialDealType);
		mapToReturn.put("imageURL", imageURL);
		mapToReturn.put("dealPercentage", dealPercentage);
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
			if (oldPriceValue != 0) {
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

		newTitle = newTitle + " (Original Price Rs. " + oldPriceValue.intValue() + ")*";

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

	public void postDeal(String amazonURL) throws Exception {
		driver.get(amazonURL);
		Map<String, String> returnMap = getFieldsFromAmazon();
		boolean dealPosted = postDeal(returnMap);
		postOnTelegram(returnMap);
	}

	public void postDesiDimeDeal(WebElement desiDimeLink) throws Exception {
		Actions actionOpenLinkInNewTab = new Actions(driver);
		actionOpenLinkInNewTab.moveToElement(desiDimeLink).keyDown(Keys.CONTROL) // MacOS: Keys.COMMAND
				.keyDown(Keys.SHIFT).click(desiDimeLink).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).perform();

		ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
		try {
			driver.switchTo().window(tabs.get(1));
			WebElement amazonLink = driver.findElement(By.cssSelector(
					"#deal-detail-like-dislike-container > div > div.grid-100.tablet-grid-100.grid-parent.gutter-bottom > div.grid-55.tablet-grid-55.grid-parent > div:nth-child(2) > a"));
			postDeal(amazonLink.getAttribute("href"));

			// driver.get("http://google.com");
			driver.close();
			driver.switchTo().window(tabs.get(0));
		} catch (Exception e) {
			// previousFirstDealTitle = firstAmazonDealTitle;
			//System.out.println("Can't Post " + amazonLink.getText());
			driver.close();
			driver.switchTo().window(tabs.get(0));
			// throw e;
		}
	}

}
