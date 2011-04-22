package org.azzyzt.jee.tools.mwe.util;


public abstract class LeveledLog implements Log {

	private Verbosity level;

	public LeveledLog(Verbosity level) {
		super();
		this.level = level;
	}

	protected abstract void loggit(String msg);
	
	@Override
	public void debug(String msg) {
		if (level.getValue() >= Verbosity.DEBUG.getValue()) loggit(msg);
	}

	@Override
	public void info(String msg) {
		if (level.getValue() >= Verbosity.INFO.getValue()) loggit(msg);
	}

	@Override
	public void error(String msg) {
		if (level.getValue() >= Verbosity.ERROR.getValue()) loggit(Log.WARNING_MARKER+msg);
	}

	@Override
	public void setVerbosity(Verbosity level) {
		this.level = level;
	}

}