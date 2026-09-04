package com.clinica.practica01.feature.medicalrecord.entity;

import com.clinica.practica01.core.domain.BaseEntity;
import com.clinica.practica01.feature.appointment.entity.Appointment;
import com.clinica.practica01.feature.doctor.entity.Doctor;
import jakarta.persistence.*;
import lombok.*;

/** Una atencion registrada en la historia clinica por un medico. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "medical_record_entries")
public class MedicalRecordEntry extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // Cita que origino la atencion (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(length = 1000)
    private String reason;

    @Column(length = 2000)
    private String diagnosis;

    @Column(length = 2000)
    private String treatment;

    @Column(length = 2000)
    private String notes;
}
