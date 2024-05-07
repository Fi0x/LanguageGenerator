insert into "LANG" (ID, NAME, USERNAME, MIN_WORD_LENGTH, MAX_WORD_LENGTH)
values(1, 'Test-Language', 'fi0x', 3, 10);

insert into "LTTRS" (ID, LETTERS)
values(1, 'a'), (2, 'b'), (3, 'c'), (4, 'd'), (5, 'e'), (6, 'f'), (7, 'g'), (8, 'h'), (9, 'i'), (10, 'j'),
      (11, 'k'), (12, 'l'), (13, 'm'), (14, 'n'), (15, 'o'), (16, 'p'), (17, 'q'), (18, 'r'), (19, 's'), (20, 't'),
      (21, 'u'), (22, 'v'), (23, 'w'), (24, 'x'), (25, 'y'), (26, 'z');

insert into "CONCOM" (ID, LANGUAGE_ID, LETTER_ID)
values(1, 1, 2), (2, 1, 3), (3, 1, 4), (4, 1, 6), (5, 1, 7), (6, 1, 8), (7, 1, 10), (8, 1, 11), (9, 1, 12), (10, 1, 13);

insert into "VOCCOM" (ID, LANGUAGE_ID, LETTER_ID)
values(1, 1, 1), (2, 1, 5), (3, 1, 9), (4, 1, 15), (5, 1, 21);