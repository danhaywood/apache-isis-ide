package com.halware.nakedide.eclipse.core.logging;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.halware.nakedide.eclipse.core.Activator;

public class LogPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	
	/**
	 * Constructor sets title.
	 */
	public LogPreferencePage() {
		super( Activator.getResourceString( "LogPreferencePage.Title" ), //$NON-NLS-1$
               FieldEditorPreferencePage.GRID );
	}
	
	/**
	 * Must implement interface else workbench cannot open it - does nowt
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(final IWorkbench workbench) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	@Override
	protected void createFieldEditors() {
		
        addField( new BooleanFieldEditor( 
                LogController.CONSOLE_APPENDER_KEY,
                Activator.getResourceString( "LogPreferencePage.ConsoleAppender" ), //$NON-NLS-1$
                getFieldEditorParent() ) );

        addField( new BooleanFieldEditor( 
                LogController.SOCKET_APPENDER_KEY,
                Activator.getResourceString( "LogPreferencePage.SocketAppender" ), //$NON-NLS-1$
                getFieldEditorParent() ) );

        addField( new StringFieldEditor(
                LogController.SOCKET_APPENDER_HOST_NAME_KEY,
                Activator.getResourceString( "LogPreferencePage.SocketAppenderHostName" ), //$NON-NLS-1$
                getFieldEditorParent() ) );

        addField( new IntegerFieldEditor(
                LogController.SOCKET_APPENDER_PORT_KEY,
                Activator.getResourceString( "LogPreferencePage.SocketAppenderPort" ), //$NON-NLS-1$
                getFieldEditorParent() ) );

        addField( new RadioGroupFieldEditor(
                LogController.LEVEL_KEY,
                Activator.getResourceString( "LogPreferencePage.Level" ), //$NON-NLS-1$
                5, 
                new String[][] {
                	{ "Debug", "debug" },
                	{ "Info", "info" },
                	{ "Warn", "warn" },
                	{ "Error", "error" },
                	{ "Fatal", "fatal" },
                },
                getFieldEditorParent(),
                true) );


	}
	
	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
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
