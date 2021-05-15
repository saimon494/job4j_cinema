CREATE TABLE account
(
    id       serial primary key,
    username VARCHAR NOT NULL,
    phone    VARCHAR NOT NULL UNIQUE,
    hall     INTEGER UNIQUE
);

CREATE TABLE hall
(
    id         serial primary key,
    row_column INTEGER UNIQUE,
    account_id INTEGER REFERENCES account (id)
);

INSERT INTO hall(row_column)
VALUES (11);
INSERT INTO hall(row_column)
VALUES (12);
INSERT INTO hall(row_column)
VALUES (13);
INSERT INTO hall(row_column)
VALUES (21);
INSERT INTO hall(row_column)
VALUES (22);
INSERT INTO hall(row_column)
VALUES (23);
INSERT INTO hall(row_column)
VALUES (31);
INSERT INTO hall(row_column)
VALUES (32);
INSERT INTO hall(row_column)
VALUES (33);

--select * from hall;
--truncate table hall RESTART IDENTITY;
--truncate table account cascade;