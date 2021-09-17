CREATE SCHEMA item
    AUTHORIZATION admin_item;



CREATE SEQUENCE item.category_id_category_seq;

CREATE TABLE item.category (
                id_category BIGINT NOT NULL DEFAULT nextval('item.category_id_category_seq'),
                id_category_parent BIGINT,
                id_usager BIGINT NOT NULL,
                name VARCHAR(15) NOT NULL,
                CONSTRAINT category_pk PRIMARY KEY (id_category)
);


ALTER SEQUENCE item.category_id_category_seq OWNED BY item.category.id_category;

CREATE SEQUENCE item.item_id_item_seq;

CREATE TABLE item.item (
                id_item BIGINT NOT NULL DEFAULT nextval('item.item_id_item_seq'),
                description VARCHAR(50),
                name VARCHAR(15) NOT NULL,
                id_usager BIGINT NOT NULL,
                id_category BIGINT,
                CONSTRAINT item_pk PRIMARY KEY (id_item)
);


ALTER SEQUENCE item.item_id_item_seq OWNED BY item.item.id_item;

CREATE SEQUENCE item.subitem_id_subitem_seq;

CREATE TABLE item.subitem (
                id_subitem BIGINT NOT NULL DEFAULT nextval('item.subitem_id_subitem_seq'),
                reference VARCHAR(15) NOT NULL,
                id_item BIGINT NOT NULL,
                CONSTRAINT subitem_pk PRIMARY KEY (id_subitem)
);


ALTER SEQUENCE item.subitem_id_subitem_seq OWNED BY item.subitem.id_subitem;

CREATE SEQUENCE item.picture_id_picture_seq;

CREATE TABLE item.picture (
                id_picture BIGINT NOT NULL DEFAULT nextval('item.picture_id_picture_seq'),
                url VARCHAR,
                name VARCHAR(15) NOT NULL,
                id_subitem BIGINT NOT NULL,
                CONSTRAINT picture_pk PRIMARY KEY (id_picture)
);


ALTER SEQUENCE item.picture_id_picture_seq OWNED BY item.picture.id_picture;

CREATE SEQUENCE item.tracking_sheet_id_tracking_sheet_seq;

CREATE TABLE item.tracking_sheet (
                id_tracking_sheet BIGINT NOT NULL DEFAULT nextval('item.tracking_sheet_id_tracking_sheet_seq'),
                date TIMESTAMP NOT NULL,
                comment VARCHAR(150) NOT NULL,
                id_subitem BIGINT NOT NULL,
                id_loan BIGINT,
                CONSTRAINT tracking_sheet_pk PRIMARY KEY (id_tracking_sheet)
);


ALTER SEQUENCE item.tracking_sheet_id_tracking_sheet_seq OWNED BY item.tracking_sheet.id_tracking_sheet;

ALTER TABLE item.item ADD CONSTRAINT category_item_fk
FOREIGN KEY (id_category)
REFERENCES item.category (id_category)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE item.subitem ADD CONSTRAINT item_subitem_fk
FOREIGN KEY (id_item)
REFERENCES item.item (id_item)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE item.tracking_sheet ADD CONSTRAINT subitem_tracking_sheet_fk
FOREIGN KEY (id_subitem)
REFERENCES item.subitem (id_subitem)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE item.picture ADD CONSTRAINT subitem_picture_fk
FOREIGN KEY (id_subitem)
REFERENCES item.subitem (id_subitem)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;