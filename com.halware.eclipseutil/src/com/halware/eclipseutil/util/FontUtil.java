package com.halware.eclipseutil.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Static methods for handling fonts.
 * 
 * <p>
 * Note this stores static state to prevent unnecessary <code>GC</code>
 * instantiations.
 * 
 * @author Mike
 */
public class FontUtil {

    // prevent instantiation
    private FontUtil() {
        super();
    }

	public enum CharWidthType { AVERAGE, MAX, SAFE }
	
	private static final Map<Font,int[]> CACHED_WIDTHS_AND_HEIGHTS
		= new HashMap<Font,int[]>();
	
	private static final String LABEL_FONT_KEY
		= FontUtil.class.getSimpleName() + ".LabelFontKey" ;  //$NON-NLS-1$
	
	/**
	 * Defines a standard label font - the default font but in bold.
	 * @return
	 */
    public static Font getLabelFont() {
        return getLabelFont(false, false);
    }

    public static Font getLabelFont(
            boolean bold, boolean italic) {
        assert Display.getCurrent() != null;
        int style = SWT.NONE;
        String key = FontUtil.LABEL_FONT_KEY;
        if (bold) {
            style |= SWT.BOLD;
            key += ".bold";
        }
        if (italic) {
            style |= SWT.ITALIC;
            key += ".italic";
        }
        // lazy instantiator
        if ( !JFaceResources.getFontRegistry().hasValueFor( key ) ) {
            Font original = JFaceResources.getDefaultFont();
            FontData data = new FontData(
                    original.getFontData()[0].getName(),
                    original.getFontData()[0].getHeight(),
                    style );
            JFaceResources.getFontRegistry().put( 
                    key, new FontData[]{data} );
        }
        return JFaceResources.getFontRegistry().get( key );
    }

    /**
     * Returns character width for sizing / placing labels.
     * <br>The second argument can be one of the following values:
     * <ul>
     * <li><code>AVERAGE</code> - as it says
     * <li><code>MAX</code> - as it says
     * <li><code>SAFE</code> - 	using the average character width often leads 
     * to clipping, whilst using the max character width results in excess 
	 * whitespace.  The value returned by this method is an experience-based
	 * compromise between these two values.
     * </ul>
     * @param drawable
     * @param type
     * @return
     */
    public static int getCharWidth( final Composite composite, final CharWidthType type ) {
    	if( composite == null ) {
			throw new IllegalArgumentException();
		}
    	if( type == null ) {
			throw new IllegalArgumentException();
		}
    	int[] widthsAndHeights = FontUtil.computeWidthsAndHeights(composite);
    	assert widthsAndHeights != null;
    	assert widthsAndHeights.length == 4;
    	switch( type ) {
    		case AVERAGE:
    			return widthsAndHeights[0];
    		case MAX :
    			return widthsAndHeights[1];
    		case SAFE:
    			return widthsAndHeights[2];
    		default:
    			assert false;
    			return Integer.MIN_VALUE;
    	}
    }


    /**
     * Height of the font used in the composite.
     * 
     * @param composite
     * @return
     */
    public static int getCharHeight( 
    		final Composite composite ) {
    	int[] widthsAndHeights = FontUtil.computeWidthsAndHeights(composite);
    	assert widthsAndHeights != null;
    	assert widthsAndHeights.length == 4;
    	return widthsAndHeights[3];
    }

    private static int[] computeWidthsAndHeights(final Composite composite) {
		int[] widths = FontUtil.CACHED_WIDTHS_AND_HEIGHTS.get( composite.getFont() );
    	if ( widths == null ) {
    		widths = new int[4];
    		GC gc = null;
    		try {
    			gc = new GC( composite );
    			widths[0] = gc.getFontMetrics().getAverageCharWidth();
    			widths[1] = gc.getCharWidth( 'W' );
    			widths[2] = ( (2*widths[0] + widths[1])/3 );
    			widths[3] = gc.getFontMetrics().getHeight();
    		}
    		finally {
    			gc.dispose();
    		}
    		FontUtil.CACHED_WIDTHS_AND_HEIGHTS.put( composite.getFont(), widths );
    	}
		return widths;
	}
	
	
	



}
