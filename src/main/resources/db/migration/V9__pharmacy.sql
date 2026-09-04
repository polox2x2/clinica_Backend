create table products (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    name varchar(255) not null,
    description varchar(255),
    price decimal(12,2) not null,
    stock integer not null,
    primary key (id)
) engine=InnoDB;

create table stock_entries (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    product_id varchar(36) not null,
    quantity integer not null,
    unit_cost decimal(12,2),
    note varchar(255),
    primary key (id)
) engine=InnoDB;

alter table stock_entries
    add constraint fk_stock_entries_product foreign key (product_id) references products (id);

create table orders (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    patient_id varchar(36),
    total decimal(12,2) not null,
    primary key (id)
) engine=InnoDB;

alter table orders
    add constraint fk_orders_patient foreign key (patient_id) references patients (id);

create table order_items (
    active bit not null,
    created_at datetime(6),
    updated_at datetime(6),
    id varchar(36) not null,
    order_id varchar(36) not null,
    product_id varchar(36) not null,
    quantity integer not null,
    unit_price decimal(12,2) not null,
    primary key (id)
) engine=InnoDB;

alter table order_items
    add constraint fk_order_items_order foreign key (order_id) references orders (id);
alter table order_items
    add constraint fk_order_items_product foreign key (product_id) references products (id);
