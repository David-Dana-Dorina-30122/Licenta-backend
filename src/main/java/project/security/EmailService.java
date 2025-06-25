package project.security;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

//    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException {
//        MimeMessage message = emailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(text, true);
//
//        emailSender.send(message);
//    }

    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException {
        System.out.println("Trimitere email către: " + to);

        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        emailSender.send(message);

        System.out.println("Email trimis cu succes!");
    }

    public void sendQRCodeEmail(String to, byte[] qrImageBytes) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("Your Reservation QR Code");
        helper.setText("Scan the QR code below to check in to your reservation.");

        helper.addAttachment("reservation-qr.png", new ByteArrayResource(qrImageBytes));

        emailSender.send(message);
    }

}
