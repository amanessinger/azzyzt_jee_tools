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
