package com.halware.nakedide.eclipse.core.logging;

/**
 * TODO: intent is to move the repeated code in LogController out into here.
 * 
 * <p>
 * It will need to interact with the LogPreferencePage to set up the fields
 * to gather the information it needs to control a particular type of appender.
 * There will be subclasses for each of the appender implementations that
 * need to be controlled.
 * 
 * @author Dan Haywood
 *
 */
public interface IAppenderController {

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
