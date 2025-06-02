import javax.swing.*;
import java.awt.*;

public class LibraryGUI extends JFrame {

    private final Library library = new Library();
    private JTextArea textArea;

    public LibraryGUI() {
        setTitle("مكتبة الإدارة");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setFont(new Font("Serif", Font.PLAIN, 16));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addBookBtn = new JButton("إضافة كتاب");
        JButton showBooksBtn = new JButton("عرض الكتب");
        JButton removeBookBtn = new JButton("حذف كتاب");
        JButton searchBookBtn = new JButton("البحث عن كتاب");

        buttonPanel.add(addBookBtn);
        buttonPanel.add(showBooksBtn);
        buttonPanel.add(removeBookBtn);
        buttonPanel.add(searchBookBtn);

        add(buttonPanel, BorderLayout.NORTH);

        addBookBtn.addActionListener(e -> addBookDialog());
        showBooksBtn.addActionListener(e -> showBooks());
        removeBookBtn.addActionListener(e -> removeBookDialog());
        searchBookBtn.addActionListener(e -> searchBookDialog());

        
    }

    private void addBookDialog() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField borrowedField = new JTextField();

        Object[] message = {
            "العنوان:", titleField,
            "المؤلف:", authorField,
            "الرمز (ISBN):", isbnField,
            "الكمية:", quantityField,
            "عدد النسخ المستعارة:", borrowedField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "إضافة كتاب جديد", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String isbn = isbnField.getText().trim();
                int quantity = Integer.parseInt(quantityField.getText().trim());
                int borrowed = Integer.parseInt(borrowedField.getText().trim());

                if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "يرجى ملء جميع الحقول النصية");
                    return;
                }

                Book book = new Book(title, author, isbn, quantity, borrowed);
                boolean success = library.addBook(book);

                if (success) {
                    JOptionPane.showMessageDialog(this, "تم إضافة الكتاب بنجاح");
                    showBooks();
                } else {
                    JOptionPane.showMessageDialog(this, "فشل في إضافة الكتاب");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "الكمية وعدد النسخ المستعارة يجب أن يكونا أرقامًا صحيحة");
            }
        }
    }

    private void showBooks() {
        String booksStr = library.printBooks();
        textArea.setText(booksStr);
    }

    private void removeBookDialog() {
        String idStr = JOptionPane.showInputDialog(this, "أدخل رقم الكتاب للحذف:");

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                boolean success = library.removeBook(id);
                if (success) {
                    JOptionPane.showMessageDialog(this, "تم حذف الكتاب");
                    showBooks();
                } else {
                    JOptionPane.showMessageDialog(this, "فشل في حذف الكتاب");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "يرجى إدخال رقم صحيح");
            }
        }
    }

    private void searchBookDialog() {
        String title = JOptionPane.showInputDialog(this, "أدخل عنوان الكتاب للبحث:");

        if (title != null) {
            String results = library.searchBookByTitle(title.trim());
            textArea.setText(results);
        }
    }
}
