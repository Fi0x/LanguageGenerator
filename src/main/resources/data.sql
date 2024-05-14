insert into "LANG" (ID, NAME, USERNAME, IS_PUBLIC, MIN_WORD_LENGTH, MAX_WORD_LENGTH)
values(0, 'Test-Language', 'fi0x', true, 3, 10);

insert into "LTTRS" (ID, LETTERS)
values(0, 'kkk'), (1, 'a'), (2, 'b'), (3, 'c'), (4, 'd'), (5, 'e'), (6, 'f'), (7, 'g'), (8, 'h'), (9, 'i'), (10, 'j'),
      (11, 'k'), (12, 'l'), (13, 'm'), (14, 'n'), (15, 'o'), (16, 'p'), (17, 'q'), (18, 'r'), (19, 's'), (20, 't'),
      (21, 'u'), (22, 'v'), (23, 'w'), (24, 'x'), (25, 'y'), (26, 'z'), (27, 'ba'), (28, 'ab'), (29, 'ack');

insert into "CONCOM" (ID, LANGUAGE_ID, LETTER_ID)
values(1, 0, 2), (2, 0, 3), (3, 0, 4), (4, 0, 6), (5, 0, 7), (6, 0, 8), (7, 0, 10), (8, 0, 11), (9, 0, 12), (10, 0, 13);

insert into "VOCCOM" (ID, LANGUAGE_ID, LETTER_ID)
values(1, 0, 1), (2, 0, 5), (3, 0, 9), (4, 0, 15), (5, 0, 21);

insert into "CONVOCCOM" (ID, LANGUAGE_ID, LETTER_ID)
values(1, 0, 27);

insert into "VOCCONCOM" (ID, LANGUAGE_ID, LETTER_ID)
values(1, 0, 28);

insert into "FORBCOM" (ID, LANGUAGE_ID, LETTER_ID)
values(1, 0, 29);