package user;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

    private final String fromEmail = "woofwisdom954@gmail.com";
    private final String password = "ww123123!";

    public void sendResetEmail(String toEmail, String resetLink) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Password Reset Request");
            message.setText("To reset your password, please click on the link below:\n" + resetLink);

            Transport.send(message);
            System.out.println("Password reset link sent successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

