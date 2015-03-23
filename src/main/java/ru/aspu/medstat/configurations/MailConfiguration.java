package ru.aspu.medstat.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfiguration {
    @Value("${mail.protocol}")
    public String protocol;
    @Value("${mail.host}")
    public String host;
    @Value("${mail.port}")
    public int port;
    @Value("${mail.smtp.auth}")
    public boolean auth;
    @Value("${mail.smtp.starttls.enable}")
    public boolean starttls;
    @Value("${mail.from}")
    public String from;
    @Value("${mail.username}")
    public String username;
    @Value("${mail.password}")
    public String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.auth", auth);
        sender.setJavaMailProperties(props);

        sender.setSession(Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                }));

        return sender;
    }
}
