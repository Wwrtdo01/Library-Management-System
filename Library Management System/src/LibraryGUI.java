import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LibraryGUI extends JFrame {
    private final Library library = new Library();

    public LibraryGUI() {
        setTitle("لوحة التحكم - المكتبة");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        buttonPanel.setBackground(Color.WHITE);

        JButton addBookBtn = new JButton("➕ إضافة كتاب");
        JButton showBooksBtn = new JButton("📚 عرض الكتب");
        JButton searchBookBtn = new JButton("🔍 البحث عن كتاب");
        JButton removeBookBtn = new JButton("🗑️ حذف كتاب");

        JButton[] buttons = { addBookBtn, showBooksBtn, searchBookBtn, removeBookBtn };
        for (JButton btn : buttons) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(250, 40));
            buttonPanel.add(btn);
            buttonPanel.add(Box.createVerticalStrut(15));
        }

        add(buttonPanel);

        // Action listeners
        addBookBtn.addActionListener(e -> addBookDialog());
        showBooksBtn.addActionListener(e -> showBooksWindow());
        searchBookBtn.addActionListener(e -> searchBookWindow());
        removeBookBtn.addActionListener(e -> removeBookDialog());
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
                } else {
                    JOptionPane.showMessageDialog(this, "فشل في إضافة الكتاب");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "الكمية وعدد النسخ المستعارة يجب أن يكونا أرقامًا صحيحة");
            }
        }
    }

    private void showBooksWindow() {
        JFrame frame = new JFrame("عرض الكتب");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.RED);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setText(library.printBooks());

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    private void searchBookWindow() {
        String title = JOptionPane.showInputDialog(this, "أدخل عنوان الكتاب للبحث:");

        if (title != null && !title.trim().isEmpty()) {
            JFrame frame = new JFrame("نتائج البحث");
            frame.setSize(600, 400);
            frame.setLocationRelativeTo(null);

            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setBackground(Color.BLACK);
            textArea.setForeground(Color.RED);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            textArea.setText(library.searchBookByTitle(title.trim()));

            JScrollPane scrollPane = new JScrollPane(textArea);
            frame.add(scrollPane);

            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "لم يتم إدخال عنوان");
        }
    }

    private void removeBookDialog() {
        String idStr = JOptionPane.showInputDialog(this, "أدخل رقم الكتاب للحذف:");

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                boolean success = library.removeBook(id);
                if (success) {
                    JOptionPane.showMessageDialog(this, "تم حذف الكتاب");
                } else {
                    JOptionPane.showMessageDialog(this, "فشل في حذف الكتاب");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "يرجى إدخال رقم صحيح");
            }
        }
    }
}
