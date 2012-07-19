package com.halware.nakedide.eclipse.core.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SocketAppender;
import org.apache.log4j.varia.NullAppender;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

import com.halware.nakedide.eclipse.core.Activator;

/**
 * Sets up and controls logging.
 * 
 * <p>
 * Instantiate once and throw away.  It registers itself with the 
 * {@link IPreferenceStore} which will hold a 
 * strong reference to it.  (Does this constitute a memory leak?)
 * 
 * <p>
 * TODO: factor out the common stuff here into IAppenderController (currently empty).
 * 
 * @author Mike
 */
public class LogController implements IPropertyChangeListener {
	
	/**
	 * Constructor starts listening on preference store and sets up initial
	 * logging state
	 */
	public LogController() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(LogController.SOCKET_APPENDER_HOST_NAME_KEY, LogController.SOCKET_APPENDER_DEFAULT_HOST_NAME);
		store.setDefault(LogController.SOCKET_APPENDER_PORT_KEY, LogController.SOCKET_APPENDER_DEFAULT_PORT);
		
		store.addPropertyChangeListener( this );
		boolean appenderAdded = false;
		
		if (store.getBoolean(LogController.CONSOLE_APPENDER_KEY)) {
			addConsoleAppender();
			appenderAdded = true;
		}
		if (store.getBoolean(LogController.SOCKET_APPENDER_KEY)){
			addSocketAppender();
			appenderAdded = true;
		}
		if (!appenderAdded) {
			// prevent 'no appender' error messages by log4j
			Logger.getRootLogger().addAppender( new NullAppender() );
		}
		setLevel(store.getString(LogController.LEVEL_KEY));
	}

	

	/**
	 * Adds/removes appenders based on changes to preferences
	 * @see org.eclipse.core.runtime.Preferences$IPropertyChangeListener#propertyChange(org.eclipse.core.runtime.Preferences.PropertyChangeEvent)
	 */
	public void propertyChange(
			final org.eclipse.jface.util.PropertyChangeEvent event) {
		
		String eventProperty = event.getProperty();
		Object eventNewValue = event.getNewValue();
		
		if( LogController.CONSOLE_APPENDER_KEY.equals( eventProperty ) ) {
			boolean appendToConsole = asBool(eventNewValue);
			if ( appendToConsole ) {
				if ( _consoleAppender == null ) {
					addConsoleAppender();
				}
			}
			else {
				if ( _consoleAppender != null ) {
					removeConsoleAppender();
				}
			}
		}

		if( LogController.SOCKET_APPENDER_KEY.equals( eventProperty ) ) {
			boolean appendToSocket = asBool(eventNewValue);
			if ( appendToSocket ) {
				if ( _socketAppender == null ) {
					addSocketAppender();
				}
			}
			else {
				if ( _socketAppender != null ) {
					removeSocketAppender();
				}
			}
		}

		if ( LogController.LEVEL_KEY.equals( eventProperty )) {
			assert eventNewValue instanceof String;
			setLevel(asString(eventNewValue));
		}
	}


	private boolean asBool(Object eventNewValue) {
		assert eventNewValue instanceof Boolean;
		return ((Boolean)eventNewValue).booleanValue();
	}
	
	private String asString(Object eventNewValue) {
		assert eventNewValue instanceof Boolean;
		return (String)eventNewValue;
	}


	///////////////////////////////////////////////////////////////////
	// ConsoleAppender
	///////////////////////////////////////////////////////////////////

	public static final String CONSOLE_APPENDER_KEY = "LogController.ShowConsole";

	private MessageConsoleAppender _consoleAppender = null;

	private void addConsoleAppender() {
		assert _consoleAppender == null;
		_consoleAppender = new MessageConsoleAppender();
		Logger.getRootLogger().addAppender( _consoleAppender );
	}
	
	private void removeConsoleAppender() {
		assert _consoleAppender != null;
		_consoleAppender.close();
		Logger.getRootLogger().removeAppender( _consoleAppender );
		_consoleAppender = null;
	}

	///////////////////////////////////////////////////////////////////
	// SocketAppender
	///////////////////////////////////////////////////////////////////

	public static final String SOCKET_APPENDER_KEY = "LogController.SocketAppender";
	public static final String SOCKET_APPENDER_HOST_NAME_KEY = "LogController.SocketAppenderHostName";
	public static final String SOCKET_APPENDER_PORT_KEY = "LogController.SocketAppenderPort";
	
	public static final String SOCKET_APPENDER_DEFAULT_HOST_NAME = "localhost";
	public static final int SOCKET_APPENDER_DEFAULT_PORT = 4445;

	private SocketAppender _socketAppender = null;

	private void addSocketAppender() {
		assert _socketAppender == null;

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String hostname = store.getString(LogController.SOCKET_APPENDER_HOST_NAME_KEY); 
		int port = store.getInt(LogController.SOCKET_APPENDER_PORT_KEY); 
		Logger.getRootLogger().addAppender( new SocketAppender(hostname, port));

		_socketAppender = new SocketAppender(hostname, port);
		Logger.getRootLogger().addAppender( _socketAppender );
	}
	
	private void removeSocketAppender() {
		assert _socketAppender != null;
		_socketAppender.close();
		Logger.getRootLogger().removeAppender( _socketAppender );
		_consoleAppender = null;
	}

	///////////////////////////////////////////////////////////////////
	// Level
	///////////////////////////////////////////////////////////////////

	public static final String LEVEL_KEY = "LogController.Level";

	private void setLevel(String levelStr) {
		Level level = Level.toLevel(levelStr);
		Logger.getRootLogger().setLevel(level);
	}


}

/******************************************************************************
 * (c) 2007 Haywood Associates Ltd.
 * 
 * Distributed under Eclipse Public License 1.0, see
 * http://www.eclipse.org/legal/epl-v10.html for full details.
 *
 * In particular:
 * THE PROGRAM IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, EITHER EXPRESS OR IMPLIED INCLUDING, WITHOUT 
 * LIMITATION, ANY WARRANTIES OR CONDITIONS OF TITLE, NON-INFRINGEMENT, 
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.
 *
 * If you require this software under any other type of license, then contact 
 * Dan Haywood through http://www.haywood-associates.co.uk.
 *
 *****************************************************************************/
