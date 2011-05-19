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

CREATE TABLE visit
(
  from_zip_area NUMBER(19, 0) NOT NULL,
  to_city NUMBER(19, 0) NOT NULL,
  create_timestamp VARCHAR2(21) DEFAULT '2011-05-19-081030.000',
  create_user VARCHAR2(255) DEFAULT 'anonymous',
  modification_timestamp VARCHAR2(21) DEFAULT '2011-05-19-081030.000',
  modification_user VARCHAR2(255) DEFAULT 'anonymous',
  number_of_visitors NUMBER(19, 0) NOT NULL,
  CONSTRAINT visit_pkey PRIMARY KEY (from_zip_area, to_city),
  CONSTRAINT fk_visit_from_zip_area FOREIGN KEY (from_zip_area)
      REFERENCES zip (id),
  CONSTRAINT fk_visit_to_city FOREIGN KEY (to_city)
      REFERENCES city (id)
);
