create table medical_records (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    patient_id varchar(36) not null,
    allergies varchar(1000),
    blood_type varchar(20),
    primary key (id)
) engine=InnoDB;

alter table medical_records add constraint uk_medical_records_patient unique (patient_id);
alter table medical_records
    add constraint fk_medical_records_patient foreign key (patient_id) references patients (id);

create table medical_record_entries (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    record_id varchar(36) not null,
    doctor_id varchar(36) not null,
    appointment_id varchar(36),
    reason varchar(1000),
    diagnosis varchar(2000),
    treatment varchar(2000),
    notes varchar(2000),
    primary key (id)
) engine=InnoDB;

alter table medical_record_entries
    add constraint fk_mre_record foreign key (record_id) references medical_records (id);
alter table medical_record_entries
    add constraint fk_mre_doctor foreign key (doctor_id) references doctors (id);
alter table medical_record_entries
    add constraint fk_mre_appointment foreign key (appointment_id) references appointments (id);
