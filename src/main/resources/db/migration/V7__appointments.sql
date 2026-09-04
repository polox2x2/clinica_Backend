create table appointments (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    patient_id varchar(36) not null,
    schedule_id varchar(36) not null,
    status varchar(20) not null,
    notes varchar(255),
    primary key (id)
) engine=InnoDB;

alter table appointments
    add constraint fk_appointments_patient foreign key (patient_id) references patients (id);
alter table appointments
    add constraint fk_appointments_schedule foreign key (schedule_id) references schedules (id);
