package com.warehouse.bear.management.utils;

import com.warehouse.bear.management.constants.WarehouseUserConstants;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.WarehouseVerifyIdentity;
import com.warehouse.bear.management.model.admin.WarehouseAdminUser;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.repository.admin.WarehouseAdminUserRepository;
import com.warehouse.bear.management.services.WarehouseTokenService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WarehouseMailUtil {

    @Value("${spring.mail.username}")
    private String warehouseEmailSender;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration config;

    @Autowired
    private WarehouseUserRepository userRepository;

    @Autowired
    private WarehouseAdminUserRepository adminUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private WarehouseTokenService warehouseTokenService;

    public WarehouseResponse warehouseSendMail(String email, Map<String, Object> model, String verifyType) {
        WarehouseResponse response = new WarehouseResponse();
        MimeMessage message = sender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // add attachment
            //helper.addAttachment("signature.png", new ClassPathResource("signature.png"));

            Template t = null;
            if (verifyType == WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_RESET_PASSWORD) {
                helper.setSubject(WarehouseUserConstants.WAREHOUSE_SUBJECT_EMAIL_FORGOT_PASSWORD);
                t = config.getTemplate("reset-password-template.ftl");
            }
            if (verifyType == WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_EMAIL) {
                helper.setSubject(WarehouseUserConstants.WAREHOUSE_SUBJECT_EMAIL_VERIFICATION);
                t = config.getTemplate("verify-email-template.ftl");
            }
            if (verifyType == WarehouseUserConstants.WAREHOUSE_VERIFY_TYPE_EMAIL_ADMIN_USER) {
                helper.setSubject(WarehouseUserConstants.WAREHOUSE_SUBJECT_EMAIL_NEW_USER);
                t = config.getTemplate("temporal-password-template.ftl");
            }

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
            helper.setTo(email);
            helper.setText(html, true);
            helper.setFrom(warehouseEmailSender);
            sender.send(message);

            response.setMessage(WarehouseUserResponse.WAREHOUSE_USER_VERIFICATION_EMAIL + email);
            response.setObject(Boolean.TRUE);

        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail Sending failure : " + e.getMessage());
            response.setObject(Boolean.FALSE);
        }

        return response;
    }

    public ResponseEntity<Object> warehouseVerificationEmail(String email, String password, String verifyType) {

        Optional<WarehouseUser> user = null;
        Optional<WarehouseAdminUser> adminUser = null;
        Map<String, Object> model = new HashMap<>();

        try {
            user = userRepository.findByEmail(email);
            if (!user.isPresent()) {
                adminUser = adminUserRepository.findByEmail(email);
            }

            WarehouseVerifyIdentity verifyIdentity = warehouseTokenService
                    .createVerificationEmailLink(user.isPresent() ? user.get().getUserId() : adminUser.get().getUserId());
            model.put("link", verifyIdentity.getLink());
            model.put("verifyType", verifyIdentity.getVerifyType());
            model.put("expirationLink", verifyIdentity.getExpiryDate());
            model.put("name", user.isPresent() ? user.get().getUsername().toUpperCase() : adminUser.get().getUsername().toUpperCase());
            model.put("userId", user.isPresent() ? user.get().getUserId().toUpperCase() : adminUser.get().getUserId().toUpperCase());
            model.put("temporalPassword", password);

            WarehouseResponse response = warehouseSendMail(user.isPresent() ? user.get().getEmail() : adminUser.get().getEmail(), model, verifyType);

            return new ResponseEntity<Object>(response,
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_VERIFICATION_EMAIL + email),
                    HttpStatus.NOT_FOUND);
        }
    }
}
