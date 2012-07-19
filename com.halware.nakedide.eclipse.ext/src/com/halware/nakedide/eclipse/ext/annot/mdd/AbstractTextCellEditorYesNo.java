package com.halware.nakedide.eclipse.ext.annot.mdd;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.halware.eclipseutil.util.SWTUtil;

/**
 * For cell editors of annotations that convert yes/no into a boolean flag.
 */
public abstract class AbstractTextCellEditorYesNo extends AbstractTextCellEditor {

	public AbstractTextCellEditorYesNo(Composite table) {
		super(table);
	}

    protected void doHookListeners(final Text text) {
        text.addVerifyListener(
                new VerifyListener() {
                    public void verifyText(VerifyEvent e) {
                        String resultantText = 
                            SWTUtil.ifAcceptedVerifyEvent(text, e);
                        e.doit = isOk(resultantText);
                    }

                    private boolean isOk(String text) {
                        getLOGGER().debug("verifyListener#isOK; text=" + text);
                        return text.equals("") || 
                               text.equals("-") || 
                               text.matches("^[nNyY-]?$");
                    }
                });
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
