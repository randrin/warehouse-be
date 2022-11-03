package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.exception.UserNotFoundException;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.glossary.WarehouseGlossary;
import com.warehouse.bear.management.model.utils.WarehouseHelp;
import com.warehouse.bear.management.payload.request.WarehouseGlossaryRequest;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.repository.glossary.WarehouseGlossaryRepository;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WarehouseGlossaryService {

    private WarehouseGlossaryRepository glossaryRepository;
    private WarehouseUserRepository userRepository;

    public ResponseEntity<Object> insertGlossary(List<WarehouseGlossaryRequest> request) {
        try {
            Optional<WarehouseUser> user = userRepository.findByUserId(request.get(0).getUserId());
            if (user != null) {
                List<String> message = checkIfGlossaryAlreadyExistsWithCodeAndLanguage(request);
                if (message.get(0) == null) {
                    List<WarehouseGlossary> glossaries = request.stream().map(glossary ->
                            new WarehouseGlossary()
                                    .builder()
                                    .id(0L)
                                    .code(glossary.getCode())
                                    .description(glossary.getDescription())
                                    .object(glossary.getObject())
                                    .language(glossary.getLanguage())
                                    .user(user.get())
                                    .createdAt(WarehouseCommonUtil.generateCurrentDateUtil())
                                    .updatedAt(WarehouseCommonUtil.generateCurrentDateUtil())
                                    .build()
                    ).collect(Collectors.toList());
                    glossaryRepository.saveAll(glossaries);
                    return new ResponseEntity<Object>(new WarehouseResponse(glossaries, WarehouseUserResponse.WAREHOUSE_OBJECT_CREATED),
                            HttpStatus.OK);
                }
                return new ResponseEntity<Object>(new WarehouseResponse(message, WarehouseUserResponse.WAREHOUSE_OBJECT_EXISTING), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID + request.get(0).getUserId()),
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<String> checkIfGlossaryAlreadyExistsWithCodeAndLanguage(List<WarehouseGlossaryRequest> request) {
        return request.stream().map(glossary -> {
            if (glossaryRepository.existsByCodeAndLanguage(glossary.getCode(), glossary.getLanguage())) {
                return WarehouseUserResponse.WAREHOUSE_OBJECT_EXISTING + glossary.getCode() + " - " + glossary.getLanguage();
            }
            return null;
        }).collect(Collectors.toList());
    }

    public ResponseEntity<Object> getGlossariesByObjectAndLanguage(String object, String language) {
        try {
            if (glossaryRepository.existsByObject(object)) {
                List<WarehouseGlossary> glossaries = glossaryRepository.findByObjectAndLanguage(object, language);
                return new ResponseEntity<Object>(glossaries, HttpStatus.OK);
            }
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + object),
                    HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteGlossary(String code) {
        try {
            List<WarehouseGlossary> glossaries = glossaryRepository.findByCode(code);
            glossaryRepository.deleteAll(glossaries);
            return new ResponseEntity<Object>(new WarehouseResponse(glossaries, WarehouseUserResponse.WAREHOUSE_OBJECT_DELETED), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + code),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> updateGlossary(WarehouseGlossaryRequest request, String code, String language) {
        try {
            WarehouseGlossary glossary = glossaryRepository.findByCodeAndLanguage(code, language)
                    .orElseThrow(() -> new UserNotFoundException(WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + code + " - " + language));
            Optional<WarehouseUser> user = userRepository.findByUserId(request.getUserId());
            if (glossary.getObject().compareToIgnoreCase(request.getObject()) == 0) {
                if (glossary.getCode().compareToIgnoreCase(request.getCode()) == 0) {
                    glossary.setDescription(request.getDescription());
                    glossary.setUser(user.get());
                    glossary.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                    glossaryRepository.save(glossary);
                } else { // If the operator update the object, find all previous glossary with the same object and make the update
                    List<WarehouseGlossary> glossaries = glossaryRepository.findByCode(glossary.getCode());
                    glossaries.forEach(gly -> {
                        gly.setCode(request.getCode());
                        gly.setDescription(gly.getLanguage().compareToIgnoreCase(language) == 0 ? request.getDescription() : gly.getDescription());
                        gly.setUser(user.get());
                        glossary.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                        glossaryRepository.save(gly);
                    });
                }
            } else { // If the operator update the code, find all previous glossary with the same code and make the update
                List<WarehouseGlossary> glossaries = glossaryRepository.findByObject(glossary.getObject());
                glossaries.forEach(gly -> {
                    gly.setObject(request.getObject());
                    gly.setDescription(gly.getLanguage().compareToIgnoreCase(language) == 0 ? request.getDescription() : gly.getDescription());
                    gly.setUser(user.get());
                    glossary.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                    glossaryRepository.save(gly);
                });
            }
            return new ResponseEntity<Object>(new WarehouseResponse(glossary, WarehouseUserResponse.WAREHOUSE_OBJECT_UPDATED), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + code + " - " + language),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> getAllGlossaries() {
        try {
            List<WarehouseGlossary> glossaries = glossaryRepository.findAll();
            return new ResponseEntity<Object>(glossaries, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
