package com.Clinica.Practica01.feature.menu.repository;

import com.Clinica.Practica01.core.repository.BaseRepository;
import com.Clinica.Practica01.feature.menu.entity.Menu;

import java.util.List;

public interface MenuRepository extends BaseRepository<Menu> {
    List<Menu> findByActiveTrueOrderByDisplayOrderAsc();
    boolean existsByLabel(String label);
}
