package com.mony.notification.service;


import com.mony.notification.dtos.PaymentEmailConfirmationDto;
import com.mony.notification.model.EmailModel;
import com.mony.notification.model.enums.EmailStatus;
import com.mony.notification.repository.EmailRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
public class EmailService {
    private final EmailRepository emailRepository;
    private final JavaMailSender mailSender;

    private final String EMAIL_FROM = "atendimento.msmony@gmail.com";

    public EmailService(EmailRepository emailRepository, JavaMailSender mailSender) {
        this.emailRepository = emailRepository;
        this.mailSender = mailSender;

    }

    public void sendEmail(EmailModel emailModel) {
        try{
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(emailModel.getReceiver());
            email.setFrom(EMAIL_FROM);
            email.setSubject(emailModel.getSubject());
            email.setText(emailModel.getBody());
            emailModel.setStatus(EmailStatus.SENT);
            emailModel.setEmailSentDate(LocalDateTime.now());
            mailSender.send(email);

        } catch(RuntimeException e){
            emailModel.setStatus(EmailStatus.ERROR);
            System.out.println(e.getMessage());
        }
        finally{
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
        emailModel.setBody(String.format("Prezado(a) "+paymentConfDto.nameUser()+","+"\nO pagamento " +
                "#"+paymentConfDto.paymentId()+" no valor de R$"+paymentConfDto.value()+" foi "+status+
                "."+"\n\nCordialmente, equipe Acabou o Mony."));
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
