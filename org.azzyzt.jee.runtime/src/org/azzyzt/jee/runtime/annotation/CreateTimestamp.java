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
 * This annotation can be added to entity fields of the types
 * <code>java.util.Calendar</code>, <code>java.util.Date</code> or <code>java.lang.String</code>.
 * It marks the field as a create timestamp. Create timestamps are automatically set by the runtime
 * and they contain a timestamp indicating when a record was created. 
 * @see org.azzyzt.jee.runtime.annotation.CreateUser
 * @see org.azzyzt.jee.runtime.annotation.ModificationTimestamp
 * @see org.azzyzt.jee.runtime.annotation.ModificationUser
 * @see java.text.SimpleDateFormat
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.FIELD)
public @interface CreateTimestamp {
	/**
	 * @return a format string as required by <code>java.text.SimpleDateFormat</code>. 
	 * Formats are needed and can only be used for fields of type <code>java.lang.String</code>.
	 */
	String format() default "";
}
