create table patients (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    document_id varchar(255) not null,
    date_of_birth date,
    phone varchar(255),
    user_id varchar(36),
    primary key (id)
) engine=InnoDB;

alter table patients add constraint uk_patients_document unique (document_id);
alter table patients add constraint uk_patients_user unique (user_id);
alter table patients
    add constraint fk_patients_user foreign key (user_id) references users (id);
