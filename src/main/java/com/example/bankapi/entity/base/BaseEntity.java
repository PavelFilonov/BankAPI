package com.example.bankapi.entity.base;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.SEQUENCE;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = "id")
public abstract class BaseEntity {

	@Id
	@GeneratedValue(strategy = SEQUENCE)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Version
	@Column(nullable = false)
	private Long version;

	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@CreatedBy
	@Column(updatable = false, nullable = false)
	private Long createdBy;

	@LastModifiedBy
	@Column(nullable = false)
	private Long updatedBy;

}
