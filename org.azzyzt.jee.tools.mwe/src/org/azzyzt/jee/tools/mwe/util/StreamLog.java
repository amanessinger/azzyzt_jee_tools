package org.azzyzt.jee.tools.mwe.util;

public class StreamLog implements Log {

	@Override
	public void log(String msg) {
		System.err.println(msg);
	}

	@Override
	public void warn(String msg) {
		System.err.println("WARNING: "+msg);
	}

}
