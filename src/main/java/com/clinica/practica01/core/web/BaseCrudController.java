package com.clinica.practica01.core.web;

import com.clinica.practica01.core.dto.BaseResponse;
import com.clinica.practica01.core.dto.PagedResponse;
import com.clinica.practica01.core.dto.SearchParams;
import com.clinica.practica01.core.security.PermissionChecker;
import com.clinica.practica01.core.service.CrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * Controlador CRUD generico: expone los 5 endpoints estandar y valida el
 * permiso "Entidad:Accion" en cada uno. Depende de la interface CrudService
 * (no de la implementacion). Cada feature lo extiende con su @RestController +
 * @RequestMapping y el prefijo de permiso.
 */
public abstract class BaseCrudController<Req, Res extends BaseResponse> {

    protected final CrudService<Req, Res> service;
    protected final PermissionChecker permissions;

    protected BaseCrudController(CrudService<Req, Res> service, PermissionChecker permissions) {
        this.service = service;
        this.permissions = permissions;
    }

    /** Prefijo de permiso de la entidad, ej. "User" -> User:Create, User:List... */
    protected abstract String permissionPrefix();

    @Operation(summary = "Crear registro",
            description = "Crea un nuevo registro. Requiere el permiso <Entidad>:Create.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "403", description = "Sin permiso")
    })
    @PostMapping
    public ResponseEntity<Res> create(@Valid @RequestBody Req request) {
        permissions.require(permissionPrefix() + ":Create");
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Obtener por id",
            description = "Devuelve un registro activo por su UUID. Requiere <Entidad>:Read.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Encontrado"),
            @ApiResponse(responseCode = "404", description = "No encontrado"),
            @ApiResponse(responseCode = "403", description = "Sin permiso")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Res> getById(@PathVariable UUID id) {
        permissions.require(permissionPrefix() + ":Read");
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Actualizar registro",
            description = "Actualiza un registro existente. Requiere <Entidad>:Update.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualizado"),
            @ApiResponse(responseCode = "404", description = "No encontrado"),
            @ApiResponse(responseCode = "403", description = "Sin permiso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Res> update(@PathVariable UUID id, @Valid @RequestBody Req request) {
        permissions.require(permissionPrefix() + ":Update");
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Eliminar (borrado logico)",
            description = "Marca el registro como inactivo (no lo borra fisicamente). Requiere <Entidad>:Delete.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminado"),
            @ApiResponse(responseCode = "404", description = "No encontrado"),
            @ApiResponse(responseCode = "403", description = "Sin permiso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        permissions.require(permissionPrefix() + ":Delete");
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar (busqueda paginada)",
            description = "Lista paginada con parametros search, sortBy, sortDirection, page, pageSize. "
                    + "Devuelve el envelope estandar (items, totalCount, hasNextPage...). Requiere <Entidad>:List.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagina de resultados"),
            @ApiResponse(responseCode = "403", description = "Sin permiso")
    })
    @GetMapping
    public ResponseEntity<PagedResponse<Res>> list(SearchParams params) {
        permissions.require(permissionPrefix() + ":List");
        return ResponseEntity.ok(service.search(params));
    }
}
