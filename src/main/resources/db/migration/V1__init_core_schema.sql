-- Esquema inicial del CORE (seguridad/RBAC). Generado desde las entidades JPA.
-- IDs UUID como CHAR(36), borrado logico (active), timestamps de auditoria.

create table permissions (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    description varchar(255),
    group_name varchar(255),
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table roles (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    description varchar(255),
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table users (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id)
) engine=InnoDB;

create table role_permissions (
    permission_id varchar(36) not null,
    role_id varchar(36) not null,
    primary key (permission_id, role_id)
) engine=InnoDB;

create table user_roles (
    role_id varchar(36) not null,
    user_id varchar(36) not null,
    primary key (role_id, user_id)
) engine=InnoDB;

alter table permissions add constraint uk_permissions_name unique (name);
alter table roles add constraint uk_roles_name unique (name);
alter table users add constraint uk_users_email unique (email);
alter table users add constraint uk_users_username unique (username);

alter table role_permissions
    add constraint fk_role_permissions_permission foreign key (permission_id) references permissions (id);
alter table role_permissions
    add constraint fk_role_permissions_role foreign key (role_id) references roles (id);
alter table user_roles
    add constraint fk_user_roles_role foreign key (role_id) references roles (id);
alter table user_roles
    add constraint fk_user_roles_user foreign key (user_id) references users (id);
