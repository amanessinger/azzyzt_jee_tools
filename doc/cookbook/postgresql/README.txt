Postgresql database setup
=========================

The subdirectory "sql" contains scripts to create a Postgresql login
role (basically a user), a database, the tables (and sequences) and to
initialize the tables with sample data.

Make sure you have Postgresql installed and have the password for the
admin user "postgres".

Connect to the database (for instance using PgAdmin III) as user
"postgres" and execute the statements in the following scripts:

  sql/create_cookbook__user.sql
  sql/create_cookbook_db.sql


Disconnect from the database, re-connect as user "cookbookuser" and
execute the statements in the following scripts:

  sql/create_tables.sql
  sql/initialize_data.sql

Should you ever want to start over again, execute the statements in
the following scripts:

  sql/drop_tables.sql
  sql/create_tables.sql
  sql/initialize_data.sql

