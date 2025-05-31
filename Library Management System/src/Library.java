import java.util.*;

import com.mysql.cj.exceptions.RSAException;

import java.sql.*;

public class Library {
//
public void addBook(Book b) {
	String sql = "INSERT INTO books (title, author, isbn, quantity, borrowed) VALUES (?, ?, ?, ?, ?)";

try (Connection conn = LibraryDB.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

    stmt.setString(1, b.getTitle());
    stmt.setString(2, b.getAuthor());
    stmt.setString(3, b.getIsbn());
    stmt.setInt(4, b.getQuantity());
    stmt.setInt(5, b.getBorrowed());

    int affectedR = stmt.executeUpdate();

    if (affectedR > 0) {
        ResultSet rst = stmt.getGeneratedKeys();
        if (rst.next()) {
            int generatedId = rst.getInt(1);
            b.setId(generatedId);  
        }
        System.out.println("Book is successfully added to the database!");
    } else {
        System.out.println("Insertion failed!");
    }
} catch (SQLException e) {
    e.printStackTrace();
}
}
	
public String printBooks() {
	StringBuilder sb = new StringBuilder();
	String sql = "SELECT * FROM books";
	try(Connection cs = LibraryDB.getConnection()){
		PreparedStatement st = cs.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		boolean found = false;
		while(rs.next()) {
			found = true;
			int id = rs.getInt("id");
            String title = rs.getString("title");
            String author = rs.getString("author");
            String isbn = rs.getString("isbn");
            int quantity = rs.getInt("quantity");
            int borrowed = rs.getInt("borrowed");
            
            sb.append("رقم الكتاب : ").append(id).append("\n");
            sb.append("العنوان :  ").append(title).append("\n");
            sb.append("المؤلف :  ").append(author).append("\n");
            sb.append("الرمز :  ").append(isbn).append("\n");
            sb.append("الكمية :  ").append(quantity).append("\n");
            sb.append("عدد النسخ المستعارة :  ").append(borrowed).append("\n");
            sb.append("--------------------------------------------\n");

		}
		if(!found) {
			sb.append("لا توجد كتب في قاعدة البيانات");
		}
	}
	catch(SQLException e) {
		sb.append("حدث خطأ في جلب البيانات");
	}
	return sb.toString();
}

public boolean removeBook(int id) {
	String sql = "DELETE FROM books where id = ?";
	try(Connection c = LibraryDB.getConnection()){
		PreparedStatement st = c.prepareStatement(sql);
		st.setInt(1, id);
		int affectedRow = st.executeUpdate();
		return affectedRow>0;
		}
	catch(SQLException e) {
		e.printStackTrace();
		return false;
	}
}
public String searchBookByTitle(String title) {
	StringBuilder sb = new StringBuilder();
	String sql = "SELECT *  FROM books WHERE title LIKE ? ";
	try(Connection coo = LibraryDB.getConnection()){
		PreparedStatement st = coo.prepareStatement(sql);
		st.setString(1, "%"+title+"%");
		ResultSet re = st.executeQuery();
		boolean found = false;
		while(re.next()) {
			found = true;
			int id = re.getInt("id");
            String bookTitle = re.getString("title");
            String author = re.getString("author");
            String isbn = re.getString("isbn");
            int quantity = re.getInt("quantity");
            int borrowed = re.getInt("borrowed");
            //
            sb.append("رقم الكتاب: ").append(id).append("\n");
            sb.append("العنوان: ").append(title).append("\n");
            sb.append("المؤلف: ").append(author).append("\n");
            sb.append("الرمز: ").append(isbn).append("\n");
            sb.append("الكمية: ").append(quantity).append("\n");
            sb.append("عدد النسخ المستعارة: ").append(borrowed).append("\n");
            sb.append("-------------------------\n");
		}
		if(!found) {
			sb.append("لا توجد نتائج مطابقة");
		}
	}
	catch(SQLException ex) {
		ex.printStackTrace()	;
		sb.append("حدث خطأ في جلب البيانات");
	}
	return sb.toString();
		
}
public boolean authenticateAdmin(String username, String password) {
	String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
	try(Connection conn = LibraryDB.getConnection()){
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,username);
		pstmt.setString(2, password);
		ResultSet re = pstmt.executeQuery();
		return re.next();
	}
	catch(SQLException e) {
		e.printStackTrace();
		return false;
	}
}
public boolean registerAdmin(String username, String password) {
	String insertSql = "INSERT INTO admins (username, password) VALUES (?,?)";
	try(Connection conn = LibraryDB.getConnection()){
		PreparedStatement insert = conn.prepareStatement(insertSql);
		insert.setString(1, username);
		insert.setString(2, password);
		int rowsI = insert.executeUpdate();
		return rowsI>0;
	}
	catch(SQLException e) {
		e.printStackTrace();
		return false;
	}
	
}
public boolean isUsernameExists(String username) {
    String sql = "SELECT * FROM admins WHERE username = ?";
    try (Connection conn = LibraryDB.getConnection()) {
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
}
