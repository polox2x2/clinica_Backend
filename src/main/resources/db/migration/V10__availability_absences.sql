create table doctor_availabilities (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    doctor_id varchar(36) not null,
    day_of_week varchar(10) not null,
    start_time time(6) not null,
    end_time time(6) not null,
    slot_duration_minutes integer not null,
    primary key (id)
) engine=InnoDB;

alter table doctor_availabilities
    add constraint uk_availability_doctor_day unique (doctor_id, day_of_week);
alter table doctor_availabilities
    add constraint fk_availability_doctor foreign key (doctor_id) references doctors (id);

create table doctor_absences (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    doctor_id varchar(36) not null,
    start_date date not null,
    end_date date not null,
    reason varchar(255),
    primary key (id)
) engine=InnoDB;

alter table doctor_absences
    add constraint fk_absence_doctor foreign key (doctor_id) references doctors (id);
