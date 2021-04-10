import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlSeniorClass {
	private static String baseUrl = "https://openapi.gg.go.kr/OldPersonClassroom";
	private static String Key = "f5d2e556dc0c42988183fa359d49f77e";
	private static String Type = "json";
	private static int pIndex = 1;
	private static int pSize = 1000;
	
	public static String getUrl() {
		String comUrl = baseUrl+"?";
		try {
			comUrl += "&"+ URLEncoder.encode("Key","UTF-8") +"=" + Key;
			comUrl += "&"+ URLEncoder.encode("Type","UTF-8") +"=" + Type;
			comUrl += "&"+ URLEncoder.encode("pIndex","UTF-8") +"=" + pIndex;
			comUrl += "&"+ URLEncoder.encode("pSize","UTF-8") +"=" + pSize;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return comUrl;
	}
	public static String getUrl(int input) {
		String comUrl = baseUrl+"?";
		try {
			comUrl += "&"+ URLEncoder.encode("Key","UTF-8") +"=" + Key;
			comUrl += "&"+ URLEncoder.encode("Type","UTF-8") +"=" + Type;
			comUrl += "&"+ URLEncoder.encode("pIndex","UTF-8") +"=" + input;
			comUrl += "&"+ URLEncoder.encode("pSize","UTF-8") +"=" + pSize;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return comUrl;
	}
}
