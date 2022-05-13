package com.warehouse.bear.management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="users_images")
@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
public class WarehouseImageUser {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private String id;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "fileType")
    private String fileType;

    @Lob
    @Column(name = "pic")
    private byte[] data;

    public WarehouseImageUser(String fileName, String fileType, byte[] data) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
    }
}

/*public class WarehouseImageUser {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Lob
    @Column(name = "pic")
    private byte[] pic;

    //Custom Construtor
    public WarehouseImageUser(String name, String type, byte[] pic) {
        this.name = name;
        this.type = type;
        this.pic = pic;
    }*/