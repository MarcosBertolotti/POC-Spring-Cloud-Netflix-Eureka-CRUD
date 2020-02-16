insert into users (username, password, enabled, first_name, last_name, email) values ('marcos', '$2a$10$w0TD/nfw.2IVHfYsZza8fe3Ug2aOShG2sMXgmgVO4m/TGRp/h0q.q', true, 'marcos', 'bertolotti', 'marcos@gmail.com');
insert into users (username, password, enabled, first_name, last_name, email) values ('admin', '$2a$10$Le.mYbWC.3XkCeb4gMAdpu7HK2zjATZWXzPYL3VkRII.RlHPht/Ru', true, 'pepe', 'argento', 'racingpasion@gmail.com');

insert into roles (name) values ('ROLE_USER');
insert into roles (name) values ('ROLE_ADMIN');

insert into users_roles (user_id, role_id) values (1, 1);
insert into users_roles (user_id, role_id) values (2, 2);
insert into users_roles (user_id, role_id) values (2, 1);