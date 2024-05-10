package com.yaroslav.booknetwork.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,
            String activationCode,
            String subject
    ) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage(); // Создание объекта MimeMessage
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MULTIPART_MODE_MIXED,
                    UTF_8.name()
            );

            Map<String, Object> properties = new HashMap<>();
            properties.put("username", username);
            properties.put("confirmationUrl", confirmationUrl);
            properties.put("activation_code", activationCode);

            Context context = new Context();
            context.setVariables(properties);

            String templateName = (emailTemplate == null) ? "confirm-email" : emailTemplate.getName();
            String template = templateEngine.process(templateName, context);

            helper.setFrom("book-net@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(template, true); // Установка текста с поддержкой HTML

            mailSender.send(mimeMessage); // Отправка электронной почты

            log.info("Email sent successfully to {}", to); // Успешное логирование

        } catch (MessagingException | MailException e) { // Обработка исключений
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e); // Логирование ошибки
        }
    }
}
