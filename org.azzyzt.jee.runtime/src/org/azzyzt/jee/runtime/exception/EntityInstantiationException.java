/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
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

package org.azzyzt.jee.runtime.exception;

public class EntityInstantiationException extends TranslatableException {

    private static final long serialVersionUID = 1L;
    
    private String className;
    
    public EntityInstantiationException(Class<?> clazz) {
        super("Access error",
  			  "An object could not be instantiated");
        this.className = clazz.getSimpleName();
        setContext("An entity of class "+clazz.getSimpleName()+" could not be instantiated");
    }

    public String getClassName() { return className; }
}
