package com.nodes.project.api.service.impl;
 
 

import com.nodes.project.api.service.IEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
 
 
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
@Service
public class EmailServiceImpl implements IEmailService {
    @Resource
    private JavaMailSender mailSender;
 
 
    @Value("${spring.mail.username}")
    private String fromEmail;
 
 
    /**
     * 发送简单文本邮件
     */
    @Override
    public void sendSimpleMail(String receiveEmail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(receiveEmail);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
    /**
     * 发送Html格式的邮件
     */
    @Override
    public  void sendHtmlMail(String receiveEmail, String subject, String emailContent) throws MessagingException
    {
        init(receiveEmail, subject, emailContent, mailSender, fromEmail);
    }
 
 
    public static void init(String receiveEmail, String subject, String emailContent, JavaMailSender mailSender, String fromEmail) throws MessagingException {
        MimeMessage message= mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true);
        helper.setFrom(fromEmail);
        helper.setTo(receiveEmail);
        helper.setSubject(subject);
        helper.setText(emailContent,true);
        mailSender.send(message);
    }
 
 
    /**
     * 发送包含附件的邮件
     */
    @Override
    public void sendAttachmentsMail(String receiveEmail, String subject, String emailContent, List<String> filePathList) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        //带附件第二个参数true
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(fromEmail);
        helper.setTo(receiveEmail);
        helper.setSubject(subject);
        helper.setText(emailContent, true);
        //添加附件资源
        for (String item : filePathList) {
            FileSystemResource file = new FileSystemResource(new File(item));
            String fileName = item.substring(item.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
        }
        //发送邮件
        mailSender.send(message);
    }
}