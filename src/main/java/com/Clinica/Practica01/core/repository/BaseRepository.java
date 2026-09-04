package com.Clinica.Practica01.core.repository;

import com.Clinica.Practica01.core.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * Repositorio base: CRUD por UUID + soporte de Specifications para el
 * search/paginacion generico. Los repos de cada feature lo extienden.
 */
@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity>
                extends JpaRepository<E, UUID>, JpaSpecificationExecutor<E> {
}
