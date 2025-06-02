import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Library {

    private static final Gson gson = new Gson();

    // Add book via API
    public boolean addBook(Book b) {
        try {
            String jsonBody = gson.toJson(b);
            String response = ApiClient.post("/books", jsonBody);

            JsonObject jsonObj = JsonParser.parseString(response).getAsJsonObject();
            return jsonObj.has("success") && jsonObj.get("success").getAsBoolean();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all books formatted string
    public String printBooks() {
        try {
            String response = ApiClient.get("/books");
            System.out.println("Books API response: " + response);

            // نحلل الرد أولًا كـ JsonElement
            JsonElement jsonElement = JsonParser.parseString(response);

            if (jsonElement.isJsonArray()) {
                // الرد هو مصفوفة كتب
                List<Book> books = gson.fromJson(response, new TypeToken<List<Book>>(){}.getType());

                if (books.isEmpty()) {
                    return "لا توجد كتب في قاعدة البيانات";
                }

                StringBuilder sb = new StringBuilder();
                for (Book b : books) {
                    sb.append("رقم الكتاب : ").append(b.getId()).append("\n")
                      .append("العنوان :  ").append(b.getTitle()).append("\n")
                      .append("المؤلف :  ").append(b.getAuthor()).append("\n")
                      .append("الرمز :  ").append(b.getIsbn()).append("\n")
                      .append("الكمية :  ").append(b.getQuantity()).append("\n")
                      .append("عدد النسخ المستعارة :  ").append(b.getBorrowed()).append("\n")
                      .append("--------------------------------------------\n");
                }
                return sb.toString();

            } else if (jsonElement.isJsonObject()) {
                // الرد JSON كائن (غالباً رسالة خطأ أو حالة)
                JsonObject obj = jsonElement.getAsJsonObject();
                if (obj.has("success") && !obj.get("success").getAsBoolean()) {
                    return "خطأ من الخادم: " + obj.get("message").getAsString();
                } else {
                    return "رد غير متوقع: " + obj.toString();
                }
            } else {
                return "تنسيق الرد غير معروف.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "حدث خطأ في جلب البيانات";
        }
    }


    // Remove book by ID
    public boolean removeBook(int id) {
        try {
            String response = ApiClient.delete("/books/" + id);
            JsonObject jsonObj = JsonParser.parseString(response).getAsJsonObject();
            return jsonObj.has("success") && jsonObj.get("success").getAsBoolean();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Search books by title
    public String searchBookByTitle(String title) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
            String response = ApiClient.get("/books/search?title=" + encodedTitle);

            List<Book> books = gson.fromJson(response, new TypeToken<List<Book>>(){}.getType());

            if (books.isEmpty()) {
                return "لا توجد نتائج مطابقة";
            }

            StringBuilder sb = new StringBuilder();
            for (Book b : books) {
                sb.append("رقم الكتاب: ").append(b.getId()).append("\n")
                  .append("العنوان: ").append(b.getTitle()).append("\n")
                  .append("المؤلف: ").append(b.getAuthor()).append("\n")
                  .append("الرمز: ").append(b.getIsbn()).append("\n")
                  .append("الكمية: ").append(b.getQuantity()).append("\n")
                  .append("عدد النسخ المستعارة: ").append(b.getBorrowed()).append("\n")
                  .append("-------------------------\n");
            }
            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "حدث خطأ في جلب البيانات";
        }
    }

    // Authenticate admin
    public boolean authenticateAdmin(String username, String password) {
        try {
            // Create JSON for login
            Credentials creds = new Credentials(username, password);
            String jsonBody = gson.toJson(creds);

            // Call API
            String response = ApiClient.post("/auth/login", jsonBody);
            System.out.println("The api response: "+response);

            // Parse response with lenient JsonReader
            JsonReader reader = new JsonReader(new StringReader(response));
            reader.setLenient(true);

            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonObject jsonObj = jsonElement.getAsJsonObject();

            // Return true if success == true in response JSON
            return jsonObj.has("success") && jsonObj.get("success").getAsBoolean();


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Register admin
    public boolean registerAdmin(String username, String password) {
        try {
            Credentials creds = new Credentials(username, password);
            String jsonBody = gson.toJson(creds);
            String response = ApiClient.post("/auth/register", jsonBody);

            // طباعة الرد الخام للمساعدة في التشخيص
            System.out.println("🔵 Response from /auth/register: " + response);

            // فحص إذا كان الرد يبدو كـ JSON أم لا
            if (!response.trim().startsWith("{")) {
                System.err.println("❌ Unexpected response: " + response);
                return false;
            }

            JsonReader reader = new JsonReader(new StringReader(response));
            reader.setLenient(true);
            JsonObject jsonObj = JsonParser.parseReader(reader).getAsJsonObject();

            return jsonObj.has("success") && jsonObj.get("success").getAsBoolean();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e) {
            System.err.println("❌ Failed to parse response as JSON: " + e.getMessage());
            return false;
        }
    }



    // Check if username exists
    public boolean isUsernameExists(String username) {
        try {
            String response = ApiClient.get("/auth/check-username?username=" + URLEncoder.encode(username, "UTF-8"));
            System.out.println("🔍 Response from /auth/check-username: " + response);

            if (!response.trim().startsWith("{")) {
                System.err.println("❌ Invalid response (not JSON): " + response);
                return false;
            }

            JsonObject jsonObj = JsonParser.parseString(response).getAsJsonObject();
            return jsonObj.has("exists") && jsonObj.get("exists").getAsBoolean();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }




    private static class Credentials {
        String username;
        String password;
        Credentials(String u, String p) {
            this.username = u;
            this.password = p;
        }
    }
}
