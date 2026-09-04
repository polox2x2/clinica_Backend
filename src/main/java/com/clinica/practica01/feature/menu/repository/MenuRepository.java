package com.clinica.practica01.feature.menu.repository;

import com.clinica.practica01.core.repository.BaseRepository;
import com.clinica.practica01.feature.menu.entity.Menu;

import java.util.List;

public interface MenuRepository extends BaseRepository<Menu> {
    List<Menu> findByActiveTrueOrderByDisplayOrderAsc();
    boolean existsByLabel(String label);
}
