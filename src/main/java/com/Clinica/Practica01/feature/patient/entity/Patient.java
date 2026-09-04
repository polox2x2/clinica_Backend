package com.Clinica.Practica01.feature.patient.entity;

import com.Clinica.Practica01.core.domain.BaseEntity;
import com.Clinica.Practica01.feature.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "patients")
public class Patient extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String documentId;

    private LocalDate dateOfBirth;

    private String phone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
