package com.warehouse.bear.management.model;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class WarehouseRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private WarehouseErole name;

	public WarehouseRole() {

	}

	public WarehouseRole(WarehouseErole name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WarehouseErole getName() {
		return name;
	}

	public void setName(WarehouseErole name) {
		this.name = name;
	}
}