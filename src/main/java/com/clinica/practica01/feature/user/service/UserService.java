package com.clinica.practica01.feature.user.service;

import com.clinica.practica01.core.service.CrudService;
import com.clinica.practica01.feature.user.dto.UserRequest;
import com.clinica.practica01.feature.user.dto.UserResponse;

/** Contrato del servicio de User. */
public interface UserService extends CrudService<UserRequest, UserResponse> {
}
