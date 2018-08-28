package com.example.harsha89.recoverdroid;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by Harsha89 on 14-04-2016.
 */
public class MailLgic {
    MimeMessage message;
    private Multipart _multipart;
    private String to_mail;
    private String subject;
    private String mailDescription;
    private String toMail;
    public String fromMail="harshavardhanreddy1995@gmail.com";
    public String pass="89harshareddi";

    public void addAttachment(String filename) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);

        _multipart.addBodyPart(messageBodyPart);
    }

    public MailLgic() {
        _multipart = new MimeMultipart();
    }

    public boolean send() throws Exception {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromMail,pass);
                    }
                });

        session.setDebug(true);


        final Transport transport = session.getTransport();
        InternetAddress addressFrom = new InternetAddress(getToMail());
        message = new MimeMessage(session);
        message.setSender(addressFrom);
        message.setSubject(getSubject());
        message.setContent(getMailDescription(), "text/plain");
        message.addRecipient(Message.RecipientType.TO, addressFrom);
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                try {
//                    if (_multipart != null) {
//                        message.setContent(_multipart);
//                    }
                    transport.connect();
                    Transport.send(message);
                    transport.close();
                    Log.e("@@@@@@@@@@@@@@", "mail sent @@@@@@@@@@@@@@@@");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();

        return false;


    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMailDescription() {
        return mailDescription;
    }

    public void setMailDescription(String mailDescription) {
        this.mailDescription = mailDescription;
    }
    public String getToMail() {
        return to_mail;
    }
    public void setToMail(String to_mail) {
        this.to_mail = to_mail;
    }
}
