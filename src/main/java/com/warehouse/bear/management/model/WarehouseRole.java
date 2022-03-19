package com.warehouse.bear.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.warehouse.bear.management.enums.WarehouseRoleEnum;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "roles")
public class WarehouseRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private WarehouseRoleEnum name;

}