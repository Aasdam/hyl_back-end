CREATE SCHEMA usager
    AUTHORIZATION admin_user;
	


CREATE SEQUENCE usager.usager_id_usager_seq;

CREATE TABLE usager.usager (
                id_usager BIGINT NOT NULL DEFAULT nextval('usager.usager_id_usager_seq'),
                pseudo VARCHAR(15) NOT NULL,
                email VARCHAR(50) NOT NULL,
                password VARCHAR(100) NOT NULL,
                surname VARCHAR(30) NOT NULL,
                name VARCHAR(15) NOT NULL,
                civility VARCHAR(10),
                cellphone VARCHAR(20),
                CONSTRAINT usager_pk PRIMARY KEY (id_usager)
);


ALTER SEQUENCE usager.usager_id_usager_seq OWNED BY usager.usager.id_usager;