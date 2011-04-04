Azzyzt JEE Tools
################

Azzyzt JEE Tools is a collection of software tools designed to assist
software developers creating software using Java Enterprise Edition
6. It is designed to be integrated into popular Java IDEs.

Copyright (c) 2011, Municipiality of Vienna, Austria


Current status
--------------

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
IDE and can even be used without an IDE, due to the fact that it also
works as a command line utility.

Azzyzt JEE Tools are known to work with the runtimes of

 * GlassFish v3
 * GlassFish v3.01
 * GlassFish v3.1

and Eclipse versions

 * Galileo SR1, SR2
 * Helios SR1, SR2

The plugins compile on Indigo M4, but currently no GlassFish plugin is
available for Indigo, thus the configuration has not been tested.

Using JBOSS AS 6.0 as a runtime, generated code mostly compiles,
@Remote annotations on generated service bean interfaces have to be
removed though, because JBOSS AS 6.0 only supports the JEE 6 web
profile. No further testing with JBOSS AS 6.0 has been done yet.

The project generator (#1) already runs with Apache Geronimo v3.0-M1,
but due to lacking support for REST, generated applications do not
compile. 

Future extensions will provide generators for additional patterns
commonly used in Java EE applications.

Contributions to Azzyzt JEE Tools are welcome. Possible areas include
support for additional Java IDEs, additional patterns, additional
runtimes, etc. Of course bug fixes are welcome as well.

Azzyzt JEE Tools were developed by Andreas Manessinger for the
Municipal Department 14 - Automated Data Processing, Information and
Communications Technology (MA 14) of the City of Vienna, Austria


Using the software
------------------

If you just want to use Azzyzt JEE Tools (as opposed to modify and
build them), the recommended way to install the software is via
an Eclipse update site. As of 29-MAR-2011, there are two update site
URLs, one for the edition used by the Municipiality of Vienna,
Austria, the other a generic version. The URLs are

    http://azzyzt.manessinger.com/azzyzt_generic/
    http://azzyzt.manessinger.com/azzyzt_magwien/

All announcements of new versions will be published on 

    http://www.azzyzt.org




Licenses
--------

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
bundles a copy of StringTemplate (http://www.stringtemplate.org/),
which is
 
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



Developing Azzyzt JEE Tools
===========================

Directory layout
----------------

Azzyzt JEE Tools are organized as a collection of Java projects. The
projects were created using Eclipse Helios, and all development on
Azzyzt JEE Tools is done with Eclipse.

The recommended way to build Azzyzt JEE Tools, is to fork the project
from GitHub (https://github.com/amanessinger/azzyzt_jee_tools) 
and use the resulting working directory as an Eclipse workspace. 
Doing so, you will end up with the following structure:

.git/                                          # local Git repository
.gitignore                                     # list of ignored files
README.txt                                     # this file
LICENCE.txt                                    # text version of EUPL
azzyzt_magwien/                                # update site project for Municipality of Vienna
azzyzt_generic/                                # generic update site project
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

If you don't work for the Municipality of Vienna, you can safely
ignore everything with "magwien" in its name.


Building the software
---------------------

Start Eclipse and open the new workspace. Don't import the projects
yet. In order to compile the source, you need a Java EE 6 runtime, for
instance GlassFish v3.1. 

Download GlassFish, unzip it. Install the Eclipse plugin for GlassFish
and use it to define a server. Now you can import the projects. They
may still not compile, and if so, the reason is most likely that your
server runtime has a different name. Just set your server runtime as
the target runtime for all Java projects and you should be fine.

[to be continued]
