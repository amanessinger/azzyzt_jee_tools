Azzyzt JEE Tools
----------------

Azzyzt JEE Tools is a collection of software tools dedicated to
assisting software developers creating software using Java Enterprise
Edition 6. It is designed to be integrated into popular Java IDEs.

As of 25-MAR-2011, Azzyzt JEE Tools consists of three main parts:

1) a generator that creates so-called azzyzted projects. An azzyzted
   project is a collection of four projects, an Enterprise Application
   Project (EAR project), an Enterprise Java Beans project (EJB
   project), its client project (EJB client project) and a Dynamic Web
   Project (servlet project). The EJB project contains all business
   code, the servlet project contains REST wrappers around business
   methods, the EJB client project contains all data types visible to
   SOAP or REST clients. These three are called member projects. The
   EAR project is a container for the member projects.

   The code generator uses a project base name, a package prefix and
   an Eclipse runtime specification capable of supporting EJB
   3.1, and from that it creates the four projects. The EJB project is
   marked with a special "azzyzted nature".

2) a generator that analyzes JPA entities in azzyzted projects, and
   uses that information to create a base application. This
   application is a set of service beans exposed as SOAP and REST
   services. They offer access to the database as it is commonly used
   by CRUD applications. All generated code is in dedicated source
   folders. The generated application can be extended and the
   extensions may use generated code like DTOs as building blocks.

3) a runtime library of code used by generated applications.

Although Azzyzt JEE Tools come as a set of extensions to the Eclipse
IDE, large portions of the code are independent of Eclipse. Given a
project structure like that created by #1, #2 could be called from any
IDE and can even be used without an IDE, because it can be called as a
command line utility.

It is expected though, that a contributor can be found, who wraps
Azzyzt JEE Tools for NetBeans or for IDEA, the other two popular Java
IDEs. 

Future extensions will provide generators for additional patterns
commonly used in Java EE applications.

Contributions to Azzyzt JEE Tools are welcome. Possible areas include
support for additional Java IDEs, additional patters, etc. Of course
bug fixes are welcome as well.

Azzyzt JEE Tools were developed by Andreas Manessinger for the
Municipal Department 14 - Automated Data Processing, Information and
Communications Technology (MA 14) of the City of Vienna, Austria


Directory Structure
-------------------

Azzyzt JEE Tools are organized as a collection of Java projects. The
projects were created using Eclipse Helios, and all development on
Azzyzt JEE Tools is done with Eclipse.

The recommended way to build Azzyzt JEE Tools, is to fork the project
from GitHub [TODO add URL] and use the resulting working directory as
an Eclipse workspace. Doing so, you will end up with the following
structure:

.git/
.gitignore
LICENCE.txt
.#README.txt@
#README.txt#
README.txt


.git                                           # local Git repository
.gitignore                                     # list of ignored files
README.txt                                     # this file
LICENCE.txt                                    # text version of EUPL
azzyzt_magwien/                                # update site project for Municipality of Vienna
org.azzyzt.jee.mwe.generic.feature/            # Eclipse feature (generic)
org.azzyzt.jee.mwe.magwien.feature/            # Eclipse feature for Municipality of Vienna
org.azzyzt.jee.runtime/                        # runtime libs for generated apps
org.azzyzt.jee.runtime.site.generic/           # generic site-specific runtime EJB
org.azzyzt.jee.runtime.site.generic.fragment/  # plugin fragment for generic site-specific runtime EJB
org.azzyzt.jee.runtime.site.magwien/           # site-specific runtime EJB for Municipality of Vienna
org.azzyzt.jee.runtime.site.magwien.fragment/  # plugin fragment for site-specific runtime EJB for Municipality of Vienna
org.azzyzt.jee.tools.common.plugin/            # plugin with plugin support code
org.azzyzt.jee.tools.mwe/                      # code generator for Modeling With Entities
org.azzyzt.jee.tools.mwe.projectgen.plugin/    # Eclipse plugin for creating azzyzted projects
org.azzyzt.jee.tools.project.plugin/           # Eclipse plugin with code for creating faceted projects

Additionally you may find a directory named ".metadata" in case
you actually use the working directory as an Eclipse workspace.




