
1 Azzyzt JEE Tools
==================

  !!! Release 1.0.0

Azzyzt JEE Tools is a collection of software tools designed to assist 
software developers creating software using Java Enterprise Edition 6. 
It is designed to be integrated into popular Java IDEs.

Copyright (c) 2011, Municipiality of Vienna, Austria Licensed under the 
EUPL, Version 1.1 or highersubsequent versions


1.1 Current status
~~~~~~~~~~~~~~~~~~

As of release 1.0.0, Azzyzt JEE Tools consists of three main parts:

  1. a generator that creates so-called azzyzted projects. An azzyzted 
    project is a collection of four projects, an Enterprise Application 
    Project (EAR project), an Enterprise Java Beans project (EJB 
    project), its client project (EJB client project) and a Dynamic Web 
    Project (servlet project). The EJB project contains all business 
    code, the servlet project contains REST wrappers around business 
    methods, the EJB client project contains all data types visible to 
    SOAP or REST clients. These three are called member projects. The 
    EAR project is a container for the member projects.

    The code generator uses a project base name, a package prefix and an 
    Eclipse runtime specification capable of supporting EJB 3.1, and 
    from that it creates the four projects. The EJB project is marked 
    with a special "azzyzted nature".
  2. a generator that analyzes JPA entities in azzyzted projects, and 
    uses that information to create a base application. This application 
    is a set of service beans exposed as SOAP and REST services. They 
    offer access to the database as it is commonly used by CRUD 
    applications. All generated code is in dedicated source folders. The 
    generated application can be extended and the extensions may use 
    generated code like DTOs as building blocks.
  3. a runtime library of code used by generated applications.

The structure of azzyzted projects and the structure of generated code 
evolved from the work on An Eclipse / GlassFish / Java EE 6 Tutorial[1] 
and on usage of that tutorial in a subsequent internal training class 
for developers at the Municipiality of Vienna, Austria.

Although Azzyzt JEE Tools come as a set of extensions to the Eclipse 
IDE, large portions of the code are independent of Eclipse. Given a 
project structure like that created by #1, #2 could be called from any 
IDE and can even be used without an IDE, because it can be called as a 
command line utility.

Azzyzt JEE Tools are known to work with the runtimes of

  * GlassFish v3
  * GlassFish v3.01
  * GlassFish v3.1

and Eclipse versions

  * Galileo SR1, SR2
  * Helios SR1, SR2

Eclipse is always understood as the generic bundle Eclipse IDE for Java 
EE Developers[2]. Other distributions may contain the required plugins 
and may work, but they were not tested.

The plugins compile on Indigo M4, but currently no plugin for GlassFish 
is available for Indigo, thus the configuration has not been tested.

Using JBOSS AS 6.0 as a runtime, generated code mostly compiles, @Remote 
annotations on generated service bean interfaces have to be removed 
though, because JBOSS AS 6.0 only supports the JEE 6 web profile. No 
further testing with JBOSS AS 6.0 has been done yet.

The project generator (#1) already runs with Apache Geronimo v3.0-M1, 
but due to lacking support for REST, generated applications do not 
compile.

Future extensions will provide generators for additional patterns 
commonly used in Java EE applications.

Contributions to Azzyzt JEE Tools are welcome. Possible areas include 
support for additional Java IDEs, additional patterns, additional 
runtimes, etc. Of course bug fixes are welcome as well.

Azzyzt JEE Tools were developed by Andreas Manessinger[3] for the 
Municipal Department 14 - Automated Data Processing, Information and 
Communications Technology[4] (MA 14) of the City of Vienna, Austria


1.2 Using the software
~~~~~~~~~~~~~~~~~~~~~~


1.2.1 Preparing the prerequisites
---------------------------------

If you just want to use Azzyzt JEE Tools (as opposed to modify and build 
them), the recommended way to install the software is via an Eclipse 
update site. As of release 1.0.0, there are two update site URLs, one 
for the edition used by the Municipiality of Vienna, Austria, the other 
a generic version. The URLs are

http://azzyzt.manessinger.com/azzyzt_generic/[5]

http://azzyzt.manessinger.com/azzyzt_magwien/[6]

All announcements of new versions will be published on

http://www.azzyzt.org[7]

When you don't see any features available from the update site, try 
unticking "Group items by category". There actually is a category called 
"Azzyzt", but you may see "There are no categorized items" anyway. I 
believe this to be a bug in Eclipse p2.

Once the feature is installed, make sure to have a Java EE 6 server 
instance configured. The server does not need to be running, but it must 
be configured, in order to make the runtime available.


1.2.2 Creating an azzyzted project
----------------------------------

Make sure you are in the "Java EE" perspective. There are three ways to 
get to the "New Azzyzted JEE Project" wizard:

  * File / New / Other / Java EE / New Azzyzted JEE Project
  * [Project Explorer] / New / Other / Java EE / New Azzyzted JEE 
    Project
  * Ctrl+N / Java EE / New Azzyzted JEE Project

I normally use the keyboard shortcut, as this is the shortest path.

In the wizard dialog enter a project base name, a package name, and 
choose the target runtime.

The project base name is a prefix that will be used for the four 
projects that together make up an azzyzted project.

The package name is actually a prefix as well. All generated Java 
packages will be below this prefix.

The target runtime is a list of all runtimes used in defined server 
instances. Thus if you have two servers running, one for GlassFish 3.01, 
one for 3.1, you will see a list of two runtimes. Choose one of them.

If the list of target runtimes is empty, then you have not yet defined a 
server. Do so from the Servers view with "New / Server".

If for example the project base name is "cookbook", you will end up with 
the following four projects:

  * cookbookEAR
  * cookbookEJB
  * cookbookEJBClient
  * cookbookServlets

The EAR project is an Enterprise Application Project, basically a 
wrapper around the three Java projects. The artifact of an EAR project 
is an enterprise archive, for instance "cookbook.ear", and this is the 
deployable application.

"cookbookEJB" is where we put all application functionality, 
"cookbookEJBClient" is the EJB client project, its artifact could be 
distributed in order to allow clients to call EJB functionality via 
CORBA. All datatypes used as parameters or return values of EJB service 
methods must be definied in the client project.

"cookbookServlets" is a Dynamic Web Project. It is used for the REST 
wrappers around service methods contained in "cookbookEJB". Additionally 
it can be used to add any kind and number of servlets. Keep in mind 
though, that you need to put your logic into the EJB project, in order 
to have the most options for accessing it. If you stick to that pattern, 
you can access services via CORBA, SOAP and REST. Accesses via CORBA and 
SOAP can even partake in distributed transactions.

The three Java projects will have two source folders each. One of them 
is always named "generated", that's where generated code goes. For the 
EJB and EJBClient projects the other source folder is "ejbModule", for 
the Servlets project it is "src". These source folders are for manually 
written code.

If for example the package name was "com.manessinger.cookbook", the 
following directories and files will be generated initially:

    cookbookEJB
    |-- ejbModule
    |   |-- com
    |   |   `-- manessinger
    |   |       `-- cookbook
    |   |           |-- entity
    |   |           `-- service
    |   |               `-- HelloTestBean.java
    |   `-- META-INF
    |       |-- ejb-jar.xml
    |       |-- MANIFEST.MF
    |       |-- persistence.xml
    |       `-- sun-ejb-jar.xml
    `-- generated
        `-- com
            `-- manessinger
                `-- cookbook
                    `-- entity
                        `-- StandardEntityListeners.java
    cookbookEJBClient
    |-- ejbModule
    |   `-- META-INF
    |       `-- MANIFEST.MF
    `-- generated
    
    cookbookServlets
    |-- generated
    |-- src
    `-- WebContent
        |-- index.jsp
        |-- META-INF
        |   `-- MANIFEST.MF
        `-- WEB-INF
            |-- lib
            `-- sun-web.xml

"com.manessinger.cookbook.service.HelloTestBean" is the only generated 
class that will ever be generated into a source folder meant to hold 
manually written code. You can keep it or throw it away. It is only 
generated upon project creation and is meant to make the project 
instantly deployable.

The bean has one (predictable) method

    @LocalBean
    @Stateless
    @WebService
    public class HelloTestBean {
    
        public String hello(String s) {
            return "Hello "+s;
        }
    }

Start the server, deploy the EAR ("Add and Remove" from the context menu 
of the server) and try it via the service test client built into the 
GlassFish Administration Console.


1.3 Azzyzted Modeling With Entities
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Upon project creation, a package "com.manessinger.cookbook.entity" 
(given the example) was generated under "cookbookEJB/ejbModule". Use 
this package to define your entities.

Entities have to extend "org.azzyzt.jee.runtime.entity.EntityBase<ID>", 
where ID is the class of the table's primary key.

There are two ways to create entities:

  * For a first cut you can choose to let Eclipse generate entities. 
    Dali, the mapping tool, comes with a wizard that lets you choose a 
    connection (defined in the Datasource Explorer view with "Database 
    Connections / New"), and it then analyzes tables defined in the 
    corresponding database schema. Start the wizard via "JPA Tools / 
    Generate Entities from Tables" from the EJB project's context menu.

    In this graphical tool you can choose the tables to be mapped, 
    create associations and/or choose from those automatically 
    discovered, set defaults for the mapping of all tables, and finally 
    specify the mappings on a per-table basis.

    This is the preferred way if the database already exists. The only 
    problem is, that the wizard can't properly cope with @ManyToMany 
    associations. It should, but errors in the code lead to errors in 
    the generated entities. Still, for medium to large databases, this 
    saves a lot of work. Just insert the @ManyToMany associations 
    manually.
  * If the database does not already exist, it can be generated from 
    manually written entities. Just write your entities and then 
    generate the database schema with "JPA Tools / Generate Tables from 
    Entities", again from the EJB project's context menu. Make sure that 
    the EJB project is already associated with a database connection. If 
    not, open the properties of the EJB project and choose a connection 
    under "Java Persistence / Connection".

Note please, that Azzyzted Modeling With Entities only supports mapping 
annotations on fields, not on accessor methods. This is not a deeply 
rooted design decision but just a matter of how it was implemented. 
Though it would be principally possible to support annotations of 
accessor methods, I currently see no reason to do so. The general 
consensus among experts seems to be, that none of the two methods has 
substantial advantages over the other.

Personally I feel that annotating the fields makes it easier to get an 
overview, and besides it does not tempt the developer to introduce side 
effects into getters/setters.


1.3.1 Azzyzt-specific annotations
---------------------------------

Azzyzt introduces the following extra annotations, that can be used on 
entity fields:

  @Internal
    marks a field as internal. It is mapped, but not exposed to service 
    clients.
  @CreateTimestamp, @ModifyTimestamp
    marks a field as create/modify timestamp. Field types can be 
    Calendar, Date or String. In case it is a string, the annotations 
    need an attribute "format", and that string has to be a valid format 
    for java.text.SimpleDateFormat
  @CreateUser, @ModificationUser
    marks a string field as user name. Determining a user name is by 
    definition a highly site-specific thing, thus we rely on some 
    InvocationMetaInfo being generated at the entry point into the 
    service, and being passed on via the standard 
    javax.transaction.TransactionSynchronizationRegistry

    As of release 1.0.0 this is only partially implemented. It works for 
    REST but not for SOAP and CORBA, and from a service accessed via 
    REST, it currently wouldn't be passed on to backend services 
    accessed via REST or CORBA. The crucial knowledge about how to 
    extract user information from a javax.interceptor.InvocationContext 
    is left to a standalone EJB that I call a SiteAdapter. Azzyzt comes 
    with a site adapter that uses information supplied in an HTTP header 
    called "x-authenticate-userid", thus it relies on some 
    authenticating portal or gateway in front of the application server.


1.3.2 Generating a base application
-----------------------------------

Once you have entities, you can generate code. In order to do this, use 
"Azzyzt / Start code generator" from the context menu of the EJB 
project. That's it :)


1.3.3 Structure of the generated code
-------------------------------------

In this example we have created three entity classes under 
"cookbookEJB/ejbModule". They correspond to the database tables CITY, 
COUNTRY and ZIP. The result of code generation looks like this:

    cookbookEAR
    |-- EarContent
    `-- lib
        |-- org.azzyzt.jee.runtime.jar
        `-- org.azzyzt.jee.runtime.site.jar
    
    cookbookEJB
    |-- ejbModule
    |   |-- com
    |   |   `-- manessinger
    |   |       `-- cookbook
    |   |           |-- entity
    |   |           |   |-- City.java
    |   |           |   |-- Country.java
    |   |           |   `-- Zip.java
    |   |           `-- service
    |   |               `-- HelloTestBean.java
    |   `-- META-INF
    |       |-- ejb-jar.xml
    |       |-- MANIFEST.MF
    |       |-- persistence.xml
    |       `-- sun-ejb-jar.xml
    `-- generated
        `-- com
            `-- manessinger
                `-- cookbook
                    |-- conv
                    |   |-- CityConv.java
                    |   |-- CountryConv.java
                    |   `-- ZipConv.java
                    |-- eao
                    |   `-- GenericEao.java
                    |-- entity
                    |   `-- StandardEntityListeners.java
                    |-- meta
                    |   |-- EntityMetaInfo.java
                    |   |-- InvocationRegistry.java
                    |   |-- SiteAdapter.java
                    |   `-- ValidAssociationPaths.java
                    `-- service
                        |-- CityFullBean.java
                        |-- CityRestrictedBean.java
                        |-- CountryFullBean.java
                        |-- CountryRestrictedBean.java
                        |-- ZipFullBean.java
                        `-- ZipRestrictedBean.java
    
    cookbookEJBClient
    |-- ejbModule
    |   `-- META-INF
    |       `-- MANIFEST.MF
    `-- generated
        `-- com
            `-- manessinger
                `-- cookbook
                    |-- dto
                    |   |-- CityDto.java
                    |   |-- CountryDto.java
                    |   `-- ZipDto.java
                    `-- service
                        |-- CityFullInterface.java
                        |-- CityRestrictedInterface.java
                        |-- CountryFullInterface.java
                        |-- CountryRestrictedInterface.java
                        |-- ZipFullInterface.java
                        `-- ZipRestrictedInterface.java
    
    cookbookServlets
    |-- generated
    |   `-- com
    |       `-- manessinger
    |           `-- cookbook
    |               `-- service
    |                   |-- CityFullDelegator.java
    |                   |-- CityRestrictedDelegator.java
    |                   |-- CountryFullDelegator.java
    |                   |-- CountryRestrictedDelegator.java
    |                   |-- RESTExceptionMapper.java
    |                   |-- RESTInterceptor.java
    |                   |-- RESTServlet.java
    |                   |-- ZipFullDelegator.java
    |                   `-- ZipRestrictedDelegator.java
    |-- src
    `-- WebContent
        |-- index.jsp
        |-- META-INF
        |   `-- MANIFEST.MF
        `-- WEB-INF
            |-- lib
            `-- sun-web.xml


1.3.4 Using the generated code
------------------------------

The first step is to deploy the EAR project. Note please, that you have 
to modify persistence.xml to refer to a jta-data-source, and that this 
data source must be defined in the application server. The Eclipse / 
GlassFish / Java EE 6 Tutorial[1] has a section titled "Specifying the 
database, testing, SQL log", that shows how to do this in GlassFish.

Now that the application is deployed, you can call the services.

For each table/entity we get a DTO (EJBClient), two EJBs, one for full 
(rw) and one for restricted (r) access, corresponding remote interfaces 
in the EJBClient project, corresponding REST wrappers around the beans 
(Servlet project). Here are some URLs, assuming GlassFish runs on port 
8080. Try for yourself, for instance with soapUI[8]:

http://localhost:8080/CityFullBeanService/CityFullBean?wsdl[9]

http://localhost:8080/CityRestrictedBeanService/CityRestrictedBean?wsdl[10]

http://localhost:8080/CountryFullBeanService/CountryFullBean?wsdl[11]

http://localhost:8080/CountryRestrictedBeanService/CountryRestrictedBean?wsdl[12]

http://localhost:8080/ZipFullBeanService/ZipFullBean?wsdl[13]

http://localhost:8080/ZipRestrictedBeanService/ZipRestrictedBean?wsdl[14]

Get the WADL description of the REST services under

http://localhost:8080/cookbookServlets/REST/application.wadl[15]


1.3.5 Examples for REST
-----------------------


1.3.5.1 List of all countries
"""""""""""""""""""""""""""""

http://localhost:8080/cookbookServlets/REST/city/all[16]


1.3.5.2 City with the ID 1
""""""""""""""""""""""""""

http://localhost:8080/cookbookServlets/REST/city/byId?id=1[17]


1.3.5.3 Sorted list of cities
"""""""""""""""""""""""""""""

POST the following XML document

    <query_spec>
       <orderBy>
           <fieldName>country.id</fieldName>
           <ascending>true</ascending>
       </orderBy>
       <orderBy>
           <fieldName>name</fieldName>
           <ascending>false</ascending>
       </orderBy>
    </query_spec>

into

http://localhost:8080/cookbookServlets/REST/city/list[18]

to get a list of all cities, but now sorted by the ID of their country 
ascending, and then alphabetically by their name descending.


1.3.5.4 Query with three conditions
"""""""""""""""""""""""""""""""""""

POST the following XML document

    <query_spec>
       <expr type="AND">
           <cond type="STRING" op="EQ" caseSensitive="true">
              <fieldName>country.name</fieldName>
              <value>Italy</value>
           </cond>
           <cond type="STRING" op="LIKE" negated="true" caseSensitive="false">
              <fieldName>name</fieldName>
              <value>r%</value>
           </cond>
           <cond type="LONG" op="EQ" negated="true">
              <fieldName>id</fieldName>
              <value>8</value>
           </cond>
       </expr>
       <orderBy>
           <fieldName>name</fieldName>
           <ascending>true</ascending>
       </orderBy>
    </query_spec>

into

http://localhost:8080/cookbookServlets/REST/city/list[18]

to get a list of all cities, where the country name equals "Italy" and 
the city's name does not begin with "r" (regardless case), but not the 
city with the ID 8.

The XML-based query language currently supports the unary expression of 
type "NOT", as well as the n-ary expressions of type "AND" and "OR".

n-ary expressions may contain any number of expressions and conditions 
freely mixed. There is no limit to the level of nesting of expressions.

Conditions have a type and an operator. Supported types are

STRING, SHORT, INTEGER, LONG, FLOAT, DOUBLE

Supported operators are

LIKE, EQ, LT, LE, GT, GE

where "LIKE" is only supported for type "STRING".

Field names in conditions or order by clauses can be cross-table 
references in the same dotted style as they are used in the Java 
Persistence Query Language (JPQL). Only references along mapped 
associations are valid. "City" has a field

    @ManyToOne
    private Country country;

and thus you can follow the association with "country.name".


1.3.5.5 Nested expressions
""""""""""""""""""""""""""

An example of a query specification with nested expressions is this:

    <query_spec>
       <expr type="OR">
           <cond type="STRING" op="EQ" caseSensitive="true">
              <fieldName>country.name</fieldName>
              <value>Italy</value>
           </cond>
           <expr type="AND">
               <cond type="STRING" op="LIKE" caseSensitive="false">
                  <fieldName>name</fieldName>
                  <value>l%</value>
               </cond>
               <cond type="LONG" op="EQ" negated="true">
                  <fieldName>id</fieldName>
                  <value>2</value>
               </cond>
           </expr>
       </expr>
       <orderBy>
           <fieldName>name</fieldName>
           <ascending>true</ascending>
       </orderBy>
    </query_spec>

POST it into

http://localhost:8080/cookbookServlets/REST/city/list[18]

to get a list of all cities in Italy and all other cities beginning with 
"l" (regardless case), but not the one with ID 2.


1.3.5.6 Update a record
"""""""""""""""""""""""

POST the following XML

    <city>
       <countryId>2</countryId>
       <id>7</id>
       <name>Rome</name>
    </city>

into

http://localhost:8080/cookbookServlets/REST/city/update[19]

to rename "Roma" to "Rome"


1.3.6 Variants of the query specification
-----------------------------------------

REST is just one way to access the services. The XML format of query 
specifications was chosen to be easily creatable from Adobe Flash 
clients. When the services are accessed via SOAP or CORBA, the "list()" 
methods take an object of type QuerySpec as parameter. Alternatively a 
second method "listByXML()" is generated, and those methods again take 
XML in form of a string parameter. The XML format is the same as for 
REST.


1.4 Developing or modifying Azzyzt JEE Tools
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


1.4.1 Getting the source
------------------------

Azzyzt JEE Tools have their home on GitHub under

https://github.com/amanessinger/azzyzt_jee_tools[20]


1.4.2 Directory layout
----------------------

Azzyzt JEE Tools are organized as a collection of Java projects. The 
projects were created using Eclipse Helios, and all development on 
Azzyzt JEE Tools is done with Eclipse.

The recommended way to build Azzyzt JEE Tools, is to fork the project 
from GitHub (https://github.com/amanessinger/azzyzt_jee_tools[20]) and 
use the resulting working directory as an Eclipse workspace. Doing so, 
you will end up with the following structure:

+-----------------------------------------------+-----------------------------+
| .git/                                         | local Git repository        |
| .gitignore                                    | list of ignored files       |
| README.txt                                    | this file                   |
| LICENCE.txt                                   | text version of EUPL        |
| azzyzt_magwien/                               | update site (magwien)       |
| azzyzt_generic/                               | update site (generic)       |
| org.azzyzt.jee.mwe.generic.feature/           | feature                     |
| org.azzyzt.jee.mwe.magwien.feature/           | feature                     |
| org.azzyzt.jee.runtime/                       | runtime for generated apps  |
| org.azzyzt.jee.runtime.site.generic/          | site-specific EJB (generic) |
| org.azzyzt.jee.runtime.site.generic.fragment/ | fragment (generic)          |
| org.azzyzt.jee.runtime.site.magwien/          | site-specific EJB (magwien) |
| org.azzyzt.jee.runtime.site.magwien.fragment/ | fragment (magwien)          |
| org.azzyzt.jee.tools.common.plugin/           | plugin (plugin tools)       |
| org.azzyzt.jee.tools.mwe/                     | code generator              |
| org.azzyzt.jee.tools.mwe.projectgen.plugin/   | plugin (project generator)  |
| org.azzyzt.jee.tools.project.plugin/          | plugin (faceted projects)   |
+-----------------------------------------------+-----------------------------+

Additionally you may find a directory named ".metadata" in case you 
actually use the working directory as an Eclipse workspace.

If you don't work for the Municipality of Vienna, you can safely ignore 
everything with "magwien" in its name.


1.4.3 Building the software
---------------------------

Start Eclipse and open the new workspace. Don't import the projects yet. 
In order to compile the source, you need a Java EE 6 runtime, for 
instance GlassFish v3.1, the current version as of Spring 2011.

Install the Oracle GlassFish plugin if you have not done so, download 
and install GlassFish if you have not done so, and then create a new 
server instance ("[Servers View] / New / server").

At this point the build will likely fail. Look at the libraries in the 
build path. The server runtime will likely be unbound. Bind it to the 
runtime of your newly created server instance.

to be continued


1.5 Licenses
~~~~~~~~~~~~

     Licensed under the EUPL, Version 1.1 or as soon they
     will be approved by the European Commission - subsequent
     versions of the EUPL (the "Licence");
     You may not use this work except in compliance with the
     Licence.
     
     For convenience a plain text copy of the English version 
     of the Licence can be found in the file LICENCE.txt in
     the top-level directory of this software distribution.
     
     You may obtain a copy of the Licence in any of 22 European
     Languages at:
    
     http://www.osor.eu/eupl
    
     Unless required by applicable law or agreed to in
     writing, software distributed under the Licence is
     distributed on an "AS IS" basis,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
     express or implied.
     See the Licence for the specific language governing
     permissions and limitations under the Licence.

For the purpose of generating code, Azzyzt JEE Tools make use of and 
bundles a copy of StringTemplate[21], which is

     Copyright (c) 2008, Terence Parr
     All rights reserved.
     Redistribution and use in source and binary forms, with or 
     without modification, are permitted provided that the 
     following conditions are met:
     
     Redistributions of source code must retain the above copyright 
     notice, this list of conditions and the following disclaimer.
     
     Redistributions in binary form must reproduce the above copyright 
     notice, this list of conditions and the following disclaimer in 
     the documentation and/or other materials provided with the distribution.
     
     Neither the name of the author nor the names of its contributors 
     may be used to endorse or promote products derived from this software 
     without specific prior written permission.
     
     THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
     "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
     LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
     FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
     COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
     INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
     BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
     LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
     CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT 
     LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
     ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
     POSSIBILITY OF SUCH DAMAGE.

This documentation was created using Deplate[22].

[1] http://programming.manessinger.com/tutorials/an-eclipse-glassfish-java-ee-6-tutorial/
[2] http://www.eclipse.org/downloads/
[3] http://programming.manessinger.com
[4] http://www.wien.gv.at/english/administration/ict/
[5] http://azzyzt.manessinger.com/azzyzt_generic/
[6] http://azzyzt.manessinger.com/azzyzt_magwien/
[7] http://www.azzyzt.org
[8] http://www.soapui.org
[9] http://localhost:8080/CityFullBeanService/CityFullBean?wsdl
[10] http://localhost:8080/CityRestrictedBeanService/CityRestrictedBean?wsdl
[11] http://localhost:8080/CountryFullBeanService/CountryFullBean?wsdl
[12] http://localhost:8080/CountryRestrictedBeanService/CountryRestrictedBean?wsdl
[13] http://localhost:8080/ZipFullBeanService/ZipFullBean?wsdl
[14] http://localhost:8080/ZipRestrictedBeanService/ZipRestrictedBean?wsdl
[15] http://localhost:8080/cookbookServlets/REST/application.wadl
[16] http://localhost:8080/cookbookServlets/REST/city/all
[17] http://localhost:8080/cookbookServlets/REST/city/byId?id=1
[18] http://localhost:8080/cookbookServlets/REST/city/list
[19] http://localhost:8080/cookbookServlets/REST/city/update
[20] https://github.com/amanessinger/azzyzt_jee_tools
[21] http://www.stringtemplate.org/
[22] http://deplate.sourceforge.net/

