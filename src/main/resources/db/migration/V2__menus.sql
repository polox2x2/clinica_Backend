-- Tabla de menus (jerarquica, filtrada por permiso).
create table menus (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    label varchar(255) not null,
    icon varchar(255),
    route varchar(255),
    display_order integer,
    required_permission varchar(255),
    parent_id varchar(36),
    primary key (id)
) engine=InnoDB;

alter table menus
    add constraint fk_menus_parent foreign key (parent_id) references menus (id);
