insert into user_account (id, created_at, updated_at, auth_enable, auth_token, date_of_recovery, email, name, password,
                          role, token)
values (1, now(), now(), true, 'auth_token', now(),
        'kvoza@gmail.com', 'Kvoza Onkay',
        '44136fa355b3678a1146ad16f7e8649e94fb4fc21fe77e8310c060f61caaff8a', 0, 'token');

insert into tag
values (1, now(), now(), '🏕', 'Nature', 1);
insert into tag
values (2, now(), now(), '💧', 'Water', 1);
insert into tag
values (3, now(), now(), '🪵', 'Wood', 1);
insert into tag
values (4, now(), now(), '🗿', 'Stone', 1);