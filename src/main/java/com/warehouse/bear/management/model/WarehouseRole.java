package com.warehouse.bear.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
	private Long id;

//	@Enumerated(EnumType.STRING)
//	@Column(length = 20)
//	private WarehouseRoleEnum name;

	private String code;
	private String description;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "userId")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private WarehouseUser user;

	private String createdAt;
	private String modifiedAt;

	public WarehouseRole(Long id, String code, String description) {
		this.id = id;
		this.code = code;
		this.description = description;
	}
}