package com.loading.xmlparser.converters.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import com.thoughtworks.xstream.converters.reflection.SunUnsafeReflectionProvider;

public class FieldBySetReflectionProvider extends SunUnsafeReflectionProvider {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void writeField(Object object, String fieldName, Object value, Class definedIn) {
		String setName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        try {
        	Field field = fieldDictionary.field(object.getClass(), fieldName, definedIn);
        	if(field == null) {
        		return;
        	}
        	Class type = field.getType();
        	if (definedIn == null) {
        		definedIn = field.getDeclaringClass();
        	}
        	
        	Method setMethod;
        	if (type.isPrimitive()) {
        		if (type.equals(Integer.TYPE)) {
        			setMethod = definedIn.getDeclaredMethod(setName, new Class[] { int.class });
        		} else if (type.equals(Long.TYPE)) {
        			setMethod = definedIn.getDeclaredMethod(setName, new Class[] { long.class });
        		} else if (type.equals(Short.TYPE)) {
        			setMethod = definedIn.getDeclaredMethod(setName, new Class[] { short.class });
        		} else if (type.equals(Character.TYPE)) {
        			setMethod = definedIn.getDeclaredMethod(setName, new Class[] { char.class });
        		} else if (type.equals(Byte.TYPE)) {
        			setMethod = definedIn.getDeclaredMethod(setName, new Class[] { byte.class });
        		} else if (type.equals(Float.TYPE)) {
        			setMethod = definedIn.getDeclaredMethod(setName, new Class[] { float.class });
        		} else if (type.equals(Double.TYPE)) {
        			setMethod = definedIn.getDeclaredMethod(setName, new Class[] { double.class });
        		} else if (type.equals(Boolean.TYPE)) {
        			setMethod = definedIn.getDeclaredMethod(setName, new Class[] { boolean.class });
        		} else {
        			throw new ObjectAccessException("Could not set field "
                            + object.getClass()
                            + "."
                            + field.getName()
                            + ": Unknown type "
                            + type);
        		}
        	} else {
        		setMethod = definedIn.getDeclaredMethod(setName, new Class[] { value.getClass() });
        	}
        	
        	// 打破JAVA封装性，使得private函数也能调用
        	setMethod.setAccessible(true);
        	setMethod.invoke(object, new Object[] { value });
        } catch (InvocationTargetException e) {
        	// 这里假设有效性检查通不过时系统将抛出IllegalArgumentException
        	if (e.getCause() instanceof IllegalArgumentException) {
        		throw (IllegalArgumentException) e.getCause();
        	} else {
        		super.writeField(object, fieldName, value, definedIn);
        	}
        } catch (Exception e) {
            super.writeField(object, fieldName, value, definedIn);
        }
	}

}
