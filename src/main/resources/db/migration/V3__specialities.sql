create table specialities (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    name varchar(255) not null,
    description varchar(255),
    parent_id varchar(36),
    primary key (id)
) engine=InnoDB;

alter table specialities add constraint uk_specialities_name unique (name);
alter table specialities
    add constraint fk_specialities_parent foreign key (parent_id) references specialities (id);
