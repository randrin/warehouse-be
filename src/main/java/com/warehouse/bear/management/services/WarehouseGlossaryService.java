package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.exception.ObjectNotFoundException;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.glossary.WarehouseGlossary;
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

import java.util.Comparator;
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
                List<String> message = checkIfGlossaryAlreadyExistsWithCodeAndLanguageOrObject(request);
                if (message.get(0) == null) {
                    List<WarehouseGlossary> glossaries = request.stream().map(glossary ->
                            new WarehouseGlossary()
                                    .builder()
                                    .id(0L)
                                    .code(glossary.getCode().toUpperCase())
                                    .description(glossary.getDescription())
                                    .object(glossary.getObject().toLowerCase())
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
                return new ResponseEntity<Object>(new WarehouseResponse(message, message.get(0)), HttpStatus.FOUND);
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

    private List<String> checkIfGlossaryAlreadyExistsWithCodeAndLanguageOrObject(List<WarehouseGlossaryRequest> request) {
        return request.stream().map(glossary -> {
            if (glossaryRepository.existsByCodeAndLanguage(glossary.getCode(), glossary.getLanguage())) {
                return WarehouseUserResponse.WAREHOUSE_OBJECT_EXISTING + glossary.getCode() + " - " + glossary.getLanguage();
            }
            if (glossaryRepository.existsByObject(glossary.getObject())) {
                return WarehouseUserResponse.WAREHOUSE_OBJECT_EXISTING + glossary.getObject();
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
                    .orElseThrow(() -> new ObjectNotFoundException(WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + code + " - " + language));
            Optional<WarehouseUser> user = userRepository.findByUserId(request.getUserId());
            if (glossary.getObject().compareToIgnoreCase(request.getObject()) != 0 &&
                    glossary.getCode().compareToIgnoreCase(request.getCode()) != 0) {
                if (!glossaryRepository.existsByCodeAndLanguage(request.getCode(), request.getLanguage()) &&
                        !glossaryRepository.existsByObject(request.getObject())) {
                    List<WarehouseGlossary> glossariesObject = glossaryRepository.findByObject(glossary.getObject());
                    glossariesObject.forEach(gly -> {
                        gly.setObject(request.getObject().toLowerCase());
                        gly.setDescription(gly.getLanguage().compareToIgnoreCase(language) == 0 ? request.getDescription() : gly.getDescription());
                        gly.setUser(user.get());
                        glossary.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                        glossaryRepository.save(gly);
                    });
                    List<WarehouseGlossary> glossariesCode = glossaryRepository.findByCode(glossary.getCode());
                    glossariesCode.forEach(gly -> {
                        gly.setCode(request.getCode().toUpperCase());
                        gly.setDescription(gly.getLanguage().compareToIgnoreCase(language) == 0 ? request.getDescription() : gly.getDescription());
                        gly.setUser(user.get());
                        glossary.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                        glossaryRepository.save(gly);
                    });
                }
                return new ResponseEntity<Object>(new WarehouseResponse(glossary, WarehouseUserResponse.WAREHOUSE_OBJECT_UPDATED), HttpStatus.OK);
            } else if (glossary.getObject().compareToIgnoreCase(request.getObject()) == 0) {
                if (glossary.getCode().compareToIgnoreCase(request.getCode()) == 0) {
                    glossary.setDescription(request.getDescription());
                    glossary.setUser(user.get());
                    glossary.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                    glossaryRepository.save(glossary);
                    return new ResponseEntity<Object>(new WarehouseResponse(glossary, WarehouseUserResponse.WAREHOUSE_OBJECT_UPDATED), HttpStatus.OK);
                } else { // If the operator update the object, find all previous glossary with the same object and make the update
                    if (!glossaryRepository.existsByCodeAndLanguage(request.getCode(), request.getLanguage())) {
                        List<WarehouseGlossary> glossaries = glossaryRepository.findByCode(glossary.getCode());
                        glossaries.forEach(gly -> {
                            gly.setCode(request.getCode().toUpperCase());
                            gly.setDescription(gly.getLanguage().compareToIgnoreCase(language) == 0 ? request.getDescription() : gly.getDescription());
                            gly.setUser(user.get());
                            glossary.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                            glossaryRepository.save(gly);
                        });
                        return new ResponseEntity<Object>(new WarehouseResponse(glossary, WarehouseUserResponse.WAREHOUSE_OBJECT_UPDATED), HttpStatus.OK);
                    }
                    return new ResponseEntity<Object>(new WarehouseResponse(request, WarehouseUserResponse.WAREHOUSE_OBJECT_EXISTING + request.getCode() + " - " + request.getLanguage()), HttpStatus.FOUND);
                }
            } else { // If the operator update the code, find all previous glossary with the same code and make the update
                if (!glossaryRepository.existsByObject(request.getObject())) {
                    List<WarehouseGlossary> glossaries = glossaryRepository.findByObject(glossary.getObject());
                    glossaries.forEach(gly -> {
                        gly.setObject(request.getObject().toLowerCase());
                        gly.setDescription(gly.getLanguage().compareToIgnoreCase(language) == 0 ? request.getDescription() : gly.getDescription());
                        gly.setUser(user.get());
                        glossary.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                        glossaryRepository.save(gly);
                    });
                    return new ResponseEntity<Object>(new WarehouseResponse(glossary, WarehouseUserResponse.WAREHOUSE_OBJECT_UPDATED), HttpStatus.OK);
                }
                return new ResponseEntity<Object>(new WarehouseResponse(request, WarehouseUserResponse.WAREHOUSE_OBJECT_EXISTING + request.getObject()), HttpStatus.FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_OBJECT_ERROR_NOT_FOUND + code + " - " + language),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> getAllGlossaries() {
        try {
            List<WarehouseGlossary> glossaries = glossaryRepository.findAll()
                    .stream().sorted(Comparator.comparing(WarehouseGlossary::getCreatedAt).reversed())
                    .collect(Collectors.toList());
            return new ResponseEntity<Object>(glossaries, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
