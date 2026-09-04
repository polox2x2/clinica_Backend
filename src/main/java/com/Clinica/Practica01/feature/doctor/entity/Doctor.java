package com.Clinica.Practica01.feature.doctor.entity;

import com.Clinica.Practica01.core.domain.BaseEntity;
import com.Clinica.Practica01.feature.speciality.entity.Speciality;
import com.Clinica.Practica01.feature.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "doctors")
public class Doctor extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String cmp; // Colegio Medico del Peru

    // Cuenta de acceso del medico (el nombre vive aqui, sin duplicar)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speciality_id")
    private Speciality speciality;
}
