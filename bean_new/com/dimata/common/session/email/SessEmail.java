/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.common.session.email;

import com.dimata.common.entity.email.PstSettingEmail;
import com.dimata.common.entity.email.SettingEmail;
import java.util.Properties;
import java.util.Vector;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
*
* @author admin
*/
public class SessEmail
{
//SettingEmail settingEmail = new SettingEmail();
Vector settingEmail=PstSettingEmail.list(0,100,"","");
SettingEmail detailSettingEmail = (SettingEmail)settingEmail.get(0);
String d_email = detailSettingEmail.getEmailName(),//"spideleyek@gmail.com",
d_password = detailSettingEmail.getPassword(),//"-",
d_host = detailSettingEmail.getHost(),//"smtp.gmail.com",
d_port = detailSettingEmail.getPort(); //"465";
/*String d_email = "spideleyek@gmail.com",
d_password = "-",
d_host = "smtp.gmail.com",
d_port = "465";*/
public SessEmail() { }
public  String sendEamil(String m_to , String m_subject , String m_text,String cc)
{
    
    String hasil="Email Sucseed";
    Properties props = new Properties();
    props.put("mail.smtp.user", d_email);
    props.put("mail.smtp.host", d_host);
    props.put("mail.smtp.port", d_port);
    //props.put("mail.smtp.starttls.enable","true");
    props.put("mail.smtp.auth", "true");
    //props.put("mail.smtp.debug", "true");
    props.put("mail.smtp.socketFactory.port", d_port);
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.put("mail.smtp.socketFactory.fallback", "false");
    SecurityManager security = System.getSecurityManager();
    try
    {
        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getInstance(props, auth);
        session.setDebug(true);
        MimeMessage msg = new MimeMessage(session);
        msg.setContent(m_text, "text/html; charset=utf-8");
        msg.setSubject(m_subject);
        msg.setFrom(new InternetAddress(d_email));
        msg.addRecipients(Message.RecipientType.TO,(InternetAddress.parse(m_to))); //agar bisa multiple email
        msg.addRecipients(Message.RecipientType.BCC, (InternetAddress.parse(cc)));
        Transport.send(msg);
    }
    catch (Exception mex)
    {
        hasil=mex.getMessage();
        mex.printStackTrace();
    }
    return hasil;
}
private class SMTPAuthenticator extends javax.mail.Authenticator
{
public PasswordAuthentication getPasswordAuthentication()
{
return new PasswordAuthentication(d_email, d_password);
}
}
}
