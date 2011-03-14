package org.azzyzt.jee.tools.mwe.util;

public interface Log {
	
	public static final String WARNING_MARKER = "!!";

	public void log(String msg);
	
	public void warn(String msg);
}
