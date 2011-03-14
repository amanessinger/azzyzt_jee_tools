package org.azzyzt.jee.tools.mwe;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.azzyzt.jee.tools.mwe.util.Log;
import org.azzyzt.jee.tools.mwe.util.QueueLog;
import org.azzyzt.jee.tools.mwe.util.StreamLog;

public class DummyMain {

	public static void main(String[] args) {
		doWork(args, new StreamLog());
	}

	public static void sideEntrance(
			String[] args, 
			ConcurrentLinkedQueue<String> log) 
	{
		doWork(args, new QueueLog(log));
	}

	private static void doWork(String[] args, Log logger) {
		logger.log("Inside of DummyMain.main()");
		for (String arg : args) {
			logger.log("  -> "+arg);
		}
	}

}
