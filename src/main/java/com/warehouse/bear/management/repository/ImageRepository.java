package com.warehouse.bear.management.repository;

import com.warehouse.bear.management.model.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
}