import java.net.HttpURLConnection;
import java.net.URL;

public class APiConnector {

    private static final String BASE_URL = "http://localhost:3000";

    public static String getBaseAPI() {
        return BASE_URL;
    }

    // Test API connection (GET /)
    public static boolean isConnected() {
        try {
            URL url = new URL(BASE_URL + "/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int code = conn.getResponseCode();
            if (code == 200) {
                System.out.println("✅ API is reachable.");
                return true;
            } else {
                System.out.println("⚠️ API responded with code: " + code);
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Cannot reach API: " + e.getMessage());
            return false;
        }
    }

}
