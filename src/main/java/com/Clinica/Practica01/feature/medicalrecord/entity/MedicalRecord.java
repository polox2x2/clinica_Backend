package com.Clinica.Practica01.feature.medicalrecord.entity;

import com.Clinica.Practica01.core.domain.BaseEntity;
import com.Clinica.Practica01.feature.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;

/**
 * Historia clinica: una por paciente (compartida entre todos los medicos).
 * Contenedor de las atenciones (entries).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "medical_records")
public class MedicalRecord extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    // Datos a nivel de historia (se pueden ampliar: alergias, tipo de sangre...)
    @Column(length = 1000)
    private String allergies;

    @Column(length = 20)
    private String bloodType;
}
