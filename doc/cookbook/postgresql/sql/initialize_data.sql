-- Database: cookbookdb

 
INSERT INTO country (id, name) VALUES (NEXTVAL('country_id_seq'), 'Austria');
INSERT INTO country (id, name) VALUES (NEXTVAL('country_id_seq'), 'Italy');
INSERT INTO country (id, name) VALUES (NEXTVAL('country_id_seq'), 'USA');
 
 
INSERT INTO city (id, country_id, name) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Austria'), 'Graz');
INSERT INTO city (id, country_id, name) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Austria'), 'Linz');
INSERT INTO city (id, country_id, name) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Austria'), 'Salzburg');
INSERT INTO city (id, country_id, name) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Austria'), 'Wien');
 
INSERT INTO city (id, country_id, name) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Italy'), 'Bologna');
INSERT INTO city (id, country_id, name) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Italy'), 'Firenze');
INSERT INTO city (id, country_id, name) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Italy'), 'Roma');
INSERT INTO city (id, country_id, name) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'Italy'), 'Venezia');
 
INSERT INTO city (id, country_id, name) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'USA'), 'Atlanta');
INSERT INTO city (id, country_id, name) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'USA'), 'Los Angeles');
INSERT INTO city (id, country_id, name) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'USA'), 'New York');
INSERT INTO city (id, country_id, name) 
    VALUES (NEXTVAL('city_id_seq'), (SELECT id FROM country WHERE name = 'USA'), 'Washington');
 
 
INSERT INTO zip (id, country_id, code, name) 
VALUES (NEXTVAL('zip_id_seq'), (SELECT id FROM country WHERE name = 'Austria'), 
       '8054', 'Graz-Webling');
 
INSERT INTO zip (id, country_id, code, name) 
VALUES (NEXTVAL('zip_id_seq'), (SELECT id FROM country WHERE name = 'Italy'), 
       '30124', 'Venezia');
 
INSERT INTO zip (id, country_id, code, name) 
VALUES (NEXTVAL('zip_id_seq'), (SELECT id FROM country WHERE name = 'USA'), 
       '20001-6000', 'Metropolitan Washington Airports Authority');
