package ru.aspu.medstat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import ru.aspu.medstat.configurations.MailConfiguration;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@PropertySource("classpath:mail.properties")
public class MailService {
    @Autowired
    public MailConfiguration config;

    @Autowired
    private JavaMailSender sender;

    public boolean send(String to, String subject, String content) {
        try {
            MimeMessage message = sender.createMimeMessage();
            message.setSubject(subject);

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(config.from);
            helper.setTo(to);
            helper.setText(content, true);

            sender.send(message);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return false;
    }
}
