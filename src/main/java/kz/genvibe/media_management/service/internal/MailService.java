package kz.genvibe.media_management.service.internal;

public interface MailService {
    void sendMail(String to, String subject, String text);
    void sendHtmlMail(String to, String subject, String htmlContent);
}
