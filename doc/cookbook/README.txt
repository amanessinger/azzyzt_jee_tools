COOKBOOK Sample Application
===========================

This directory contains files you need to create the Cookbook sample
application. Choose a database-specific subdirectory and follow the
instructions in that directory.

After you have created the database, make sure you have a definition
of a connection pool in GlassFish. Ping the database from the
GlassFish Administration Console.

Also make sure you have a JDBC resource with the name
"jdbc/cookbookdb" defined, and that it points to the correct
datasource.

Now start Eclipse and create an azzyzted project. Use the following
data:

  Project base name: cookbook
  Package name:      com.manessinger.cookbook
  Target runtime:    <your server runtime>

Next copy the contents of "<database>/src/" over your workspace. This
will copy entity classes and a "persistence.xml" into your
project. Refresh the workspace. If an error is shown on the project
"cookbookEJB", use 

  Project / Clean ... / Clean all projects

to clean all projects. This will make the error indicator go away.

Now you can run the code generator. The result will be a running
application that can be deployed.

