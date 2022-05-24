package com.warehouse.bear.management.utils;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class WarehouseMailUtil {

    @Value("${spring.mail.username}")
    private String warehouseEmailSender;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration config;

    public WarehouseResponse warehouseSendMail(WarehouseUser user, Map<String, Object> model) {
        WarehouseResponse response = new WarehouseResponse();
        MimeMessage message = sender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // add attachment
            //helper.addAttachment("signature.png", new ClassPathResource("signature.png"));

            Template t = config.getTemplate("verify-email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

            helper.setTo(user.getEmail());
            helper.setText(html, true);
            helper.setSubject(WarehouseUserConstants.WAREHOUSE_SUBJECT_EMAIL_VERIFICATION);
            helper.setFrom(warehouseEmailSender);
            sender.send(message);

            response.setMessage(WarehouseUserResponse.WAREHOUSE_USER_VERIFICATION_EMAIL + user.getEmail());
            response.setObject(Boolean.TRUE);

        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail Sending failure : " + e.getMessage());
            response.setObject(Boolean.FALSE);
        }

        return response;
    }
}
