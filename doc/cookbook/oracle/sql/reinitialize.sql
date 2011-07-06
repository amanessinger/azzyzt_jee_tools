-- Database: cookbookdb

DROP TABLE visit;
DROP TABLE lang_table;
DROP TABLE zip;
DROP TABLE city;
DROP TABLE country;

DROP SEQUENCE zip_id_seq;
DROP SEQUENCE city_id_seq;
DROP SEQUENCE country_id_seq;
-- Database: cookbookdb


CREATE SEQUENCE country_id_seq;

 
CREATE TABLE country
(
  id NUMBER(19, 0) NOT NULL,
  create_timestamp TIMESTAMP DEFAULT SYSDATE,
  create_user VARCHAR2(255) DEFAULT 'anonymous',
  modification_timestamp TIMESTAMP DEFAULT SYSDATE,
  modification_user VARCHAR2(255) DEFAULT 'anonymous',
  name VARCHAR2(255),
  CONSTRAINT country_pkey PRIMARY KEY (id)
);
 
 
CREATE SEQUENCE city_id_seq;
 
CREATE TABLE city
(
  id NUMBER(19, 0) NOT NULL,
  create_timestamp TIMESTAMP DEFAULT SYSDATE,
  create_user VARCHAR2(255) DEFAULT 'anonymous',
  modification_timestamp TIMESTAMP DEFAULT SYSDATE,
  modification_user VARCHAR2(255) DEFAULT 'anonymous',
  name VARCHAR2(255),
  country_id NUMBER(19, 0),
  CONSTRAINT city_pkey PRIMARY KEY (id),
  CONSTRAINT fk_city_country_id FOREIGN KEY (country_id)
      REFERENCES country (id)
);
 
 
CREATE SEQUENCE zip_id_seq;
 
CREATE TABLE zip
(
  id NUMBER(19, 0) NOT NULL,
  code VARCHAR2(255),
  create_timestamp TIMESTAMP DEFAULT SYSDATE,
  create_user VARCHAR2(255) DEFAULT 'anonymous',
  modification_timestamp TIMESTAMP DEFAULT SYSDATE,
  modification_user VARCHAR2(255) DEFAULT 'anonymous',
  name VARCHAR2(255),
  country_id NUMBER(19, 0),
  CONSTRAINT zip_pkey PRIMARY KEY (id),
  CONSTRAINT fk_zip_country_id FOREIGN KEY (country_id)
      REFERENCES country (id)
);

CREATE TABLE lang_table
(
  lang_code VARCHAR2(8) NOT NULL, 
  lang_name VARCHAR2(100) NOT NULL, 
  CONSTRAINT lang_table_pkey PRIMARY KEY (lang_code)
);

CREATE TABLE visit
(
  from_zip_area NUMBER(19, 0) NOT NULL,
  to_city NUMBER(19, 0) NOT NULL,
  lang_used VARCHAR2(8) NOT NULL, 
  total_number_of_visitors NUMBER(19, 0) NOT NULL,
  create_timestamp VARCHAR2(21) DEFAULT '2011-05-19-081030.000',
  create_user VARCHAR2(255) DEFAULT 'anonymous',
  modification_timestamp VARCHAR2(21) DEFAULT '2011-05-19-081030.000',
  modification_user VARCHAR2(255) DEFAULT 'anonymous',
  CONSTRAINT visit_pkey PRIMARY KEY (from_zip_area, to_city, lang_used),
  CONSTRAINT fk_visit_from_zip_area FOREIGN KEY (from_zip_area)
      REFERENCES zip (id),
  CONSTRAINT fk_visit_to_city FOREIGN KEY (to_city)
      REFERENCES city (id),
  CONSTRAINT fk_visit_lang_used FOREIGN KEY (lang_used)
      REFERENCES lang_table (lang_code)
);
-- Database: cookbookdb

 
INSERT INTO country (id, name, create_user, modification_user)
    VALUES (country_id_seq.NEXTVAL, 'Austria', 'admin', 'admin');
INSERT INTO country (id, name, create_user, modification_user)
    VALUES (country_id_seq.NEXTVAL, 'Italy', 'admin', 'admin');
INSERT INTO country (id, name, create_user, modification_user)
    VALUES (country_id_seq.NEXTVAL, 'USA', 'admin', 'admin');
 
 
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (city_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'Austria'), 'Graz', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (city_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'Austria'), 'Linz', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (city_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'Austria'), 'Salzburg', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (city_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'Austria'), 'Wien', 'admin', 'admin');
 
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (city_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'Italy'), 'Bologna', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (city_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'Italy'), 'Firenze', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (city_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'Italy'), 'Roma', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (city_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'Italy'), 'Venezia', 'admin', 'admin');
 
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (city_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'USA'), 'Atlanta', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (city_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'USA'), 'Los Angeles', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (city_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'USA'), 'New York', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (city_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'USA'), 'Washington', 'admin', 'admin');
 
 
INSERT INTO zip (id, country_id, code, name, create_user, modification_user) 
VALUES (zip_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'Austria'), 
       '8054', 'Graz-Webling', 'admin', 'admin');
 
INSERT INTO zip (id, country_id, code, name, create_user, modification_user) 
VALUES (zip_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'Italy'), 
       '30124', 'Venezia', 'admin', 'admin');
 
INSERT INTO zip (id, country_id, code, name, create_user, modification_user) 
VALUES (zip_id_seq.NEXTVAL, (SELECT id FROM country WHERE name = 'USA'), 
       '20001-6000', 'Metropolitan Washington Airports Authority', 'admin', 'admin');

INSERT INTO lang_table (lang_code, lang_name)
VALUES ('de_AT', 'Deutsch (Österreich)');

INSERT INTO lang_table (lang_code, lang_name)
VALUES ('it_IT', 'Italiano');

INSERT INTO lang_table (lang_code, lang_name)
VALUES ('en_US', 'English (US)');
