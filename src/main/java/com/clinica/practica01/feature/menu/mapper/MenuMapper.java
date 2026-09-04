package com.clinica.practica01.feature.menu.mapper;

import com.clinica.practica01.core.mapper.BaseMapper;
import com.clinica.practica01.feature.menu.dto.MenuRequest;
import com.clinica.practica01.feature.menu.dto.MenuResponse;
import com.clinica.practica01.feature.menu.entity.Menu;
import com.clinica.practica01.feature.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuMapper implements BaseMapper<Menu, MenuRequest, MenuResponse> {

    private final MenuRepository menuRepository;

    @Override
    public Menu toEntity(MenuRequest r) {
        Menu m = Menu.builder()
                .label(r.getLabel())
                .icon(r.getIcon())
                .route(r.getRoute())
                .displayOrder(r.getOrder())
                .requiredPermission(r.getRequiredPermission())
                .build();
        applyParent(m, r);
        return m;
    }

    @Override
    public void updateEntity(Menu e, MenuRequest r) {
        e.setLabel(r.getLabel());
        e.setIcon(r.getIcon());
        e.setRoute(r.getRoute());
        e.setDisplayOrder(r.getOrder());
        e.setRequiredPermission(r.getRequiredPermission());
        applyParent(e, r);
    }

    @Override
    public MenuResponse toResponse(Menu e) {
        MenuResponse res = new MenuResponse();
        res.setLabel(e.getLabel());
        res.setIcon(e.getIcon());
        res.setRoute(e.getRoute());
        res.setOrder(e.getDisplayOrder());
        res.setRequiredPermission(e.getRequiredPermission());
        if (e.getParent() != null) {
            res.setParentId(e.getParent().getId());
        }
        return res;
    }

    private void applyParent(Menu m, MenuRequest r) {
        m.setParent(r.getParentId() == null ? null
                : menuRepository.findById(r.getParentId()).orElse(null));
    }
}
