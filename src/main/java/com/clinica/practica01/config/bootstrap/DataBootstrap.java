package com.clinica.practica01.config.bootstrap;

import com.clinica.practica01.feature.menu.entity.Menu;
import com.clinica.practica01.feature.menu.repository.MenuRepository;
import com.clinica.practica01.feature.permission.entity.Permission;
import com.clinica.practica01.feature.permission.repository.PermissionRepository;
import com.clinica.practica01.feature.role.entity.Role;
import com.clinica.practica01.feature.role.repository.RoleRepository;
import com.clinica.practica01.feature.user.entity.User;
import com.clinica.practica01.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Bootstrap del sistema (idempotente): catalogo de permisos (Entidad:Accion),
 * roles Admin/Medico/Paciente con sus permisos, usuario admin inicial y menus.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataBootstrap implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;
    private final PasswordEncoder passwordEncoder;

    private static final List<String> ENTITIES = List.of(
            "User", "Role", "Permission", "Menu",
            "Speciality", "Doctor", "Patient", "Schedule", "Appointment", "MedicalRecord",
            "Product", "StockEntry", "Order",
            "Availability", "Absence", "Calendar");
    private static final List<String> ACTIONS = List.of("Create", "Read", "Update", "Delete", "List");
    private static final String PERM_APPOINTMENT_LIST = "Appointment:List";
    private static final String PERM_SCHEDULE_LIST = "Schedule:List";

    @Override
    @Transactional
    public void run(String... args) {
        Map<String, Permission> permissions = seedPermissions();

        Role admin = seedRole("Admin", "Acceso total", new HashSet<>(permissions.values()));
        seedRole("Medico", "Personal medico", pick(permissions,
                PERM_APPOINTMENT_LIST, "Appointment:Read", "Appointment:Update",
                "Schedule:Create", "Schedule:Read", "Schedule:Update", "Schedule:Delete", PERM_SCHEDULE_LIST,
                "Patient:Read", "Patient:List",
                "Availability:Read", "Availability:List", "Absence:Read", "Absence:List", "Absence:Create",
                "Calendar:Read", "Calendar:List",
                "MedicalRecord:Create", "MedicalRecord:Read", "MedicalRecord:List"));
        seedRole("Paciente", "Rol por defecto de pacientes", pick(permissions,
                "Appointment:Create", "Appointment:Read", PERM_APPOINTMENT_LIST,
                "Doctor:List", "Speciality:List", PERM_SCHEDULE_LIST,
                "MedicalRecord:Read"));
        // Rol dinamico de ejemplo para la farmacia (el admin puede crear mas)
        seedRole("Caja", "Caja / farmacia", pick(permissions,
                "Product:List", "Product:Read",
                "StockEntry:Create", "StockEntry:List", "StockEntry:Read",
                "Order:Create", "Order:List", "Order:Read"));

        seedAdminUser(admin);
        seedMenus();
    }

    private Map<String, Permission> seedPermissions() {
        Map<String, Permission> map = new LinkedHashMap<>();
        for (String entity : ENTITIES) {
            for (String action : ACTIONS) {
                String name = entity + ":" + action;
                Permission p = permissionRepository.findByName(name).orElseGet(() ->
                        permissionRepository.save(Permission.builder()
                                .name(name).groupName(entity).description(action + " " + entity).build()));
                map.put(name, p);
            }
        }
        return map;
    }

    private Set<Permission> pick(Map<String, Permission> all, String... names) {
        Set<Permission> set = new HashSet<>();
        for (String n : names) {
            Permission p = all.get(n);
            if (p != null) {
                set.add(p);
            }
        }
        return set;
    }

    private Role seedRole(String name, String description, Set<Permission> permissions) {
        return roleRepository.findByName(name).orElseGet(() ->
                roleRepository.save(Role.builder()
                        .name(name).description(description).permissions(permissions).build()));
    }

    private void seedAdminUser(Role admin) {
        if (userRepository.findByUsername("admin").isPresent()) {
            return;
        }
        Set<Role> roles = new HashSet<>();
        roles.add(admin);
        userRepository.save(User.builder()
                .firstName("Admin").lastName("Sistema")
                .username("admin").email("admin@clinica.com")
                .password(passwordEncoder.encode("admin123"))
                .roles(roles).build());
        log.info("Bootstrap: usuario admin creado (admin / admin123)");
    }

    private void seedMenus() {
        if (menuRepository.existsByLabel("Seguridad")) {
            return;
        }
        Menu seguridad = menuRepository.save(Menu.builder()
                .label("Seguridad").icon("shield").displayOrder(1).build());
        menuRepository.save(menuItem("Usuarios", "users", "/dashboard/usuarios", 1, "User:List", seguridad));
        menuRepository.save(menuItem("Roles", "shield", "/dashboard/roles", 2, "Role:List", seguridad));
        menuRepository.save(menuItem("Permisos", "key", "/dashboard/permisos", 3, "Permission:List", seguridad));
        menuRepository.save(menuItem("Menus", "menu", "/dashboard/menus", 4, "Menu:List", seguridad));

        Menu gestion = menuRepository.save(Menu.builder()
                .label("Gestion Clinica").icon("stethoscope").displayOrder(2).build());
        menuRepository.save(menuItem("Especialidades", "activity", "/dashboard/especialidades", 1, "Speciality:List", gestion));
        menuRepository.save(menuItem("Medicos", "stethoscope", "/dashboard/medicos", 2, "Doctor:List", gestion));
        menuRepository.save(menuItem("Pacientes", "user-round", "/dashboard/pacientes", 3, "Patient:List", gestion));
        menuRepository.save(menuItem("Horarios", "calendar", "/dashboard/horarios", 4, PERM_SCHEDULE_LIST, gestion));
        menuRepository.save(menuItem("Disponibilidad", "clock", "/dashboard/disponibilidad", 7, "Availability:List", gestion));
        menuRepository.save(menuItem("Ausencias", "calendar-off", "/dashboard/ausencias", 8, "Absence:List", gestion));
        menuRepository.save(menuItem("Agenda Medica", "calendar-days", "/dashboard/agenda", 9, "Calendar:Read", gestion));
        menuRepository.save(menuItem("Citas", "calendar-check", "/dashboard/citas", 5, PERM_APPOINTMENT_LIST, gestion));
        menuRepository.save(menuItem("Historias Clinicas", "file-text", "/dashboard/historias", 6, "MedicalRecord:List", gestion));

        Menu farmacia = menuRepository.save(Menu.builder()
                .label("Farmacia").icon("pill").displayOrder(3).build());
        menuRepository.save(menuItem("Productos", "package", "/dashboard/productos", 1, "Product:List", farmacia));
        menuRepository.save(menuItem("Entradas de Stock", "package-plus", "/dashboard/entradas", 2, "StockEntry:List", farmacia));
        menuRepository.save(menuItem("Ventas", "shopping-cart", "/dashboard/ventas", 3, "Order:List", farmacia));
    }

    private Menu menuItem(String label, String icon, String route, int order, String permission, Menu parent) {
        return Menu.builder()
                .label(label).icon(icon).route(route)
                .displayOrder(order).requiredPermission(permission).parent(parent)
                .build();
    }
}
