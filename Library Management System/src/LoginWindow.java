import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    private final Library library = new Library();
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPass;
    private JButton loginButton, createAccountButton;

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

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "يرجى إدخال اسم المستخدم أو كلمة المرور");
            return;
        }

        loginButton.setEnabled(false);
        createAccountButton.setEnabled(false);

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return library.authenticateAdmin(username, password);
            }

            @Override
            protected void done() {
                loginButton.setEnabled(true);
                createAccountButton.setEnabled(true);
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(LoginWindow.this, "تم تسجيل الدخول بنجاح");
                        LibraryGUI gui = new LibraryGUI();
                        gui.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(LoginWindow.this, "المستخدم غير موجود أو كلمة المرور خاطئة");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LoginWindow.this, "حدث خطأ في الاتصال بالخادم");
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void handleCreateAccount() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "يرجى إدخال اسم المستخدم أو كلمة المرور");
            return;
        }

        loginButton.setEnabled(false);
        createAccountButton.setEnabled(false);

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                if (library.isUsernameExists(username)) {
                    return false;
                }
                return library.registerAdmin(username, password);
            }

            @Override
            protected void done() {
                loginButton.setEnabled(true);
                createAccountButton.setEnabled(true);
                try {
                    boolean success = get();
                    if (success) {
                        JOptionPane.showMessageDialog(LoginWindow.this, "تم إنشاء حساب جديد");
                    } else {
                        JOptionPane.showMessageDialog(LoginWindow.this, "اسم المستخدم موجود مسبقاً");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LoginWindow.this, "حدث خطأ في الاتصال بالخادم");
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginWindow::new);
    }
}
