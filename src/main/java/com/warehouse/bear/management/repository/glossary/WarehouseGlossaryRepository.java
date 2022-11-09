package com.warehouse.bear.management.repository.glossary;

import com.warehouse.bear.management.model.glossary.WarehouseGlossary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseGlossaryRepository extends JpaRepository<WarehouseGlossary, Long> {

    boolean existsByObject(String object);
    boolean existsByCodeAndLanguage(String code, String language);
    List<WarehouseGlossary> findByObjectAndLanguage(String object, String language);
    List<WarehouseGlossary> findByObjectAndCode(String object, String code);
    List<WarehouseGlossary> findByObject(String object);
    List<WarehouseGlossary> findByCode(String code);
    Optional<WarehouseGlossary> findByCodeAndLanguage(String code, String language);
}
