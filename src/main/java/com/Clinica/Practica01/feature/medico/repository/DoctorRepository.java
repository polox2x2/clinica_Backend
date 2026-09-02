package com.Clinica.Practica01.feature.medico.repository;

import com.Clinica.Practica01.feature.medico.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
