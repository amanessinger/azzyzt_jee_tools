package org.azzyzt.jee.runtime.entity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public abstract class EntityListenerBase {

	protected abstract Map<Method, SimpleDateFormat> getStringDateFormats();

	protected abstract Map<Method, DateFieldType> getDateFieldTypes();
	
	protected static SimpleDateFormat yieldDateFormat(String format, Map<String, SimpleDateFormat> formatsByString) {
        if (formatsByString.containsKey(format)) {
            return formatsByString.get(format);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            formatsByString.put(format, sdf);
            return sdf;
        }
    }

	protected void maintainDateField(Object target, Class<?> clazz, Map<Class<?>, Method> setters) {
        Method m;
        DateFieldType dateFieldType;

        if (setters.containsKey(clazz)) {
            m = setters.get(clazz);
            dateFieldType = getDateFieldTypes().get(m);
            switch (dateFieldType) {
            case DATE:
                invokeDateSetter(target, m, new Date());
                break;
            case CALENDAR:
                invokeCalendarSetter(target, m, Calendar.getInstance());
                break;
            case STRING:
                invokeStringDateSatter(target, m, getStringDateFormats().get(m), new Date());
                break;
            }
        }
    }

    private void invokeStringDateSatter(Object o, Method m, SimpleDateFormat df, Date d) {
        invokeSetter(o, m, df.format(d));
    }

    private void invokeDateSetter(Object o, Method m, Date d) {
        invokeSetter(o, m, d);
    }

    private void invokeCalendarSetter(Object o, Method m, Calendar c) {
        invokeSetter(o, m, c);
    }

    private void invokeSetter(Object o, Method m, Object v) {
        try {
            m.invoke(o, v);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
