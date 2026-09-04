package com.clinica.practica01.feature.absence.entity;

import com.clinica.practica01.core.domain.BaseEntity;
import com.clinica.practica01.feature.doctor.entity.Doctor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/** Ausencia/bloqueo de un medico: rango de fechas no disponible (vacaciones, etc.). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "doctor_absences")
public class DoctorAbsence extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String reason;
}
