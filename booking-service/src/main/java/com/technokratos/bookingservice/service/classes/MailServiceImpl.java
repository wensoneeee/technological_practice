package com.technokratos.bookingservice.service.classes;

import com.technokratos.bookingservice.service.interfaces.MailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String mailForm;

    //private final JavaMailSender mailSender;

    private final Template confirmMailTemplate;

    public MailServiceImpl() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateLoader(
                new SpringTemplateLoader(
                        new ClassRelativeResourceLoader(
                                this.getClass()), "/"));
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        try {
            this.confirmMailTemplate = configuration.getTemplate("templates/confirm_mail_page.ftlh");
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void sendMailForConfirm(String email, String code) {
        String mailText = getEmailText(code);
        MimeMessagePreparator messagePreparator = getEmail(email, mailText);
        //mailSender.send(messagePreparator);
    }

    private String getEmailText(String code) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("confirmCode", code);
        StringWriter writer = new StringWriter();
        try {
            confirmMailTemplate.process(attributes, writer);
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    private MimeMessagePreparator getEmail(String email, String mailText) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setText(mailText, true);
            messageHelper.setTo(email);
            messageHelper.setFrom(mailForm);
            messageHelper.setSubject("Регистрация");
        };
        return messagePreparator;
    }
}
