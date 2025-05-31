import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    Library library = new Library();
    JTextField usernameField;
    JPasswordField passwordField;
    JCheckBox showPass;
    JButton loginButton, createAccountButton;

    public LoginWindow() {
        setTitle("تسجيل الدخول إلى النظام");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        showPass = new JCheckBox("عرض كلمة المرور");
        loginButton = new JButton("تسجيل الدخول");
        createAccountButton = new JButton("إنشاء حساب جديد");

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("اسم المستخدم"));
        panel.add(usernameField);

        panel.add(new JLabel("كلمة المرور"));
        panel.add(passwordField);

        panel.add(showPass);
        panel.add(new JLabel()); 

        panel.add(loginButton);
        panel.add(createAccountButton);

        add(panel);

        showPass.addActionListener(e -> {
            if (showPass.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•'); 
            }
        });

        loginButton.addActionListener(e -> handleLogin());
        createAccountButton.addActionListener(e -> handleCreateAccount());

        setVisible(true);
    }

    private void handleLogin() { // entering login data
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "يرجى إدخال اسم المستخدم أو كلمة المرور");
            return;
        }

        boolean exists = library.authenticateAdmin(username, password);
        if (exists) {
            JOptionPane.showMessageDialog(this, "تم تسجيل الدخول بنجاح");
            LibraryGUI gui = new LibraryGUI(); 
            gui.setVisible(true);              
            this.dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "المستخدم غير موجود أو كلمة المرور خاطئة");
        }
    }

    private void handleCreateAccount() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "يرجى إدخال اسم المستخدم أو كلمة المرور");
            return;
        }

        if (isUsernameExists(username)) {
            JOptionPane.showMessageDialog(this, "اسم المستخدم موجود مسبقًا، يرجى تسجيل الدخول");
            return;
        }

        if (addNewAdmin(username, password)) {
            JOptionPane.showMessageDialog(this, "تم إنشاء الحساب بنجاح، يمكنك الآن تسجيل الدخول");
        } else {
            JOptionPane.showMessageDialog(this, "فشل في إنشاء الحساب، حاول لاحقًا");
        }
    }

    private boolean isUsernameExists(String username) {
        return library.isUsernameExists(username);
    }

    private boolean addNewAdmin(String username, String password) {
        return library.registerAdmin(username, password);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow());
    }
}
