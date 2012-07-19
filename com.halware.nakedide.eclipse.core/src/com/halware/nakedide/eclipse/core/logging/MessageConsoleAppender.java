package com.halware.nakedide.eclipse.core.logging;

import java.io.IOException;
import java.io.PrintStream;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.halware.eclipseutil.jobs.AbstractUserJob;
import com.halware.eclipseutil.util.WorkbenchUtil;
import com.halware.nakedide.eclipse.core.Activator;

/**
 * Log4j appender that displays output in a JFace MessageConsole.
 * 
 * <p>
 * Opens/closes such consoles as necessary
 *  
 * @author Mike
 */
class MessageConsoleAppender extends AppenderSkeleton {
	
	// lazily instantiated
	private MessageConsoleStream _consoleStream = null;
	
	/**
	 * Constructor sets pattern to a default.
	 */
	MessageConsoleAppender() {
		super();
		setLayout( new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN ) ) ;
	}
	

	/* (non-Javadoc)
	 * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
	 */
	@Override
	protected void append( final LoggingEvent event) {
		// things get logged before workbench exists
		if ( PlatformUI.isWorkbenchRunning() ) {
			// may not be in UI thread
			if ( Display.getCurrent() != null ) {
				print( event );
			}
			else {
				new AbstractUserJob( "Log" ){
					@Override
					public IStatus runInUIThread(final IProgressMonitor monitor) {
						print( event );
						return Status.OK_STATUS;
					}
				}.schedule();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.Appender#close()
	 */
	public void close() {
		if ( _consoleStream != null ) {
			try {
				_consoleStream.flush();
				_consoleStream.close();
				IConsoleManager mgr = ConsolePlugin.getDefault().getConsoleManager();
				mgr.removeConsoles( new IConsole[]{ _consoleStream.getConsole() } );
			}
			catch( IOException ioe ) {
				_consoleStream.println( Activator.getResourceString("MessageConsoleAppender.ErrorClosing" ) );
				ioe.printStackTrace( new PrintStream( _consoleStream ) );
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.log4j.Appender#requiresLayout()
	 */
	public boolean requiresLayout() {
		return true;
	}
	
	// print to stream, lazily instantiating and opening console if necessary
	private void print( final LoggingEvent event ) {
		// ensure in UI thread
		assert  Display.getCurrent() != null;
		
		// ensure console view is open
		if ( WorkbenchUtil.getView( IConsoleConstants.ID_CONSOLE_VIEW ) == null ) {
			try {
				IWorkbenchPage activePage = WorkbenchUtil.getActivePage();
				if (activePage != null) {
					activePage.showView( IConsoleConstants.ID_CONSOLE_VIEW );
				}
			}
			catch ( PartInitException pie ) {
				// ignore for now
			}
		}
		
		// lazily instantiate console if necessary
		if ( _consoleStream == null ) {
			IConsoleManager mgr = ConsolePlugin.getDefault().getConsoleManager();
			MessageConsole logConsole = new MessageConsole( 
					Activator.getResourceString("MessageConsoleAppender.Log" ), //$NON-NLS-1$
					null ) ;
			mgr.addConsoles( new IConsole[]{ logConsole } );
			mgr.showConsoleView( logConsole );
			_consoleStream = logConsole.newMessageStream();
		}

		// print message
		_consoleStream.print( getLayout().format( event ) );
	    if( getLayout().ignoresThrowable() ) {
	        String[] lines = event.getThrowableStrRep();
	        if (lines != null ) {
	        	for( String line : lines ) {
	        		_consoleStream.println( line );
	        	}
	        }
	    }
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
