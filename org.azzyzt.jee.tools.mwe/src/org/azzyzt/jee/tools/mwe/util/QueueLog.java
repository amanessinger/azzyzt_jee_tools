package org.azzyzt.jee.tools.mwe.util;

import java.util.Queue;

public class QueueLog implements Log {
	
	Queue<String> logQueue;

	public QueueLog(Queue<String> logQueue) {
		this.logQueue = logQueue;
	}
	
	@Override
	public void log(String msg) {
        try {
        	logQueue.add(msg);
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}

	@Override
	public void warn(String msg) {
		logQueue.add(Log.WARNING_MARKER+msg);
	}

}
