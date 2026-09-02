package com.Clinica.Practica01.feature.medico.repository;

import com.Clinica.Practica01.feature.medico.entity.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
}
