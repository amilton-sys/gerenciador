insert into user (id, account_non_locked, email, failed_attempt, is_enable, salary, lock_time, name, password, reset_token, role) values (1, 1, 'admin@admin.com', 0, 1, '3500',null, 'admin',"$2a$10$issi409pMEymxWO4m0susuRtlrBxPNdfsXKXi38/.XETxDnd69/eW" , null, "ROLE_USER");
insert into category (id, name) values (1, 'Urgentes')

-- Abril de 2025
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (86,'Internet', 79.90, '2025-01-01', true, true, 1, 1);
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (87,'Mercado', 750, '2025-01-01', true, true, 1, 1);
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (88,'Curso', 300, '2025-01-01', true, true, 1, 1);
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (89,'Energia', 250, '2025-01-01', true, true, 1, 1);
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (90,'Gas', 120, '2025-01-01', true, true, 1, 1);
-- Março de 2025   
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (91,'Internet', 79.90, '2025-02-01', true, true, 1, 1);
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (92,'Mercado', 740, '2025-02-01', true, true, 1, 1);
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (93,'Curso', 290, '2025-02-01', true, true, 1, 1);
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (94,'Energia', 230, '2025-02-01', true, true, 1, 1);
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (95,'Gas', 125, '2025-02-01', true, true, 1, 1);
-- Março de 2025  
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (96,'Internet', 79.90, '2025-03-01', true, true, 1, 1);
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (97,'Mercado', 800, '2025-03-01', true, true, 1, 1);
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (98,'Curso', 360, '2025-03-01', true, true, 1, 1);
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (99,'Energia', 190, '2025-03-01', true, true, 1, 1);
insert into expense (id, name, value, date, is_recurring, is_paid, user_id, category_id) values (100,'Gas', 90, '2025-03-01', true, true, 1, 1);




