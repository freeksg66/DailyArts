package com.github.arts_email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by legao005426 on 2018/6/27.
 */

public class SendMailUtil {
    public static boolean send_qqmail(String strMail, String strTitle, String strText){
        boolean bret = false;
        try
        {
            final Properties props = new Properties();

            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", "smtp.qq.com");
            //你自己的邮箱
            props.put("mail.user", "2331743678@qq.com");
            //你开启pop3/smtp时的验证码
            props.put("mail.password", "aijzijmovorudjdf");
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.starttls.enable", "true");

            Authenticator authenticator = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            // 创建邮件消息
            final MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            String username = props.getProperty("mail.user");
            InternetAddress form = new InternetAddress(username);
            message.setFrom(form);

            InternetAddress to = new InternetAddress(strMail);
            message.setRecipient(MimeMessage.RecipientType.TO, to);

            // 设置邮件标题
            message.setSubject(strTitle);

            // 设置邮件的内容体
            message.setContent(strText, "text/html;charset=UTF-8");

            // 发送邮件
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            bret = true;
        }
        catch (AddressException e) {
            e.printStackTrace();
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return bret;
    }
}
