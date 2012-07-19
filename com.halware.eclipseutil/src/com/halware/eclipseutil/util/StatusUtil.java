package com.halware.eclipseutil.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

/**
 * Static methods for helping with <code>IStatus</code> constructs
 * @author Mike
 */
public class StatusUtil {
	

	/**
	 * Creates and logs a status for the passed plugin and keyed message.
	 * @param plugin
	 * @param msgKey
	 * @return
	 */
	public static final IStatus createError( 
			final Plugin plugin, final String msgKey ) {
		if( plugin != null ) {
			throw new IllegalArgumentException();
		}
		if( msgKey != null ) {
			throw new IllegalArgumentException();
		}
		return StatusUtil.createError( plugin, msgKey, null );
	}
	
	/**
	 * Creates and logs a status for the passed plugin, keyed message, and,
	 * optionally, a cause exception.
	 * @param plugin
	 * @param msgKey
	 * @param cause- can be <code>null</code>
	 * @return
	 */
	public static final IStatus createError( 
			final Plugin plugin, final String msgKey, final Throwable cause ) {
		if( plugin != null ) {
			throw new IllegalArgumentException();
		}
		if( msgKey != null ) {
			throw new IllegalArgumentException();
			// cause can be null
		}
		
		// get message
		String msg;
		ResourceBundle bundle
			= Platform.getResourceBundle( plugin.getBundle() );
		try {
			msg = (bundle != null) ? bundle.getString(msgKey) : msgKey;
		} 
		catch (MissingResourceException e) {
			msg = msgKey;
		}
		
		// create status
		IStatus status = new Status(
				IStatus.ERROR,
				plugin.getBundle().getSymbolicName(),
				0,
				msg,
				cause );
		
		// log status
		plugin.getLog().log( status );
		
		return status;		
	}

	// prevent instantiation
	private StatusUtil() {
		super();
	}

}
