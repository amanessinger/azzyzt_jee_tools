/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * 
 * For convenience a plain text copy of the English version 
 * of the Licence can be found in the file LICENCE.txt in
 * the top-level directory of this software distribution.
 * 
 * You may obtain a copy of the Licence in any of 22 European
 * Languages at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package org.azzyzt.jee.runtime.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be added to entity fields of the type <code>java.lang.String</code>.
 * Create user fields are automatically set by the runtime. The exact method how a username is 
 * determined, is delegated to a so-called site adapter bean. Azzyzt JEE Tools currently come
 * with a site adapter that can extract user names from HTTP headers. At the moment this works
 * with REST only. 
 * @see org.azzyzt.jee.runtime.annotation.CreateTimestamp
 * @see org.azzyzt.jee.runtime.annotation.ModificationTimestamp
 * @see org.azzyzt.jee.runtime.annotation.ModificationUser
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface CreateUser {
	
}
