CREATE TABLE USER (
  ID         INTEGER NOT NULL,
  NAME       VARCHAR(255),
  DATE_BIRTH DATE,
  PRIMARY KEY (ID)
);

INSERT INTO USER VALUES (1, 'Вагон Героев', parsedatetime('1987-01-27', 'yyyy-MM-dd'));
INSERT INTO USER VALUES (2, 'Абырвалг', parsedatetime('1983-05-24', 'yyyy-MM-dd'));
INSERT INTO USER VALUES (3, 'Darth Vader', parsedatetime('2017-11-30', 'yyyy-MM-dd'));