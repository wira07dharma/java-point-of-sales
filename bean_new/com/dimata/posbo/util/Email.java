/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.util;

import com.dimata.common.entity.system.PstSystemProperty;
import java.util.Properties;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Dimata 007
 */
public class Email {
    
    String d_email = PstSystemProperty.getValueByName("USER_EMAIL_BOOKING_ONLINE");//"onlinebooking@hotelpuriayu.com";//PstSystemProperty.getValueByName("USER_EMAIL_BOOKING_ONLINE");
    String d_password = PstSystemProperty.getValueByName("PWD_EMAIL_BOOKING_ONLINE");//"omnamasiva";//PstSystemProperty.getValueByName("PWD_EMAIL_BOOKING_ONLINE");
    String d_host = PstSystemProperty.getValueByName("HOST_EMAIL_BOOKING_ONLINE");//"sidatapa.tvnhosting.com";//PstSystemProperty.getValueByName("HOST_EMAIL_BOOKING_ONLINE");
    String d_port = PstSystemProperty.getValueByName("PORT_EMAIL_BOOKING_ONLINE");//"26";//PstSystemProperty.getValueByName("PORT_EMAIL_BOOKING_ONLINE");
    String d_emailhotel = PstSystemProperty.getValueByName("EMAIL_HOTEL_BOOKING_ONLINE");//"dev2@dimata.com";//PstSystemProperty.getValueByName("EMAIL_HOTEL_BOOKING_ONLINE");
    String d_emailBCC = PstSystemProperty.getValueByName("EMAIL_BCC_BOOKING_ONLINE");//"dev2@dimata.com";//PstSystemProperty.getValueByName("EMAIL_BCC_BOOKING_ONLINE");
    
    public Email() {
        
    }

    public void sendEmail(String m_to, String m_subject, String m_text, boolean useSSL) {
        Properties props = new Properties();
        props.put("mail.smtp.user", d_email);
        props.put("mail.smtp.host", d_host);
        props.put("mail.smtp.port", d_port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        //jika menggunakan SSL atau menggunakan email melalui gmail atau melalui SSL
        if (useSSL) {
            props.put("mail.smtp.socketFactory.port", d_port);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        props.put("mail.smtp.socketFactory.fallback", "false");
        
        SecurityManager security = System.getSecurityManager();
        
        try {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            MimeMessage msg = new MimeMessage(session);
            msg.setContent(m_text, "text/html; charset=utf-8");
            msg.setSubject(m_subject);
            msg.setFrom(new InternetAddress(d_email));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(m_to));
            msg.addRecipients(Message.RecipientType.CC, (InternetAddress.parse(d_emailhotel)));
            msg.addRecipients(Message.RecipientType.BCC, (InternetAddress.parse(d_emailBCC)));
            Transport.send(msg);
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }

    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(d_email, d_password);
        }
    }
}
