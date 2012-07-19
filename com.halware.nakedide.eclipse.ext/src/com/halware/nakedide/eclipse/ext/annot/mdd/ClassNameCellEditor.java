package com.halware.nakedide.eclipse.ext.annot.mdd;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.halware.eclipseutil.util.SWTUtil;
import com.halware.nakedide.eclipse.ext.annot.utils.AstUtils;

/**
 * For cell editors of annotations that check the text is (probably) a
 * valid class name.
 */
public class ClassNameCellEditor extends AbstractTextCellEditor {


    private static final Logger LOGGER = Logger.getLogger(ClassNameCellEditor.class);
    @Override
    public Logger getLOGGER() {
        return LOGGER;
    }

    public static final String REGEX_CLASS_NAME = "^[_a-zA-Z][_a-zA-Z0-9\\.\\$]*$";

	public ClassNameCellEditor(Composite table) {
		super(table);
	}
    
    protected void doHookListeners(final Text text) {
        text.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                String resultantText = 
                    SWTUtil.ifAcceptedVerifyEvent(text, e);
                e.doit = isOk(resultantText);
            }

            private boolean isOk(String text) {
                getLOGGER().debug("verifyListener#isOK; text=" + text);
                return text.equals("") || text.matches(REGEX_CLASS_NAME);
            }
        });
    }

    /**
     * @return an {@link AstUtils.TypeLiteralValue}, or <tt>null</tt>.
     */
    @Override
    protected Object doGetValue() {
        Object retval = null;
        try {
            String value = (String) super.doGetValue();
            if (isEmptyString(value)) {
                retval = null;
            } else {
                retval = new AstUtils.TypeLiteralValue(value);
            }
            return retval;
        } finally {
            getLOGGER().debug("doGetValue() returning: >>" + retval + "<<");
        }
    }

    /**
     * Converts the supplied {@link AstUtils.TypeLiteralValue} into a string representation,
     * and calls superclass (which requires a string). 
     * 
     * <p>
     * <tt>null</tt> values are converted into an empty string.
     * 
     * @param value - an {@link AstUtils.TypeLiteralValue}, or <tt>null</tt>.
     */
    @Override
    protected void doSetValue(Object value) {
        getLOGGER().debug("doSetValue(value): value= >>" + value + "<<");
        AstUtils.TypeLiteralValue valueClassHandle = (AstUtils.TypeLiteralValue)value;
        String valueStr = "";
        if (valueClassHandle != null) {
            valueStr = stripClassSuffixIfPresent(valueClassHandle.getClassName());
        }
        super.doSetValue(valueStr);
    }

    private static final String CLASS_SUFFIX = ".class";
    private String stripClassSuffixIfPresent(String valueStr) {
        if (valueStr.endsWith(CLASS_SUFFIX)) {
            return valueStr.substring(0, valueStr.length()-CLASS_SUFFIX.length());
        }
        return valueStr;
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
