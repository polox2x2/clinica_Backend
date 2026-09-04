package com.Clinica.Practica01.feature.menu.service;

import com.Clinica.Practica01.core.service.AbstractCrudService;
import com.Clinica.Practica01.feature.menu.dto.MenuNode;
import com.Clinica.Practica01.feature.menu.dto.MenuRequest;
import com.Clinica.Practica01.feature.menu.dto.MenuResponse;
import com.Clinica.Practica01.feature.menu.entity.Menu;
import com.Clinica.Practica01.feature.menu.mapper.MenuMapper;
import com.Clinica.Practica01.feature.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class MenuServiceImpl
        extends AbstractCrudService<Menu, MenuRequest, MenuResponse>
        implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository repository, MenuMapper mapper) {
        super(repository, mapper);
        this.menuRepository = repository;
    }

    @Override
    protected String resourceName() {
        return "Menu";
    }

    @Override
    protected List<String> searchableFields() {
        return List.of("label", "route");
    }

    @Override
    public List<MenuNode> getTree(Set<String> permissions) {
        List<Menu> all = menuRepository.findByActiveTrueOrderByDisplayOrderAsc();
        Map<UUID, List<Menu>> byParent = new LinkedHashMap<>();
        for (Menu m : all) {
            UUID parentId = m.getParent() == null ? null : m.getParent().getId();
            byParent.computeIfAbsent(parentId, k -> new ArrayList<>()).add(m);
        }
        return buildChildren(null, byParent, permissions);
    }

    private List<MenuNode> buildChildren(UUID parentId, Map<UUID, List<Menu>> byParent, Set<String> perms) {
        List<MenuNode> result = new ArrayList<>();
        for (Menu m : byParent.getOrDefault(parentId, List.of())) {
            List<MenuNode> children = buildChildren(m.getId(), byParent, perms);
            boolean permitted = m.getRequiredPermission() == null
                    || perms.contains(m.getRequiredPermission());
            // Se muestra si el usuario tiene el permiso, o si tiene hijos visibles.
            if (permitted || !children.isEmpty()) {
                result.add(MenuNode.builder()
                        .id(m.getId())
                        .label(m.getLabel())
                        .icon(m.getIcon())
                        .route(m.getRoute())
                        .parentId(parentId)
                        .order(m.getDisplayOrder())
                        .children(children)
                        .build());
            }
        }
        return result;
    }
}
