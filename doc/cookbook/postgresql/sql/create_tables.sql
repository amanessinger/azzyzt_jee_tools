-- Database: cookbookdb


CREATE SEQUENCE country_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
ALTER TABLE country_id_seq OWNER TO cookbookuser;
 
CREATE TABLE country (
    id INTEGER NOT NULL,
    name CHARACTER VARYING(10) NOT NULL
);
ALTER TABLE country OWNER TO cookbookuser;
ALTER TABLE ONLY country
    ADD CONSTRAINT country_name_idx UNIQUE (name);
ALTER TABLE ONLY country
    ADD CONSTRAINT country_pk PRIMARY KEY (id);
 
 
CREATE SEQUENCE city_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
ALTER TABLE city_id_seq OWNER TO cookbookuser;
 
CREATE TABLE city (
    id INTEGER NOT NULL,
    country_id INTEGER NOT NULL,
    name CHARACTER VARYING(30) NOT NULL
);
ALTER TABLE city OWNER TO cookbookuser;
ALTER TABLE ONLY city
    ADD CONSTRAINT city_name_idx UNIQUE (name);
ALTER TABLE ONLY city
    ADD CONSTRAINT city_pk PRIMARY KEY (id);
ALTER TABLE ONLY city
    ADD CONSTRAINT city_country_fk FOREIGN KEY (country_id) 
    REFERENCES country(id) ON DELETE CASCADE;
 
 
CREATE SEQUENCE zip_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;
ALTER TABLE zip_id_seq OWNER TO cookbookuser;
 
CREATE TABLE zip (
    id INTEGER NOT NULL,
    country_id INTEGER NOT NULL,
    code CHARACTER VARYING(20) NOT NULL,
    name CHARACTER VARYING(50) NOT NULL
);
ALTER TABLE zip OWNER TO cookbookuser;
ALTER TABLE ONLY zip
    ADD CONSTRAINT zip_name_idx UNIQUE (name);
ALTER TABLE ONLY zip
    ADD CONSTRAINT zip_pk PRIMARY KEY (id);
ALTER TABLE ONLY zip
    ADD CONSTRAINT zip_country_fk FOREIGN KEY (country_id) 
    REFERENCES country(id) ON DELETE CASCADE;
