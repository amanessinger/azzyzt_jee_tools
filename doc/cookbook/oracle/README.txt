Oracle database setup
=====================

The subdirectory "sql" contains scripts to create/drop tables and
sequences in a pre-existing Oracle database schema ("Bring Your Own
User and Schema"), and a script to initialize the tables with values
used in the tutorial.

Make sure your user has the right to create/drop tables and sequences.

Connect to the database (for instance via Eclipse's SQL Scrapbook) and
execute the statements in the following scripts:

  sql/create_tables.sql
  sql/initialize_data.sql

Should you ever want to start over again, execute the statements in
the following scripts:

  sql/drop_tables.sql
  sql/create_tables.sql
  sql/initialize_data.sql

