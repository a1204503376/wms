package com.nodes.project.api.service;
 
 
import javax.mail.MessagingException;
import java.util.List;
 
 
public interface IEmailService {
    /**
     * 发送简单文本邮件
     */
    void sendSimpleMail(String receiveEmail, String subject, String content);
    /**
     * 发送HTML格式的邮件
     */
    void sendHtmlMail(String receiveEmail, String subject, String emailContent) throws MessagingException;
    /**
     * 发送包含附件的邮件
     */
    void sendAttachmentsMail(String receiveEmail, String subject, String emailContent, List<String> filePathList) throws MessagingException;
}