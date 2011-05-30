COOKBOOK Sample Application
===========================

This directory contains files you need to create the Cookbook sample
application. Choose a database-specific subdirectory and follow the
instructions in that directory to set up a database.

After you have created the database, make sure you have a definition
of a connection pool in GlassFish. Ping the database from the
GlassFish Administration Console.

Also make sure you have a JDBC resource with the name
"jdbc/cookbookdb" defined, and that it points to the correct
datasource.

Once you have the database of your choice, you can carry on with
the tutorial in "using_azzyzt.html", one level up from this directory.