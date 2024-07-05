insert into lang (ID, NAME, USERNAME, VISIBLE, MIN_WORD_LENGTH, MAX_WORD_LENGTH, CHARS_BEFORE_SPECIAL, CHARS_AFTER_SPECIAL, MIN_SPECIAL_CHARS, MAX_SPECIAL_CHARS, SPECIAL_CHARACTER_CHANCE)
values (0, 'Elven-Language', 'fi0x', true, 2, 12, 0, 0, 0, 0, 1);

insert into lttrs (ID, LETTERS)
values (0, 'a'),
       (1, 'i'),
       (2, 'o'),
       (3, 'ai'),
       (4, 'ia'),
       (5, 'io'),
       (6, 'oi'),
       (7, 'ae'),

       (8, 'an'),
       (9, 'ah'),
       (10, 'aj'),
       (11, 'al'),
       (12, 'ay'),
       (13, 'ih'),
       (14, 'il'),
       (15, 'iv'),
       (16, 'og'),
       (17, 'oh'),
       (18, 'ol'),
       (19, 'on'),
       (20, 'ov'),
       (21, 'oy'),
       (22, 'ess'),

       (23, 'd'),
       (24, 'g'),
       (25, 'h'),
       (26, 'j'),
       (27, 'l'),
       (28, 'n'),
       (29, 'v'),
       (30, 'y'),
       (31, 'gl'),
       (32, 'gn'),
       (33, 'gy'),
       (34, 'hn'),
       (35, 'hy'),
       (36, 'ln'),
       (37, 'nh'),
       (38, 'ny'),
       (39, 'vy'),
       (40, 'yh'),
       (41, 'yl'),
       (42, 'yv'),
       (43, 'll'),
       (44, 'nn'),

       (45, 'da'),
       (46, 'do'),
       (47, 'ga'),
       (48, 'go'),
       (49, 'ha'),
       (50, 'ja'),
       (51, 'jo'),
       (52, 'la'),
       (53, 'li'),
       (54, 'lo'),
       (55, 'na'),
       (56, 'no'),
       (57, 'va'),
       (58, 'vo'),
       (59, 'ya'),
       (60, 'yo'),

       (61, 'kkk'),

       (62, '`');

insert into concom (ID, LANGUAGE_ID, LETTER_ID)
values (0, 0, 23),
       (1, 0, 24),
       (2, 0, 25),
       (3, 0, 26),
       (4, 0, 27),
       (5, 0, 28),
       (6, 0, 29),
       (7, 0, 30),
       (8, 0, 31),
       (9, 0, 32),
       (10, 0, 33),
       (11, 0, 34),
       (12, 0, 35),
       (13, 0, 36),
       (14, 0, 37),
       (15, 0, 38),
       (16, 0, 39),
       (17, 0, 40),
       (18, 0, 41),
       (19, 0, 42),
       (20, 0, 43),
       (21, 0, 44);

insert into voccom (ID, LANGUAGE_ID, LETTER_ID)
values (0, 0, 0),
       (1, 0, 1),
       (2, 0, 2),
       (3, 0, 3),
       (4, 0, 4),
       (5, 0, 5),
       (6, 0, 6),
       (7, 0, 7);

insert into convoccom (ID, LANGUAGE_ID, LETTER_ID)
values (0, 0, 45),
       (1, 0, 46),
       (2, 0, 47),
       (3, 0, 48),
       (4, 0, 49),
       (5, 0, 50),
       (6, 0, 51),
       (7, 0, 52),
       (8, 0, 53),
       (9, 0, 54),
       (10, 0, 55),
       (11, 0, 56),
       (12, 0, 57),
       (13, 0, 58),
       (14, 0, 59),
       (15, 0, 60);

insert into vocconcom (ID, LANGUAGE_ID, LETTER_ID)
values (0, 0, 8),
       (1, 0, 9),
       (2, 0, 10),
       (3, 0, 11),
       (4, 0, 12),
       (5, 0, 13),
       (6, 0, 14),
       (7, 0, 15),
       (8, 0, 16),
       (9, 0, 17),
       (10, 0, 18),
       (11, 0, 19),
       (12, 0, 20),
       (13, 0, 21),
       (14, 0, 22);

insert into forbcom (ID, LANGUAGE_ID, LETTER_ID)
values (0, 0, 61);

insert into spechacom (ID, LANGUAGE_ID, LETTER_ID)
values (0, 0, 62);