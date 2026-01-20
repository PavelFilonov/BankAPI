package com.example.bankapi.entity.auth;

import com.example.bankapi.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "role")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"code"}, callSuper = false)
@ToString
public class Role extends BaseEntity {

	@Column(unique = true, nullable = false)
	private String code;

	@ManyToMany(fetch = LAZY)
	@JoinTable(
			name = "role_operation",
			joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns = @JoinColumn(name = "operation_id")
	)
	private Set<Operation> operations;

}


