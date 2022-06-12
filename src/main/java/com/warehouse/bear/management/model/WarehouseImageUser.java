package com.warehouse.bear.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private WarehouseUser user;

    @Lob
    private byte[] data;

    private String lastUploadDate;

    public WarehouseImageUser(String fileName, String fileType, WarehouseUser user, byte[] data, String lastUploadDate) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.user = user;
        this.data = data;
        this.lastUploadDate = lastUploadDate;
    }
}