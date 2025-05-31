
public class Book {
	private int id;
private String author, title,isbn;
private int quantity;
private int borrowed;
public Book(String title, String author, String isbn, int quantity,int borrowed) {
	super();
	this.title = title;
	this.author = author;
	this.isbn = isbn;
	this.quantity = quantity;
	this.borrowed = borrowed;
}
public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public String getAuthor() {
	return author;
}

public void setAuthor(String author) {
	this.author = author;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getIsbn() {
	return isbn;
}

public void setIsbn(String isbn) {
	this.isbn = isbn;
}

public int getQuantity() {
	return quantity;
}

public void setQuantity(int quantity) {
	this.quantity = quantity;
}

public int getBorrowed() {
	return borrowed;
}

public void setBorrowed(int borrowed) {
	this.borrowed = borrowed;
}

@Override
public String toString() {
	return "Book [id=" + id + ", author=" + author + ", title=" + title + ", isbn=" + isbn + ", quantity=" + quantity
			+ ", borrowed=" + borrowed + "]";
}



}
