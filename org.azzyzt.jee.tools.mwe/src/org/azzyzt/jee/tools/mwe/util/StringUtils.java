package org.azzyzt.jee.tools.mwe.util;

/**
 * Code blatantly copied from Skopje -{@link http://www.blackperl.com/java/skopje/}
 * @author advman
 *
 */
public class StringUtils {
	
    public static String ucFirst(String s) {
    	if (s.length() > 1) {
    		char ucfirst = Character.toUpperCase(s.charAt(0));
            return ucfirst + s.substring(1);
    	} else {
    		return Character.toUpperCase(s.charAt(0))+"";
    	}
    }

    public static String lcFirst(String s) {
    	if (s.length() > 1) {
    		char lcfirst = Character.toLowerCase(s.charAt(0));
            return lcfirst + s.substring(1);
    	} else {
    		return Character.toLowerCase(s.charAt(0))+"";
    	}
    }

}
