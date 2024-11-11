package com.mony.notification.service;

import com.google.api.client.json.JsonFactory;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.Gmail;
import com.mony.notification.config.GmailOAuth2;
import com.mony.notification.dtos.PaymentEmailConfirmationDto;
import com.mony.notification.model.EmailModel;
import com.mony.notification.model.enums.EmailStatus;
import com.mony.notification.repository.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Properties;

@Service
public class EmailService {
    private final EmailRepository emailRepository;
    //private final JavaMailSender mailSender;
    //private static final String CLIENT_SECRET_FILE = "../config/credentials/client_secret.json";

    private final String EMAIL_FROM = "atendimento.msmony@gmail.com";

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;

    }

    public void sendEmail(EmailModel emailModel) {
        try{
            Gmail service = GmailOAuth2.getGmailService();
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(EMAIL_FROM));
            email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(emailModel.getReceiver()));
            email.setSubject(emailModel.getSubject());
            email.setText(emailModel.getBody());

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.getEncoder().encodeToString(rawMessageBytes);

            Message message = new Message();
            message.setRaw(encodedEmail);

            message = service.users().messages().send("me", message).execute();
            System.out.println("Mensagem enviada com ID: " + message.getId());
        }
        catch (MessagingException e) {
            System.err.println("Erro ao compor o email: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Erro de I/O ao enviar o email: " + e.getMessage());
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            System.err.println("Erro de segurança ao configurar o Gmail service: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            emailRepository.save(emailModel);
        }

    }

    public EmailModel createPaymentConfirmationEmail(PaymentEmailConfirmationDto paymentConfDto) {
        EmailModel emailModel = new EmailModel();
        String status = paymentStatusTranslation(paymentConfDto.paymentStatus());
        emailModel.setUserId(paymentConfDto.userId());
        emailModel.setReceiver(paymentConfDto.emailTo());
        emailModel.setSender(EMAIL_FROM);
        emailModel.setSubject("Pagamento - Pedido nº: #"+paymentConfDto.orderCode());
        emailModel.setBody(String.format("Prezado(a) "+paymentConfDto.nameUser()+"\nO pagamento" +
                "#"+paymentConfDto.paymentId()+" no valor de R$"+paymentConfDto.value()+"foi "+status+
                "\nCordialmente, equipe Acabou o Mony."));
        return emailModel;
    }

    public String paymentStatusTranslation(String status){
        if(status.equalsIgnoreCase("CONFIRMED"))
            return "CONFIRMADO";
        if(status.equalsIgnoreCase("CANCELLED"))
            return "CANCELADO";

        return status;
    }

    /*
    public EmailModel createCodeEmail(Code2FADto code2FADto) {
        EmailModel emailModel = new EmailModel();
        emailModel.setUserId(code2FADto.userId());
        emailModel.setEmailTo(code2FADto.emailTo());
        emailModel.setSubject("Código de verificação: " + code2FADto.code2FA());
        emailModel.setText("O seu código de verificação é " + code2FADto.code2FA() + ".\n" +
                "Se você não solicitou isso, simplesmente ignore esta mensagem.\n\n" +
                "Cordialmente,\n" +
                "A Equipe do Acabou o Mony!");
        return emailModel;
    }
    */
}
