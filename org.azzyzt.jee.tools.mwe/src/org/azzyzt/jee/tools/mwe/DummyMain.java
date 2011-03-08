package org.azzyzt.jee.tools.mwe;

public class DummyMain {

	public static void main(String[] args) {
		System.err.println("Inside of DummyMain.main()");
		for (String arg : args) {
			System.err.println("  -> "+arg);
		}
	}

}
