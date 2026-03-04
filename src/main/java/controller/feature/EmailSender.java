/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.feature;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;

/**
 *
 * @author TR_NGHIA
 */

public class EmailSender {

    private final String fromEmail;
    private final String appPassword;
    private final String smtpHost;
    private final int smtpPort;
    private final Session session;

    public EmailSender() {
        this.fromEmail = EnvConfig.getRequired("EMAIL_FROM");
        this.appPassword = EnvConfig.getRequired("EMAIL_PASSWORD");
        this.smtpHost = EnvConfig.get("SMTP_HOST", "smtp.gmail.com");
        this.smtpPort = EnvConfig.getInt("SMTP_PORT", 587);

        this.session = createSession();

        System.out.println("? EmailSender initialized successfully!");
        System.out.println("  From: " + fromEmail);
        System.out.println("  SMTP: " + smtpHost + ":" + smtpPort);
    }

    public EmailSender(String fromEmail, String appPassword, String smtpHost, int smtpPort) {
        this.fromEmail = fromEmail;
        this.appPassword = appPassword;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.session = createSession();
    }

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", String.valueOf(smtpPort));
        props.put("mail.smtp.auth", "true");
        
        if (smtpPort == 465) {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
            props.put("mail.smtp.ssl.trust", smtpHost);
        } else {
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
            props.put("mail.smtp.ssl.trust", smtpHost);
        }
        
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");
        
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });
    }

    public boolean sendTextEmail(String toEmail, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("? Email sent successfully to: " + toEmail);
            return true;

        } catch (MessagingException e) {
            System.err.println("? Error sending email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendHtmlEmail(String toEmail, String subject, String htmlBody) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(htmlBody, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("? HTML email sent successfully to: " + toEmail);
            return true;

        } catch (MessagingException e) {
            System.err.println("? Error sending HTML email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendEmailWithAttachment(String toEmail, String subject, String body, String filePath) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("? File not found: " + filePath);
                return false;
            }
            ((MimeBodyPart) messageBodyPart).attachFile(file);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("? Email with attachment sent successfully to: " + toEmail);
            return true;

        } catch (Exception e) {
            System.err.println("? Error sending email with attachment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendEmailWithMultipleAttachments(String toEmail, String subject, String body, List<String> filePaths) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            for (String filePath : filePaths) {
                File file = new File(filePath);
                if (!file.exists()) {
                    System.err.println("? Warning: File not found, skipping: " + filePath);
                    continue;
                }
                messageBodyPart = new MimeBodyPart();
                ((MimeBodyPart) messageBodyPart).attachFile(file);
                multipart.addBodyPart(messageBodyPart);
            }

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("? Email with multiple attachments sent successfully to: " + toEmail);
            return true;

        } catch (Exception e) {
            System.err.println("? Error sending email with multiple attachments: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendEmailToMultipleRecipients(List<String> toEmails, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));

            InternetAddress[] addresses = new InternetAddress[toEmails.size()];
            for (int i = 0; i < toEmails.size(); i++) {
                addresses[i] = new InternetAddress(toEmails.get(i));
            }
            message.setRecipients(Message.RecipientType.TO, addresses);

            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("? Email sent successfully to " + toEmails.size() + " recipients");
            return true;

        } catch (MessagingException e) {
            System.err.println("? Error sending email to multiple recipients: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendEmailWithCcBcc(String toEmail, List<String> ccEmails, List<String> bccEmails,
                                      String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

            if (ccEmails != null && !ccEmails.isEmpty()) {
                InternetAddress[] ccAddresses = new InternetAddress[ccEmails.size()];
                for (int i = 0; i < ccEmails.size(); i++) {
                    ccAddresses[i] = new InternetAddress(ccEmails.get(i));
                }
                message.setRecipients(Message.RecipientType.CC, ccAddresses);
            }

            if (bccEmails != null && !bccEmails.isEmpty()) {
                InternetAddress[] bccAddresses = new InternetAddress[bccEmails.size()];
                for (int i = 0; i < bccEmails.size(); i++) {
                    bccAddresses[i] = new InternetAddress(bccEmails.get(i));
                }
                message.setRecipients(Message.RecipientType.BCC, bccAddresses);
            }

            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("? Email with CC/BCC sent successfully");
            return true;

        } catch (MessagingException e) {
            System.err.println("? Error sending email with CC/BCC: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}