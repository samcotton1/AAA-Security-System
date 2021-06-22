import java.lang.*;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class UserValidator {
    int symbols = 0;
    int characters = 0;
    int upper = 0;
    int lower = 0;
    int number = 0;
    String passcode="";
    String specialCharacters = "£/*!@-#$%^&*()\"{}_[]|\\?/<>,.";
    Properties prop = new Properties();

    public Boolean isValidPassword(String password) {
        for (int i = 0; i < password.length(); i++) {
            char letter = password.charAt(i);
            String sLetter = String.valueOf(letter);
            if (specialCharacters.contains(sLetter)) {
                symbols++;
            }

            if (Character.isUpperCase(letter)) {
                upper++;
            }
            if (Character.isLowerCase(letter)) {
                lower++;
            }
            if (Character.isDigit(letter)) {
                number++;
            }
        }
        

        if (symbols > 0 && upper > 0 && number > 0 && password.length() > 7) {
            return true;
        } else {
            return false;
        }

    }

    public void sendPasscode(String email) throws AddressException, MessagingException{
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        Session newSession = Session.getDefaultInstance(prop, null);
        String senderEmail = "noreplyGroupM10@gmail.com";
        String host = "smtp.gmail.com";
        String password = "secur3pa33w0rd123";

        String emailReceipient = email;
        String subject = "Verification Code";
        String emailBody = "Your verification code is " + generateNewPasscode();
        MimeMessage mime = new MimeMessage(newSession);   
        mime.addRecipient(Message.RecipientType.TO, new InternetAddress(emailReceipient));
        mime.setSubject(subject);
        MimeMultipart mult = new MimeMultipart();
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(emailBody, "text/html; charset=utf-8");
        mult.addBodyPart(bodyPart);
        mime.setContent(mult);
        Transport transport = newSession.getTransport("smtp");
        transport.connect(host, senderEmail, password);
        transport.sendMessage(mime, mime.getAllRecipients());
        transport.close();
    }

    public String generateNewPasscode()
    {
        String sequence = "";
        int passcodeLength = 6;
        Random rand = new Random();
        for(int i=0; i<passcodeLength; i++)
        {
            sequence += rand.nextInt(9);
        }
        return sequence;
    }

    public Boolean isCorrectPasscode()
    {
        return false;
    }

    

}