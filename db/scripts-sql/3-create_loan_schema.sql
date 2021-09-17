CREATE SCHEMA loan
    AUTHORIZATION admin_loan;


CREATE SEQUENCE loan.loan_id_loan_seq;

CREATE TABLE loan.loan (
                id_loan BIGINT NOT NULL DEFAULT nextval('loan.loan_id_loan_seq'),
                id_usager BIGINT NOT NULL,
                start_date TIMESTAMP NOT NULL,
                end_date TIMESTAMP,
                reference VARCHAR(15) NOT NULL,
                beneficiary VARCHAR(15) NOT NULL,
                information VARCHAR(100),
                reminder TIMESTAMP,
                CONSTRAINT loan_pk PRIMARY KEY (id_loan)
);


ALTER SEQUENCE loan.loan_id_loan_seq OWNED BY loan.loan.id_loan;