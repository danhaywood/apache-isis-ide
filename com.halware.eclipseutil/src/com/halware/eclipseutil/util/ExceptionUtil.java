package com.halware.eclipseutil.util;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

@SuppressWarnings("deprecation")
public final class ExceptionUtil {
	
	private ExceptionUtil() {}

	/**
	 * A convenience method for converting an exception into a CoreException
	 */
	static public void throwCoreException(final Throwable t)
			throws CoreException {
		throwCoreException(IStatus.ERROR, "com.rpc.core", 0, t.getMessage(), t);
	}

//	/**
//	 * A convenience method for converting an exception into a CoreException
//	 */
//	static public void throwCoreException(IConfigurationElement element,
//			Throwable t) throws CoreException {
//		throwCoreException(element.getDeclaringExtension(), t);
//	}
//
//	/**
//	 * A convenience method for converting an exception into a CoreException
//	 */
//	static public void throwCoreException(IConfigurationElement element,
//			String message, Throwable t) throws CoreException {
//		throwCoreException(element.getDeclaringExtension(), message, t);
//	}
//
//	static public void throwCoreException(IExtension extension, Throwable t)
//			throws CoreException {
//		throwCoreException(extension.getDeclaringPluginDescriptor(), t);
//	}
//
//	@SuppressWarnings("deprecation")
//	static public void throwCoreException(IExtension extension, String message,
//			Throwable t) throws CoreException {
//		throwCoreException(extension.getDeclaringPluginDescriptor(), message, t);
//	}
//
//	@SuppressWarnings("deprecation")
//	static public void throwCoreException(
//			final org.eclipse.core.runtime.IPluginDescriptor descriptor,
//			final Throwable t) throws CoreException {
//		throwCoreException(IStatus.ERROR, descriptor.getUniqueIdentifier(), 0,
//				t.getMessage(), t);
//	}
//
//	static public void throwCoreException(
//			final org.eclipse.core.runtime.IPluginDescriptor descriptor,
//			final String message, final Throwable t) throws CoreException {
//		throwCoreException(IStatus.ERROR, descriptor.getUniqueIdentifier(), 0,
//				message, t);
//	}
//
//	static public void throwCoreException(IConfigurationElement element,
//			String message) throws CoreException {
//		throwCoreException(element.getDeclaringExtension(), message);
//	}
//
//	static public void throwCoreException(IExtension extension, String message)
//			throws CoreException {
//		throwCoreException(extension.getDeclaringPluginDescriptor(), message);
//	}
//
//	static public void throwCoreException(
//			final org.eclipse.core.runtime.IPluginDescriptor descriptor,
//			final String message) throws CoreException {
//		throwCoreException(IStatus.ERROR, descriptor.getUniqueIdentifier(), 0,
//				message, null);
//	}

	/**
	 * A convenience method for converting an exception into a CoreException
	 */
	static public void throwCoreException(final String message,
			final Throwable t) throws CoreException {
		throwCoreException(IStatus.ERROR, "com.rpc.core", 0, message, t);
	}

	/**
	 * A convenience method for converting an exception into a CoreException
	 */
	static public void throwCoreException(final int severity,
			final String pluginId, final int code, final String message,
			final Throwable t) throws CoreException {
		if (t instanceof CoreException) {
			throw (CoreException) t;
		}
		Status status = new Status(severity, pluginId, code, message, t);
		throw new CoreException(status);
	}

}
