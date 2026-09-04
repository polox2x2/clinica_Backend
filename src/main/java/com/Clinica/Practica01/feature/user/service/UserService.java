package com.Clinica.Practica01.feature.user.service;

import com.Clinica.Practica01.core.service.CrudService;
import com.Clinica.Practica01.feature.user.dto.UserRequest;
import com.Clinica.Practica01.feature.user.dto.UserResponse;

/** Contrato del servicio de User. */
public interface UserService extends CrudService<UserRequest, UserResponse> {
}
