import java.text.SimpleDateFormat;
import java.util.Date;

public class TestApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String toEncode = "I am saying hello" + "https://goo.gl/gEX6iw";
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		System.out.println(date);
	}

}
