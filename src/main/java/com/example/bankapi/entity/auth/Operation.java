package com.example.bankapi.entity.auth;

import com.example.bankapi.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "operation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"code"}, callSuper = false)
public class Operation extends BaseEntity {

	@Column(unique = true, nullable = false)
	private String code;

	@Column(nullable = false)
	private String description;

}

