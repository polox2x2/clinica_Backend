package com.clinica.practica01.feature.availability.entity;

import com.clinica.practica01.core.domain.BaseEntity;
import com.clinica.practica01.feature.doctor.entity.Doctor;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Plantilla semanal de disponibilidad de un medico: una fila por dia que trabaja,
 * con su horario y la duracion de bloque de ESE dia (puede variar por dia).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "doctor_availabilities",
        uniqueConstraints = @UniqueConstraint(name = "uk_availability_doctor_day",
                columnNames = {"doctor_id", "day_of_week"}))
public class DoctorAvailability extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 10)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    // Duracion del bloque de citas ese dia (15/30/45/60)
    @Column(nullable = false)
    private Integer slotDurationMinutes;
}
