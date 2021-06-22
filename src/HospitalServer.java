import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.MessagingException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;

public class HospitalServer 
{
    public static final String[] protocols = new String[] {"TLSv1.3"};
    public static final String[] cipher_suites = new String[] {"TLS_AES_128_GCM_SHA256"};

    private final UserValidator validator = new UserValidator();
    private Connection conn;

    public static void init()
    {
        System.setProperty("javax.net.ssl.keyStore", "data/keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password123");
        System.setProperty("javax.net.ssl.trustStore", "data/TrustStore");
        System.setProperty("javax.net.ssl.trustStorePassword", "password123");
    }

    private void initDB() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:data/hospital");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean register(String email, String password, String role) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (!validator.isValidPassword(password)) {
            // password too weak
            return false;
        }

        PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (email, password, password_salt, password_expiration, role) VALUES (?, ?, ?, ?, ?)");
        stmt.setString(1, email);
        stmt.setString(5, role);
        byte[] salt = RandomNumber.generateSecureNum(20);
        stmt.setString(3, Base64.getEncoder().encodeToString(salt));

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        stmt.setString(2, Base64.getEncoder().encodeToString(hash));

        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        LocalDate local = date.toLocalDate();
        stmt.setDate(4, java.sql.Date.valueOf(local.plusMonths(1)));

        stmt.executeUpdate();
        stmt.close();
        return true;
    }

    private synchronized boolean login(String email, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, MessagingException {
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT password, password_salt, password_expiration FROM users WHERE email=?");
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            byte[] storedPassword = Base64.getDecoder().decode(rs.getString("password").getBytes());
            byte[] salt = Base64.getDecoder().decode(rs.getString("password_salt").getBytes());

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] test = skf.generateSecret(spec).getEncoded();

            java.sql.Date expiry = rs.getDate("password_expiration");

            rs.close();
            stmt.close();

            java.sql.Date currentDate = new java.sql.Date(new java.util.Date().getTime());
            if (currentDate.after(expiry)) {
                // prompt to reset password
                return false;
            } else if (Arrays.equals(test, storedPassword)) {
                validator.sendPasscode(email);
                // prompt user to enter passcode
                return true;
            } else {
                // wrong password
                return false;
            }
        } else {
            // invalid email
            return false;
        }
    }

    private synchronized boolean passwordReset(String email, String newPassword) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        if (!validator.isValidPassword(newPassword)) return false;

        PreparedStatement stmt = conn.prepareStatement(
                "UPDATE users SET password=?, password_salt=?, password_expiration=? WHERE email=?");
        stmt.setString(4, email);

        byte[] salt = RandomNumber.generateSecureNum(20);
        stmt.setString(2, Base64.getEncoder().encodeToString(salt));

        PBEKeySpec spec = new PBEKeySpec(newPassword.toCharArray(), salt, 65536, 128);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        stmt.setString(1, Base64.getEncoder().encodeToString(hash));

        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        LocalDate local = date.toLocalDate();
        stmt.setDate(3, java.sql.Date.valueOf(local.plusMonths(1)));

        stmt.executeUpdate();
        stmt.close();
        return true;
    }

    private void run() throws Exception {
        SSLServerSocket serverSocket = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(8080);
        serverSocket.setEnabledProtocols(protocols);
        serverSocket.setEnabledCipherSuites(cipher_suites);
        System.out.println("Server is ready...");
        while(true)
        {
            try
            {
                //accepts connecting socket
                SSLSocket socket = (SSLSocket) serverSocket.accept();
                //new thread for each connecting client
                ServerThread newThread=new ServerThread(socket);
                //starting thread
                newThread.start();
            }
            catch(Exception e)
            {
                System.out.println("Exception: "+e);
            }
        }
    }

    private class ServerThread extends Thread {
        private SSLSocket socket;

        private ServerThread(SSLSocket socket) {
            this.socket = socket;
        }

        public void run() {
            InputStream is;
            try
            {
                is = new BufferedInputStream(socket.getInputStream());
                byte[] received = new byte[2048];
                while(true)
                {
                    int len = is.read(received);
                    if (len <= 0) throw new IOException();
                    System.out.printf("Server received %d bytes: %s\n", len, new String(received, 0, len));
                    HospitalServer.this.initDB();
                }
            }
            catch (Exception e)
            {
                // e.printStackTrace();
                if (e instanceof javax.net.ssl.SSLException)
                {
                    System.out.println("A client terminated session");
                }
            }
        }
    }

    public static void main(String[] args) throws Exception 
    {
        init();
        HospitalServer server = new HospitalServer();
        server.run();
    }
}
