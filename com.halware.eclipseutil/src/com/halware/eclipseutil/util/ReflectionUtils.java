package com.halware.eclipseutil.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



public final class ReflectionUtils {

	private ReflectionUtils() {}
	
	public static Object getField(Object object, String fieldName) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		return getField(object.getClass(), object, fieldName);
	}
	public static Object getField(Class clazz, String fieldName) throws SecurityException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		return getField(clazz, null, fieldName);
	}
	public static Object getField(Class clazz, Object object, String fieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(object);
	}

	public static void setField(Object object, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		setField(object.getClass(), object, fieldName, value);
	}
	public static void setField(Class clazz, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		setField(clazz, null, fieldName, value);
	}
	public static void setField(Class clazz, Object object, String fieldName, Object value) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(object, value);
	}
	
	public static Object invokeMethod(Object object, String methodName, Class[] paramTypes, Object[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return invokeMethod(object.getClass(), object, methodName, paramTypes, args);
	}

	public static Object invokeMethod(Class clazz, String methodName, Class[] paramTypes, Object[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return invokeMethod(clazz, null, methodName, paramTypes, args);
	}

	@SuppressWarnings("unchecked")
	public static Object invokeMethod(Class clazz, Object object, String methodName, Class[] paramTypes, Object[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method = clazz.getDeclaredMethod(methodName, paramTypes);
		method.setAccessible(true);
		return method.invoke(object, args);
	}

}
