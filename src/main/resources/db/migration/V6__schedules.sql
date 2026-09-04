create table schedules (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    doctor_id varchar(36) not null,
    available_date date not null,
    start_time time(6) not null,
    end_time time(6) not null,
    booked bit not null,
    primary key (id)
) engine=InnoDB;

alter table schedules
    add constraint fk_schedules_doctor foreign key (doctor_id) references doctors (id);
