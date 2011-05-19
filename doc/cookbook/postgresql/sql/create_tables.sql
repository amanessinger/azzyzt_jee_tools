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
  create_timestamp timestamp without time zone DEFAULT now(),
  create_user character varying(255) DEFAULT 'anonymous'::character varying,
  modification_timestamp timestamp without time zone DEFAULT now(),
  modification_user character varying(255) DEFAULT 'anonymous'::character varying,
  name character varying(255),
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
  create_timestamp timestamp without time zone DEFAULT now(),
  create_user character varying(255) DEFAULT 'anonymous'::character varying,
  modification_timestamp timestamp without time zone DEFAULT now(),
  modification_user character varying(255) DEFAULT 'anonymous'::character varying,
  name character varying(255),
  country_id bigint,
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
  create_timestamp timestamp without time zone DEFAULT now(),
  create_user character varying(255) DEFAULT 'anonymous'::character varying,
  modification_timestamp timestamp without time zone DEFAULT now(),
  modification_user character varying(255) DEFAULT 'anonymous'::character varying,
  name character varying(255),
  country_id bigint,
  CONSTRAINT zip_pkey PRIMARY KEY (id),
  CONSTRAINT fk_zip_country_id FOREIGN KEY (country_id)
      REFERENCES country (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE zip OWNER TO cookbookuser;

CREATE TABLE visit
(
  from_zip_area bigint NOT NULL,
  to_city bigint NOT NULL,
  create_timestamp character varying(21) DEFAULT '2011-05-19-081030.000'::character varying,
  create_user character varying(255) DEFAULT 'anonymous'::character varying,
  modification_timestamp character varying(21) DEFAULT '2011-05-19-081030.000'::character varying,
  modification_user character varying(255) DEFAULT 'anonymous'::character varying,
  number_of_visitors bigint NOT NULL,
  CONSTRAINT visit_pkey PRIMARY KEY (from_zip_area, to_city),
  CONSTRAINT fk_visit_from_zip_area FOREIGN KEY (from_zip_area)
      REFERENCES zip (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_visit_to_city FOREIGN KEY (to_city)
      REFERENCES city (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE visit OWNER TO cookbookuser;
