CREATE SCHEMA memo
    AUTHORIZATION admin_memo;



CREATE SEQUENCE memo.memo_id_memo_seq;

CREATE TABLE memo.memo (
                id_memo BIGINT NOT NULL DEFAULT nextval('memo.memo_id_memo_seq'),
                title VARCHAR(35) NOT NULL,
                content VARCHAR(250) NOT NULL,
                last_modif TIMESTAMP NOT NULL,
				id_usager BIGINT NOT NULL,
                CONSTRAINT memo_pk PRIMARY KEY (id_memo)
);


ALTER SEQUENCE memo.memo_id_memo_seq OWNED BY memo.memo.id_memo;

CREATE SEQUENCE memo.reminder_by_date_id_reminder_by_date_seq;

CREATE TABLE memo.reminder_by_date (
                id_reminder_by_date BIGINT NOT NULL DEFAULT nextval('memo.reminder_by_date_id_reminder_by_date_seq'),
                reminder_date TIMESTAMP NOT NULL,
                id_memo BIGINT NOT NULL,
                CONSTRAINT reminder_by_date_pk PRIMARY KEY (id_reminder_by_date)
);


ALTER SEQUENCE memo.reminder_by_date_id_reminder_by_date_seq OWNED BY memo.reminder_by_date.id_reminder_by_date;

CREATE SEQUENCE memo.reminder_by_day_id_reminder_by_day_seq;

CREATE TABLE memo.reminder_by_day (
                id_reminder_by_day BIGINT NOT NULL DEFAULT nextval('memo.reminder_by_day_id_reminder_by_day_seq'),
                monday BOOLEAN NOT NULL,
                tuesday BOOLEAN NOT NULL,
                wednesday BOOLEAN NOT NULL,
                thursday BOOLEAN NOT NULL,
                friday BOOLEAN NOT NULL,
                saturday BOOLEAN NOT NULL,
                sunday BOOLEAN NOT NULL,
                id_memo BIGINT NOT NULL,
                CONSTRAINT reminder_by_day_pk PRIMARY KEY (id_reminder_by_day)
);


ALTER SEQUENCE memo.reminder_by_day_id_reminder_by_day_seq OWNED BY memo.reminder_by_day.id_reminder_by_day;

ALTER TABLE memo.reminder_by_day ADD CONSTRAINT memo_reminder_by_day_fk
FOREIGN KEY (id_memo)
REFERENCES memo.memo (id_memo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE memo.reminder_by_date ADD CONSTRAINT memo_reminder_by_date_fk
FOREIGN KEY (id_memo)
REFERENCES memo.memo (id_memo)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;