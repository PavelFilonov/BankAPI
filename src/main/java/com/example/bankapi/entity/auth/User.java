package com.example.bankapi.entity.auth;

import com.example.bankapi.entity.Card;
import com.example.bankapi.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class User extends BaseEntity {

	@Column(nullable = false, unique = true)
	private String login;

	@Column(nullable = false)
	private String password;

	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "active_role_id", nullable = false)
	private Role activeRole;

	@ManyToMany(fetch = LAZY)
	@JoinTable(
			name = "user_role",
			joinColumns = @JoinColumn(name = "user_id", nullable = false),
			inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
	)
	private Set<Role> roles;

	@OneToMany(mappedBy = "owner", fetch = LAZY, cascade = ALL, orphanRemoval = true)
	private List<Card> cards;

}
