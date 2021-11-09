package com.itsm.dranswer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Component
public class CustomMailSender {

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    @Autowired
    public CustomMailSender(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine){
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendMail(String template, String subject, String[] to, Context context) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject(subject);
        helper.setTo(to);
        String html = templateEngine.process(template, context);
        helper.setText(html, true);

        javaMailSender.send(message);
    }

    public void sendAcceptMail(String email, String mailsubject, String title, String userName, String subject) {
        String template = "mail/accept";
//        String mailsubject = "[닥터앤서]이메일 인증을 위한 인증번호가 발급되었습니다.";
        String[] to = {email};
        Context ctx = new Context();
        ctx.setVariable("title", title);
        ctx.setVariable("userName", userName);
        ctx.setVariable("subject", subject);

        try {
            this.sendMail(template, mailsubject, to, ctx);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void sendRejectMail(String email, String mailsubject, String title, String userName, String subject, String reject) {
        String template = "mail/reject";
//        String mailsubject = "[닥터앤서]이메일 인증을 위한 인증번호가 발급되었습니다.";
        String[] to = {email};
        Context ctx = new Context();
        ctx.setVariable("title", title);
        ctx.setVariable("userName", userName);
        ctx.setVariable("subject", subject);
        ctx.setVariable("reject", reject);

        try {
            this.sendMail(template, mailsubject, to, ctx);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public void sendReqMail(String email, String mailSubject, String title, String agencyName, String userName) {
        String template = "mail/request";
        String[] to = {email};
        Context ctx = new Context();
        ctx.setVariable("title", title);
        ctx.setVariable("agencyName", agencyName);
        ctx.setVariable("userName", userName);

        try {
            this.sendMail(template, mailSubject, to, ctx);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void sendExpiredAlarmMail(String email, String mailSubject, String title, String msg) {
        String template = "mail/expiredAlarm";
        String[] to = {email};
        Context ctx = new Context();
        ctx.setVariable("title", title);
        ctx.setVariable("msg", msg);

        try {
            this.sendMail(template, mailSubject, to, ctx);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void sendJoinReqMail(String email, String mailSubject, String userName) {
        String template = "mail/joinRequest";
        String[] to = {email};
        Context ctx = new Context();
        ctx.setVariable("userName", userName);

        try {
            this.sendMail(template, mailSubject, to, ctx);
        } catch (MessagingException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
