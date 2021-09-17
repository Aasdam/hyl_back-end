/* --------------------------------------------------- RESET --------------------------------------------------- */
DELETE FROM usager.usager WHERE id_usager > 0;
ALTER SEQUENCE usager.usager_id_usager_seq RESTART WITH 1;

DELETE FROM loan.loan WHERE id_loan > 0;
ALTER SEQUENCE loan.loan_id_loan_seq RESTART WITH 1;

DELETE FROM item.tracking_sheet WHERE id_tracking_sheet > 0;
ALTER SEQUENCE item.tracking_sheet_id_tracking_sheet_seq RESTART WITH 1;

DELETE FROM item.picture WHERE id_picture > 0;
ALTER SEQUENCE item.picture_id_picture_seq RESTART WITH 1;

DELETE FROM item.subitem WHERE id_subitem > 0;
ALTER SEQUENCE item.subitem_id_subitem_seq RESTART WITH 1;

DELETE FROM item.item WHERE id_item > 0;
ALTER SEQUENCE item.item_id_item_seq RESTART WITH 1;

DELETE FROM item.category WHERE id_category > 0;
ALTER SEQUENCE item.category_id_category_seq RESTART WITH 1;

DELETE FROM memo.memo WHERE id_memo > 0;
ALTER SEQUENCE memo.memo_id_memo_seq RESTART WITH 1;

DELETE FROM memo.reminder_by_date WHERE id_reminder_by_date > 0;
ALTER SEQUENCE memo.reminder_by_date_id_reminder_by_date_seq RESTART WITH 1;

DELETE FROM memo.reminder_by_day WHERE id_reminder_by_day > 0;
ALTER SEQUENCE memo.reminder_by_day_id_reminder_by_day_seq RESTART WITH 1;