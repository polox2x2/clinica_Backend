create table doctors (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    cmp varchar(255) not null,
    user_id varchar(36),
    speciality_id varchar(36),
    primary key (id)
) engine=InnoDB;

alter table doctors add constraint uk_doctors_cmp unique (cmp);
alter table doctors add constraint uk_doctors_user unique (user_id);
alter table doctors
    add constraint fk_doctors_user foreign key (user_id) references users (id);
alter table doctors
    add constraint fk_doctors_speciality foreign key (speciality_id) references specialities (id);
