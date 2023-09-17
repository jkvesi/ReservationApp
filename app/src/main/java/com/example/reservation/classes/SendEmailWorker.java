package com.example.reservation.classes;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailWorker extends Worker {
    public SendEmailWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
            try {
                String stringSenderEmail = "julijakvesic15@gmail.com";
                String stringReceiverEmail = "julijakvesic@gmail.com";
                String stringPasswordSenderEmail = "ouyyylpkodsefpei";

                String stringHost = "smtp.gmail.com";

                Properties properties = System.getProperties();

                properties.put("mail.smtp.host", stringHost);
                properties.put("mail.smtp.port", "465");
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.auth", "true");

                javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                    }
                });

                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

                mimeMessage.setSubject("Subject: Android App email");
                mimeMessage.setText("Your reservation is closeee!");

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Transport.send(mimeMessage);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        return Result.success();
    }
}

