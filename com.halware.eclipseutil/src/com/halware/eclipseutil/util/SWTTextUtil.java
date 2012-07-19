package com.halware.eclipseutil.util;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Text;

public final class SWTTextUtil {

	private SWTTextUtil() {}

    /**
     * Generates what the value in the passed <code>Text</code> would be if the
     * passed <code>VerifyEvent</code> is applied
     * @param event
     * @return
     */
    public static String buildResultantText( final Text text, final VerifyEvent event ) {
    	if ( text == null ) {
			throw new IllegalArgumentException() ;
		}
    	if ( event == null ) {
			throw new IllegalArgumentException() ;
		}
    	
		String orig = text.getText();
		if ( orig.length() == 0 ) {
			return event.text;
		}
		else {
			StringBuffer sb = new StringBuffer();
			sb.append( orig.substring( 0, event.start ) );
			sb.append( event.text );
			sb.append( orig.substring( event.end, orig.length() ) );
			return sb.toString();
		}
    }

}
