import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.*;  
import java.awt.event.*;  
import javax.swing.*;
import javax.swing.border.Border;

public class GUIBuilder implements ActionListener {

    private JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JButton btnLogin = new JButton("Login");
    JLabel lblEmail = new JLabel("Email");
    JButton btnRegister = new JButton("Regsiter");
    JButton btnRetry = new JButton("Retry");
    JLabel lblPassword = new JLabel("Password");
    JLabel lblRegType = new JLabel("Enter Type of User");
    JLabel lblRegEmail = new JLabel("Enter Email");
    JLabel lblRegPassword = new JLabel("Enter Password");
    JLabel lblWelcome = new JLabel("Welcome to the Hospital Online System");
    JLabel lblLoading = new JLabel("Loading System...");
    JLabel lblBadLogin = new JLabel("Incorrect Username or Password, Please Try Again");
    JLabel lblEmptyField = new JLabel("Not All Fields Filled, Please Try Again");
    JLabel lblBadRegister = new JLabel("Email or Password not Allowed, Please Try Again");
    JLabel lblBadPassword = new JLabel("Password Must Be at least 7 Character and contain at least 1 symbol, number, an upper case letter");
    JButton btnRegSubmit = new JButton("Submit Register");
    String[] options = { "Patient", "Hospital Staff", "Regulator", "Admin" };
    
    final JComboBox<String> cb = new JComboBox<String>(options);

    JTextField txtUser = new JTextField("");
    JPasswordField txtPassword = new JPasswordField("");
    JTextField txtRegUser = new JTextField("");
    
    JPasswordField txtRegPassword = new JPasswordField("");
    JTextField txtRegType = new JTextField("");
    JLabel lblSuccess = new JLabel("Success");
    String email;
    String password;
    String regEmail;
    String regPassword;
    String userType;
    
    GUIBuilder() 
    {
        frame.setSize(500, 500);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        lblEmail.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnRegister.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblEmail.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridLayout(0, 1));
        btnRegister.addActionListener(this);
        btnRegSubmit.addActionListener(this);
        btnRetry.addActionListener(this);
        btnLogin.addActionListener(this);
        txtPassword.setEchoChar('*');
        txtRegPassword.setEchoChar('*');
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Hospital Server");
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
 }

    
    public void loadLogin()
    {
        panel.removeAll();
        panel.add(lblEmail);
        panel.add(txtUser);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(btnLogin);
        panel.add(btnRegister);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    public void loadRegister()
    {
        panel.removeAll();
        panel.add(lblRegType);
        panel.add(cb);
        panel.add(lblRegEmail);
        panel.add(txtRegUser);
        panel.add(lblRegPassword);
        panel.add(txtRegPassword);
        panel.add(btnRegSubmit);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }
    public void loadMainScreen()
    {
        panel.removeAll();
        panel.add(lblSuccess);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    public void loadWelcomeScreen()
    {
        panel.removeAll();
        panel.add(lblWelcome);
        panel.add(lblLoading);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }
    public void loadBadLogin()
    {
        panel.removeAll();
        panel.add(lblBadLogin);
        panel.add(btnRetry);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    public void loadBadRegister()
    {
        panel.removeAll();
        panel.add(lblBadRegister);
        panel.add(btnRetry);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }
    public void loadBadPassword()
    {
        panel.removeAll();
        panel.add(lblBadPassword);
        panel.add(btnRetry);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }
    public void loadEmptyField()
    {
        panel.removeAll();
        panel.add(lblEmptyField);
        panel.add(btnRetry);
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == btnLogin)
        {  
            if(txtUser.getText().equals("") ||txtPassword.getText().equals(""))
            {
                loadEmptyField();
            }
            else
            {
                email = txtUser.getText();
                password = txtPassword.getText();
                loadMainScreen();
            }
            
        }
        else if(e.getSource() == btnRegister)
        {
            loadRegister();
        }
        else if(e.getSource() == btnRegSubmit)
        {
            if(txtRegUser.getText().equals("") || txtRegPassword.getText().equals(""))
            {
                loadEmptyField();
            }
            else
            {
                regEmail = txtRegUser.getText();
                regPassword = txtRegPassword.getText();
                userType = cb.getSelectedItem().toString();
                loadLogin();
            }
        }
        else if(e.getSource() == btnRetry)
        {
            loadLogin();
        }
    }

    public String getLoginEmail()
    {
        return email;
    }
    public String getLoginPassword()
    {
        return password;
    }

    public String getRegisterEmail()
    {
        return regEmail;
    }

    public String getRegisterPassword()
    {
        return regPassword;
    }

    public String getUserType()
    {
        return userType;
    }
}
