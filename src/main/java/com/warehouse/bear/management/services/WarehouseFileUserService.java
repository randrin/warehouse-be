package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserEndpoints;
import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.model.WarehouseImageUser;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.payload.response.WarehouseDataResponse;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.repository.WarehouseImageUserRepository;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class WarehouseFileUserService {
    @Autowired
    private WarehouseImageUserRepository warehouseImageUserRepository;

    @Autowired
    private WarehouseUserRepository userRepository;

    public ResponseEntity<Object> saveAttachment(MultipartFile file, String userId, String imageType) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String downloadURl = "";
        WarehouseImageUser imageUser = null;

        try {
            if (fileName.contains("....")) {
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        fileName + WarehouseUserResponse.WAREHOUSE_USER_ERROR_SAVE_ATTACHMENT),
                        HttpStatus.BAD_REQUEST);
            }
            WarehouseUser user = userRepository.findByUserId(userId).get();
            Optional<WarehouseImageUser> userImageFound = warehouseImageUserRepository.findByUserAndImageType(user, imageType);

            // If present, update the current element, otherwise add new one
            if (userImageFound.isPresent()) {
                imageUser = userImageFound.get();
                imageUser.setFileName(fileName);
                imageUser.setFileType(file.getContentType());
                imageUser.setImageType(imageType.toUpperCase(Locale.ROOT));
                imageUser.setData(file.getBytes());
                imageUser.setFileSize(file.getSize());
                imageUser.setLastUploadDate(WarehouseCommonUtil.generateCurrentDateUtil());
            } else {
                imageUser = new WarehouseImageUser(
                        fileName,
                        file.getContentType(),
                        imageType.toUpperCase(Locale.ROOT),
                        file.getSize(),
                        user,
                        file.getBytes(),
                        WarehouseCommonUtil.generateCurrentDateUtil());
            }
            warehouseImageUserRepository.save(imageUser);

            downloadURl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path(WarehouseUserEndpoints.WAREHOUSE_DOWNLOAD_ENDPOINT + "/")
                    .path(user.getUserId())
                    .toUriString();
            WarehouseDataResponse responseData = new WarehouseDataResponse(fileName,
                    downloadURl,
                    file.getContentType(),
                    imageType.toUpperCase(Locale.ROOT),
                    file.getSize());
            return new ResponseEntity<Object>(new WarehouseResponse(responseData, WarehouseUserResponse.WAREHOUSE_USER_UPLOAD_PROFILE), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_NAME + userId),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> getAttachment(String userId) {

        WarehouseUser user = userRepository.findByUserId(userId).get();
        Optional<WarehouseImageUser> warehouseImageUser = warehouseImageUserRepository.findByUser(user);
        if (warehouseImageUser.isPresent()) {
            return new ResponseEntity<Object>(new WarehouseResponse(warehouseImageUser, WarehouseUserResponse.WAREHOUSE_USER_UPLOAD_PROFILE), HttpStatus.OK);
//        return ResponseEntity
//                .ok()
//                .contentType(MediaType.parseMediaType(warehouseImageUser.getFileType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + warehouseImageUser.getFileName() + "\"")
//                .body(new ByteArrayResource(warehouseImageUser.getData()));
        } else {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_ATTACHMENT + userId), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> deleteAttachment(String userId, String imageType) {
        WarehouseUser user = userRepository.findByUserId(userId).get();
        Optional<WarehouseImageUser> warehouseImageUser = warehouseImageUserRepository.findByUserAndImageType(user, imageType);
        if (warehouseImageUser.isPresent()) {
            warehouseImageUserRepository.delete(warehouseImageUser.get());
            return new ResponseEntity<Object>(new WarehouseResponse(warehouseImageUser,
                    WarehouseUserResponse.WAREHOUSE_USER_DELETE_PROFILE), HttpStatus.OK);
        } else {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_ATTACHMENT + userId), HttpStatus.NOT_FOUND);
        }
    }
    public void getExcelUsersData(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HHmm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);

        List<WarehouseUser> listUsers = userRepository.findAll(Sort.by("email").ascending());


        StringWriter writer = new StringWriter();
        CsvBeanWriter beanWriter = new CsvBeanWriter(writer, CsvPreference.EXCEL_PREFERENCE);
        final String[] csvHeader = {"id", "user Id", "username", "full name", "sex", "email", "emailPec","password", "roles", "date Of Birth", "last Login", "created"};
        final String[] nameMapping = {"id", "userId", "username", "fullname", "gender", "email", "emailPec","password", "roles", "dateOfBirth", "lastLogin", "createdAt"};

        beanWriter.writeHeader(csvHeader);
        beanWriter.flush();
        if (listUsers != null && !listUsers.isEmpty()) {
            for (WarehouseUser user : listUsers) {
                beanWriter.write(user, nameMapping);
            }
        }

        beanWriter.close();
        return;
    }

    public Object getPdfUserData(HttpServletResponse response) {
        try {
            DefaultResourceLoader loader = new DefaultResourceLoader();
            InputStream is = loader.getResource("classpath:META-INF/resources/Accepted.pdf").getInputStream();
            IOUtils.copy(is, response.getOutputStream());
            response.setHeader("Content-Disposition", "attachment; filename=Accepted.pdf");
            response.flushBuffer();
            return new ResponseEntity<Object>(new WarehouseResponse(null,WarehouseUserResponse.WAREHOUSE_USER_VIEW_PDF), HttpStatus.OK);
        } catch (IOException ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_ERROR_VIEW_PDF), HttpStatus.NOT_FOUND);
        }
    }
}
