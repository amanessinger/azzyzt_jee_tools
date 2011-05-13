/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or � as soon they
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

package org.azzyzt.jee.tools.mwe.builder;

import org.azzyzt.jee.tools.mwe.model.MetaModel;
import org.azzyzt.jee.tools.mwe.model.type.MetaClass;
import org.azzyzt.jee.tools.mwe.model.type.MetaEntity;
import org.azzyzt.jee.tools.mwe.model.type.MetaField;

public class StandardEntityListenerModelBuilder extends DerivedModelBuilder implements Builder {

	public StandardEntityListenerModelBuilder(MetaModel entityModel, String targetPackageName) {
		super(entityModel, targetPackageName);
	}

	@Override
	public MetaModel build() {
		
		MetaClass target = null;
		boolean needsDate = false;
		boolean needsCalendar = false;
		
		for (MetaEntity me : masterModel.getTargetEntities()) {

			if (target == null) {
				// create MetaClass
				String packageName = me.getPackageName();
				targetModel.follow(packageName);
				
				// upon first entity create the class 
				String simpleName = "StandardEntityListeners";
				target = MetaClass.forName(packageName, simpleName);
				target.setModifiers(std.mod_public);
				target.setSuperMetaClass(std.entityListenerBase);
				target.addReferencedForeignType(std.javaUtilHashMap);
				target.addReferencedForeignType(std.javaUtilMap);
				target.addReferencedForeignType(std.javaTextSimpleDateFormat);
				target.addReferencedForeignType(std.javaLangReflectMethod);
				target.addReferencedForeignType(std.javaxPersistencePrePersist);
				target.addReferencedForeignType(std.javaxPersistencePreUpdate);
				target.addReferencedForeignType(std.dateFieldType);
			}
			
			// we may need to reference Date
			if (!needsDate) {
				if (me.isUsingCreateTimestampField()) {
					if (me.getCreateTimestampField().isDateField()) {
						needsDate = true;
					}
				}
				if (!needsDate && me.isUsingModificationTimestampField()) {
					if (me.getModificationTimestampField().isDateField()) {
						needsDate = true;
					}
				}
			}
			
			// we may need to reference Calendar
			if (!needsCalendar) {
				if (me.isUsingCreateTimestampField()) {
					if (me.getCreateTimestampField().isCalendarField()) {
						needsCalendar = true;
					}
				}
				if (!needsCalendar && me.isUsingModificationTimestampField()) {
					if (me.getModificationTimestampField().isCalendarField()) {
						needsCalendar = true;
					}
				}
			}
			
			// add a pseudo-field that we can use in the template
			MetaField mf = new MetaField(target, me.getLcFirstSimpleName());
			mf.setFieldType(me);
			target.addField(mf);
		}
		
		if (needsDate) {
			target.addReferencedForeignType(std.javaUtilDate);
		}
		if (needsCalendar) {
			target.addReferencedForeignType(std.javaUtilCalendar);
		}
		
		return targetModel;
	}
}
