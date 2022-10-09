package com.warehouse.bear.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name="images")
public class WarehouseImageUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String fileName;
    private String fileType;
    private String imageType;
    private long fileSize;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private WarehouseUser user;

    @Lob
    private byte[] data;

    private String lastUploadDate;

    public WarehouseImageUser(String fileName, String fileType, String imageType, long fileSize, WarehouseUser user, byte[] data, String lastUploadDate) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.imageType = imageType;
        this.user = user;
        this.data = data;
        this.lastUploadDate = lastUploadDate;
    }
}