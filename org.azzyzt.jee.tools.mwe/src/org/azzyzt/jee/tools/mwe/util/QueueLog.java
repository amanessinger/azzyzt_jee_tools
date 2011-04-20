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

package org.azzyzt.jee.tools.mwe.util;

import java.util.Queue;

public class QueueLog extends LeveledLog implements Log {
	
	Queue<String> logQueue;
	
	public QueueLog(Queue<String> logQueue) {
		super(Verbosity.INFO);
		this.logQueue = logQueue;
	}
	
	@Override
	protected void loggit(String msg) {
        try {
        	logQueue.add(msg);
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}

}
