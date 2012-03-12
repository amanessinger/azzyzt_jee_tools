-- Database: cookbookdb

DROP TABLE tour;
DROP TABLE visit;
DROP TABLE lang_table;
DROP TABLE zip;
DROP TABLE city;
DROP TABLE country;

DROP SEQUENCE tour_id_seq;
DROP SEQUENCE zip_id_seq;
DROP SEQUENCE city_id_seq;
DROP SEQUENCE country_id_seq;
-- Database: cookbookdb


CREATE SEQUENCE country_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE country_id_seq OWNER TO cookbookuser;

 
CREATE TABLE country
(
  id bigint NOT NULL,
  name character varying(255),
  create_timestamp timestamp without time zone DEFAULT now(),
  create_user character varying(255) DEFAULT 'anonymous'::character varying,
  modification_timestamp timestamp without time zone DEFAULT now(),
  modification_user character varying(255) DEFAULT 'anonymous'::character varying,
  CONSTRAINT country_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE country OWNER TO cookbookuser;
 
 
CREATE SEQUENCE city_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE city_id_seq OWNER TO cookbookuser;
 
CREATE TABLE city
(
  id bigint NOT NULL,
  country_id bigint,
  name character varying(255),
  create_timestamp timestamp without time zone DEFAULT now(),
  create_user character varying(255) DEFAULT 'anonymous'::character varying,
  modification_timestamp timestamp without time zone DEFAULT now(),
  modification_user character varying(255) DEFAULT 'anonymous'::character varying,
  CONSTRAINT city_pkey PRIMARY KEY (id),
  CONSTRAINT fk_city_country_id FOREIGN KEY (country_id)
      REFERENCES country (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE city OWNER TO cookbookuser;
 
 
CREATE SEQUENCE zip_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE zip_id_seq OWNER TO cookbookuser;
 
CREATE TABLE zip
(
  id bigint NOT NULL,
  code character varying(255),
  name character varying(255),
  country_id bigint,
  create_timestamp timestamp without time zone DEFAULT now(),
  create_user character varying(255) DEFAULT 'anonymous'::character varying,
  modification_timestamp timestamp without time zone DEFAULT now(),
  modification_user character varying(255) DEFAULT 'anonymous'::character varying,
  CONSTRAINT zip_pkey PRIMARY KEY (id),
  CONSTRAINT fk_zip_country_id FOREIGN KEY (country_id)
      REFERENCES country (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zip OWNER TO cookbookuser;

CREATE TABLE lang_table
(
   lang_code character varying(8) NOT NULL, 
   lang_name character varying(100) NOT NULL, 
   CONSTRAINT lang_table_pkey PRIMARY KEY (lang_code)
) 
WITH (
  OIDS = FALSE
)
;
ALTER TABLE lang_table OWNER TO cookbookuser;

CREATE TABLE visit
(
  from_zip_area bigint NOT NULL,
  to_city bigint NOT NULL,
  lang_used character varying(8) NOT NULL, 
  total_number_of_visitors bigint NOT NULL,
  create_timestamp character varying(21) DEFAULT '2011-05-19-081030.000'::character varying,
  create_user character varying(255) DEFAULT 'anonymous'::character varying,
  modification_timestamp character varying(21) DEFAULT '2011-05-19-081030.000'::character varying,
  modification_user character varying(255) DEFAULT 'anonymous'::character varying,
  CONSTRAINT visit_pkey PRIMARY KEY (from_zip_area, to_city, lang_used),
  CONSTRAINT fk_visit_from_zip_area FOREIGN KEY (from_zip_area)
      REFERENCES zip (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_visit_to_city FOREIGN KEY (to_city)
      REFERENCES city (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_visit_lang_used FOREIGN KEY (lang_used)
      REFERENCES lang_table (lang_code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE visit OWNER TO cookbookuser;

CREATE SEQUENCE tour_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE tour_id_seq OWNER TO cookbookuser;
 
CREATE TABLE tour
(
  id bigint NOT NULL,
  country_id bigint,
  name character varying(255),
  lang_code character varying(8) NOT NULL, 
  create_timestamp timestamp without time zone DEFAULT now(),
  create_user character varying(255) DEFAULT 'anonymous'::character varying,
  modification_timestamp timestamp without time zone DEFAULT now(),
  modification_user character varying(255) DEFAULT 'anonymous'::character varying,
  CONSTRAINT tour_pkey PRIMARY KEY (id),
  CONSTRAINT fk_tour_country_id FOREIGN KEY (country_id)
      REFERENCES country (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_tour_lang_id FOREIGN KEY (lang_code)
      REFERENCES lang_table (lang_code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tour OWNER TO cookbookuser;

-- Database: cookbookdb

 
INSERT INTO country (id, name, create_user, modification_user)
    VALUES (NEXTVAL('country_id_seq'), 'Austria', 'admin', 'admin');
INSERT INTO country (id, name, create_user, modification_user)
    VALUES (NEXTVAL('country_id_seq'), 'Italy', 'admin', 'admin');
INSERT INTO country (id, name, create_user, modification_user)
    VALUES (NEXTVAL('country_id_seq'), 'USA', 'admin', 'admin');
 
 
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Austria'), 'Graz', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Austria'), 'Linz', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Austria'), 'Salzburg', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Austria'), 'Wien', 'admin', 'admin');
 
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Italy'), 'Bologna', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Italy'), 'Firenze', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Italy'), 'Roma', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Italy'), 'Venezia', 'admin', 'admin');
 
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'USA'), 'Atlanta', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'USA'), 'Los Angeles', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'USA'), 'New York', 'admin', 'admin');
INSERT INTO city (id, country_id, name, create_user, modification_user) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'USA'), 'Washington', 'admin', 'admin');
 
 
INSERT INTO zip (id, country_id, code, name, create_user, modification_user) 
VALUES (NEXTVAL('zip_id_seq'), (SELECT id FROM country WHERE name = 'Austria'), 
       '8054', 'Graz-Webling', 'admin', 'admin');
 
INSERT INTO zip (id, country_id, code, name, create_user, modification_user) 
VALUES (NEXTVAL('zip_id_seq'), (SELECT id FROM country WHERE name = 'Italy'), 
       '30124', 'Venezia', 'admin', 'admin');
 
INSERT INTO zip (id, country_id, code, name, create_user, modification_user) 
VALUES (NEXTVAL('zip_id_seq'), (SELECT id FROM country WHERE name = 'USA'), 
       '20001-6000', 'Metropolitan Washington Airports Authority', 'admin', 'admin');

INSERT INTO lang_table (lang_code, lang_name)
VALUES ('de_AT', 'Deutsch (Österreich)');

INSERT INTO lang_table (lang_code, lang_name)
VALUES ('it_IT', 'Italiano');

INSERT INTO lang_table (lang_code, lang_name)
VALUES ('en_US', 'English (US)');
