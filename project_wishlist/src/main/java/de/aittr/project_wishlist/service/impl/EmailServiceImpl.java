package de.aittr.project_wishlist.service.impl;

import de.aittr.project_wishlist.domain.entity.User;
import de.aittr.project_wishlist.service.interfaces.ConfirmationService;
import de.aittr.project_wishlist.service.interfaces.EmailService;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final Configuration mailConfiguration;
    private final ConfirmationService confirmationService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    public EmailServiceImpl(ConfirmationService confirmationService, Configuration mailConfiguration, JavaMailSender javaMailSender) {
        this.confirmationService = confirmationService;
        this.mailConfiguration = mailConfiguration;
        this.javaMailSender = javaMailSender;


        mailConfiguration.setDefaultEncoding("UTF-8");
        mailConfiguration.setTemplateLoader(new ClassTemplateLoader(EmailService.class, "/mail/"));

    }

    @Override
    public void sendConfirmationEmail(User user) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        String text = generateMessageText(user);

        try {
            helper.setFrom("only.for.it.2023@gmail.com");
            helper.setTo(user.getUsername());
            helper.setSubject("Registration Confirmation");
            helper.setText(text, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        javaMailSender.send(mimeMessage);
    }

    private String generateMessageText(User user) {
        try {
            Template template = mailConfiguration
                    .getTemplate("confirm_registration_mail.ftlh");
            String code = confirmationService.generateConfirmationCode(user);

            Map<String, Object> model = new HashMap<>();
            model.put("name", user.getUsername());
            model.put("link", baseUrl + "/api/users/activate?code=" + code);

            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
