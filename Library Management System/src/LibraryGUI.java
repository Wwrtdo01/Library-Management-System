import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LibraryGUI extends JFrame {
    Library logic = new Library();

    public LibraryGUI() {
        setTitle("لوحة تحكم المدير--نظام إدارة المكتبة");
        setSize(300, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        showAdminWindow();  // Build the interface on this frame
    }

    private void showAdminWindow() {
        JButton addBook = new JButton("اضافة كتاب");
        JButton deleteBook = new JButton("حذف كتاب");
        JButton searchBook = new JButton("البحث عن كتاب");
        JButton printBook = new JButton("الاستعلام عن الكتب");

        addBook.addActionListener(event -> showAddBookForm());
        deleteBook.addActionListener(event -> showDeleteBookForm());
        printBook.addActionListener(event -> showAllBookWindow());
        searchBook.addActionListener(event -> showSearchBookForm());

        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(addBook);
        panel.add(deleteBook);
        panel.add(searchBook);
        panel.add(printBook);

        setContentPane(panel); // Set this panel as the main content
    }

    private void showAddBookForm() {
        JFrame form = new JFrame("اضافة كتاب");
        form.setSize(400, 300);
        form.setLocationRelativeTo(null);

        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField isbnField = new JTextField(20);
        JTextField quantityField = new JTextField(20);
        JTextField borrowedField = new JTextField(20);
        JButton submitBtn = new JButton("حفظ و إرسال");

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.add(createRow("عنوان الكتاب:", titleField));
        formPanel.add(createRow("المؤلف:", authorField));
        formPanel.add(createRow("رمز الكتاب:", isbnField));
        formPanel.add(createRow("الكمية:", quantityField));
        formPanel.add(createRow("عدد النسخ المستعارة:", borrowedField));

        JPanel btnPanel = new JPanel();
        btnPanel.add(submitBtn);
        formPanel.add(btnPanel);

        submitBtn.addActionListener(event -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            int quantity;
            int borrowed;

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()
                || quantityField.getText().trim().isEmpty()
                || borrowedField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(form, "يرجى تعبئة جميع الحقول");
                return;
            }

            try {
                quantity = Integer.parseInt(quantityField.getText().trim());
                borrowed = Integer.parseInt(borrowedField.getText().trim());
                Book book = new Book(title, author, isbn, quantity, borrowed);
                logic.addBook(book);
                JOptionPane.showMessageDialog(form, "تمت إضافة الكتاب بنجاح");
                form.dispose();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(form, "أدخل رقم صحيح");
            }
        });

        form.add(formPanel);
        form.setVisible(true);
    }

    private void showAllBookWindow() {
        JFrame frame = new JFrame("قائمة الكتب");
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);

        JTextArea textA = new JTextArea();
        textA.setEditable(false);
        textA.setBackground(Color.BLACK);
        textA.setForeground(Color.red);
        textA.setFont(new Font("Arial", Font.PLAIN, 25));

        JScrollPane scrollPane = new JScrollPane(textA);
        frame.add(scrollPane);

        String bookData = logic.printBooks();
        textA.setText(bookData);

        frame.setVisible(true);
    }

    private void showDeleteBookForm() {
        JFrame frame = new JFrame("حذف كتاب");
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JLabel label = new JLabel("ادخل رقم الكتاب");
        JTextField idField = new JTextField();
        JButton delButton = new JButton("حذف");

        panel.add(label);
        panel.add(idField);
        panel.add(new JLabel());
        panel.add(delButton);

        frame.add(panel);
        frame.setVisible(true);

        delButton.addActionListener(event -> {
            String idText = idField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "يرجى إدخال رقم الكتاب");
                return;
            }
            try {
                int id = Integer.parseInt(idText);
                boolean deleted = logic.removeBook(id);
                if (deleted) {
                    JOptionPane.showMessageDialog(frame, "تم حذف الكتاب بنجاح");
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "لم يتم العثور على كتاب بهذا الرقم");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "يرجى إدخال رقم صحيح");
            }
        });
    }

    private void showSearchBookForm() {
        JFrame frame = new JFrame("البحث عن كتاب");
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);

        JLabel label = new JLabel("أدخل عنوان الكتاب");
        JTextField searchField = new JTextField();
        JButton searchBtn = new JButton("بحث");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel rowPanel = new JPanel(new BorderLayout(5, 5));
        rowPanel.add(label, BorderLayout.WEST);
        rowPanel.add(searchField, BorderLayout.CENTER);

        panel.add(rowPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(searchBtn);

        frame.add(panel);
        frame.setVisible(true);

        searchBtn.addActionListener(event -> {
            String title = searchField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "يرجى إدخال عنوان الكتاب");
                return;
            }
            String results = logic.searchBookByTitle(title);
            showSearchResults(results);
        });
    }

    private void showSearchResults(String results) {
        JFrame frame = new JFrame("نتائج البحث");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setText(results);
        textArea.setBackground(Color.black);
        textArea.setForeground(Color.red);
        textArea.setFont(new Font("Arial", Font.PLAIN, 25));

        JScrollPane scP = new JScrollPane(textArea);
        frame.add(scP);
        frame.setVisible(true);
    }

    private JPanel createRow(String labelText, JTextField textField) {
        JPanel row = new JPanel(new BorderLayout(10, 10));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 30));
        row.add(label, BorderLayout.WEST);
        row.add(textField, BorderLayout.CENTER);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return row;
    }

   
}
