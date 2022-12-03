package com.warehouse.bear.management.services;

import com.warehouse.bear.management.constants.WarehouseUserResponse;
import com.warehouse.bear.management.enums.WarehousePackageEnum;
import com.warehouse.bear.management.exception.ObjectNotFoundException;
import com.warehouse.bear.management.model.WarehouseUser;
import com.warehouse.bear.management.model.organization.WarehouseOrganization;
import com.warehouse.bear.management.model.organization.WarehouseOrganizationCollaborator;
import com.warehouse.bear.management.model.utils.WarehouseAddress;
import com.warehouse.bear.management.model.utils.WarehouseContact;
import com.warehouse.bear.management.payload.request.WareHouseOrganizationRequest;
import com.warehouse.bear.management.payload.response.WarehouseMessageResponse;
import com.warehouse.bear.management.payload.response.WarehouseOrganizationData;
import com.warehouse.bear.management.payload.response.WarehouseResponse;
import com.warehouse.bear.management.repository.WarehouseUserRepository;
import com.warehouse.bear.management.repository.organization.WarehouseOrganizationCollaboratorRepository;
import com.warehouse.bear.management.repository.organization.WarehouseOrganizationRepository;
import com.warehouse.bear.management.repository.utils.WarehouseAddressRepository;
import com.warehouse.bear.management.repository.utils.WarehouseContactRepository;
import com.warehouse.bear.management.utils.WarehouseCommonUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WarehouseOrganizationService {

    private WarehouseOrganizationRepository organizationRepository;
    private WarehouseOrganizationCollaboratorRepository organizationCollaboratorRepository;
    private WarehouseUserRepository userRepository;
    private WarehouseAddressRepository addressRepository;
    private WarehouseContactRepository contactRepository;

    public ResponseEntity<Object> insertOrganization(WareHouseOrganizationRequest request) {
        if (organizationRepository.existsByOrganizationName(request.getOrganizationName())) {
            return new ResponseEntity<Object>(
                    new WarehouseMessageResponse(WarehouseUserResponse.WAREHOUSE_OBJECT_EXISTING + request.getOrganizationName()),
                    HttpStatus.FOUND);
        }
        // Create new organization's account
        String organizationId = null;
        WarehouseAddress address = new WarehouseAddress();
        WarehouseContact contact = new WarehouseContact();
        do {
            organizationId = WarehouseCommonUtil.generateOrganizationId();
        } while (organizationRepository.findByOrganizationId(organizationId) != null);

        // Set organization address
        address.setUserId(organizationId);
        address.setCountry(request.getAddress().getCountry());
        address.setState(request.getAddress().getState());
        address.setAddressLine(request.getAddress().getAddressLine());
        address.setZipCode(request.getAddress().getZipCode());

        // Set organization contact
        contact.setUserId(organizationId);
        contact.setPhoneNumber(request.getContact().getPhoneNumber());
        contact.setPhonePrefix(request.getContact().getPhonePrefix());
        contact.setLandlineNumber(request.getContact().getLandlineNumber());
        contact.setLandlinePrefix(request.getContact().getLandlinePrefix());

        WarehouseOrganization organization = new WarehouseOrganization()
                .builder()
                .id(0L)
                .organizationId(organizationId)
                .organizationName(request.getOrganizationName())
                .organizationDescription(request.getOrganizationDescription())
                .website(request.getWebsite())
                .collaborators(new LinkedHashSet<>())
                .referent(request.getReferent())
                .maxNumberCollaborators(request.getMaxNumberCollaborators())
                .organizationPackage(request.getOrganizationPackage())
                .trial(request.isTrial())
                .startPackage(request.getStartPackage())
                .endPackage(request.getEndPackage())
                .createdAt(WarehouseCommonUtil.generateCurrentDateUtil())
                .updatedAt(WarehouseCommonUtil.generateCurrentDateUtil())
                .build();
        addressRepository.save(address);
        contactRepository.save(contact);
        organizationRepository.save(organization);
        return new ResponseEntity<Object>(new WarehouseResponse(organization, WarehouseUserResponse.WAREHOUSE_OBJECT_CREATED),
                HttpStatus.OK);
    }

    public ResponseEntity<Object> getAllOrganizations() {
        try {
            List<WarehouseOrganizationData> organizations = organizationRepository.findAll()
                    .stream().map(organization -> new WarehouseOrganizationData(organization,
                            addressRepository.findByUserId(organization.getOrganizationId()).get(),
                            contactRepository.findByUserId(organization.getOrganizationId()).get())
                    ).collect(Collectors.toList());
            return new ResponseEntity<Object>(organizations, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteOrganization(String organizationId) {
        try {
            WarehouseOrganization organization = organizationRepository.findByOrganizationId(organizationId);
            if (organization != null) {
                organization.setDeletedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                organizationRepository.save(organization);
                return new ResponseEntity<Object>(new WarehouseResponse(organization,
                        WarehouseUserResponse.WAREHOUSE_ORGANIZATION_DELETED), HttpStatus.OK);
            }
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_ORGANIZATION_ERROR_NOT_FOUND_WITH_ID + organizationId), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> findOrganization(String organizationId) {
        try {
            WarehouseOrganization organization = organizationRepository.findByOrganizationId(organizationId);
            if (organization != null) {
                return new ResponseEntity<Object>(new WarehouseResponse(organization, WarehouseUserResponse.WAREHOUSE_ORGANIZATION_FOUND), HttpStatus.OK);
            }
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_ORGANIZATION_ERROR_NOT_FOUND_WITH_ID + organizationId), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> changePackageOrganization(String organizationId, WarehousePackageEnum packageOrganization) {
        try {
            WarehouseOrganization organization = organizationRepository.findByOrganizationId(organizationId);
            if (organization != null) {
                // TODO: Verify first the payment success before save the packages
                organization.setOrganizationPackage(packageOrganization);
                organization.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                organizationRepository.save(organization);
                return new ResponseEntity<Object>(new WarehouseResponse(organization,
                        WarehouseUserResponse.WAREHOUSE_ORGANIZATION_ASSIGNED_COLLABORATORS), HttpStatus.OK);
            }
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_ORGANIZATION_ERROR_NOT_FOUND_WITH_ID + organizationId), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> assignCollaboratorsToOrganization(String organizationId, List<String> collaborators) {
        Set<WarehouseOrganizationCollaborator> collaboratorsToAssign = new LinkedHashSet<>();
        try {
            WarehouseOrganization organization = organizationRepository.findByOrganizationId(organizationId);
            if (organization != null) {
                collaborators.forEach(collaborator -> {
                    WarehouseUser user = userRepository.findByUserId(collaborator)
                            .orElseThrow(() -> new ObjectNotFoundException(WarehouseUserResponse.WAREHOUSE_USER_ERROR_NOT_FOUND_WITH_ID + collaborator));
                    if (user != null) {
                        WarehouseOrganizationCollaborator orgCollaborator = new WarehouseOrganizationCollaborator(
                                0L,
                                organization,
                                user.getUserId()
                        );
                        organizationCollaboratorRepository.save(orgCollaborator);
                        collaboratorsToAssign.add(orgCollaborator);
                    }
                });
                organization.setUpdatedAt(WarehouseCommonUtil.generateCurrentDateUtil());
                organizationRepository.save(organization);
                return new ResponseEntity<Object>(new WarehouseMessageResponse(
                        WarehouseUserResponse.WAREHOUSE_ORGANIZATION_ASSIGNED_COLLABORATORS),
                        HttpStatus.OK);
            }
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_ORGANIZATION_ERROR_NOT_FOUND_WITH_ID + organizationId), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<Object>(new WarehouseMessageResponse(
                    WarehouseUserResponse.WAREHOUSE_USER_GENERIC_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
