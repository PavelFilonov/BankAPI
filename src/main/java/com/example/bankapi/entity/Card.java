package com.example.bankapi.entity;

import com.example.bankapi.entity.auth.User;
import com.example.bankapi.entity.base.BaseEntity;
import com.example.bankapi.enums.CardStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.example.bankapi.enums.CardStatus.ACTIVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"number"}, callSuper = false)
public class Card extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String number;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false)
    private LocalDate deactivationDate;

    @Enumerated(STRING)
    @Column(name = "type", nullable = false)
    private CardStatus status = ACTIVE;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal balance;

}

