package com.Clinica.Practica01.feature.paciente.repository;

import com.Clinica.Practica01.feature.paciente.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
