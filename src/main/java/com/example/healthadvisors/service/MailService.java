package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.Appointment;
import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String toEmail, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }

    //HTML mail sending
    @Async
    public void sendHtmlEmail(String to, String subject, User user,
                              String link, String templateName,
                              Locale locale) throws MessagingException {
        final Context context = new Context(locale);
        context.setVariable("name", user.getName());
        context.setVariable("surname", user.getSurname());
        context.setVariable("url", link);

        final String htmlContent = templateEngine.process(templateName, context);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        message.setSubject(subject);
        message.setTo(to);
        message.setText(htmlContent,true);

        this.javaMailSender.send(mimeMessage);

    }


    public void send(Locale locale, User newUser) throws MessagingException {
        String subject = "WELCOME TO OUR WEBSITE";
        String link = "http://localhost:8080/user/activate?token="+ newUser.getToken();
        sendHtmlEmail(newUser.getEmail(),subject, newUser,link,"404", locale);
    }

    public void sendAppointmentEmail(Doctor doctor, Patient patient, Appointment newAppointment) {
        String subject = "New Appointment";
        String message = "Doctor " +  doctor.getUser().getSurname() + ", you have a new appointment. \n " +
                "Patient: " + patient.getUser().getName() + " " + patient.getUser().getSurname() + "\n" +
                "Date " + newAppointment.getAppointmentDate();

        sendEmail(doctor.getUser().getEmail(),subject,message);
    }
}
