import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GetSeleniumDriver {
	private static WebDriver driver = new ChromeDriver();
	private static JavascriptExecutor js = (JavascriptExecutor) driver;
	private static WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));

	public static JavascriptExecutor getJs() {
		return js;
	}

	public static WebDriverWait getWait() {
		return wait;
	}

	static {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Shubham Arora\\Downloads\\chromedriver.exe");
		
	}

	private GetSeleniumDriver() {

	}

	public static WebDriver getDriver() {
		return driver;
	}
}
